package com.linktic.prueba.inventory.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@ToString
public class InventoryChangedEvent {
    private final UUID productId;
    private final Integer newAvailableBalance;
    private final LocalDateTime timestamp;
}
