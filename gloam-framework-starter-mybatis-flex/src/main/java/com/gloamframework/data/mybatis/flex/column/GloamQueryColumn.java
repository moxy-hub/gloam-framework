package com.gloamframework.data.mybatis.flex.column;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.table.TableDef;

import java.util.Collection;

/**
 * 对mybatis-flex的查询字段进行增强
 *
 * @author 晓龙
 */
public class GloamQueryColumn extends QueryColumn {

    public GloamQueryColumn() {
    }

    public GloamQueryColumn(String name) {
        super(name);
    }

    public GloamQueryColumn(String tableName, String name) {
        super(tableName, name);
    }

    public GloamQueryColumn(String schema, String tableName, String name) {
        super(schema, tableName, name);
    }

    public GloamQueryColumn(String schema, String tableName, String name, String alias) {
        super(schema, tableName, name, alias);
    }

    public GloamQueryColumn(QueryTable queryTable, String name) {
        super(queryTable, name);
    }

    public GloamQueryColumn(TableDef tableDef, String name) {
        super(tableDef, name);
    }

    public GloamQueryColumn(TableDef tableDef, String name, String alias) {
        super(tableDef, name, alias);
    }

    public QueryCondition eqIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.eq(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition neIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.ne(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition gtIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.gt(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition geIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.ge(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition ltIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.lt(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition leIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.le(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition inIfPresent(Object... value) {
        if (this.ifPresentValue(value)) {
            return super.in(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition notInIfPresent(Object... value) {
        if (this.ifPresentValue(value)) {
            return super.notIn(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition betweenIfPresent(Object start, Object end) {
        if (this.ifPresentValue(start) && this.ifPresentValue(end)) {
            return super.between(start, end);
        }
        QueryCondition queryCondition = QueryCondition.createEmpty();
        if (this.ifPresentValue(start)) {
            queryCondition.and(this.geIfPresent(start));
        }
        if (this.ifPresentValue(end)) {
            queryCondition.and(this.leIfPresent(end));
        }
        return queryCondition;
    }


    public QueryCondition notBetweenIfPresent(Object start, Object end) {
        if (this.ifPresentValue(start) && this.ifPresentValue(end)) {
            return super.notBetween(start, end);
        }
        QueryCondition queryCondition = QueryCondition.createEmpty();
        if (this.ifPresentValue(start)) {
            queryCondition.and(this.ltIfPresent(start));
        }
        if (this.ifPresentValue(end)) {
            queryCondition.and(this.gtIfPresent(end));
        }
        return queryCondition;
    }


    public QueryCondition likeIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.like(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition likeLeftIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.likeLeft(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition likeRightIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.likeRight(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition likeRawIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.likeRaw(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition notLikeIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.notLike(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition notLikeLeftIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.notLikeLeft(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition notLikeRightIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.notLikeRight(value);
        }
        return QueryCondition.createEmpty();
    }


    public QueryCondition notLikeRawIfPresent(Object value) {
        if (this.ifPresentValue(value)) {
            return super.notLikeRaw(value);
        }
        return QueryCondition.createEmpty();
    }

    private boolean ifPresentValue(Object value) {
        // 为空，不存在
        if (value == null) {
            return false;
        }
        // 字符不为空，则返回true，存在
        if (String.class.isAssignableFrom(value.getClass())) {
            return StrUtil.isNotBlank((CharSequence) value);
        }
        // 集合
        if (Collection.class.isAssignableFrom(value.getClass())) {
            return CollectionUtil.isNotEmpty((Collection<?>) value);
        }
        // 数组
        if (ArrayUtil.isArray(value)) {
            return ArrayUtil.isNotEmpty(value);
        }
        return ObjectUtil.isNotEmpty(value);
    }
}
