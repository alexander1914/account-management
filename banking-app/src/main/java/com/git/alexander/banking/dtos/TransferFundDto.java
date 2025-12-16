package com.git.alexander.banking.dtos;

public record TransferFundDto(Long fromAccountId,
                              Long toAccountId,
                              double amount) {
}
