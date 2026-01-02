package com.task.inventory.repository;

import com.task.inventory.entity.ItemLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemLogsRepository extends JpaRepository<ItemLogs, UUID> {
}

