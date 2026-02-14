package com.enigmacamp.Loan.Management.System.repository;

import com.enigmacamp.Loan.Management.System.entities.CustomerProfile;
import com.enigmacamp.Loan.Management.System.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {
    // by customer-self
    List<Loan> findByCustomer(CustomerProfile customer);
    List<Loan> findByCustomerId(UUID customerId);
    List<Loan> findByStatus(Loan.LoanStatus status);
}
