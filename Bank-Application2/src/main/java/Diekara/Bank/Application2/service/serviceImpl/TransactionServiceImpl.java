package Diekara.Bank.Application2.service.serviceImpl;

import Diekara.Bank.Application2.Repository.TransactionRepository;
import Diekara.Bank.Application2.dto.Data;
import Diekara.Bank.Application2.dto.Response;
import Diekara.Bank.Application2.dto.TransactionDto;
import Diekara.Bank.Application2.entity.Transaction;
import Diekara.Bank.Application2.entity.User;
import Diekara.Bank.Application2.util.ResponseUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto transaction) {


        Transaction newTransaction = Transaction.builder()
                .transactionType(transaction.getTransactionType())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .build();

        transactionRepository.save(newTransaction);
    }

    @Override
    public List<TransactionDto> fetchAllTransactions(User user) {
        List<Transaction> transactionList = transactionRepository.findAll();

        List<TransactionDto> transactionDto = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            transactionDto.add(TransactionDto.builder()
                    .transactionType(transaction.getTransactionType())
                    .accountNumber(transaction.getAccountNumber())
                    .amount(transaction.getAmount())
                    .build());
        }
        return transactionDto;
    }

    public List<TransactionDto> fetchSingleUserTransaction(String accountNumber, String debitOrCredit) {
        List<Transaction> transactionList = transactionRepository.findByAccountNumber(accountNumber);
        if (!transactionRepository.existsByAccountNumber(accountNumber)) {
            return null;
        }
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getTransactionType().equalsIgnoreCase(debitOrCredit)) {
                TransactionDto transactionDto = TransactionDto.builder()
                        .transactionType(transaction.getTransactionType())
                        .accountNumber(transaction.getAccountNumber())
                        .amount(transaction.getAmount())
                        .build();
                transactionDtoList.add(transactionDto);

            }

        }
        return transactionDtoList;

    }
}