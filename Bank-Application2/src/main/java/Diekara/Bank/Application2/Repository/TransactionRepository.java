package Diekara.Bank.Application2.Repository;

import Diekara.Bank.Application2.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String>{

}
