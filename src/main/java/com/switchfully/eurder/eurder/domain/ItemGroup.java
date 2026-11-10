package com.switchfully.eurder.eurder.domain;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "item_group")
public class ItemGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Eurder eurder;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column(name = "AMOUNT")
    private int amount;

    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "SHIPPING_DATE")
    private LocalDate shippingDate;

    public ItemGroup(Eurder order, Item item, int amount) {
        this.eurder = order;
        this.item = item;
        this.amount = amount;
        this.unitPrice = item.getPrice();  // At instantiation, the item group gets the current price of the item
    }

    public ItemGroup() {
    }

    public Long getId() {
        return id;
    }

    public Eurder getEurder() {
        return eurder;
    }

    public void setEurder(Eurder eurder) {
        this.eurder = eurder;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public BigDecimal calculatePrice() {
        return unitPrice.multiply(BigDecimal.valueOf(amount));
    }

    public BigDecimal getTotalPrice() {
        return calculatePrice();
    }

    public void setShippingDate(LocalDate shipping_date) {
        this.shippingDate = shipping_date;
    }

    public ItemGroup duplicateWithUpdatedItemPrice(Eurder order) {
        return new ItemGroup(
                order,
                this.getItem(),
                this.getAmount()
        );
    }
}
