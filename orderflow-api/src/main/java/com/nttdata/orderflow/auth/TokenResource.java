package com.nttdata.orderflow.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Duration;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {
  @Inject
  ClientAuthService authService;

  @POST
  @Path("/token")
  public Response token(@Valid TokenRequest request) {
    if (!authService.isValid(request.clientId(), request.clientSecret())) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    var jwt = Jwt.issuer("orderflow-api")
        .subject(request.clientId())
        .groups(Set.of("service"))
        .expiresIn(Duration.ofHours(1))
        .sign();
    return Response.ok(new TokenResponse(jwt, 3600)).build();
  }
}
