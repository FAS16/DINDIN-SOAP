package soap_server;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

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
public class Logic implements LogicI {

	public boolean login(String username, String password) throws Exception {
		
		Credentials creds = new Credentials();
		creds.setUsername(username);
		creds.setPassword(password);
		String json = new Gson().toJson(creds);

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/dindin/webapi/login");
		Entity<String> data = Entity.entity(json, MediaType.APPLICATION_JSON);
		Response response = target.request(MediaType.APPLICATION_JSON).post(data);
		
		
		if (response.getStatus() == Status.OK.getStatusCode()) {
//			String result = response.readEntity(String.class);
//			JSONObject o = new JSONObject(result);
//			String name = o.getString("fornavn");
			
			return true;
			
		} else  {
			return false;
		}
	
	}

	@Override
	public Object getUsers(String adminUsername, String password) {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget usersTarget = baseTarget.path("users");
		String users = usersTarget.request(MediaType.APPLICATION_JSON).get(String.class);

		return users;
	}

	@Override
	public Object getSpecificUser(String adminUsername, String password, int userId) throws JSONException {

		Client client = ClientBuilder.newClient();

		WebTarget baseTarget = client.target("http://localhost:8080/dindin/webapi/");
		WebTarget usersTarget = baseTarget.path("users");
		WebTarget singeUserTarget = usersTarget.path("{userId}"); // Placeholder for user
		Response response = singeUserTarget.resolveTemplate("userId", userId).request().get();
		String result = response.readEntity(String.class);
		JSONObject o = new JSONObject(result);

		if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			int errorCode = o.getInt("errorCode");
			String errorMessage = o.getString("errorMessage");
			return "FEJL - BRUGER FINDES IKKE\n" + "Fejlkode: " + errorCode + "\nFejlbesked: " + errorMessage;
			
		} else if (response.getStatus() == Status.SERVICE_UNAVAILABLE.getStatusCode()) {
			return "Fejl, prøv igen senere.";
		}

		else {
			int id = o.getInt("id");
			String email = o.getString("email");
			String username = o.getString("userName");
			String firstName = o.getString("firstName");
			String lastName = o.getString("lastName");
			
			return "BRUGER FUNDET\n" + "ID: " + id + "\nE-MAIL: " + email + "\nBrugernavn: " + username + "\nFornavn: "
					+ firstName + "\nEfternavn: " + lastName;
		}

	}

	@Override
	public Object getRestaurants(String adminUsername, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteUser(String adminUsername, String password, int restaurantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addUser(String adminUsername, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSpecificRestaurant(String adminUsername, String password, int restaurantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteRestaurant(String adminUsername, String password, int restaurantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addRestaurant(String adminUsername, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
