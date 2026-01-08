package com.git.alexander.banking.controller;

import com.git.alexander.banking.dtos.AccountDto;
import com.git.alexander.banking.dtos.TransactionDto;
import com.git.alexander.banking.dtos.TransferFundDto;
import com.git.alexander.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/accounts")
@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id){
        AccountDto accountDto = accountService.getAccountById(id);

        return ResponseEntity.ok(accountDto);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        List<AccountDto> accounts = accountService.getAllAccounts();

        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> depositAccount(@PathVariable Long id,
                                                     @RequestBody Map<String, Double> request){
        Double amount = request.get("amount");

        AccountDto accountDto = accountService.deposit(id, amount);

        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withdrawAccount(@PathVariable Long id,
                                                      @RequestBody Map<String, Double> request){
        Double amount = request.get("amount");

        AccountDto accountDto = accountService.withdraw(id, amount);

        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id,
                                                    @RequestBody AccountDto accountDto){
        AccountDto dtoRecord = new AccountDto(id, accountDto.accountHolderName(), accountDto.balance());

        AccountDto updateAccount = accountService.updateAccount(id, dtoRecord);

        return ResponseEntity.ok(updateAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);

        return ResponseEntity.ok("Account is deleted with success...");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFundAccount(@RequestBody TransferFundDto transferFundDto){
        accountService.transferFunds(transferFundDto);

        return ResponseEntity.ok("Transfer Successfully");
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> fetchAccountTransactions(@PathVariable("id") Long accountId){
        List<TransactionDto> transactionDtoList = accountService.getAccountTransactions(accountId);

        return ResponseEntity.ok(transactionDtoList);
    }
}
