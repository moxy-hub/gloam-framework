#set(isCacheExample = serviceImplConfig.cacheExample)
#set(primaryKey = table.getPrimaryKey())
#set(entityClassName = table.buildEntityClassName())
package #(packageConfig.serviceImplPackage);

import #(serviceImplConfig.buildSuperClassImport());
import #(packageConfig.entityPackage).#(table.buildEntityClassName());
import #(packageConfig.mapperPackage).#(table.buildMapperClassName());
import #(packageConfig.servicePackage).#(table.buildServiceClassName());
import org.springframework.stereotype.Service;
#if(isCacheExample)
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import com.gloamframework.core.boot.context.SpringContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
#end

/**
 * #(table.getComment()) 服务层实现。
 *
 * @author #(javadocConfig.getAuthor())
 * @since #(javadocConfig.getSince())
 */
@Service
#if(isCacheExample)
@CacheConfig(cacheNames = "#(firstCharToLowerCase(entityClassName))")
#end
public class #(table.buildServiceImplClassName()) extends #(serviceImplConfig.buildSuperClassName())<#(table.buildMapperClassName()), #(table.buildEntityClassName())> implements #(table.buildServiceClassName()) {

#if(isCacheExample)
    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(QueryWrapper query) {
        return super.remove(query);
    }

    public boolean remove(QueryCondition condition) {
        return this.getSelf().remove(this.query().where(condition));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(#(entityClassName) entity) {
        return super.save(entity);
    }
    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<#(entityClassName)> entities) {
        return super.saveBatch(entities);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<#(entityClassName)> entities, int batchSize) {
       return super.saveBatch(entities,batchSize);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(#(entityClassName) entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<#(entityClassName)> entities) {
       return super.saveOrUpdateBatch(entities);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<#(entityClassName)> entities, int batchSize) {
       return super.saveOrUpdateBatch(entities,batchSize);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(#(entityClassName) entity, QueryWrapper query) {
        return super.update(entity, query);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateById(#(entityClassName) entity, boolean ignoreNulls) {
        return super.updateById(entity, ignoreNulls);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateById(#(entityClassName) entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatch(Collection<#(entityClassName)> entities, int batchSize) {
        return super.updateBatch(entities, batchSize);
    }

    @Override
    @Cacheable(key = "#id")
    public #(entityClassName) getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public #(entityClassName) getOne(QueryWrapper query) {
        return super.getOne(query);
    }

    public #(entityClassName) getOne(QueryCondition condition) {
        return this.getSelf().getOne(this.query().where(condition));
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public <R> R getOneAs(QueryWrapper query, Class<R> asType) {
        return super.getOneAs(query, asType);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public Object getObj(QueryWrapper query) {
        return super.getObj(query);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public <R> R getObjAs(QueryWrapper query, Class<R> asType) {
        return super.getObjAs(query, asType);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public List<Object> objList(QueryWrapper query) {
        return super.objList(query);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public <R> List<R> objListAs(QueryWrapper query, Class<R> asType) {
        return super.objListAs(query, asType);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public List<#(entityClassName)> list(QueryWrapper query) {
        return super.list(query);
    }

    @Override
    public List<#(entityClassName)> list(QueryCondition condition) {
        return this.getSelf().list(this.query().where(condition));
    }

    @Override
    public List<#(entityClassName)> list() {
         return this.getSelf().list(this.query());
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public <R> List<R> listAs(QueryWrapper query, Class<R> asType) {
        return super.listAs(query, asType);
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #query.toSQL()")
    public long count(QueryWrapper query) {
        return super.count(query);
    }

    @Override
    public long count() {
        return this.getSelf().count(this.query());
    }

    @Override
    public long count(QueryCondition condition) {
        return this.getSelf().count(this.query().where(condition));
    }

    @Override
    @Cacheable(key = "#root.methodName + ':' + #page.getPageSize() + ':' + #page.getPageNumber() + ':' + #query.toSQL()")
    public <R> Page<R> pageAs(Page<R> page, QueryWrapper query, Class<R> asType) {
        return super.pageAs(page, query, asType);
    }

    @Override
    public Page<#(entityClassName)> page(Page<#(entityClassName)> page, QueryWrapper query) {
        return this.getSelf().pageAs(page, query, null);
    }

    private #(table.buildServiceClassName()) getSelf(){
        return SpringContext.getContext().getBean(#(table.buildServiceClassName()).class);
    }

#end
}
