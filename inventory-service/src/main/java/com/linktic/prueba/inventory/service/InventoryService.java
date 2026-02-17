package com.linktic.prueba.inventory.service;

import com.linktic.prueba.inventory.dto.PurchaseRequest;
import com.linktic.prueba.inventory.model.Inventory;

import java.util.UUID;

public interface InventoryService {
    Inventory getInventory(UUID productId);

    void purchaseProduct(PurchaseRequest request, String idempotencyKey);

    Inventory addInventory(UUID productId, Integer quantity);
}
