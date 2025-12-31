package com.task.repository;

import com.task.entity.Owners;
import com.task.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsernameAndActiveTrue(String username);

    Optional<Users> findByEmailAndActiveTrue(String email);

    @Query("SELECT u FROM Users u WHERE u.deletedAt IS NULL")
    List<Users> findAllUsers();

    Page<Users> findAllByDeletedAtIsNull(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void softDeleteById(UUID id);
}
