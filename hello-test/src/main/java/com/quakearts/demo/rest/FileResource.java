package com.quakearts.demo.rest;

import java.io.InputStream;
import java.text.MessageFormat;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Hidden;

@Path("ui")
@Hidden
public class FileResource {

	private MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
	private MessageFormat format = new MessageFormat("swagger-ui/{0}");
	
	@GET
	@Path("{resource}")
	@Produces(MediaType.MEDIA_TYPE_WILDCARD)
	public Response getResource(@PathParam("resource") String resource) {		
		if(resource == null || resource.startsWith(".."))
			return Response.status(404).build();
		
			
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(format.format(new String[]{resource}));
		
		if(in == null)
			return Response.status(404).build();
		
		return Response.status(200)
				.header("Content-Type", resource.endsWith(".css")?"text/css":typeMap.getContentType(resource))
				.entity(in)
				.build();
	}
}
