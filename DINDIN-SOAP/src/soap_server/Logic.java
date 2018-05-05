package soap_server;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import model.Credentials;

@WebService(endpointInterface = "soap_server.LogicI")
public class Logic implements LogicI
{

	private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

	public String login(String username, String password) throws Exception {

		Credentials creds = new Credentials();
		creds.setUsername(username);
		creds.setPassword(password);
		String json = new Gson().toJson(creds);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/dindin/webapi/login");
		Entity<String> data = Entity.entity(json, MediaType.APPLICATION_JSON);
		Response response = target.request(MediaType.APPLICATION_JSON).post(data);
		String authHeader = response.getHeaderString("Authorization");

		if (response.getStatus() == Status.OK.getStatusCode()) {
			String token = authHeader.replaceFirst("Bearer ", "");

			return token;

		} else {
			return null;
		}
	}

	
	public Object getUsers(String token) {

		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget usersTarget = baseTarget.path("users");
		String users = usersTarget.request(MediaType.APPLICATION_JSON)
				.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token).get(String.class);

		return users;
	}

	
	public Object getSpecificUser(int userId, String token) throws JSONException {

		Client client = ClientBuilder.newClient();

		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget usersTarget = baseTarget.path("users");
		WebTarget singeUserTarget = usersTarget.path("{userId}"); // Placeholder for user
		Response response = singeUserTarget.resolveTemplate("userId", userId).request()/*.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token)*/.get();
		String result = response.readEntity(String.class);
		

		if (response.getStatus() == Status.OK.getStatusCode()) {
			JSONObject o = new JSONObject(result);
			int id = o.getInt("id");
			String email = o.getString("email");
			String username = o.getString("username");
			String firstName = o.getString("firstName");
			String lastName = o.getString("lastName");

			return "BRUGER FUNDET\n" + "ID: " + id + "\nE-MAIL: " + email + "\nBrugernavn: " + username + "\nFornavn: "
					+ firstName + "\nEfternavn: " + lastName;
		}

		else if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			JSONObject o = new JSONObject(result);
			int errorCode = o.getInt("errorCode");
			String errorMessage = o.getString("errorMessage");
			return "FEJL - BRUGER FINDES IKKE\n" + "Fejlkode: " + errorCode + "\nFejlbesked: " + errorMessage;

		} else {
			return "Fejl, prøv igen senere.";
		}

		
	}



	@Override
	public Object getNumOfUsers(String token) throws JSONException {
		JsonArray array = new Gson().fromJson((String) getUsers(token), JsonArray.class);
		return array.size();
	}



	@Override
	public Object deleteUser(int userId, String token) {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget usersTarget = baseTarget.path("users");
		WebTarget singeUserTarget = usersTarget.path("{userId}"); 
		Response response = singeUserTarget.resolveTemplate("userId", userId).request()/*.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token)*/.delete();
		
		if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			return "Bruger med id "+userId+" er slettet.";
		} else {
			return "Fejl ved sletning!";
		}
	}


	@Override
	public Object getRestaurants(String token) {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget restaurantsTarget = baseTarget.path("restaurants");
		String restaurants = restaurantsTarget.request(MediaType.APPLICATION_JSON)
				.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token).get(String.class);

		return restaurants;
	}


	@Override
	public Object getSpecificRestaurant(int restaurantId, String token) throws JSONException {

		Client client = ClientBuilder.newClient();

		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget restaurantsTarget = baseTarget.path("restaurants");
		WebTarget singeRestTarget = restaurantsTarget.path("{userId}"); // Placeholder for user
		Response response = singeRestTarget.resolveTemplate("userId", restaurantId).request()/*.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token)*/.get();
		String result = response.readEntity(String.class);
		

		if (response.getStatus() == Status.OK.getStatusCode()) {
			JSONObject o = new JSONObject(result);
			int id = o.getInt("id");
			String name = o.getString("name");
			int zipcode = o.getInt("zipcode");
			String cuisine = o.getString("cuisine");

			return "RESTAURANT FUNDET\n" + "ID: " + id + "\nNAVN: " + name + "\nPOSTNUMMER: " + zipcode + "\nKØKKEN: "
					+ cuisine;
		}

		else if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			JSONObject o = new JSONObject(result);
			return "FEJL - RESTAURANTS FINDES IKKE: 404";

		} else {
			return "Fejl, prøv igen senere.";
		}
	}


	@Override
	public Object getNumOfRestaurants(String token) throws JSONException {
		JsonArray array = new Gson().fromJson((String) getRestaurants(token), JsonArray.class);
		return array.size();
	}


	@Override
	public Object addRestaurant(String name, int zipcode, String address, String cuisine, String budget, String phone, String website, String token) throws JSONException {
		JSONObject restaurant = new JSONObject();
		restaurant.put("name", name);
		restaurant.put("zipcode", zipcode);
		restaurant.put("address", address);
		restaurant.put("cuisine", cuisine);
		restaurant.put("budget", budget);
		restaurant.put("phone", phone);
		restaurant.put("website", website);
		
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget restaurantsTarget = baseTarget.path("restaurants");
		Response response = restaurantsTarget.request()/*.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token)*/.post(Entity.entity(restaurant.toString(), MediaType.APPLICATION_JSON));
		
		if (response.getStatus() == Status.CREATED.getStatusCode()) {
			return "Restaurant oprettet";
		} else {
			return "Fejl ved oprettelse!";
		}
	
	}


	@Override
	public Object deleteRestaurant(int restaurantId, String token) {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget restaurantsTarget = baseTarget.path("restaurants");
		WebTarget singeRestTarget = restaurantsTarget.path("{restId}"); 
		Response response = singeRestTarget.resolveTemplate("restId", restaurantId).request()/*.header("Authorization", AUTHORIZATION_HEADER_PREFIX + token)*/.delete();
		
		if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
			return "Restaurant med id "+restaurantId+" er slettet.";
		} else {
			return "Fejl ved sletning!";
		}
	}
	
	
	
	
	

	
	
}

