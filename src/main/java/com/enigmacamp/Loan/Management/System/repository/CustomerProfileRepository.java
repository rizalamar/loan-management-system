package com.enigmacamp.Loan.Management.System.repository;

import com.enigmacamp.Loan.Management.System.entities.CustomerProfile;
import com.enigmacamp.Loan.Management.System.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, UUID> {
    Optional<CustomerProfile> findByUser(User user);
    Optional<CustomerProfile> findByUserId(UUID userId);
    boolean existsByEmail(String email);
}
