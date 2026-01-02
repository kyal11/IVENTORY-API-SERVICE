package com.task.inventory.services;

import com.task.inventory.constant.ItemLogType;
import com.task.inventory.entity.ItemLogs;
import com.task.inventory.entity.ItemTransactions;
import com.task.inventory.entity.Items;
import com.task.inventory.entity.Users;
import com.task.inventory.repository.ItemLogsRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemLogsService {

    private final ItemLogsRepository itemLogsRepository;
    private final EntityManager entityManager;

    public void log(
            UUID itemId,
            UUID transactionId,
            ItemLogType logType,
            String message,
            String beforeState,
            String afterState,
            UUID performedByUserId
    ) {
        ItemLogs log = new ItemLogs();

        log.setItem(entityManager.getReference(Items.class, itemId));

        if (transactionId != null) {
            log.setTransaction(
                    entityManager.getReference(ItemTransactions.class, transactionId)
            );
        }

        if (performedByUserId != null) {
            log.setPerformedBy(
                    entityManager.getReference(Users.class, performedByUserId)
            );
        }
        log.setLogType(logType);
        log.setMessage(message);
        log.setBeforeState(beforeState);
        log.setAfterState(afterState);

        itemLogsRepository.save(log);
    }
}

