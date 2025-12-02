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
    public AccountDto createAccount(AccountDto accountDto) {

        Account account = AccountMapper.mapToAccount(accountDto);

        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }
}
