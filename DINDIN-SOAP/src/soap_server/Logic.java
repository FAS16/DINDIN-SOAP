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
public class Logic 
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
			String username = o.getString("userName");
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

	
	public Object getRestaurants(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object deleteUser(int userId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object addUser(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object getSpecificRestaurant(int restaurantId, String token) {
		// TODO Auto-generated method stub
		return null;
	}


	public Object deleteRestaurant(int restaurantId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object addRestaurant(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object getNumOfUsers(String token) throws JSONException {	
		JsonArray array = new Gson().fromJson((String) getUsers(token), JsonArray.class);
		
		
		return array.size();
	}


	public Object getUsersLikes(String token) {
		
		return null;
	}

}

