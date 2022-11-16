package com.saber.spring_camel_web_mvc_demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.saber.spring_camel_web_mvc_demo.site"
        ,includeFilters = @ComponentScan.Filter({Component.class})
)
@PropertySource(value = "classpath:application.yml")
//@EnableSwagger2
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,true)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true)
                .configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT,true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,true)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,true)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(mapper());
        jackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        jackson2HttpMessageConverter.setPrettyPrint(true);
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_XML);
        jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        return jackson2HttpMessageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());
    }

    @Bean
    public CamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext springCamelContext = new SpringCamelContext(applicationContext);
        springCamelContext.setAutoStartup(true);
        springCamelContext.setDumpRoutes(true);
        String[] names = applicationContext.getBeanNamesForAnnotation(Component.class);
        for (String name : names) {
            springCamelContext.addRoutes((RoutesBuilder) applicationContext.getBean(name));
        }
        return springCamelContext;
    }

//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("TITLE")
//                .description("DESCRIPTION")
//                .version("VERSION")
//                .termsOfServiceUrl("http://terms-of-services.url")
//                .license("LICENSE")
//                .licenseUrl("http://url-to-license.com")
//                .build();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
