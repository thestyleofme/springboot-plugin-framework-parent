package com.github.codingdebugallday.plugin.framework.factory.process.post.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.codingdebugallday.plugin.framework.annotation.Caller;
import com.github.codingdebugallday.plugin.framework.annotation.Supplier;
import com.github.codingdebugallday.plugin.framework.exceptions.PluginException;
import com.github.codingdebugallday.plugin.framework.factory.PluginInfoContainer;
import com.github.codingdebugallday.plugin.framework.factory.PluginRegistryInfo;
import com.github.codingdebugallday.plugin.framework.factory.SpringBeanRegister;
import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group.CallerGroup;
import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group.SupplierGroup;
import com.github.codingdebugallday.plugin.framework.factory.process.post.PluginPostProcessor;
import com.github.codingdebugallday.plugin.framework.utils.AopUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * <p>
 * 处理插件中类之间相互调用的的功能
 * </p>
 *
 * @author isaac 2020/6/16 15:13
 * @since 1.0
 */
public class PluginInvokePostProcessor implements PluginPostProcessor {

    private static final String KEY_SUPPERS = "PluginInvokePostProcessorSuppers";
    private static final String KEY_CALLERS = "PluginInvokePostProcessorCallers";

    private final GenericApplicationContext applicationContext;
    private final SpringBeanRegister springBeanRegister;

