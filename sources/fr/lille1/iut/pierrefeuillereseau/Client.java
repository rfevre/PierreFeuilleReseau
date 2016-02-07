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
	private String pseudo, idPartie;

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
	}

	public void start() throws IOException {
		char carac;
		String reponse;

		do {
			reponse="";
			carac='0';
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

	private void creerPartie() throws IOException{
		String tmp;
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
		do {
			send(requete, InetAddress.getByName(adresse), Integer.parseInt(port));
			tmp = receive();
			if(tmp.equals("KO")) {
				System.out.println("Probléme création : " + tmp);
				start();
			}
			else if (tmp.equals("OK")) {
				System.out.println("Partie créée => " + tmp + ", en attente d'un joueur.");
			}
		}while(!tmp.equals("READY"));
		System.out.println("Partie ready => " + tmp);
		lancementPartie("Createur");
	}

	private void rejoindrePartie() throws IOException {
		String tmp;
		System.out.println("Choisissez un pseudonyme ?");
		pseudo = sc.nextLine();
		System.out.println("Choisissez un identifiant pour la partie ?");
		idPartie = sc.nextLine();

		// `JOIN:PSEUDO:ID_PARTIE`
		String requete = "JOIN:"+pseudo+":"+idPartie;
		do {
			send(requete, InetAddress.getByName(adresse), Integer.parseInt(port));
			tmp = receive();
			if(tmp.equals("ERROR")) {
				System.out.println("Probleme pour rejoindre la partie : " + tmp);
				start();
			}
			else if(tmp.equals("WAIT")) {
				System.out.println("Connexion réussie, en attente des autres joueurs : " + tmp);
			}
		}while(!tmp.equals("READY"));
		System.out.println("Partie ready => " + tmp);
		lancementPartie("Challenger");
	}

	private void lancementPartie(String status) throws IOException {
		System.out.println("Partie lancée !");
		String tmp, choix = null;
		Integer numManche = 0;
		do {
			do {
				System.out.println("================================================================================");
				System.out.println("Votre choix ROCK/PAPER/SCISSORS ?");
				choix = sc.nextLine();
			}while((!choix.equals("ROCK")) && (!choix.equals("PAPER")) && (!choix.equals("SCISSORS")));

			String requete = choix + ":" + pseudo + ":" + idPartie;
			System.out.println("En attente du choix du second joueur !");

			do {
				send(requete, InetAddress.getByName(adresse), Integer.parseInt(port));
				tmp = receive();
				if(!tmp.equals("WAIT")) {
					System.out.println("================================================================================");
					if (tmp.split(":")[1].equals("KO")) {
						if (tmp.split(":")[2].equals("x")) {
							System.out.println("Egalité avec " + tmp.split(":")[3] + " points partout");
						} else {
							System.out.println("Le gagnant est : " + tmp.split(":")[2] + " avec " + tmp.split(":")[3] + " points");
						}
					} else {
						numManche++;
						if(status.equals("Createur")) {
							System.out.println("Votre choix : "+tmp.split(":")[0]);
							System.out.println("Choix adverse : "+tmp.split(":")[1]);
							System.out.println("Vos points : "+tmp.split(":")[2]);
							System.out.println("Points adverse : "+tmp.split(":")[3]);
						} else {
							System.out.println("Votre choix : "+tmp.split(":")[1]);
							System.out.println("Choix adverse : "+tmp.split(":")[0]);
							System.out.println("Vos points : "+tmp.split(":")[3]);
							System.out.println("Points adverse : "+tmp.split(":")[2]);
						}
						System.out.println("Nombres de manches : " + numManche + "/" + Integer.parseInt((tmp.split(":"))[4]));
					}
				}
			}while(tmp.equals("WAIT"));
		}while(!(tmp.split(":"))[1].equals("KO"));
	}

}
