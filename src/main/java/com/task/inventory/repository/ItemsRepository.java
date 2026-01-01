package com.task.inventory.repository;

import com.task.inventory.dto.item.ItemRes;
import com.task.inventory.entity.Items;
import com.task.inventory.constant.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemsRepository extends JpaRepository<Items, UUID> {

    Optional<Items> findByCodeProduct(String codeProduct);

    Optional<Items> findByCodeProductAndStatus(String codeProduct, ItemStatus status);

    boolean existsByCodeProduct(String codeProduct);

    @Query("""
        SELECT i.codeProduct FROM Items i
        WHERE i.codeProduct LIKE :prefix%
        ORDER BY i.codeProduct DESC
    """)
    Optional<String> findLastCodeByPrefix(@Param("prefix") String prefix);

    List<Items> findByStatus(ItemStatus status);

    List<Items> findByNameContainingIgnoreCase(String name);

    List<Items> findByStatusIn(List<ItemStatus> statuses);

    Page<Items> findAll(Pageable pageable);
}
