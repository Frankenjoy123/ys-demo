package com.yunsoo.data.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Zhe on 2015/3/22.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "hibernate")
public class HibernateSetting {
    private String dialect;
    private String show_sql;
    private String hbm2ddl_auto;
    private String package_to_scan;

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getShow_sql() {
        return show_sql;
    }

    public void setShow_sql(String show_sql) {
        this.show_sql = show_sql;
    }

    public String getHbm2ddl_auto() {
        return hbm2ddl_auto;
    }

    public void setHbm2ddl_auto(String hbm2ddl_auto) {
        this.hbm2ddl_auto = hbm2ddl_auto;
    }

    public String getPackage_to_scan() {
        return package_to_scan;
    }

    public void setPackage_to_scan(String package_to_scan) {
        this.package_to_scan = package_to_scan;
    }
}
