package com.gloamframework.data.mybatis.flex.interceptor;

import lombok.Setter;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 晓龙
 */
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
                @Signature(type = StatementHandler.class, method = "getBoundSql", args = {}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class MybatisFlexInterceptor implements Interceptor {

    @Setter
    private List<FlexInterceptor> interceptors = new ArrayList<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        if (target instanceof Executor) {
            final Executor executor = (Executor) target;
            Object parameter = args[1];
            boolean isUpdate = args.length == 2;
            MappedStatement ms = (MappedStatement) args[0];
            if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler<?> resultHandler = (ResultHandler<?>) args[3];
                BoundSql boundSql;
                if (args.length == 4) {
                    boundSql = ms.getBoundSql(parameter);
                } else {
                    // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[6]]
                    boundSql = (BoundSql) args[5];
                }
                for (FlexInterceptor query : interceptors) {
                    if (!query.willDoQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql)) {
                        return Collections.emptyList();
                    }
                    query.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                }
                CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
                return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            } else if (isUpdate) {
                for (FlexInterceptor update : interceptors) {
                    if (!update.willDoUpdate(executor, ms, parameter)) {
                        return -1;
                    }
                    update.beforeUpdate(executor, ms, parameter);
                }
            }
        } else {
            // StatementHandler
            final StatementHandler sh = (StatementHandler) target;
            // 目前只有StatementHandler.getBoundSql方法args才为null
            if (null == args) {
                for (FlexInterceptor innerInterceptor : interceptors) {
                    innerInterceptor.beforeGetBoundSql(sh);
                }
            } else {
                Connection connections = (Connection) args[0];
                Integer transactionTimeout = (Integer) args[1];
                for (FlexInterceptor innerInterceptor : interceptors) {
                    innerInterceptor.beforePrepare(sh, connections, transactionTimeout);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void addInnerInterceptor(FlexInterceptor innerInterceptor) {
        this.interceptors.add(innerInterceptor);
    }

    public List<FlexInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }

}
