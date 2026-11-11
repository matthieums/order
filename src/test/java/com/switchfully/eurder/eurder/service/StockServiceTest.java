package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.factory.OrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    private StockService stockService;

    @BeforeEach
    void setup() {
        stockService = new StockService();
    }

    @Test
    void shouldReduceStockAndShipTomorrow_whenItemIsImmediatelyAvailable() {
            Item item = ItemFactory.itemWithAmount(10);
            Eurder order = OrderFactory.order();
            ItemGroup group = new ItemGroup(order, item, 2);

            stockService.processItemGroupStock(group);

            assertEquals(8, item.getAmount());
            assertEquals(LocalDate.now().plusDays(1), group.getShippingDate());
        }

    @Test
    void shouldShipIn7Days_whenItemIsNotAvailable() {
        Item item = ItemFactory.itemWithAmount(1);
        Eurder order = OrderFactory.order();
        ItemGroup group = new ItemGroup(order, item, 2);

        stockService.processItemGroupStock(group);

        assertEquals(1, item.getAmount());
        assertEquals(LocalDate.now().plusDays(7), group.getShippingDate());
    }
}
