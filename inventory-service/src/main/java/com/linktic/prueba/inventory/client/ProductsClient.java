package com.linktic.prueba.inventory.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class ProductsClient {

    private final RestClient restClient;

    public ProductsClient(@Value("${products.service.url}") String productsServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(productsServiceUrl)
                .build();
    }

    @CircuitBreaker(name = "productsService", fallbackMethod = "existsProductFallback")
    @Retry(name = "productsService")
    public boolean existsProduct(String productId) {
        try {
            return restClient.get()
                    .uri("/{id}", productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity()
                    .getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error calling products service", e);
            throw e;
        }
    }

    public boolean existsProductFallback(String productId, Throwable t) {
        log.error("Fallback: Products service is unavailable or returned error for product {}", productId, t);
        // Fallback logic:
        // Strict consistency: return false (prevent purchase)
        // Degraded availablity: might depend on business rule.
        // For this test, if we cannot verify product exists, we should probably fail
        // safe.
        // But the requirement says "Manejo de degradación: si Inventory no responde, el
        // frontend debe ver un error claro".
        // Here we are in Inventory calling Products. If Products is down, we cannot
        // verify existence.
        // We should throw an exception that indicates service unavailability.
        throw new RuntimeException("Products Service Unavailable");
    }
}
