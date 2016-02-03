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
	private Scanner sc;
	private String adresse,port;

	public Client() throws IOException {
		this.adresse = "localhost";
		this.port = "3630";
		this.sc = new Scanner(System.in);
		this.dgSocket = new DatagramSocket();
	}

	public Client(String adresse,String port) throws IOException {
		this.adresse = adresse;
		this.port = port;
		this.sc = new Scanner(System.in);
		this.dgSocket = new DatagramSocket();
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
		Client client = new Client();
		client.start();

		// String msg = "Hello ";
		// client.send(msg, InetAddress.getByName("localhost"), Integer.parseInt("3630"));
		// System.out.println(client.receive());
	}

	public void start() throws IOException {
		char carac;
		String reponse;

		do {
			reponse="";
			System.out.println("================================================================================");
			System.out.println("Bonjour, et bienvenue sur le Pierre Feuille Reseau !");
			System.out.println("1 => Créer une partie");
			System.out.println("2 => Rejoindre une partie");
			System.out.println("q => Quitter");
			System.out.println("================================================================================");
			reponse = sc.nextLine();
			carac = reponse.charAt(0);
			if (carac == '1') {
				this.creerPartie();
			}
			else if (carac == '2') {
				this.rejoindrePartie();
			}
			else if (carac == 'q') {
				System.out.println("Quittage en cours ...");
			}
		} while (carac != 'q');
	}

	private void creerPartie() throws IOException {
		String pseudo, idPartie;
		int nbJoueurs, nbManches;

		System.out.println("Choisissez un pseudonyme ?");
		pseudo = sc.nextLine();
		System.out.println("Choisissez un identifiant pour la partie ?");
		idPartie = sc.nextLine();
		System.out.println("Choisissez un nombre de manches pour la partie ?");
		nbManches = sc.nextInt();
		sc.nextLine();

		// `CREATE:PSEUDO:ID_PARTIE:NB_JOUEURS:NB_MANCHES`
		String requete = "CREATE:"+pseudo+":"+idPartie+":2:"+nbManches;
		send(requete, InetAddress.getByName(adresse), Integer.parseInt(port));
	 	if(receive().equals("OK")){
			System.out.println("Partie créée.");
			lancementPartie();
		} else {
			System.out.println("Erreur lors de la création de la partie.");
		}
	}

	private void rejoindrePartie() throws IOException {
		String pseudo, idPartie;
		System.out.println("Choisissez un pseudonyme ?");
		pseudo = sc.nextLine();
		System.out.println("Choisissez un identifiant pour la partie ?");
		idPartie = sc.nextLine();

		// `JOIN:PSEUDO:ID_PARTIE`
		String requete = "JOIN:"+pseudo+":"+idPartie;
		send(requete, InetAddress.getByName(adresse), Integer.parseInt(port));
		String temp;
		while(!(temp = receive()).equals("READY")) {
			System.out.println(temp);
		}
	}

	private void lancementPartie() throws IOException {
	}

}
