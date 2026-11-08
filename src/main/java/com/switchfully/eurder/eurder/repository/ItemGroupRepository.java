package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
    List<ItemGroup> findAllByEurder_Id(Long eurderId);
    List<ItemGroup> findAllByShipping_dateEquals(LocalDate date);

}
