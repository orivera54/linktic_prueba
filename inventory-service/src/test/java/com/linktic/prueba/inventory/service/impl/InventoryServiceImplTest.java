package com.linktic.prueba.inventory.service.impl;

import com.linktic.prueba.inventory.client.ProductsClient;
import com.linktic.prueba.inventory.dto.PurchaseRequest;
import com.linktic.prueba.inventory.event.InventoryChangedEvent;
import com.linktic.prueba.inventory.model.Inventory;
import com.linktic.prueba.inventory.repository.IdempotencyKeyRepository;
import com.linktic.prueba.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private IdempotencyKeyRepository idempotencyKeyRepository;
    @Mock
    private ProductsClient productsClient;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private UUID productId;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        inventory = Inventory.builder()
                .productId(productId)
                .available(10)
                .reserved(0)
                .version(0L)
                .build();
    }

    @Test
    void purchaseProduct_ShouldPublishEvent() {
        // Arrange
        PurchaseRequest request = new PurchaseRequest();
        request.setProductId(productId);
        request.setQuantity(2);

        when(idempotencyKeyRepository.existsById(any())).thenReturn(false);
        when(productsClient.existsProduct(any())).thenReturn(true);
        when(inventoryRepository.findById(productId)).thenReturn(Optional.of(inventory));

        // Act
        inventoryService.purchaseProduct(request, "valid-key");

        // Assert
        verify(inventoryRepository).save(any(Inventory.class));

        ArgumentCaptor<InventoryChangedEvent> eventCaptor = ArgumentCaptor.forClass(InventoryChangedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        InventoryChangedEvent event = eventCaptor.getValue();
        assertEquals(productId, event.getProductId());
        assertEquals(8, event.getNewAvailableBalance());
    }

    @Test
    void addInventory_ShouldPublishEvent() {
        // Arrange
        when(inventoryRepository.findById(productId)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // Act
        inventoryService.addInventory(productId, 5);

        // Assert
        verify(inventoryRepository).save(any(Inventory.class));

        ArgumentCaptor<InventoryChangedEvent> eventCaptor = ArgumentCaptor.forClass(InventoryChangedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        InventoryChangedEvent event = eventCaptor.getValue();
        assertEquals(productId, event.getProductId());
        assertEquals(15, event.getNewAvailableBalance());
    }
}
