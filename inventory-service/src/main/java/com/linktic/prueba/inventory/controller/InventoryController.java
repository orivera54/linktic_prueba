package com.linktic.prueba.inventory.controller;

import com.linktic.prueba.inventory.dto.PurchaseRequest;
import com.linktic.prueba.inventory.model.Inventory;
import com.linktic.prueba.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryService.getInventory(productId));
    }

    @PostMapping("/purchases")
    public ResponseEntity<Void> purchase(@Valid @RequestBody PurchaseRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        inventoryService.purchaseProduct(request, idempotencyKey);
        return ResponseEntity.ok().build();
    }

    // Helper endpoint to add inventory (not explicitly requested but needed for
    // testing)
    @PostMapping("/{productId}")
    public ResponseEntity<Inventory> addInventory(@PathVariable UUID productId, @RequestBody Integer quantity) {
        return ResponseEntity.ok(inventoryService.addInventory(productId, quantity));
    }
}
