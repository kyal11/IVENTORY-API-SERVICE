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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owners owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private Owners borrower;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime borrowedAt;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private LocalDateTime returnedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_transaction_id")
    private ItemTransactions borrowTransaction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_transaction_id")
    private ItemTransactions returnTransaction;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}

