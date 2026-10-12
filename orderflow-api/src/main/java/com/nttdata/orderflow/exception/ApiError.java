package com.nttdata.orderflow.exception;

import java.time.LocalDateTime;

public record ApiError(String code, String message, LocalDateTime timestamp) {
}
