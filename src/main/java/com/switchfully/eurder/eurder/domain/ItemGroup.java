package com.switchfully.eurder.eurder.domain;

import com.switchfully.eurder.eurder.repository.ItemRepository;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

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
    private double unitPrice;

    @Column(name = "SHIPPING_DATE")
    private LocalDate shipping_date;

    public ItemGroup(Item item, int amount, double unitPrice) {
        this.item = item;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    public ItemGroup() {

    }

    public Long getId() {
        return id;
    }

    public Eurder getEurder() {
        return eurder;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public LocalDate getShipping_date() {
        return shipping_date;
    }

//    public boolean hasShippingDateOf(LocalDate date) {
//        return shippingDate.isEqual(date);
//    }

}
