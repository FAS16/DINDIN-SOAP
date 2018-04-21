package soap_server;

import java.net.MalformedURLException;
import javax.ws.rs.core.Response.Status;
import java.net.URL;
import java.util.Scanner;

import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.json.JSONException;

public class CMDClient {

	private static final String URL = "";

	public static void main(String[] args) throws MalformedURLException, JSONException {

		URL url = new URL("http://localhost:9901/dindinadmin?wsdl");
		QName qname = new QName("http://soap_server/", "LogicService");
		Service service = Service.create(url, qname);
		LogicI logic = service.getPort(LogicI.class);

		boolean userAuthenticated = false;
		String adminUsername, password;
		Scanner scan = new Scanner(System.in);

		System.out.println("Velkommen til dindins admin-klient \nlog ind for at administrere:");
		while (!userAuthenticated) {
			System.out.println("Indtast brugernavn:");
			adminUsername = scan.nextLine();
			System.out.println("Indtast adgangskode:");
			password = scan.nextLine();

			try {
				if (logic.login(adminUsername, password)) {
					userAuthenticated = true;
					System.out.println("Logget ind");
				}

			} catch (Exception e) {
				System.out.println("Fejl ved log ind, prøv igen!\n");
				e.printStackTrace();

			}

		}

		while (true) {

			int choice;
			System.out.println("________________________________");
			System.out.println("\nTryk 1 for at se antal brugere i databasen");
			System.out.println("Tryk 2 for at søge efter en bruger databasen");
			System.out.println("Tryk 3 for at se antal restauranter i databasen");
			System.out.println("Tryk 4 for at afslutte");

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
				System.out.println(logic.getUsers("bliver måske", "brugt til authentication på rest-serveren?"));
				break;

			case 2:
				whileLoop: while (true) {
					int userId;
					System.out.println("Indtast brugerens id:"); // Lav for mail istedet
					if (scan.hasNextInt()) {
						userId = scan.nextInt();
						scan.nextLine();
					} else {
						System.out.println("Indtast venligst et gyldigt id (numerisk værdi)");
						scan.nextLine();
						continue whileLoop;
					}
					System.out.println(logic.getSpecificUser("", "", userId));
					break whileLoop;
				}

				break;

			case 4:
				System.out.println("\nProgrammet afsluttes...");
				scan.close();
				System.exit(0);
			default:
				System.out.println("Ugyldigt valg, prøv igen.");
			}
		}
	}

}
