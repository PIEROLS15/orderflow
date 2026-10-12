package com.nttdata.orderflow.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
  @Override
  public Response toResponse(Throwable exception) {
    if (exception instanceof BusinessException businessException) {
      return build(businessException.status(), businessException.code(), businessException.getMessage());
    }
    if (exception instanceof NotFoundException) {
      return build(Response.Status.NOT_FOUND, "RECURSO_NO_ENCONTRADO", "Recurso no encontrado");
    }
    if (exception instanceof ConstraintViolationException validationException) {
      var message = validationException.getConstraintViolations().stream()
          .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
          .collect(Collectors.joining("; "));
      return build(Response.Status.BAD_REQUEST, "ERROR_VALIDACION", message);
    }
    return build(Response.Status.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", exception.getMessage());
  }

  private Response build(Response.Status status, String code, String message) {
    return Response.status(status)
        .entity(new ApiError(code, message, LocalDateTime.now()))
        .build();
  }
}
