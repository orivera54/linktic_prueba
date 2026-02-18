package com.linktic.prueba.inventory.controller;

import com.linktic.prueba.inventory.dto.PurchaseRequest;
import com.linktic.prueba.inventory.model.Inventory;
import com.linktic.prueba.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "API para la gestión de stock y compras")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    @Operation(summary = "Consultar inventario", description = "Obtiene el stock actual disponible para un producto")
    public ResponseEntity<Inventory> getInventory(@PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryService.getInventory(productId));
    }

    @PostMapping("/purchases")
    @Operation(summary = "Realizar compra", description = "Registra una compra y reduce el stock del producto")
    public ResponseEntity<Void> purchase(@Valid @RequestBody PurchaseRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        inventoryService.purchaseProduct(request, idempotencyKey);
        return ResponseEntity.ok().build();
    }

    // Helper endpoint to add inventory (not explicitly requested but needed for
    // testing)
    @PostMapping("/{productId}")
    @Operation(summary = "Agregar stock", description = "Incrementa el inventario de un producto (Endpoint de utilidad)")
    public ResponseEntity<Inventory> addInventory(@PathVariable UUID productId,
            @RequestBody Integer quantity) {
        return ResponseEntity.ok(inventoryService.addInventory(productId, quantity));
    }
}
