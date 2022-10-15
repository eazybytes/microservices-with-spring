package com.eaztbytes.gatewayserver;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Autowired
	private TokenRelayGatewayFilterFactory filterFactory;

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route(p -> p
	            .path("/eazybank/accounts/**")
	            .filters(f -> f.filters(filterFactory.apply())
					.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
					.removeRequestHeader("Cookie"))
	            .uri("lb://ACCOUNTS")).
	        route(p -> p
		            .path("/eazybank/loans/**")
					.filters(f -> f.filters(filterFactory.apply())
							.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")
							.removeRequestHeader("Cookie"))
		            .uri("lb://LOANS")).
	        route(p -> p
		            .path("/eazybank/cards/**")
					.filters(f -> f.filters(filterFactory.apply())
							.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}")
							.removeRequestHeader("Cookie"))
		            .uri("lb://CARDS")).build();
	}

}
