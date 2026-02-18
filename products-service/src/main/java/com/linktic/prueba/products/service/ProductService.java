package com.linktic.prueba.products.service;

import com.linktic.prueba.products.dto.ProductRequest;
import com.linktic.prueba.products.dto.ProductResponse;
import com.linktic.prueba.products.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface ProductService {
    @NonNull
    ProductResponse createProduct(ProductRequest request);

    @NonNull
    ProductResponse getProductById(@NonNull UUID id);

    @NonNull
    ProductResponse updateProduct(@NonNull UUID id, ProductRequest request);

    void deleteProduct(@NonNull UUID id);

    @NonNull
    Page<ProductResponse> getAllProducts(String search, ProductStatus status,
            @NonNull Pageable pageable);
}
