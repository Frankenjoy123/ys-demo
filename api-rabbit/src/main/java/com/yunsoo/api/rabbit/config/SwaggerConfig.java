package com.yunsoo.api.rabbit.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yunsoo.api.rabbit.Constants;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/9/14
 * Descriptions:
 */
@EnableSwagger2
@ConditionalOnProperty(value = "yunsoo.debug", havingValue = "true")
@Configuration
public class SwaggerConfig {

    @Bean
    @Autowired
    public Docket docket(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("api-rabbit")
                        .description("documentation for api-rabbit")
                        .version("1.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yunsoo.api.rabbit.controller"))//.paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(DateTime.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)), typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .globalOperationParameters(globalOperationParameters())
                .globalResponseMessage(RequestMethod.GET,
                        Lists.newArrayList(new ResponseMessageBuilder()
                                .code(500)
                                .message("server error")
                                .responseModel(new ModelRef("ErrorResult"))
                                .build()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(securityContexts())
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .enableUrlTemplating(true);
    }

    private List<Parameter> globalOperationParameters() {
        return Lists.newArrayList(
                new ParameterBuilder()
                        .name(Constants.HttpHeaderName.APP_ID)
                        .description("appId")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build(),
                new ParameterBuilder()
                        .name(Constants.HttpHeaderName.DEVICE_ID)
                        .description("deviceId")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build(),
                new ParameterBuilder()
                        .name(Constants.HttpHeaderName.ACCESS_TOKEN)
                        .description("deviceId")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build());
    }

    private ApiKey apiKey() {
        return new ApiKey(Constants.HttpHeaderName.ACCESS_TOKEN, "accessToken", "header");
    }

    private List<SecurityContext> securityContexts() {
        return Lists.newArrayList(SecurityContext.builder()
                .securityReferences(Lists.newArrayList(
                        new SecurityReference(Constants.HttpHeaderName.ACCESS_TOKEN, new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})))
                .forPaths(PathSelectors.regex("/user.*"))
                .build());
    }

//    @Bean
//    SecurityConfiguration security() {
//        return new SecurityConfiguration(
//                "test-app-client-id",
//                "test-app-realm",
//                "test-app",
//                "apiKey");
//    }
//
//    @Bean
//    UiConfiguration uiConfig() {
//        return new UiConfiguration(
//                "validatorUrl");
//    }

}

