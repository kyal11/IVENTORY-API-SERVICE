package com.task.entity;

import com.task.constant.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "item_transactions")
public class ItemsTransactions {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;


    @Column(name = "from_owner_id")
    private UUID fromOwnerId;


    @Column(name = "to_owner_id")
    private UUID toOwnerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;


    @Column(nullable = false)
    private Integer quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_user_id", nullable = false)
    private Users performedBy;


    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
