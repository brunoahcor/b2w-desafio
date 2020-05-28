package br.com.b2w.desafio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.b2w.desafio.apis"))
                .build()
                .apiInfo(metaData());
    }
    
	@SuppressWarnings("deprecation")
	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("Desafio B2W REST API",
				"Desafio B2W REST API", "1.0",
				"Termos de serviço",
				"Bruno Rocha",
				"B2W License Version 1.0",
				"http://localhost:8080/licenses/LICENSE-1.0");
        return apiInfo;
    }
    
}