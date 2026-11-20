package com.switchfully.order.service;

import com.switchfully.order.IntegrationTest;
import com.switchfully.order.domain.Item;
import com.switchfully.order.domain.OrderItemCreatedEvent;
import com.switchfully.order.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.switchfully.order.domain.ItemTestBuilder.anItem;
import static com.switchfully.order.domain.OrderItemTestBuilder.anOrderItem;

class ItemEventHandlerTest extends IntegrationTest{

    @Autowired
    private ItemEventHandler.OrderItemCreatedEventListener orderItemCreatedEventListener;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void onApplicationEvent_givenAnOrderItemCreatedEvent_thenUpdateStockOfItem() {
        Item persistedItem = itemRepository.save(anItem().withAmountOfStock(10).build());

        orderItemCreatedEventListener.onApplicationEvent(new OrderItemCreatedEvent(anOrderItem()
                .withItemId(persistedItem.getId()).withOrderedAmount(4).build()));

        Assertions.assertThat(itemRepository.get(persistedItem.getId()).getAmountOfStock())
                .isEqualTo(6);
    }

}
