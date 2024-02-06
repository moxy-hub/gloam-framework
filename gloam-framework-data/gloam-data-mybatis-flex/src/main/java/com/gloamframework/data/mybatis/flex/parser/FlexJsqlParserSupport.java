package com.gloamframework.data.mybatis.flex.parser;

import com.gloamframework.common.error.GloamInternalException;
import com.gloamframework.data.mybatis.flex.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 增强mybatis flex的sql解析
 *
 * @author 晓龙
 */
@Slf4j
public abstract class FlexJsqlParserSupport {


    public String parserSingle(String sql, Object obj) {
        if (log.isDebugEnabled()) {
            log.debug("original SQL: " + sql);
        }
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            return processParser(statement, 0, sql, obj);
        } catch (JSQLParserException e) {
            throw new GloamInternalException("Failed to process, Error SQL: {}", e.getCause(), sql);
        }
    }

    public String parserMulti(String sql, Object obj) {
        if (log.isDebugEnabled()) {
            log.debug("original SQL: " + sql);
        }
        try {
            // fixed github pull/295
            StringBuilder sb = new StringBuilder();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (i > 0) {
                    sb.append(StringPool.SEMICOLON);
                }
                sb.append(processParser(statement, i, sql, obj));
                i++;
            }
            return sb.toString();
        } catch (JSQLParserException e) {
            throw new GloamInternalException("Failed to process, Error SQL: {}", e.getCause(), sql);
        }
    }

    /**
     * 执行 SQL 解析
     *
     * @param statement JsqlParser Statement
     * @return sql
     */
    protected String processParser(Statement statement, int index, String sql, Object obj) {
        if (log.isDebugEnabled()) {
            log.debug("SQL to parse, SQL: " + sql);
        }
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement, index, sql, obj);
        } else if (statement instanceof Select) {
            this.processSelect((Select) statement, index, sql, obj);
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement, index, sql, obj);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement, index, sql, obj);
        }
        sql = statement.toString();
        if (log.isDebugEnabled()) {
            log.debug("parse the finished SQL: " + sql);
        }
        return sql;
    }

    /**
     * 新增
     */
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * 删除
     */
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * 更新
     */
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询
     */
    protected void processSelect(Select select, int index, String sql, Object obj) {
        throw new UnsupportedOperationException();
    }
}
