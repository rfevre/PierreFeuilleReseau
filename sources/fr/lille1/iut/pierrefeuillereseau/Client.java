package fr.lille1.iut.pierrefeuillereseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class Client {
	private final static int _dgLength = 50;
	private DatagramSocket dgSocket;
	private DatagramPacket dgPacket;
	private static Client client;

	public Client() throws IOException {
		client = new Client();
		dgSocket = new DatagramSocket();
	}

	private String receive() throws IOException {
		byte[] buffer = new byte[_dgLength];
		dgPacket = new DatagramPacket(buffer, _dgLength);
		dgSocket.receive(dgPacket);
		return new String(dgPacket.getData(), dgPacket.getOffset(), dgPacket.getLength());
	}

	private void send(String msg, InetAddress address, int port) throws IOException {
		byte[] buffer = msg.getBytes();
		dgPacket = new DatagramPacket(buffer, 0, buffer.length);
		dgPacket.setAddress(address);
		dgPacket.setPort(port);
		dgSocket.send(dgPacket);
	}

	public static void main(String[] args) throws IOException {
		Scanner	sc = new Scanner(System.in);
		char carac;
		String reponse;

		do {
			System.out.println("================================================================================");
			System.out.println("Bonjour, et bienvenue sur le Pierre Feuille Reseau !");
			System.out.println("1 => Créer une partie");
			System.out.println("2 => Rejoindre une partie");
			System.out.println("q => Quitter");
			System.out.println("================================================================================");
			reponse = sc.nextLine();
			carac = reponse.charAt(0);
			if (carac == '1') {
				creerPartie();
			}
			else if (carac == '2') {
				rejoindrePartie();
			}
			else if (carac == 'q') {
				System.out.println("Quittage en cours ...");
			}
		} while (carac != 'q');
		// String msg = "Hello ";
		// client.send(msg, InetAddress.getByName("localhost"), Integer.parseInt("3630"));
		// System.out.println(client.receive());
	}

	private static void creerPartie() throws IOException {
		Scanner	sc = new Scanner(System.in);
		String pseudo, idPartie;
		int nbJoueurs, nbManches;

		System.out.println("Choisissez un pseudonyme ?");
		pseudo = sc.nextLine();
		System.out.println("Choisissez un identifiant pour la partie ?");
		idPartie = sc.nextLine();
		System.out.println("Choisissez un nombre de manches pour la partie ?");
		sc.nextLine();
		nbManches = sc.nextInt();

		// `CREATE:PSEUDO:ID_PARTIE:NB_JOUEURS:NB_MANCHES`
		String requete = "CREATE:"+pseudo+":"+idPartie+":2:"+nbManches;
		client.send(requete, InetAddress.getByName("localhost"), 3630);
	 	System.out.println(client.receive());
	}

	private static void rejoindrePartie() throws IOException {
		Scanner	sc = new Scanner(System.in);

		System.out.println("C'est oui !");
		String str = sc.nextLine();
	}

}
