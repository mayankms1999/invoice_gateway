
//package com.ims.routes;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
//import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.function.RequestPredicates;
//import org.springframework.web.servlet.function.RouterFunction;
//import org.springframework.web.servlet.function.ServerResponse;

//@Configuration
//public class Routes {
//
//    @Value("${spring.product.port}")
//    private String productPort;
//
//    @Value("${spring.invoice.port}")
//    private String invoicePort;
//
//    @Bean
//    public RouterFunction<ServerResponse> productServiceRoute() {
//        System.out.println(productPort+" : "+invoicePort);
//        return GatewayRouterFunctions.route("product_service")
//                .route(RequestPredicates.path("/api/product"), request -> {
//                    System.out.println("Matched /api/product: " + request.requestPath());
//                    return ServerResponse.ok().build();
//                })
//                .route(RequestPredicates.GET("/api/invoices"),
//                        HandlerFunctions.http("http://product-service:" + productPort))
//                .route(RequestPredicates.path("/api/product/by-name"),
//                        HandlerFunctions.http("http://product-service:" + productPort))
//                .route(RequestPredicates.path("/api/product/{id}")
//                                .and(request -> request.pathVariable("id").matches("\\d+")),
//                        HandlerFunctions.http("http://product-service:" + productPort))
//                .route(RequestPredicates.DELETE("/api/product/{id}")
//                                .and(request -> request.pathVariable("id").matches("\\d+")),
//                        HandlerFunctions.http("http://product-service:" + productPort))
//                .build();
//    }
//
//    @Bean
//    public RouterFunction<ServerResponse> invoiceServiceRoute() {
//        return GatewayRouterFunctions.route("invoice_service")
//                .route(RequestPredicates.GET("/api/invoices"),
//                        HandlerFunctions.http("http://invoice-service:" + invoicePort))
//                .route(RequestPredicates.GET("/api/invoices/{id}")
//                                .and(request -> request.pathVariable("id").matches("\\d+")),
//                        HandlerFunctions.http("http://invoice-service:" + invoicePort))
//                .route(RequestPredicates.GET("/api/invoices/items/{itemId}")
//                                .and(request -> request.pathVariable("itemId").matches("\\d+")),
//                        HandlerFunctions.http("http://invoice-service:" + invoicePort))
//                .route(RequestPredicates.POST("/api/invoices"),
//                        HandlerFunctions.http("http://invoice-service:" + invoicePort))
//                .route(RequestPredicates.DELETE("/api/invoices/{id}")
//                                .and(request -> request.pathVariable("id").matches("\\d+")),
//                        HandlerFunctions.http("http://invoice-service:" + invoicePort))
//                .build();
//    }
//
//
//
//}
//
package com.ims.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
public class Routes {

    @Value("${spring.product.port}")
    private String productPort;

    @Value("${spring.invoice.port}")
    private String invoicePort;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        System.out.println("🔹 Product Service Running on Port: " + productPort);

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.POST("/api/product/create"),
                        HandlerFunctions.http("http://localhost:" + productPort + "/api/product/create"))

                .route(RequestPredicates.GET("/api/product/get-all"),
                        HandlerFunctions.http("http://localhost:" + productPort + "/api/product/get-all"))

                .route(RequestPredicates.GET("/api/product/get/{id}")
                                .and(request -> request.pathVariable("id").matches("\\d+")),
                        request -> HandlerFunctions.http(buildUrl("http://localhost:" + productPort + "/api/product/get/", request))
                                .handle(request))

                .route(RequestPredicates.GET("/api/product/get/by-name"),
                        HandlerFunctions.http("http://localhost:" + productPort + "/api/product/get/by-name"))

                .route(RequestPredicates.DELETE("/api/product/delete/{id}")
                                .and(request -> request.pathVariable("id").matches("\\d+")),
                        request -> HandlerFunctions.http(buildUrl("http://localhost:" + productPort + "/api/product/delete/", request))
                                .handle(request))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> invoiceServiceRoute() {
        System.out.println("🔹 Invoice Service Running on Port: " + invoicePort);

        return GatewayRouterFunctions.route("invoice_service")
                .route(RequestPredicates.POST("/api/invoices/create"),
                        HandlerFunctions.http("http://localhost:" + invoicePort + "/api/invoices/create"))

                .route(RequestPredicates.GET("/api/invoices/get-all"),
                        HandlerFunctions.http("http://localhost:" + invoicePort + "/api/invoices/get-all"))

                .route(RequestPredicates.GET("/api/invoices/get/{id}")
                                .and(request -> request.pathVariable("id").matches("\\d+")),
                        request -> HandlerFunctions.http(buildUrl("http://localhost:" + invoicePort + "/api/invoices/get/", request))
                                .handle(request))

                .route(RequestPredicates.DELETE("/api/invoices/delete/{id}")
                                .and(request -> request.pathVariable("id").matches("\\d+")),
                        request -> HandlerFunctions.http(buildUrl("http://localhost:" + invoicePort + "/api/invoices/delete/", request))
                                .handle(request))
                .build();
    }

    private URI buildUrl(String baseUrl, ServerRequest request) {
        String pathVarName = request.pathVariables().keySet().iterator().next();
        String id = request.pathVariable(pathVarName);
        return UriComponentsBuilder.fromHttpUrl(baseUrl + id).build().toUri();
    }
}
