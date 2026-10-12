package com.nttdata.orderflow.auth;

public record TokenResponse(String accessToken, long expiresIn) {
}
