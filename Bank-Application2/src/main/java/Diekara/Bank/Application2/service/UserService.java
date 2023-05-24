package Diekara.Bank.Application2.service;

import Diekara.Bank.Application2.dto.TransactionDto;
import Diekara.Bank.Application2.dto.TransactionRequest;
import Diekara.Bank.Application2.dto.Response;
import Diekara.Bank.Application2.dto.UserRequest;

import java.util.List;


public interface UserService {

    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long id) throws Exception;
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);
    List<TransactionDto> fetchTransactionByUser(String accountNumber);

}
