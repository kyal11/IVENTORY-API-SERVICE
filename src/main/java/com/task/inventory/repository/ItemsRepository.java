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

import java.time.LocalDateTime;
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

    @Query("""
        SELECT i FROM Items i
        WHERE (:name IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:code IS NULL OR i.codeProduct = :code)
          AND (:status IS NULL OR i.status = :status)
          AND (:minQty IS NULL OR i.totalQuantity >= :minQty)
          AND (CAST(:startDate AS date) IS NULL OR i.createdAt >= :startDate)
          AND (CAST(:endDate AS date) IS NULL OR i.createdAt <= :endDate)
    """)
    Page<Items> searchItems(
            @Param("name") String name,
            @Param("code") String code,
            @Param("status") ItemStatus status,
            @Param("minQty") Integer minQty,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
