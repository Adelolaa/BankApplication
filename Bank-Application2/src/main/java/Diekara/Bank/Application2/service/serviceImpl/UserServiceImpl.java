package Diekara.Bank.Application2.service.serviceImpl;

import Diekara.Bank.Application2.Repository.UserRepository;
import Diekara.Bank.Application2.dto.*;
import Diekara.Bank.Application2.email.EmailDetail;
import Diekara.Bank.Application2.email.EmailService;
import Diekara.Bank.Application2.entity.User;
import Diekara.Bank.Application2.service.UserService;
import Diekara.Bank.Application2.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static Diekara.Bank.Application2.util.ResponseUtil.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TransactionService transactionService;
    private EmailService emailService;





    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, EmailService emailService) {
        this.userRepository = userRepository;
        this.transactionService=transactionService;
        this.emailService=emailService;

    }

    @Override
    public Response registerUser(UserRequest userRequest) {
        /**
         * check if the user exists, if the user doesn't exist return response,
         * generate account number
         * go ahead
         */


        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            return Response.builder()
                    .responseCode(ResponseUtil.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtil.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();


        }
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtil.generateAccountNumber(ResponseUtil.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();

        User savedUser = userRepository.save(user);
        EmailDetail message = EmailDetail.builder()
                .recipient(user.getEmail())
                .subject("Adelola Bank")
                .messageBody("Congratulations! your account has been successfully created \n" +
                        "Your Account name is:"+ savedUser.getFirstName()+" "+ savedUser.getLastName()+ " "+ savedUser.getOtherName()+".\n" +
                        " Your Account Number is: "+ savedUser.getAccountNumber()+".")
                .build();
        emailService.sendSimpleEmail(message);




        return Response.builder()
                .responseCode(ResponseUtil.SUCCESS)
                .responseMessage(USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public List<Response> allUsers() {
        List<User> userList = userRepository.findAll();

        return userList.stream().map(user -> Response.builder()
                .responseCode(SUCCESS)
                .responseMessage(SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build()).collect(Collectors.toList());
    }

    @Override
    public Response fetchUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return Response.builder()
                    .responseCode(ResponseUtil.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtil.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findById(userId).get();

        return Response.builder()
                .responseMessage(SUCCESS_MESSAGE)
                .responseCode(SUCCESS)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();

    }


    @Override
    public Response balanceEnquiry(String accountNumber) {
        /**
         * check if accNum exists
         * return the balance info
         */
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(ResponseUtil.USER_NOT_FOUND_CODE)
                    .responseMessage(USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtil.SUCCESS)
                .responseMessage(SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist) {
            return Response.builder()
                    .responseCode(USER_NOT_FOUND_CODE)
                    .responseMessage(USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseMessage(SUCCESS_MESSAGE)
                .responseCode(SUCCESS)
                .data(Data.builder()
                        .accountBalance(null)
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response credit(TransactionRequest transactionRequest) {
        /**
         * add amount to current accountBalance
         */
        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(USER_NOT_FOUND_CODE)
                    .responseMessage(USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        receivingUser.setAccountBalance(receivingUser.getAccountBalance().add(transactionRequest.getAmount()));
        userRepository.save(receivingUser);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());

        transactionService.saveTransaction(transactionDto);

        EmailDetail message = EmailDetail.builder()
                .recipient(receivingUser.getEmail())
                .subject("SA Bank Credit Alert")
                .messageBody("Txn:Credit" + "\nAcct:xxxx" + "\nAmt"+ "NGN"+ transactionDto.getAmount()+ "\nDesc:"+ receivingUser.getFirstName() + receivingUser.getLastName()+ "\n" + LocalDateTime.now()+"\n"+"Bal:"+receivingUser.getAccountBalance())
                .build();

        emailService.sendSimpleEmail(message);

        return Response.builder()
                .responseCode(SUCCESSFUL_TRANSACTION)
                .responseMessage(ACCOUNT_CREDITED)
                .data(Data.builder()
                        .accountName(transactionRequest.getAccountNumber())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountNumber(receivingUser.getAccountNumber())
                        .accountName(receivingUser.getFirstName() + " " + receivingUser.getLastName() + receivingUser.getOtherName())
                        .build())
                .build();


    }

    @Override
    public Response debit(TransactionRequest transactionRequest) {
        User debitedUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())) {
            return Response.builder()
                    .responseCode(USER_NOT_FOUND_CODE)
                    .responseMessage(USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        if (debitedUser.getAccountBalance().compareTo(transactionRequest.getAmount()) > 0){

        debitedUser.setAccountBalance(debitedUser.getAccountBalance().subtract(transactionRequest.getAmount()));
        userRepository.save(debitedUser);

        TransactionDto transactionDto= new TransactionDto();
        transactionDto.setTransactionType("Debit");
        transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());

        transactionService.saveTransaction(transactionDto);

             EmailDetail message = EmailDetail.builder()
                    .recipient(debitedUser.getEmail())
                    .subject("Adelola Bank Debit Alert")
                    .messageBody("Txn:Debit" + "\nAcct:xxxx" + "\nAmt"+ "NGN"+ transactionDto.getAmount()+ "\nDes:MOB2/UTO/TO"+ " "+ debitedUser.getFirstName() + debitedUser.getLastName()+ "\n" + LocalDateTime.now()+"\n"+"Bal:"+debitedUser.getAccountBalance())
                    .build();
                    
            emailService.sendSimpleEmail(message);

        return Response.builder()
                .responseCode(SUCCESSFUL_TRANSACTION)
                .responseMessage(ACCOUNT_DEBITED)
                .data(Data.builder()
                        .accountName(transactionRequest.getAccountNumber())
                        .accountBalance(debitedUser.getAccountBalance())
                        .accountName(debitedUser.getFirstName() + " " + debitedUser.getLastName() + debitedUser.getOtherName())
                        .build())
                .build();
    }
    return Response.builder()
            .responseCode(UNSUCCESSFUL_TRANSACTION)
            .responseMessage(INSUFFICIENT_FUND)
            .data(null)
            .build();
}


    @Override
    public List<TransactionDto> fetchAllTransactionsByUser(String accountNumber) {
        User user = userRepository.findByAccountNumber(accountNumber);
        return transactionService.fetchAllTransactions(user);
    }

    @Override
    public Response transfer(TransferRequest transactionRequest) {
        User sourceAccount = userRepository.findByAccountNumber(transactionRequest.getSendingAccountNumber());
        User destinationAccount = userRepository.findByAccountNumber(transactionRequest.getReceivingAccountNumber());

           if (!userRepository.existsByAccountNumber(transactionRequest.getSendingAccountNumber()) ||
                   !userRepository.existsByAccountNumber(transactionRequest.getReceivingAccountNumber()))  {

               return Response.builder()
                       .responseCode(USER_NOT_FOUND_CODE)
                       .responseMessage(USER_NOT_FOUND_MESSAGE)
                       .data(null)
                       .build();
           }
           if (sourceAccount.getAccountBalance().compareTo(transactionRequest.getAmount()) > 0) {
            sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transactionRequest.getAmount()));
            userRepository.save(sourceAccount);

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setTransactionType("Debit");
            transactionDto.setAccountNumber(transactionRequest.getSendingAccountNumber());
            transactionDto.setAmount(transactionRequest.getAmount());

            transactionService.saveTransaction(transactionDto);

            destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transactionRequest.getAmount()));
            userRepository.save(destinationAccount);

            TransactionDto transactionDto1 = new TransactionDto();
            transactionDto1.setTransactionType("Credit");
            transactionDto1.setAccountNumber(transactionRequest.getReceivingAccountNumber());
            transactionDto1.setAmount(transactionRequest.getAmount());

            transactionService.saveTransaction(transactionDto1);

               EmailDetail message = EmailDetail.builder()
                       .recipient(destinationAccount.getEmail())
                       .subject("Adelola Bank Credit Alert")
                       .messageBody("Txn:Credit" + "\nAcct:xxxx" + "\nAmt"+ "NGN"+ transactionDto.getAmount()+ "\nDesc:"+ destinationAccount.getFirstName() + destinationAccount.getLastName()+ "\n" + LocalDateTime.now()+"\n"+"Bal:"+destinationAccount.getAccountBalance())
                       .build();
               emailService.sendSimpleEmail(message);

//               EmailDetail message = EmailDetail.builder()
//                       .recipient(sourceAccount.getEmail())
//                       .subject("Adelola Bank")
//                       .messageBody("Your account has been successfully created \n" +
//                               "Your Account name is:"+ sourceAccount.getFirstName()+" "+ sourceAccount.getLastName()+ " "+ sourceAccount.getOtherName()+".\n" +
//                               " Your Account Number is: "+ sourceAccount.getAccountNumber()+".")
//                       .build();
//               emailService.sendSimpleEmail(message);

            return Response.builder()
                    .responseCode(SUCCESSFUL_TRANSACTION)
                    .responseMessage(TRANSFER_SUCCESSFUL_MESSAGE)
                    .data(Data.builder()
                            .accountNumber(transactionRequest.getSendingAccountNumber())
                            .accountName(sourceAccount.getFirstName() + " " + sourceAccount.getOtherName() + " " + sourceAccount.getOtherName())
                            .accountBalance(sourceAccount.getAccountBalance())
                            .accountNumber(transactionRequest.getReceivingAccountNumber())
                            .accountName(destinationAccount.getFirstName() + " " + destinationAccount.getLastName() + " " + destinationAccount.getOtherName())
                            .accountBalance(destinationAccount.getAccountBalance())
                             .build())
                    .build();
        }
        return Response.builder()
                .responseCode(UNSUCCESSFUL_TRANSACTION)
                .responseMessage(INSUFFICIENT_FUND)
                .data(null)
                .build();
    }



}





