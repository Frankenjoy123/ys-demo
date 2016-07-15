package com.yunsoo.api.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yan on 7/11/2016.
 */
public class PropertyUtils {

    static Properties properties;

    private static void init() {

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        properties = yaml.getObject();

    }

    public static String getProperty(String key) {
        if (properties == null)
            init();
        return properties.getProperty(key);
    }
}
