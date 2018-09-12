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
import com.quakearts.demo.rest.beans.GreetingService;
import com.quakearts.demo.rest.beans.Timed;

@Path("/greet")
@RequestScoped
public class GreetingResource {
	
	@Inject
	private GreetingService service;
	
	@Inject
	private Locale locale;
	
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
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Logged @Timed
	public CRUDResponse updateGreeting(@CheckGreeting String greeting) {
		service.updateHello(locale, greeting);
		return new CRUDResponse().withStatus("updated");
	}

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
