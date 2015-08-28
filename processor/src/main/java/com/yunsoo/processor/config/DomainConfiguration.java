package com.yunsoo.processor.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by yan on 8/27/2015.
 */
@Configurable
@ComponentScan(basePackages = "com.yunsoo.processor.domain")
public class DomainConfiguration {
}
