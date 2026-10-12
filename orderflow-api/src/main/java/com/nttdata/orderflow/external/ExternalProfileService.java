package com.nttdata.orderflow.external;

import com.nttdata.orderflow.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class ExternalProfileService {
  @Inject
  @RestClient
  ExternalAuthClient client;

  public ExternalProfileResponse validateProfile() {
    try {
      var login = client.login(new ExternalLoginRequest("john@mail.com", "changeme"));
      return client.profile("Bearer " + login.accessToken());
    } catch (RuntimeException exception) {
      throw new BusinessException("ERROR_API_EXTERNA", "Error al validar el perfil externo", Response.Status.BAD_GATEWAY);
    }
  }
}
