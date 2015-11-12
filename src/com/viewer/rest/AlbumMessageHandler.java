package com.viewer.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.stream.ImageInputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.model.SearchQuery;

@Path("/viewer")
public class AlbumMessageHandler {

	AlbumBeanLocal albumBean;

	public AlbumMessageHandler() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * Retrieves all albums for given user
	 * 
	 * @param userkey
	 * @return JSON response
	 */
	@GET
	@Path("/albums")
	public Response getUserAlbums(@QueryParam("usr") String userkey, @QueryParam("parent") long parentId) {

		List<AlbumDTO> albums = albumBean.fetchAllUserAlbums(1, parentId);
		String output = generateAlbumJSON(albums);
		return Response.status(200).entity(output).build();
	}

	/**
	 * Retrieves all photos of an album for given user
	 * 
	 * @param userkey
	 * @return JSON response
	 */
	@GET
	@Path("/photos")
	public Response getUserAlbumPhotos(@QueryParam("usr") String userkey, @QueryParam("albumid") long albumid) {
		List<PhotoDTO> photos = albumBean.fetchUserAlbumPhotos(1, albumid);
		String output = generatePhotoJSON(photos);
		return Response.status(200).entity(output).build();
	}

	/**
	 * Retrieves searched albums based on criteria for given user
	 * 
	 * @param userkey
	 * @param searchValue
	 * @param searchTags
	 * @return JSON response
	 */
	@GET
	@Path("/search/albums")
	public Response getUserSearchAlbums(@QueryParam("usr") String userkey, @QueryParam("name") String searchValue,
			@QueryParam("tag") String searchTags, @QueryParam("cateid") String categoryIds) {

		List<String> names = Arrays.asList(searchValue.split(" "));

		SearchQuery searchQuery = new SearchQuery(names);

		List<AlbumDTO> albums = albumBean.fetchSearchedUserAlbums(1, searchQuery.toSearchQueryDTO());
		String output = generateAlbumJSON(albums);
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/thumbnail")
	public Response getUserAlbumThumbnail(@QueryParam("usr") String userkey, @QueryParam("photoid") int photoId) {
		ImageInputStream imageStream = albumBean.fetchPhotoThumbnailData(1, photoId);
		String output = "";
		try {
			byte[] bytes = new byte[1024];
			while (imageStream.read(bytes) >= 0) {
				output = Base64.encodeBase64URLSafeString(bytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/image")
	public Response getUserAlbumPhoto(@QueryParam("usr") String userkey, @QueryParam("photoid") int photoId) {
		ImageInputStream imageStream = albumBean.fetchPhotoData(1, photoId);
		String output = "";
		try {
			byte[] bytes = new byte[1024];
			while (imageStream.read(bytes) >= 0) {
				output = Base64.encodeBase64URLSafeString(bytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(output).build();
	}

	private String generatePhotoJSON(List<PhotoDTO> photos) {
		JSONArray photosJSON = new JSONArray();
		try {
			for (PhotoDTO photo : photos) {
				JSONObject photoJSON = new JSONObject();
				photoJSON.put("id", photo.getId());
				photoJSON.put("name", photo.getName());
				photosJSON.put(photoJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return photosJSON.toString();
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
				albumJSON.put("subtitle", album.getSubtitle());
				albumJSON.put("coverId", album.getCoverId());
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON.toString();
	}
}