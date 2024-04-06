package com.example.swaggerlatest.configuration;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SwaggerConfig {

	@Value("${swagger.openapi.dev-url}")
	private String devUrl;

	@Bean
	public GroupedOpenApi groupedOpenApi() {

		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL in Development environment");

		return GroupedOpenApi.builder().group("employee").pathsToMatch("/**").build();
	}

	// SwaggerUiConfigParameters SwaggerUiConfigProperties

	@Bean
	@Primary
	public SwaggerUiConfigProperties swaggerUiConfigProperties() {
		SwaggerUiConfigProperties configProperties = new SwaggerUiConfigProperties();
		configProperties.setDisplayRequestDuration(true);
		configProperties.setValidatorUrl(StringUtils.EMPTY);
		configProperties.setTryItOutEnabled(true);
		configProperties.setPath("/swagger-ui.html");
		configProperties.setOperationsSorter("method");
		return configProperties;
	}

	@Bean
	public GroupedOpenApi groupedOpenApi1() {

		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL in Development environment");

		return GroupedOpenApi.builder().group("register").pathsToMatch("/registerNewUser/**").build();
	}

	@Bean
	public OpenAPI openAPI() {

		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL in Development environment");

		Info info = new Info().title("Sample Swagger Test").version("2.3.0")
				.description("This API exposes endpoints to manage Latest Swagger");

		OpenAPI openAPI = new OpenAPI().info(info).servers(List.of(devServer));

		PathItem authyenticatePathItem = new PathItem();
		authyenticatePathItem.setPost(new Operation());
		openAPI.path("/authenticate", authyenticatePathItem);

		PathItem employeesPathItem = new PathItem();
		employeesPathItem.setGet(new Operation());
		openAPI.path("/employees", employeesPathItem);

		PathItem employeePathItem = new PathItem();
		employeePathItem.setGet(new Operation());
		openAPI.path("/employee/{id}", employeePathItem);
		
		PathItem registerUserPathItem = new PathItem();
		registerUserPathItem.setPost(new Operation());
		openAPI.path("/registerNewUser", registerUserPathItem);
		
		PathItem urlForUserPathItem =new PathItem();
		urlForUserPathItem.setGet(new Operation());
		openAPI.path("/employee/forUser", urlForUserPathItem);
		
		PathItem urlForAdminPathItem = new PathItem();
		urlForAdminPathItem.setGet(new Operation());
		openAPI.path("/employee/forAdmin", urlForAdminPathItem);

		Pattern pattern = Pattern.compile("/employee/.*");
		
        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String path = entry.getKey();
            PathItem pathItem = entry.getValue();

            if (!path.equals("/authenticate")) {
                if (pattern.matcher(path).matches() || path.equals("/employees")) {
                    pathItem.getGet().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
                }
            }
        }


		return openAPI;

	}
}
