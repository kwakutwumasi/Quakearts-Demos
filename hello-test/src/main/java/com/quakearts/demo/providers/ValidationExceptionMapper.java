package com.quakearts.demo.providers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.quakearts.demo.rest.beans.ErrorResponse;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		List<String> violations = exception.getConstraintViolations().stream().map((violation)->{
			return violation.getMessage();
		}).collect(Collectors.toList());
		
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(new ErrorResponse()
						.withMessageAs(violations.toString())).build();
	}

}
