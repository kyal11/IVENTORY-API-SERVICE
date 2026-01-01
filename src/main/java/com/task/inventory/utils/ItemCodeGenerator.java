package com.task.inventory.utils;

import com.task.inventory.repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ItemCodeGenerator {

    private final ItemsRepository itemsRepository;

    private static final String PREFIX = "IVNTR_";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generate() {
        String today = LocalDate.now().format(DATE_FORMAT);
        String codePrefix = PREFIX + today + "_";

        String lastCode = itemsRepository
                .findLastCodeByPrefix(codePrefix)
                .orElse(null);

        int nextNumber = 1;

        if (lastCode != null) {
            String[] parts = lastCode.split("_");
            nextNumber = Integer.parseInt(parts[2]) + 1;
        }

        return codePrefix + String.format("%04d", nextNumber);
    }
}
