package com.linktic.prueba.products.service.impl;

import com.linktic.prueba.products.dto.ProductRequest;
import com.linktic.prueba.products.dto.ProductResponse;
import com.linktic.prueba.products.exception.ConflictException;
import com.linktic.prueba.products.exception.ResourceNotFoundException;
import com.linktic.prueba.products.model.Product;
import com.linktic.prueba.products.model.ProductStatus;
import com.linktic.prueba.products.repository.ProductRepository;
import com.linktic.prueba.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    @NonNull
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();

        Product savedProduct = Objects.requireNonNull(productRepository.save(product));
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public ProductResponse getProductById(@NonNull UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    @Transactional
    @NonNull
    public ProductResponse updateProduct(@NonNull UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Check if SKU is being changed to one that already exists
        if (!product.getSku().equals(request.getSku()) && productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("Product with SKU " + request.getSku() + " already exists");
        }

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());

        Product updatedProduct = Objects.requireNonNull(productRepository.save(product));
        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(@NonNull UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<ProductResponse> getAllProducts(String search, ProductStatus status,
            @NonNull Pageable pageable) {
        Page<Product> products;

        if (search != null && !search.isEmpty()) {
            // Basic search implementation - could be improved with Specifications
            products = productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(search, search,
                    pageable);
        } else if (status != null) {
            products = productRepository.findByStatus(status, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::mapToResponse);
    }

    @NonNull
    private ProductResponse mapToResponse(@NonNull Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
