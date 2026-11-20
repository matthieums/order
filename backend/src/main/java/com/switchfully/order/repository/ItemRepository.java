package com.switchfully.order.repository;

import com.switchfully.order.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ItemRepository extends BaseRepository<Item> {

    @Override
    public List<Item> getAll() {
        return getEntityManager()
                .createQuery("FROM Item", Item.class)
                .getResultList();
    }

    @Override
    public Item get(UUID entityId) {
        return getEntityManager()
                .createQuery("FROM Item WHERE id = :id", Item.class)
                    .setParameter("id", entityId.toString())
                .getSingleResult();
    }
}
