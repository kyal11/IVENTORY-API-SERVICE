package com.task.repository;

import com.task.entity.Items;
import com.task.constant.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemsRepository extends JpaRepository<Items, UUID> {

    Optional<Items> findByCodeProduct(String codeProduct);

    Optional<Items> findByCodeProductAndStatus(String codeProduct, ItemStatus status);

    boolean existsByCodeProduct(String codeProduct);

    List<Items> findByStatus(ItemStatus status);

    List<Items> findByNameContainingIgnoreCase(String name);

    List<Items> findByStatusIn(List<ItemStatus> statuses);
}