    public PluginInvokePostProcessor(ApplicationContext applicationContext) {
        Objects.requireNonNull(applicationContext);
        this.applicationContext = (GenericApplicationContext) applicationContext;
        this.springBeanRegister = new SpringBeanRegister(applicationContext);
    }

    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void register(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginRegistryInfo pluginRegistryInfo : pluginRegistryInfos) {
            AopUtils.resolveAop(pluginRegistryInfo.getPluginWrapper());
            try {
                List<Class<?>> suppers = pluginRegistryInfo.getGroupClasses(SupplierGroup.GROUP_ID);
                if (suppers == null) {
                    continue;
                }
                processSupper(pluginRegistryInfo, suppers);
            } finally {
                AopUtils.recoverAop();
            }
        }
        for (PluginRegistryInfo pluginRegistryInfo : pluginRegistryInfos) {
            AopUtils.resolveAop(pluginRegistryInfo.getPluginWrapper());
            try {
                List<Class<?>> callers = pluginRegistryInfo.getGroupClasses(CallerGroup.GROUP_ID);
                if (callers == null) {
                    continue;
                }
                processCaller(pluginRegistryInfo, callers);
            } finally {
                AopUtils.recoverAop();
            }
        }
    }

    @Override
    public void unregister(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginRegistryInfo pluginRegistryInfo : pluginRegistryInfos) {
            Set<String> supperNames = pluginRegistryInfo.getProcessorInfo(getKey(KEY_SUPPERS, pluginRegistryInfo));
            Set<String> callerNames = pluginRegistryInfo.getProcessorInfo(getKey(KEY_CALLERS, pluginRegistryInfo));
            String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
            unregister(pluginId, supperNames);
            unregister(pluginId, callerNames);
        }
    }

    /**
     * 处理被调用者
     *
     * @param pluginRegistryInfo 插件注册的信息
     * @param supperClasses      被调用者集合
     */
    private void processSupper(PluginRegistryInfo pluginRegistryInfo,
                               List<Class<?>> supperClasses) {
        if (supperClasses.isEmpty()) {
            return;
        }
        Set<String> beanNames = new HashSet<>();
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        for (Class<?> supperClass : supperClasses) {
            if (supperClass != null && supperClass.getAnnotation(Supplier.class) != null) {
                Supplier supplier = supperClass.getAnnotation(Supplier.class);
                String beanName = supplier.value();
                if (PluginInfoContainer.existRegisterBeanName(beanName)) {
                    String error = MessageFormat.format(
                            "Plugin {0} : Bean @Supplier name {1} already exist of {2}",
                            pluginRegistryInfo.getPluginWrapper().getPluginId(), beanName, supperClass.getName());
                    throw new PluginException(error);
                }
                springBeanRegister.registerOfSpecifyName(pluginId, beanName, supperClass);
                beanNames.add(beanName);
            }
        }
        pluginRegistryInfo.addProcessorInfo(getKey(KEY_SUPPERS, pluginRegistryInfo), beanNames);
    }

    /**
     * 处理调用者
     *
     * @param pluginRegistryInfo 插件注册的信息
     * @param callerClasses      调用者集合
     */
    private void processCaller(PluginRegistryInfo pluginRegistryInfo, List<Class<?>> callerClasses) {
        if (callerClasses == null || callerClasses.isEmpty()) {
            return;
        }
        Set<String> beanNames = new HashSet<>();
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        for (Class<?> callerClass : callerClasses) {
            Caller caller = callerClass.getAnnotation(Caller.class);
            if (caller == null) {
                continue;
            }
            Object supper = applicationContext.getBean(caller.value());
            String beanName = springBeanRegister.register(pluginId, callerClass, beanDefinition -> {
                beanDefinition.getPropertyValues().add("callerInterface", callerClass);
                beanDefinition.getPropertyValues().add("supper", supper);
                beanDefinition.setBeanClass(CallerInterfaceFactory.class);
                beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            });
            beanNames.add(beanName);
        }
        pluginRegistryInfo.addProcessorInfo(getKey(KEY_CALLERS, pluginRegistryInfo), beanNames);
    }

    /**
     * 得到往RegisterPluginInfo->processorInfo 保存的key
     *
     * @param key                key前缀
     * @param pluginRegistryInfo 插件注册的信息
     * @return String
     */
    private String getKey(String key, PluginRegistryInfo pluginRegistryInfo) {
        return key + "_" + pluginRegistryInfo.getPluginWrapper().getPluginId();
    }

    /**
     * 通过beanName卸载
     *
     * @param pluginId  插件id
     * @param beanNames beanNames集合
     */
    private void unregister(String pluginId, Set<String> beanNames) {
        if (beanNames == null || beanNames.isEmpty()) {
            return;
        }
        for (String beanName : beanNames) {
            springBeanRegister.unregister(pluginId, beanName);
        }
    }

    /**
     * 代理类
     */
    private static class ProxyHandler implements InvocationHandler {

        private final Object supplier;
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private ProxyHandler(Object supplier) {
            this.supplier = supplier;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Caller.Method callerMethod = method.getAnnotation(Caller.Method.class);
            if (callerMethod == null) {
                return notAnnotationInvoke(method, args);
            } else {
                return annotationInvoke(method, callerMethod, args);
            }
        }

        /**
         * 有注解的调用
         *
         * @param method       调用接口的方法
         * @param callerMethod 调用者方法注解
         * @param args         传入参数
         * @return 返回值
         */
        private Object annotationInvoke(Method method, Caller.Method callerMethod, Object[] args) {
            String callerMethodName = callerMethod.value();
            Class<?> aClass = supplier.getClass();
            Method[] methods = aClass.getMethods();
            Method supplierMethod = null;
            for (Method m : methods) {
                Supplier.Method supplierMethodAnnotation = m.getAnnotation(Supplier.Method.class);
                if (supplierMethodAnnotation != null &&
                        Objects.equals(supplierMethodAnnotation.value(), callerMethodName)) {
                    supplierMethod = m;
                    break;
                }
            }
            if (supplierMethod == null) {
                // 如果为空, 说明没有找到被调用者的注解, 则走没有注解的代理调用。
                return notAnnotationInvoke(method, args);
            }
            Class<?>[] parameterTypes = supplierMethod.getParameterTypes();
            if (parameterTypes.length != args.length) {
                // 参数不匹配
                return notAnnotationInvoke(method, args);
            }
            try {
                Object[] supplierArgs = new Object[args.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    Object arg = args[i];
                    if (parameterType == arg.getClass()) {
                        supplierArgs[i] = arg;
                    } else {
                        // 类型不匹配, 尝试使用json序列化
                        String json = OBJECT_MAPPER.writeValueAsString(arg);
                        Object serializeObject = OBJECT_MAPPER.readValue(json, parameterType);
                        supplierArgs[i] = serializeObject;
                    }
                }
                Object invokeReturn = supplierMethod.invoke(supplier, supplierArgs);
                return getReturnObject(invokeReturn, method);
            } catch (InvocationTargetException | JsonProcessingException | IllegalAccessException e) {
                throw new PluginException(e);
            }
        }

        /**
         * 没有注解调用
         *
         * @param method 调用接口的方法
         * @param args   传入参数
         * @return 返回值
         */
        private Object notAnnotationInvoke(Method method, Object[] args) {
            String name = method.getName();
            Class<?>[] argClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }
            try {
                Method supplierMethod = supplier.getClass().getMethod(name, argClasses);
                Object invokeReturn = supplierMethod.invoke(supplier, args);
                return getReturnObject(invokeReturn, method);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new PluginException(e);
            }
        }

        /**
         * 得到返回值对象
         *
         * @param invokeReturn 反射调用后返回的对象
         * @param method       调用接口的方法
         * @return 返回值对象
         */
        private Object getReturnObject(Object invokeReturn, Method method) {
            if (invokeReturn == null) {
                return null;
            }
            Class<?> returnType = method.getReturnType();
            if (ClassUtils.isAssignable(invokeReturn.getClass(), returnType)) {
                return invokeReturn;
            } else {
                try {
                    String json = OBJECT_MAPPER.writeValueAsString(invokeReturn);
                    return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructType(method.getGenericReturnType()));
                } catch (JsonProcessingException e) {
                    throw new PluginException(e);
                }
            }
        }
    }

    /**
     * 调用者的接口工厂
     *
     * @param <T> 接口泛型
     */
    private static class CallerInterfaceFactory<T> implements FactoryBean<T> {

        private Class<T> callerInterface;
        private Object supper;

        @SuppressWarnings("unchecked")
        @Override
        public T getObject() {
            ClassLoader classLoader = callerInterface.getClassLoader();
            Class<?>[] interfaces = new Class[]{callerInterface};
            ProxyHandler proxy = new ProxyHandler(supper);
            return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
        }

        @Override
        public Class<?> getObjectType() {
            return callerInterface;
        }


        @Override
        public boolean isSingleton() {
            return true;
        }

        public Class<T> getCallerInterface() {
            return callerInterface;
        }

        public void setCallerInterface(Class<T> callerInterface) {
            this.callerInterface = callerInterface;
        }

        public Object getSupper() {
            return supper;
        }

        public void setSupper(Object supper) {
            this.supper = supper;
        }
    }

}
