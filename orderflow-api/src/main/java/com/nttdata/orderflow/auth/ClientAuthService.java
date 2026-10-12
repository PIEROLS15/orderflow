package com.nttdata.orderflow.auth;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class ClientAuthService {
  private final Map<String, String> clients = Map.of(
      "orders-service", "1234",
      "admin-service", "admin123"
  );

  public boolean isValid(String clientId, String secret) {
    return secret != null && secret.equals(clients.get(clientId));
  }
}
