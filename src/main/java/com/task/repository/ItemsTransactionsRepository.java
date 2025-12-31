package com.task.repository;

import com.task.entity.ItemsTransactions;
import com.task.constant.TransactionType;
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
public interface ItemsTransactionsRepository extends JpaRepository<ItemsTransactions, UUID> {

    List<ItemsTransactions> findByItemIdOrderByCreatedAtDesc(UUID itemId);

    List<ItemsTransactions> findByTransactionType(TransactionType transactionType);

    List<ItemsTransactions> findByItemIdAndFromOwnerIdOrToOwnerId(
            UUID itemId,
            UUID fromOwnerId,
            UUID toOwnerId
    );

    List<ItemsTransactions> findByPerformedById(UUID userId);

    List<ItemsTransactions> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<ItemsTransactions> findByItemIdAndTransactionTypeOrderByCreatedAtDesc(
            UUID itemId,
            TransactionType transactionType
    );

    Page<ItemsTransactions> findAllTransactions(Pageable pageable);


    @Query("""
            SELECT t FROM ItemsTransactions t
            JOIN t.items i
            JOIN t.users u
            WHERE (:itemCode IS NULL OR i.codeProduct LIKE %:itemCode%)
            AND (:itemName IS NULL OR i.name LIKE %:itemName%)
            AND (:transactionType IS NULL OR t.transactionType = :transactionType)
            AND (:performedByUserId IS NULL OR u.id = :performedByUserId)
            AND (:performedByName IS NULL OR u.username LIKE %:performedByName%)
            """)
    Page<ItemsTransactions> searchTransactions(
            @Param("itemCode") String itemCode,
            @Param("itemName") String itemName,
            @Param("transactionType") TransactionType transactionType,
            @Param("performedByUserId") UUID performedByUserId,
            @Param("performedByName") String performedByName,
            Pageable pageable
    );
}
