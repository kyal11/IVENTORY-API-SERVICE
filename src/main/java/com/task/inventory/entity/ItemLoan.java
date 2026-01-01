package com.task.inventory.entity;

import com.task.inventory.constant.LoanStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "item_loans")
public class ItemLoan {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    private Items item;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private UUID borrowerId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime borrowedAt;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}

