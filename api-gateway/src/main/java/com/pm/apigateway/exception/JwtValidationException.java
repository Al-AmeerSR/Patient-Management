// Package declaration - defines the namespace of this class
package com.pm.apigateway.exception;

// Imports HTTP status codes like 401 UNAUTHORIZED
import org.springframework.http.HttpStatus;

// Imports annotation used to handle exceptions in a method
import org.springframework.web.bind.annotation.ExceptionHandler;

// Combines @ControllerAdvice and @ResponseBody for REST APIs
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Exception thrown when WebClient receives an HTTP error response
import org.springframework.web.reactive.function.client.WebClientResponseException;

// Represents the HTTP request and response in a reactive application
import org.springframework.web.server.ServerWebExchange;

// Reactive type representing a single asynchronous result
import reactor.core.publisher.Mono;

// Marks this class as global exception handler for REST controllers
@RestControllerAdvice
public class JwtValidationException {

    // Handles exceptions of type WebClientResponseException.Unauthorized (HTTP 401)
    @ExceptionHandler(WebClientResponseException.Unauthorized.class)
    public Mono<Void> handelUnauthorized(ServerWebExchange exchange) {
        // Sets the HTTP status code of the response to 401 Unauthorized
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        // Completes the response; returns a Mono that indicates completion
        return exchange.getResponse().setComplete();
    }
}
