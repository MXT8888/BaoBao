package cn.xiaoandx.conf;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@SuppressWarnings("unchecked")
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${sys.version}")
	private String systemPublish;
	@Bean
    public Docket taskApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("任务相关API")
                .pathMapping("/")
                .select()
                .paths(or(regex("/v1/open/task/.*")))//过滤的接口
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("任务相关API")
                        .version(systemPublish)
                        .build());
    }
	
	@Bean
    public Docket parApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("参与相关API")
                .pathMapping("/")
                .select()
                .paths(or(regex("/v1/open/participant/.*")))//过滤的接口
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("参与相关API")
                        .version(systemPublish)
                        .build());
    }
}