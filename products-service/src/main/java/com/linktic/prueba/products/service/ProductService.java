package com.linktic.prueba.products.service;

import com.linktic.prueba.products.dto.ProductRequest;
import com.linktic.prueba.products.dto.ProductResponse;
import com.linktic.prueba.products.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(UUID id);

    ProductResponse updateProduct(UUID id, ProductRequest request);

    void deleteProduct(UUID id);

    Page<ProductResponse> getAllProducts(String search, ProductStatus status, Pageable pageable);
}
