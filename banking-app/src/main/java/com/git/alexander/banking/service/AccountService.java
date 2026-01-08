package com.git.alexander.banking.service;

import com.git.alexander.banking.dtos.AccountDto;
import com.git.alexander.banking.dtos.TransactionDto;
import com.git.alexander.banking.dtos.TransferFundDto;

import java.util.List;

public interface AccountService {

    AccountDto getAccountById(Long id);
    List<AccountDto> getAllAccounts();
    AccountDto createAccount(AccountDto accountDto);
    AccountDto deposit(Long id, double amount);
    AccountDto withdraw(Long id, double amount);
    AccountDto updateAccount(Long id, AccountDto accountDto);
    void deleteAccount(Long id);
    void transferFunds(TransferFundDto transferFundDto);
    List<TransactionDto> getAccountTransactions(Long accountId);
}
