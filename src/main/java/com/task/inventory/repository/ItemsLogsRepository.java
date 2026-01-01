package com.task.inventory.repository;

import com.task.inventory.entity.ItemLogs;
import com.task.inventory.constant.ItemLogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsLogsRepository extends JpaRepository<ItemLogs, UUID> {
    List<ItemLogs> findByItemIdOrderByCreatedAtDesc(UUID itemId);

    List<ItemLogs> findByLogType(ItemLogType logType);

    List<ItemLogs> findByPerformedById(UUID userId);

    List<ItemLogs> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<ItemLogs> findByTransactionId(UUID transactionId);
}
