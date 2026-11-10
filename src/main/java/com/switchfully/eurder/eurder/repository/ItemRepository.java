package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
