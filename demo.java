package com.honghu.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.honghu.cloud.commonservice.apigateway.filter.OAuthTokenFilter;

@SpringBootApplication
@EnableZuulProxy
@RestController
public class ApiGatewayApplication implements CommandLineRunner {
	
	private static final Logger  logger = LoggerFactory.getLogger(ApiGatewayApplication.class);
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiGatewayApplication.class).web(true).run(args);
    }

    @Bean
    @Profile("CORS")
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
    @Bean
	public OAuthTokenFilter oAuthTokenFilter() {
		return new OAuthTokenFilter();
	}
    
    @Override
    public void run(String... args) throws Exception {
        try {
        	// 动态加载zuul filters
            //FilterLoader.getInstance().setCompiler(new GroovyCompiler());
            //FilterFileManager.setFilenameFilter(new GroovyFileFilter());
            //FilterFileManager.init(10,new String [] {"groovy/filters/pre","groovy/filters/route","groovy/filters/post"});
        } catch (Exception e) {
        	logger.error("zuul filters load error",e);
            throw new RuntimeException(e);
        }
    }
    
    @GetMapping("/")
    public String index(){
    	return "this is api gateway";
    }
}
