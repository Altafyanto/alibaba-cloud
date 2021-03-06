package com.springframework.swgger.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author summer
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(value = {SwaggerConfigProperties.class})
public class SwaggerConfig {

    private final SwaggerConfigProperties swaggerConfigProperties;

    public SwaggerConfig(SwaggerConfigProperties swaggerConfigProperties) {
        this.swaggerConfigProperties = swaggerConfigProperties;
    }

    private ApiInfo apiInfo() {
        springfox.documentation.service.Contact contact = null;
        if (swaggerConfigProperties.getContact() != null) {
            contact = new springfox.documentation.service.Contact(swaggerConfigProperties.getContact().getName(), swaggerConfigProperties.getContact().getUrl(), swaggerConfigProperties.getContact().getEmail());
        }
        return new ApiInfoBuilder()
                .title(swaggerConfigProperties.getTitle())
                .description(swaggerConfigProperties.getDescription())
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl(swaggerConfigProperties.getTermsOfServiceUrl())
                .version(swaggerConfigProperties.getVersion())
                .contact(contact)
                .extensions(swaggerConfigProperties.getVendorExtensions())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();
    }
}
