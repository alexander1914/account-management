package com.git.alexander.banking.service.impl;

import com.git.alexander.banking.dtos.AccountDto;
import com.git.alexander.banking.entity.Account;
import com.git.alexander.banking.mapper.AccountMapper;
import com.git.alexander.banking.repository.AccountRepository;
import com.git.alexander.banking.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto getAccountById(Long id){

        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exists"));

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        Account account = AccountMapper.mapToAccount(accountDto);

        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto deposit(Long id, double amount){

        // Find an account
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exists"));

        // Add new deposit
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        // Find an account
        Account account = accountRepository.
                findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exists"));

        // Validate the balance on account
        if (account.getBalance() < amount){
            throw new RuntimeException("Insufficient amount");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        // Find an account
        Account account = accountRepository.
                findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exists"));

        // Mapping DTO with Entity
        account.setAccountHolderName(accountDto.getAccountHolderName());
        account.setBalance(accountDto.getBalance());

        Account updatedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(updatedAccount);
    }
}
