package com.viewer.rest;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.PhotoDTO;

@Path("/viewer")
public class AlbumMessageHandler {

	@GET
	@Path("/album")
	public Response albumInfo() {

		try {

			Context c = new InitialContext();
			AlbumBeanLocal albumRemote = (AlbumBeanLocal) c
					.lookup("java:global/PictureViewerEAR/PictureViewerEJB/AlbumBean!com.viewer.beans.AlbumBeanLocal");

			List<PhotoDTO> albums = albumRemote.fetchUserAlbumPhotos(1, 1);
			System.out.println(albums.get(0).getName());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		String output = "hello";
		return Response.status(200).entity(output).build();
	}
}
