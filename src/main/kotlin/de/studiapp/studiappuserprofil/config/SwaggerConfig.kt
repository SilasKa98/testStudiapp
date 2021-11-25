package de.studiapp.studiappuserprofil

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api() : Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("de.studiapp.studiappuserprofil"))
            .paths(PathSelectors.any())
            .build().apiInfo(metaData())
    }

    fun metaData() : ApiInfo{
        return ApiInfoBuilder()
            .title("UserProfil Service - API Description")
            .description("Description how to use the UserProfil Microservice")
            .version("0.1")
            .build()
    }
}