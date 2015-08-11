package com.yunsoo.api;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by yan on 7/23/2015.
 */
@Service
public class AppContext implements ApplicationContextAware {

    private static ApplicationContext appContext;

    private AppContext() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        appContext = applicationContext;

    }

    public static Object getBean(String beanName) {
        if(appContext != null)
            return appContext.getBean(beanName);
        return null;
    }

    public static boolean containsBean(String name) {
        if(appContext != null)
            return appContext.containsBean(name);
        return false;
    }

}
