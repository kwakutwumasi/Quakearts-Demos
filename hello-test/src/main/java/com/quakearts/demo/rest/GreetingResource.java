package com.quakearts.demo.rest;

import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.quakearts.demo.rest.beans.Logged;
import com.quakearts.demo.rest.beans.CheckGreeting;
import com.quakearts.demo.rest.beans.ErrorResponse;
import com.quakearts.demo.rest.beans.GreetingService;
import com.quakearts.demo.rest.beans.Timed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/greet")
@RequestScoped
public class GreetingResource {
	
	@Inject
	private GreetingService service;
	
	@Inject
	private Locale locale;
	
	@Operation(description="Get a greeting with your name in it. Change your locale by adding a header called 'Accept-Language' with your"
			+ " language tag. The originally defined locales are 'ak', 'fr-FR' and the default 'en-US'. Try 'Kwaku' for a special response ;).")
	@ApiResponse(responseCode="200", content=@Content(schema=@Schema(implementation=GreetingResponse.class)))
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Logged @Timed
	public GreetingResponse getGreeting(@NotNull(message="Parameter 'name' cannot be null") @QueryParam("name") String name) {
		return new GreetingResponse().withGreeting(service.generateHello(name, locale));
	}
	
	public class GreetingResponse {
		String greeting;
		
		public String getGreeting() {
			return greeting;
		}
		
		public GreetingResponse withGreeting(String greeting) {
			this.greeting = greeting;
			return this;
		}
	}
	
	@Operation(description="Update the greeting for a locale. The greeting should use '{0}' as a placeholder for the name. This is required.")
	@ApiResponse(responseCode="200", content=@Content(schema=@Schema(implementation=CRUDResponse.class)))
	@ApiResponse(responseCode="404", description="The locale was not found.", content=@Content(schema=@Schema(implementation=ErrorResponse.class)))
	@ApiResponse(responseCode="400", description="The input is invalid.", content=@Content(schema=@Schema(implementation=ErrorResponse.class)))
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Logged @Timed
	public CRUDResponse updateGreeting(@CheckGreeting String greeting) {
		service.updateHello(locale, greeting);
		return new CRUDResponse().withStatus("updated");
	}

	@Operation(description="Create the greeting for a locale. The greeting should use '{0}' as a placeholder for the name. This is required.")
	@ApiResponse(responseCode="200", content=@Content(schema=@Schema(implementation=CRUDResponse.class)))
	@ApiResponse(responseCode="406", description="The locale already exists.", content=@Content(schema=@Schema(implementation=ErrorResponse.class)))
	@ApiResponse(responseCode="400", description="The input is invalid.", content=@Content(schema=@Schema(implementation=ErrorResponse.class)))
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Logged @Timed
	public CRUDResponse createGreeting(@CheckGreeting String greeting) {
		service.createHello(locale, greeting);
		return new CRUDResponse().withStatus("created");
	}

	public class CRUDResponse {
		String status;
		
		public String getStatus() {
			return status;
		}
		
		public CRUDResponse withStatus(String status) {
			this.status = status;
			return this;
		}
	}
}
