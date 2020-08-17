package com.github.codingdebugallday.plugin.framework.extension.mybatis.support.mybatisplus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/**
 * <p>
 * mybatis plus ServiceImpl 的包装。解决原生mybatis plus 中ServiceImpl Mapper无法注入的问题
 * </p>
 *
 * @author isaac 2020/6/18 16:02
 * @since 1.0
 */
public class WrapperServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {

    protected Log log = LogFactory.getLog(this.getClass());
    private final ServiceImpl<M, T> serviceImpl;

    protected M baseMapper;

    public WrapperServiceImpl(M baseMapper) {
        this.baseMapper = Objects.requireNonNull(baseMapper);
        this.serviceImpl = new ServiceImpl<>();
        setMapper();
    }

    /**
     * 给ServiceImpl设置Mapper
     */
    @SuppressWarnings("rawtypes")
    private void setMapper() {
        Class<? extends ServiceImpl> aClass = serviceImpl.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            boolean fieldNameMatch = Objects.equals(field.getName(), "baseMapper");
            boolean fieldTypeMatch = this.baseMapper != null && this.baseMapper.getClass() == field.getType();
            if (fieldNameMatch || fieldTypeMatch) {
                field.setAccessible(true);
                try {
                    field.set(serviceImpl, this.baseMapper);
                } catch (IllegalAccessException e) {
                    log.error("field set error", e);
                }
            }
        }
    }

    @Override
    public boolean save(T entity) {
        return serviceImpl.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        return serviceImpl.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return serviceImpl.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    public boolean removeById(Serializable id) {
        return serviceImpl.removeById(id);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return serviceImpl.removeByMap(columnMap);
    }

    @Override
    public boolean remove(Wrapper<T> queryWrapper) {
        return serviceImpl.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return serviceImpl.removeByIds(idList);
    }

    @Override
    public boolean updateById(T entity) {
        return serviceImpl.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return serviceImpl.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return serviceImpl.updateBatchById(entityList, batchSize);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        return serviceImpl.saveOrUpdate(entity);
    }

    @Override
    public T getById(Serializable id) {
        return serviceImpl.getById(id);
    }

    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        return serviceImpl.listByIds(idList);
    }

    @Override
    public List<T> listByMap(Map<String, Object> columnMap) {
        return serviceImpl.listByMap(columnMap);
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        return serviceImpl.getOne(queryWrapper, throwEx);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return serviceImpl.getMap(queryWrapper);
    }

    @Override
    public <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return serviceImpl.getObj(queryWrapper, mapper);
    }

    @Override
    public int count(Wrapper<T> queryWrapper) {
        return serviceImpl.count(queryWrapper);
    }

    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        return serviceImpl.list(queryWrapper);
    }

    @Override
    public <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper) {
        return serviceImpl.page(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return serviceImpl.listMaps(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return serviceImpl.listObjs(queryWrapper, mapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<T> queryWrapper) {
        return serviceImpl.pageMaps(page, queryWrapper);
    }

    @Override
    public M getBaseMapper() {
        return this.baseMapper;
    }

}
