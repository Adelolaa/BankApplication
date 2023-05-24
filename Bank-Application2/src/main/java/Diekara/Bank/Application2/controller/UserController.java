package Diekara.Bank.Application2.controller;

import Diekara.Bank.Application2.dto.Response;
import Diekara.Bank.Application2.dto.TransactionDto;
import Diekara.Bank.Application2.dto.TransactionRequest;
import Diekara.Bank.Application2.dto.UserRequest;
import Diekara.Bank.Application2.service.UserService;
import Diekara.Bank.Application2.service.serviceImpl.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Response registerUser(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }

    @GetMapping
    public List<Response> allRegisteredUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public Response fetchUser(@PathVariable(name = "userId") Long userId) throws Exception {
        return userService.fetchUser(userId);

    }

    @GetMapping("/balEnquiry")
    public Response balanceEnquiry(@RequestParam(name = "accountNumber") String accountNumber) {
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public Response nameEnquiry(@RequestParam(name = "accountNumber") String accountNumber) {
        return userService.nameEnquiry(accountNumber);
    }

    @PutMapping("/credit")
    public Response credit(@RequestBody TransactionRequest transactionRequest) {
        return userService.credit(transactionRequest);
    }
    @PutMapping("/debit")
    public Response debit (@RequestBody TransactionRequest transactionRequest){
        return userService.debit(transactionRequest);
    }


    }





