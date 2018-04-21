package soap_server;

import javax.jws.WebMethod;
import javax.jws.WebService;


import org.json.JSONException;

@WebService
public interface LogicI {
	
	@WebMethod boolean login(String adminUsername, String password) throws Exception;
	@WebMethod Object getUsers(String adminUsername, String password) throws JSONException;
	@WebMethod Object getSpecificUser(String adminUsername, String password, int userId) throws JSONException;
	@WebMethod Object deleteUser(String adminUsername, String password, int restaurantId);
	@WebMethod Object addUser(String adminUsername, String password);
	@WebMethod Object getRestaurants(String adminUsername, String password);
	@WebMethod Object getSpecificRestaurant(String adminUsername, String password, int restaurantId);
	@WebMethod Object deleteRestaurant(String adminUsername, String password, int restaurantId);
	@WebMethod Object addRestaurant(String adminUsername, String password);
	  
}
  