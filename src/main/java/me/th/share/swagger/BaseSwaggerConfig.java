package me.th.share.swagger;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseSwaggerConfig {

    @Value("${security.jwt.header:Authorization}")
    private String securityHeader;

    @Value("${swagger.enabled:true}")
    private Boolean enabled;

    public abstract SwaggerProperties swaggerProperties();

    @Bean
    public Docket createRestApi(TypeResolver typeResolver) {
        SwaggerProperties swaggerProperties = swaggerProperties();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(List.class, LocalDateTime.class),
                                typeResolver.resolve(List.class, Date.class)
                        )
                )
                .directModelSubstitute(LocalDateTime.class, Date.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(ZonedDateTime.class, String.class)
                .directModelSubstitute(Timestamp.class, Date.class)
                .enable(enabled)
                .pathMapping("/")
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .paths(PathSelectors.regex("^(?!/error).*"))
                .paths(PathSelectors.any())
                .build();
        if (swaggerProperties.isEnableSecurity()) {
            // 添加登录认证
            docket.securitySchemes(securitySchemes())
                    .securityContexts(securityContexts());
        }
        return docket;
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle() + " 服务API接口文档")
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(),
                        swaggerProperties.getContactEmail()))
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> securitySchemes = new ArrayList<>();
        ApiKey apiKey = new ApiKey(securityHeader, securityHeader, "header");
        securitySchemes.add(apiKey);
        return securitySchemes;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        SecurityContext securityContext = SecurityContext.builder()
                .securityReferences(securityReferences())
                .operationSelector(operationContext -> operationContext.requestMappingPattern().matches("^(?!/auth).*$"))
                .build();
        securityContexts.add(securityContext);
        return securityContexts;
    }

    private List<SecurityReference> securityReferences() {
        List<SecurityReference> securityReference = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        securityReference.add(new SecurityReference(securityHeader, authorizationScopes));
        return securityReference;
    }
}
