package com.linktic.prueba.inventory.service.impl;

import com.linktic.prueba.inventory.client.ProductsClient;
import com.linktic.prueba.inventory.dto.PurchaseRequest;
import com.linktic.prueba.inventory.exception.ConflictException;
import com.linktic.prueba.inventory.exception.ResourceNotFoundException;
import com.linktic.prueba.inventory.model.IdempotencyKey;
import com.linktic.prueba.inventory.model.Inventory;
import com.linktic.prueba.inventory.repository.IdempotencyKeyRepository;
import com.linktic.prueba.inventory.repository.InventoryRepository;
import com.linktic.prueba.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ProductsClient productsClient;

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventory(UUID productId) {
        return inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));
    }

    @Override
    @Transactional
    public void purchaseProduct(PurchaseRequest request, String idempotencyKey) {
        // 1. Check Idempotency
        if (idempotencyKey != null && idempotencyKeyRepository.existsById(idempotencyKey)) {
            log.info("Idempotent request detected for key: {}", idempotencyKey);
            return; // Already processed
        }

        // 2. Validate Product Existence (External Call)
        // Note: productsClient.existsProduct will throw exception if service
        // unavailable (handled by global handler)
        // or return false if 404/error.
        // Actually my implementation of client returns boolean for 2xx.
        // I need to handle if it returns false (product not found).
        try {
            if (!productsClient.existsProduct(request.getProductId().toString())) {
                throw new ResourceNotFoundException("Product not found in Products Service: " + request.getProductId());
            }
        } catch (Exception e) {
            // If circuit breaker is open or fallback triggered exception
            throw new ConflictException("Cannot verify product existence: " + e.getMessage());
        }

        // 3. Get Inventory (or create if not valid? No, usually inventory must exist
        // logic)
        // Requirement says "Consultar inventario... debe validar que producto existe".
        // Here we are purchasing.
        Inventory inventory = inventoryRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not initialized for product: " + request.getProductId()));

        // 4. Check Stock
        if (inventory.getAvailable() < request.getQuantity()) {
            throw new ConflictException("Insufficient stock. Available: " + inventory.getAvailable());
        }

        // 5. Deduct Stock
        inventory.setAvailable(inventory.getAvailable() - request.getQuantity());

        try {
            inventoryRepository.save(inventory);
        } catch (OptimisticLockingFailureException e) {
            throw new ConflictException("Concurrency conflict. Please try again.");
        }

        // 6. Save Idempotency Key
        if (idempotencyKey != null) {
            idempotencyKeyRepository.save(IdempotencyKey.builder()
                    .keyId(idempotencyKey)
                    .responseStatus(200)
                    .build());
        }

        log.info("Purchase successful for product {} quantity {}", request.getProductId(), request.getQuantity());
        // TODO: Publish event (InventoryChanged)
    }

    @Override
    @Transactional
    public Inventory addInventory(UUID productId, Integer quantity) {
        Optional<Inventory> existing = inventoryRepository.findById(productId);
        Inventory inventory;
        if (existing.isPresent()) {
            inventory = existing.get();
            inventory.setAvailable(inventory.getAvailable() + quantity);
        } else {
            inventory = Inventory.builder()
                    .productId(productId)
                    .available(quantity)
                    .reserved(0)
                    .version(0L)
                    .build();
        }
        return inventoryRepository.save(inventory);
    }
}
