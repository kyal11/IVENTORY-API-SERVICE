package com.task.repository;

import com.task.entity.ItemsTransactions;
import com.task.constant.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
}
