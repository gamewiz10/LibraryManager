package com.gamewiz10.library.repository;

import com.gamewiz10.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{

    List<Loan> findByUserId(Long id);

    List<Loan> findByBookId(Long id);

    List<Loan> findByUserIdAndReturnFalse(Long id);
}
