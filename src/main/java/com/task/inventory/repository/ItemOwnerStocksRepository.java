package com.task.inventory.repository;

import com.task.inventory.entity.ItemOwnerStocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemOwnerStocksRepository extends JpaRepository<ItemOwnerStocks, UUID> {

    List<ItemOwnerStocks> findByItemId(UUID itemId);

    List<ItemOwnerStocks> findByOwnerId(UUID ownerId);

    Optional<ItemOwnerStocks> findByItemIdAndOwnerId(UUID itemId, UUID ownerId);

    boolean existsByItemIdAndOwnerId(UUID itemId, UUID ownerId);

    List<ItemOwnerStocks> findByItemIdAndQuantityGreaterThan(UUID itemId, Integer quantity);

    @Query("""
        SELECT COALESCE(SUM(ios.borrowedQuantity), 0)
        FROM ItemOwnerStocks ios
        WHERE ios.item.id = :itemId
        """)
    Integer getSumBorrowedQtyByItemId(@Param("itemId") UUID itemId);
}
