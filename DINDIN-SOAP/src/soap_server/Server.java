package soap_server;

import javax.xml.ws.Endpoint;

public class Server {
	public static void main(String[] args) {
		System.out.println("Publishing DINDIN admin service");
		Logic logic = new Logic();
		Endpoint.publish("http://[::]:9901/dindinadmin",logic);
		System.out.println("DINDIN admin service is published");
		
		
	}

}
