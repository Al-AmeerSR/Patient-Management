package com.pm.apigateway.filter;

// Importing necessary Spring and WebFlux components
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Custom JWT validation filter for Spring Cloud Gateway.
 * It verifies if the request has a valid JWT token by calling an external auth service.
 */
@Component // Registers this class as a Spring Bean
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    // WebClient is used to make an HTTP call to the Auth Service for token validation
    private final WebClient webClient;

    /**
     * Constructor that builds the WebClient using the base URL provided in application.yml
     *
     * @param webClientBuilder - Spring's reactive HTTP client builder
     * @param authServiceUrl - Injected from application.yml (auth.service.url), this is the base URL of the auth service
     */
    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl) {
        // Build the WebClient instance with the auth service base URL
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    /**
     * Core logic of the filter. This method is called for every incoming request.
     * It checks the Authorization header, validates the token via /validate endpoint,
     * and proceeds if valid.
     *
     * @param config - No configuration is used, hence type is Object
     * @return GatewayFilter - the logic that gets executed for each request
     */
    @Override
    public GatewayFilter apply(Object config) {

        // Returning a lambda filter implementation
        return (exchange, chain) -> {
            // Extracting the "Authorization" header from the request
            String token = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            // If token is missing or doesn't start with "Bearer ", reject the request
            if (token == null || !token.startsWith("Bearer ")) {
                // Set the HTTP status code to 401 Unauthorized
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                // Complete the response without forwarding the request
                return exchange.getResponse().setComplete();
            }

            // If token is present, call the /validate endpoint of the Auth Service
            return webClient.get()
                    .uri("/validate") // Endpoint to validate JWT
                    .header(HttpHeaders.AUTHORIZATION, token) // Pass the same token in header
                    .retrieve() // Send the GET request
                    .toBodilessEntity() // Ignore the response body, only care if it's successful
                    .then(chain.filter(exchange)); // If valid, continue with the next filter/route
        };
    }
}
