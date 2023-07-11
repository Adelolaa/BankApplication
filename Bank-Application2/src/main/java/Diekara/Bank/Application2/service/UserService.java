package Diekara.Bank.Application2.service;

import Diekara.Bank.Application2.dto.*;

import java.util.List;


public interface UserService {

    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long id) throws Exception;
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);
    List<TransactionDto> fetchAllTransactionsByUser(String accountNumber);
    Response transfer(TransferRequest transferRequest);

//    List<TransactionDto> fetchSingleUserTransaction(String accountNumber, String debitOrCredit);
}
