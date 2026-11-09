package com.switchfully.eurder.eurder.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;

    @BeforeEach
    void setup() {
        item = new Item(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10
        );
    }

    @Test
    void givenValidAmount_whenSubtractingStock_thenStockIsReduced() {
        item.subtractAmount(3);

        assertEquals(7, item.getAmount());
    }

    @Test
    void givenAmountGreaterThanStock_whenSubtracting_thenStockCannotBecomeNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> item.subtractAmount(11)
        );

        assertEquals(10, item.getAmount());
    }

    @Test
    void givenZeroStock_whenSubtracting_thenStockCannotBecomeNegative() {
        item.subtractAmount(10);

        assertEquals(0, item.getAmount());
    }

    @Test
    void givenNegativeAmount_whenSettingStock_thenExceptionIsThrown() {
        assertThrows(
                IllegalArgumentException.class,
                () -> item.setAmount(-1)
        );
    }

    @Test
    void givenNegativeAmount_whenSubtracting_thenExceptionIsThrown() {
        assertThrows(
                IllegalArgumentException.class,
                () -> item.subtractAmount(-1)
        );
    }

}