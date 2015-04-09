package com.viewer.rest;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;

@Path("/viewer")
public class AlbumMessageHandler {

	AlbumBeanLocal albumRemote;

	public AlbumMessageHandler() {
		try {
			Context c = new InitialContext();
			albumRemote = (AlbumBeanLocal) c
					.lookup("java:global/PictureViewerEAR/PictureViewerEJB/AlbumBean!com.viewer.beans.AlbumBeanLocal");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}

	@GET
	@Path("/album")
	public Response getUserAlbums() {

		List<AlbumDTO> albums = albumRemote.fetchAllUserAlbums(1);
		String output = generateAlbumJSON(albums);
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/image")
	public Response getUserAlbumPhoto() {
		byte[] imageByteArray = albumRemote.fetchPhotoData(1);
		String output = Base64.encodeBase64URLSafeString(imageByteArray);
		return Response.status(200).entity(output).build();
	}

	/**
	 * Turns a collection of AlbumDTOs to JSON string
	 * 
	 * @param albums
	 * @return JSON string
	 */
	private String generateAlbumJSON(List<AlbumDTO> albums) {
		JSONArray albumsJSON = new JSONArray();
		try {
			for (AlbumDTO album : albums) {
				JSONObject albumJSON = new JSONObject();
				albumJSON.put("id", album.getId());
				albumJSON.put("name", album.getName());
				albumJSON.put("coverId", album.getCoverId());
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON.toString();
	}
}