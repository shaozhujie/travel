package com.project.travel.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description: spring容器工具
 * @date 2023/8/10 16:15
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

    /** Spring应用上下文环境 */
    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        SpringUtils.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
    * @description: 获取对象
    * @param: name
    * @return:
    * @date: 2023/9/14 11:18
    */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException
    {
        return (T) beanFactory.getBean(name);
    }

    /**
    * @description: 获取类型为requiredType的对象
    * @param: clz
    * @return:
    * @date: 2023/9/14 11:18
    */
    public static <T> T getBean(Class<T> clz) throws BeansException
    {
        T result = (T) beanFactory.getBean(clz);
        return result;
    }

    /**
    * @description: 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
    * @param: name
    * @return:
    * @date: 2023/9/14 11:18
    */
    public static boolean containsBean(String name)
    {
        return beanFactory.containsBean(name);
    }

    /**
    * @description: 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
    * @param: name
    * @return:
    * @date: 2023/9/14 11:18
    */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.isSingleton(name);
    }

    /**
    * @description: 注册对象的类型
    * @param: name
    * @return:
    * @date: 2023/9/14 11:18
    */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.getType(name);
    }

    /**
    * @description: 如果给定的bean名字在bean定义中有别名，则返回这些别名
    * @param: name
    * @return:
    * @date: 2023/9/14 11:18
    */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException
    {
        return beanFactory.getAliases(name);
    }

    /**
    * @description: 获取aop代理对象
    * @param: invoker
    * @return:
    * @date: 2023/9/14 11:19
    */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker)
    {
        return (T) AopContext.currentProxy();
    }

    /**
    * @description: 获取当前的环境配置，无配置返回null
    * @param:
    * @return:
    * @date: 2023/9/14 11:19
    */
    public static String[] getActiveProfiles()
    {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
    * @description: 获取配置文件中的值
    * @param: key
    * @return:
    * @date: 2023/9/14 11:19
    */
    public static String getRequiredProperty(String key)
    {
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }

}
