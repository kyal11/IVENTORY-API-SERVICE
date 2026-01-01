package com.task.inventory.repository;

import com.task.inventory.entity.ItemTransactions;
import com.task.inventory.constant.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsTransactionsRepository extends JpaRepository<ItemTransactions, UUID> {

    List<ItemTransactions> findByItemIdOrderByCreatedAtDesc(UUID itemId);

    List<ItemTransactions> findByTransactionType(TransactionType transactionType);

    List<ItemTransactions> findByItemIdAndFromOwnerIdOrToOwnerId(
            UUID itemId,
            UUID fromOwnerId,
            UUID toOwnerId
    );

    List<ItemTransactions> findByPerformedById(UUID userId);

    List<ItemTransactions> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<ItemTransactions> findByItemIdAndTransactionTypeOrderByCreatedAtDesc(
            UUID itemId,
            TransactionType transactionType
    );

    Page<ItemTransactions> findAll(Pageable pageable);


    @Query("""
        SELECT t FROM ItemTransactions t
        JOIN t.item i
        JOIN t.performedBy u
        WHERE (:itemCode IS NULL OR i.codeProduct LIKE %:itemCode%)
          AND (:itemName IS NULL OR i.name LIKE %:itemName%)
          AND (:transactionType IS NULL OR t.transactionType = :transactionType)
          AND (:performedByUserId IS NULL OR u.id = :performedByUserId)
          AND (:performedByName IS NULL OR u.name LIKE %:performedByName%)
    """)
    Page<ItemTransactions> searchTransactions(
            @Param("itemCode") String itemCode,
            @Param("itemName") String itemName,
            @Param("transactionType") TransactionType transactionType,
            @Param("performedByUserId") UUID performedByUserId,
            @Param("performedByName") String performedByName,
            Pageable pageable
    );

}
