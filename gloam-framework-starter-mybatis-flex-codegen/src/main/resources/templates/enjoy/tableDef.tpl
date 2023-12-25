#set(tableDefClassName = table.buildTableDefClassName())
#set(schema = table.schema == null ? "" : table.schema)
package #(packageConfig.tableDefPackage);

import com.gloamframework.data.mybatis.flex.column.GloamQueryColumn;
import com.gloamframework.data.mybatis.flex.table.GloamTableDef;

/**
 * #(table.getComment()) 表定义层。
 *
 * @author #(javadocConfig.getAuthor())
 * @since #(javadocConfig.getSince())
 */
public class #(tableDefClassName) extends GloamTableDef {

    /**
     * #(table.getComment())
     */
    public static final #(tableDefClassName) #(tableDefConfig.buildFieldName(table.buildEntityClassName() + tableDefConfig.instanceSuffix)) = new #(tableDefClassName)();

#for(column: table.getSortedColumns())
    #(column.buildComment())
    public final GloamQueryColumn #(tableDefConfig.buildFieldName(column.property)) = new GloamQueryColumn(this, "#(column.name)");

#end
    /**
     * 所有字段。
     */
    public final GloamQueryColumn #(tableDefConfig.buildFieldName("allColumns")) = new GloamQueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final GloamQueryColumn[] #(tableDefConfig.buildFieldName("defaultColumns")) = new GloamQueryColumn[]{#for(column: table.columns)#if(column.isDefaultColumn())#(tableDefConfig.buildFieldName(column.property))#if(for.index + 1 != for.size), #end#end#end};

    public #(tableDefClassName)() {
        super("#(schema)", "#(table.name)");
    }

}
