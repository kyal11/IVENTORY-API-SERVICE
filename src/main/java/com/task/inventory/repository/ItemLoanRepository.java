package com.task.inventory.repository;

import com.task.inventory.entity.ItemLoan;
import com.task.inventory.constant.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemLoanRepository extends JpaRepository<ItemLoan, UUID> {

    List<ItemLoan> findByItemId(UUID itemId);

    List<ItemLoan> findByStatus(LoanStatus status);

    Optional<ItemLoan> findByIdAndStatus(UUID id, LoanStatus status);

    List<ItemLoan> findByItemIdAndStatus(UUID itemId, LoanStatus status);

    boolean existsByItemIdAndStatus(UUID itemId, LoanStatus status);

    @Query("""
        SELECT l
        FROM ItemLoan l
        JOIN l.item i
        WHERE i.codeProduct = :codeProduct
        ORDER BY l.borrowedAt DESC
    """)
    List<ItemLoan> findAllItemLoanHistoryByCodeProduct(
            @Param("codeProduct") String codeProduct
    );

    @Query("""
        SELECT l
        FROM ItemLoan l
        JOIN l.item i
        WHERE i.codeProduct = :codeProduct
          AND l.status = 'BORROWED'
        ORDER BY l.borrowedAt DESC
    """)
    List<ItemLoan> findActiveBorrowedByCodeProduct(
            @Param("codeProduct") String codeProduct
    );

    List<ItemLoan> findByBorrowedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
        SELECT l
        FROM ItemLoan l
        WHERE l.status = 'BORROWED'
          AND l.borrowedAt < :threshold
    """)
    List<ItemLoan> findOverdueLoans(LocalDateTime threshold);
}

