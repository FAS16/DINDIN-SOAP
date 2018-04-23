package soap_server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.json.JSONException;



public class CMDClient {

	public static void main(String[] args) throws MalformedURLException, JSONException {
		final String URL = "http://130.225.170.248:9901/dindinadmin?wsdl";
		URL url = new URL("http://localhost:9901/dindinadmin?wsdl");
		//URL url = new URL(URL);
		QName qname = new QName("http://soap_server/", "LogicService");
		Service service = Service.create(url, qname);
		LogicI logic = service.getPort(LogicI.class);

		boolean userAuthenticated = false;
		String adminUsername, password, token = null;
		Scanner scan = new Scanner(System.in);

		System.out.println("Velkommen til dindins admin-klient \nlog ind for at administrere:");
		while (!userAuthenticated) {
			System.out.println("Indtast brugernavn:");
			adminUsername = scan.nextLine();
			System.out.println("Indtast adgangskode:");
			password = scan.nextLine();

			try {
				token = logic.login(adminUsername, password);
				if (token != null) {
					userAuthenticated = true;
					System.out.println("Logget ind");
				} else
					System.out.println("Forkert brugernavn og/eller adgangskode, prøv igen!\n");

			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		while (true) {

			int choice;
			System.out.println("________________________________");
			System.out.println("\nTryk 1 for at se alle brugere i databasen");
			System.out.println("Tryk 2 for at søge efter en bruger databasen");
			System.out.println("Tryk 3 for at få antal tilmeldte brugere fra databasen");
			System.out.println("Tryk 4 for at en specifik brugeres likes");
			System.out.println("Tryk 5 for at slette en bruger");

			System.out.println("Tryk 6 for at se alle restauranter i databasen");
			System.out.println("Tryk 7 for at søge efter en restaurant i databasen");
			System.out.println("Tryk 8 for at få antal tilmeldte restauranter fra databasen");
			System.out.println("Tryk 9 for at søge efter restauranter i et bestemt postnummer");
			System.out.println("Tryk 10 for at slette en restaurant");
			System.out.println("Tryk 11 for at afslutte");

			if (scan.hasNextInt()) {
				choice = scan.nextInt();
				scan.nextLine();
			} else {
				System.out.println("Ugyldigt valg, prøv igen.");
				scan.nextLine();
				continue;
			}

			switch (choice) {
			case 1:
				System.out.println(logic.getUsers(token));
				break;

			case 2:
				whileLoop: while (true) {
					int userId;
					System.out.println("Indtast brugernavn:");
					if (scan.hasNextLine()) {
						userId = scan.nextInt();
						scan.nextLine();
					} else {
						System.out.println("Indtast venligst et gyldigt id (numerisk værdi)");
						scan.nextLine();
						continue whileLoop;
					}
					System.out.println(logic.getSpecificUser(userId, token));
					break whileLoop;
				}

				break;

			case 3:
				System.out.println("Antal tilmeldte brugere: " + logic.getNumOfUsers(token));
				break;

			case 5:
				System.out.println("\nProgrammet afsluttes...");
				scan.close();
				System.exit(0);
			default:
				System.out.println("Ugyldigt valg, prøv igen.");
			}
		}
	}

}
