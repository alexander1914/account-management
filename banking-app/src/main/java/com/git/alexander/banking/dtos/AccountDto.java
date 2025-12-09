package com.git.alexander.banking.dtos;

/// Record: is a special type of class declaration aimed at reducing the boilerplate code
public record AccountDto(Long id,
                         String accountHolderName,
                         double balance) {
}
