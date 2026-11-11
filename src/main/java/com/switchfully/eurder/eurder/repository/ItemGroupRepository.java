package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
    List<ItemGroup> findAllByEurder_Id(Long eurderId);
    List<ItemGroup> findAllByEurder_IdIn(List<Long> orderIds);
    List<ItemGroup> findAllByShippingDate(LocalDate date);
}
