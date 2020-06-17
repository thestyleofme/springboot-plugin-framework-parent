package com.github.codingdebugallday.loader;

import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 资源包装类
 * </p>
 *
 * @author isaac 2020/6/16 11:03
 * @since 1.0
 */
public class ResourceWrapper {

    private final List<Resource> resources = new ArrayList<>();
    private final Set<String> classPackageNames = new HashSet<>();
    private final Map<String, Object> extensions = new HashMap<>();

    public void addResource(Resource resource) {
        if (Objects.nonNull(resource)) {
            resources.add(resource);
        }
    }

    public void addResources(List<Resource> resources) {
        if (!CollectionUtils.isEmpty(resources)) {
            this.resources.addAll(resources);
        }
    }

    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    public void addClassPackageName(String classFullName) {
        if (!StringUtils.isEmpty(classFullName)) {
            classPackageNames.add(classFullName);
        }
    }

    public void addClassPackageNames(Set<String> classPackageNames) {
        if (!CollectionUtils.isEmpty(classPackageNames)) {
            this.classPackageNames.addAll(classPackageNames);
        }
    }

    public Set<String> getClassPackageNames() {
        return Collections.unmodifiableSet(classPackageNames);
    }


    public void addExtension(String key, Object value) {
        extensions.put(key, value);
    }

    public Object getExtension(String key) {
        return extensions.get(key);
    }

}
