package com.task.repository;

import com.task.entity.Owners;
import com.task.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnersRepository extends JpaRepository<Owners, UUID> {

    Optional<Owners> findByCodeOwner(String codeOwner);

    Optional<Owners> findById(UUID id);

    Optional<Owners> findByCodeOwnerAndActiveTrue(String codeOwner);

    List<Owners> findByNameContainingIgnoreCase(String name);

    List<Owners> findByActiveTrue();

    @Query("SELECT o FROM Owners o")
    List<Owners> findAllOwners();

    Page<Owners> findAllOwnersPageable(Pageable pageable);

    boolean existsByCodeOwner(String codeOwner);
}
