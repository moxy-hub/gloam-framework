package com.gloamframework.data.mybatis.flex.table;

import com.gloamframework.data.mybatis.flex.column.GloamQueryColumn;
import com.mybatisflex.core.table.TableDef;

/**
 * 对于底层表字段的默认支持，如果表中没有这些基础字段，谨慎使用!!!
 *
 * @author 晓龙
 */
public class GloamTableDef extends TableDef {

    public GloamTableDef(String schema, String tableName) {
        super(schema, tableName);
    }

    public GloamTableDef(String tableName) {
        super(tableName);
    }

    /**
     * 创建时间
     */
    public final GloamQueryColumn CREATE_TIME = new GloamQueryColumn(this, "create_time");

    /**
     * 更新时间
     */
    public final GloamQueryColumn UPDATE_TIME = new GloamQueryColumn(this, "update_time");

    /**
     * 创建者
     */
    public final GloamQueryColumn CREATOR = new GloamQueryColumn(this, "creator");

    /**
     * 更新者
     */
    public final GloamQueryColumn UPDATER = new GloamQueryColumn(this, "updater");
}
