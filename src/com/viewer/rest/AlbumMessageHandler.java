package com.viewer.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/viewer")
public class AlbumMessageHandler {
	
	@GET
	@Path("/album")
	public Response albumInfo() {

		String output = "hello";
		return Response.status(200).entity(output).build();
	}
}
