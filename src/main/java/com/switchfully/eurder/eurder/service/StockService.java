package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

    @Service
    @Transactional
    public class StockService {

        public void processItemGroupStock(ItemGroup group) {
            boolean immediatelyAvailable = setShippingDateBasedOnStock(group);
            if (immediatelyAvailable) {
                subtractFromStock(group.getItem(), group.getAmount());
            }
        }

        private void subtractFromStock(Item item, int requiredAmount) {
            item.subtractAmount(requiredAmount);
        }


    private boolean setShippingDateBasedOnStock(ItemGroup group) {
        boolean available = group.getItem().getAmount() >= group.getAmount();

        group.setShippingDate(
                LocalDate.now().plusDays((available ? 1 : 7))
        );
        return available;
    }
}