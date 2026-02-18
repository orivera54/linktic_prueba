package com.linktic.prueba.inventory.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryEventListener {

    @EventListener
    public void handleInventoryChangedEvent(InventoryChangedEvent event) {
        log.info("Handling InventoryChangedEvent: {}", event);
        // This is a placeholder for future logic, like notifying other services
        // or updating a read-model.
    }
}
