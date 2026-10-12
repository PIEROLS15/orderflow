package com.nttdata.orderflow.product;

import jakarta.annotation.security.PermitAll;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
  @Inject
  ProductService service;

  @POST
  @RolesAllowed("service")
  public Response create(@Valid ProductRequest request) {
    var response = service.create(request);
    return Response.created(URI.create("/api/products/" + response.id())).entity(response).build();
  }

  @GET
  @PermitAll
  public Response list() {
    return Response.ok(service.list()).build();
  }

  @GET
  @Path("/active")
  @PermitAll
  public Response active() {
    return Response.ok(service.listActive()).build();
  }

  @GET
  @Path("/{id}")
  @PermitAll
  public Response get(@PathParam("id") UUID id) {
    return Response.ok(service.get(id)).build();
  }

  @PUT
  @Path("/{id}")
  @RolesAllowed("service")
  public Response update(@PathParam("id") UUID id, @Valid ProductRequest request) {
    return Response.ok(service.update(id, request)).build();
  }

  @DELETE
  @Path("/{id}")
  @RolesAllowed("service")
  public Response delete(@PathParam("id") UUID id) {
    service.delete(id);
    return Response.noContent().build();
  }
}
