//package com.example.NotificationSender.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.media.Content;
//import io.swagger.v3.oas.models.media.MediaType;
//import io.swagger.v3.oas.models.responses.ApiResponse;
//import io.swagger.v3.oas.models.responses.ApiResponses;
//import org.springdoc.core.SpringDocConfigProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        // Define a Content type for the response
//        Map<String, Content> contentMap = new HashMap<>();
//        Content content = new Content()
//                .addMediaType("application/json", new MediaType().schema(new io.swagger.v3.oas.models.media.Schema()));
//        contentMap.put("200", content);
//
//        // Define API responses
//        ApiResponses apiResponses = new ApiResponses()
//                .addApiResponse("200", new ApiResponse().description("Success").content(content));
//
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Notification API")
//                        .version("1.0")
//                        .description("API for sending notifications"))
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addResponses("200", new ApiResponse().description("Success").content(content)));
//    }
//}



package com.example.NotificationSender.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification API")
                        .version("1.0")
                        .description("API for sending notifications"));
    }
}
