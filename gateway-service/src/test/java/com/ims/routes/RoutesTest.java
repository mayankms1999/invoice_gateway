//package com.ims.routes;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.web.servlet.function.ServerRequest;
//
//
//import org.springframework.web.servlet.function.RouterFunction;
//import org.springframework.web.servlet.function.ServerResponse;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//
//@SpringBootTest
//class RoutesTest {
//
//    @Autowired
//    private Routes routes;
//
//    private void debugRouteMatch(String path) {
//        boolean isMatched = routes.productServiceRoute().route(mockRequest(path)).isPresent();
//        System.out.println("Route Match for " + path + " -> " + isMatched);
//    }
//    @Test
//    void productServiceRoute_shouldContainCorrectRoutes() {
//        RouterFunction<ServerResponse> productRoute = routes.productServiceRoute();
//        assertThat(productRoute).isNotNull();
//
//        debugRouteMatch("/api/product"); // Should be true
//        debugRouteMatch("/api/product/by-name"); // Should be true
//        debugRouteMatch("/api/product/123"); // Should be true
//        debugRouteMatch("/api/product/non-existent"); // Should be false
//
//        assertThat(productRoute.route(mockRequest("/api/product")).isPresent()).isTrue();
//        assertThat(productRoute.route(mockRequest("/api/product/by-name")).isPresent()).isTrue();
//        assertThat(productRoute.route(mockRequest("/api/product/123")).isPresent()).isTrue();
//        assertThat(productRoute.route(mockRequest("/api/product/non-existent")).isPresent()).isFalse();
//    }
//
//
//    @Test
//    void invoiceServiceRoute_shouldContainCorrectRoutes() {
//        RouterFunction<ServerResponse> invoiceRoute = routes.invoiceServiceRoute();
//        assertThat(invoiceRoute).isNotNull();
//
//        // Valid routes
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices")).isPresent()).isTrue();
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices/123")).isPresent()).isTrue();
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices/items/456")).isPresent()).isTrue();
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices")).isPresent()).isTrue();
//
//        // Invalid routes
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices/abc")).isPresent()).isFalse();
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices/items/xyz")).isPresent()).isFalse();
//        assertThat(invoiceRoute.route(mockRequest("/api/invoices/invalid")).isPresent()).isFalse();
//    }
//
//    private ServerRequest mockRequest(String path) {
//        MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", path);
//        return ServerRequest.create(servletRequest, List.of());
//    }
//
//}
//
//
