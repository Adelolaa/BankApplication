package Diekara.Bank.Application2.Repository;

import Diekara.Bank.Application2.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,String>{

    boolean existsByAccountNumber(String accountNumber);

    List<Transaction> findByAccountNumber(String accountNumber);
}
