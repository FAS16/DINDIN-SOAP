package soap_server;

import javax.jws.WebMethod;
import javax.jws.WebService;


import org.json.JSONException;

@WebService
public interface LogicI {
	
	@WebMethod String login(String adminUsername, String password) throws Exception;
	@WebMethod Object getUsers(String token) throws JSONException;
	@WebMethod Object getSpecificUser(int userId, String token) throws JSONException;
	@WebMethod Object getNumOfUsers(String token) throws JSONException;
	@WebMethod Object deleteUser(int userId, String token);
	
	
	@WebMethod Object getRestaurants(String token);
	@WebMethod Object getSpecificRestaurant(int restaurantId, String token) throws JSONException;
	@WebMethod Object getNumOfRestaurants(String token) throws JSONException;
	@WebMethod Object addRestaurant(String name, int zipcode, String address, String cuisine, String budget, String phone, String website, String token) throws JSONException;
	@WebMethod Object deleteRestaurant(int restaurantId, String token);
	  
}