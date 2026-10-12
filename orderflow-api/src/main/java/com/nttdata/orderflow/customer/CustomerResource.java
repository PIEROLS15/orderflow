package com.nttdata.orderflow.customer;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("service")
public class CustomerResource {
  @Inject
  CustomerService service;

  @POST
  public Response create(@Valid CustomerRequest request) {
    var response = service.create(request);
    return Response.created(URI.create("/api/customers/" + response.id())).entity(response).build();
  }

  @GET
  public Response list() {
    return Response.ok(service.list()).build();
  }

  @GET
  @Path("/{id}")
  public Response get(@PathParam("id") UUID id) {
    return Response.ok(service.get(id)).build();
  }

  @GET
  @Path("/search")
  public Response search(@QueryParam("email") String email) {
    return Response.ok(service.searchByEmail(email)).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") UUID id, @Valid CustomerRequest request) {
    return Response.ok(service.update(id, request)).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") UUID id) {
    service.delete(id);
    return Response.noContent().build();
  }
}
