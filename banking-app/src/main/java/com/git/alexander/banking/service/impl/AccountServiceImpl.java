package com.git.alexander.banking.service.impl;

import com.git.alexander.banking.dtos.AccountDto;
import com.git.alexander.banking.dtos.TransactionDto;
import com.git.alexander.banking.dtos.TransferFundDto;
import com.git.alexander.banking.entity.Account;
import com.git.alexander.banking.entity.Transaction;
import com.git.alexander.banking.enums.TypeTransactions;
import com.git.alexander.banking.exception.AccountException;
import com.git.alexander.banking.mapper.AccountMapper;
import com.git.alexander.banking.repository.AccountRepository;
import com.git.alexander.banking.repository.TransactionsRepository;
import com.git.alexander.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionsRepository transactionsRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionsRepository transactionsRepository) {
        this.accountRepository = accountRepository;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public AccountDto getAccountById(Long id){
        // Find an account
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        // Find all accounts on database
        List<Account> accounts = accountRepository.findAll();

        return accounts
                .stream()
                .map((AccountMapper::mapToAccountDto))
                .toList();
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
                .orElseThrow(() -> new AccountException("Account does not exists"));

        // Add new deposit
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        // Save transaction history of deposit
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TypeTransactions.DEPOSIT.getDescription());
        transaction.setTimestamp(LocalDateTime.now());

        transactionsRepository.save(transaction);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        // Find an account
        Account account = accountRepository.
                findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        // Validate the balance on account
        if (account.getBalance() < amount){
            throw new RuntimeException("Insufficient amount");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        // Save transaction history of withdraw
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TypeTransactions.WITHDRAW.getDescription());
        transaction.setTimestamp(LocalDateTime.now());

        transactionsRepository.save(transaction);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        // Find an account
        Account account = accountRepository.
                findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        // Mapping DTO with Entity
        account.setAccountHolderName(accountDto.accountHolderName());
        account.setBalance(accountDto.balance());

        Account updatedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(updatedAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        // Find an account
        Account account = accountRepository.
                findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        accountRepository.deleteById(account.getId());
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {
        // Retrieve the account from which we send the amount
        Account fromAccount = accountRepository
                .findById(transferFundDto.fromAccountId())
                .orElseThrow(() -> new AccountException("Account does not exists"));

        // Retrieve the account to which we send the amound
        Account toAccount = accountRepository
                .findById(transferFundDto.toAccountId())
                .orElseThrow(() -> new AccountException("Account does not exists"));

        if (fromAccount.getBalance() < transferFundDto.amount()){
            throw new RuntimeException("Insufficient Amount =(");
        }

        // Debit the amount from fromAccount object
        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());

        // Credit the amount to toAccount object
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

        // Save these changes objects on database
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Save the history of Transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundDto.fromAccountId());
        transaction.setAmount(transferFundDto.amount());
        transaction.setTransactionType(TypeTransactions.TRANSFER.getDescription());
        transaction.setTimestamp(LocalDateTime.now());

        transactionsRepository.save(transaction);
    }

    @Override
    public List<TransactionDto> getAccountTransactions(Long accountId) {
        // Find all the transactions on database
        List<Transaction> transactions = transactionsRepository.findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private TransactionDto convertEntityToDto(Transaction transaction){
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }
}
