package Diekara.Bank.Application2.service.serviceImpl;

import Diekara.Bank.Application2.dto.Response;
import Diekara.Bank.Application2.dto.TransactionDto;
import Diekara.Bank.Application2.entity.User;

import java.util.List;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
    List<TransactionDto> fetchAllTransactions(User user);
    List<TransactionDto> fetchSingleUserTransaction(String accountNumber, String debitOrCredit);
}
