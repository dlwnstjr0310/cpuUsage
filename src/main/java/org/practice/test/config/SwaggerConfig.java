package org.practice.test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig implements WebMvcConfigurer {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
				.title("testAPI")
				.version("v1")
				.description("과제 API 문서");


		return new OpenAPI()
				.info(info)
				.addServersItem(new Server().url("/"));
	}
}
