package com.task.repository;

import com.task.entity.ItemsLogs;
import com.task.constant.ItemLogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsLogsRepository extends JpaRepository<ItemsLogs, UUID> {
    List<ItemsLogs> findByItemIdOrderByCreatedAtDesc(UUID itemId);

    List<ItemsLogs> findByLogType(ItemLogType logType);

    List<ItemsLogs> findByPerformedById(UUID userId);

    List<ItemsLogs> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<ItemsLogs> findByTransactionId(UUID transactionId);
}
