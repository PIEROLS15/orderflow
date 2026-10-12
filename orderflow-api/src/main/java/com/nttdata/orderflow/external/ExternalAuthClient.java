package com.nttdata.orderflow.external;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/v1/auth")
@RegisterRestClient(configKey = "external-auth-api")
public interface ExternalAuthClient {
  @POST
  @Path("/login")
  ExternalLoginResponse login(ExternalLoginRequest request);

  @GET
  @Path("/profile")
  ExternalProfileResponse profile(@HeaderParam("Authorization") String accessToken);
}
