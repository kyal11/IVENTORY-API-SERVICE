package com.task.entity;

import com.task.constant.ItemStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(
        name = "items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code_product")
        }
)
public class Items {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;


    @Column(name = "code_product", nullable = false, unique = true)
    private String codeProduct;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemOwnerStocks> ownerStocks;

}
