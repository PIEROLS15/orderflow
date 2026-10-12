package com.nttdata.orderflow.order;

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

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("service")
public class OrderResource {
  @Inject
  OrderService service;

  @POST
  public Response create(@Valid OrderRequest request) {
    var response = service.create(request);
    return Response.created(URI.create("/api/orders/" + response.id())).entity(response).build();
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
  @Path("/customer/{customerId}")
  public Response byCustomer(@PathParam("customerId") UUID customerId) {
    return Response.ok(service.byCustomer(customerId)).build();
  }

  @GET
  @Path("/status/{status}")
  public Response byStatus(@PathParam("status") String status) {
    return Response.ok(service.byStatus(status)).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") UUID id, @Valid OrderRequest request) {
    return Response.ok(service.update(id, request)).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") UUID id) {
    service.delete(id);
    return Response.noContent().build();
  }

  @POST
  @Path("/{id}/confirm")
  public Response confirm(@PathParam("id") UUID id) {
    return Response.ok(service.confirm(id)).build();
  }

  @POST
  @Path("/{id}/cancel")
  public Response cancel(@PathParam("id") UUID id) {
    return Response.ok(service.cancel(id)).build();
  }

  @GET
  @Path("/{id}/events")
  public Response logs(@PathParam("id") UUID id) {
    return Response.ok(service.logs(id)).build();
  }
}
