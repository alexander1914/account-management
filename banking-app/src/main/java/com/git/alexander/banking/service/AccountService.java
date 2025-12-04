package com.git.alexander.banking.service;

import com.git.alexander.banking.dtos.AccountDto;

public interface AccountService {

    AccountDto getAccountById(Long id);
    AccountDto createAccount(AccountDto accountDto);
    AccountDto deposit(Long id, double amount);
    AccountDto withdraw(Long id, double amount);
    AccountDto updateAccount(Long id, AccountDto accountDto);
}
