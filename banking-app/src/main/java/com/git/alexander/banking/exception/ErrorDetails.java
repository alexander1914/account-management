package com.git.alexander.banking.exception;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime dateTime,
                           String message,
                           String details,
                           String errorCode) {
}
