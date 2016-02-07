package fr.lille1.iut.pierrefeuillereseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class Serveur {
	private final static int _dgLength = 50;
	private int _udpPort = 3630;
	private DatagramSocket dgSocket;
	private DatagramPacket dgPacket;
	private Partie p;
	private String status;

	public Serveur() throws IOException {
		dgSocket = new DatagramSocket(_udpPort);
		p = null;
	}

	private void go() throws IOException {
		InetAddress address;
		int port;
		status = "CREATE";
		while ( true ) {

			// Attente de réception d'un datagramme
			String msg = receive();

			// Vérification du message et création du message de retour :
			String ret = process(msg);

			// 	Récupération de l'adresse et du port du client
			address = dgPacket.getAddress();
			port = dgPacket.getPort();
			send(ret, address, port);
		}

	}

	/**
	*
	*/
	private String process(String mess) {
		String command;
		String ret = "ERROR";
		if(verifierMessage(getCommand(mess)) && (mess.split(":")).length >= 3) {
			command = getCommand(mess);
			if(status.equals("CREATE") && command.equals("CREATE")) {
				System.out.println("Tentative de création d'une partie...");
				ret = creerPartie(mess);
			}
			else if(status.equals("CONNEXION")) {
				if(command.equals("JOIN")) {
					System.out.println("Un joueur essaye de se connecter à une partie...");
					ret = rejoindrePartie(mess);
				}
				else if (command.equals("CREATE")) {
					if(p.getChallenger() == null) {
						ret = "WAIT";
					}
					else {
						ret = "READY";
						status = "PLAYING";
					}
				}
			}
			else if(status.equals("PLAYING") && ((command.equals("ROCK") || command.equals("PAPER") || command.equals("SCISSORS") || command.equals("DISCONNECT") || command.equals("CLOSE")))) {
				ret = choix(mess);
			}
			else {
				return "ERROR";
			}
		}
		return ret;
	}

	/**
	* Retourne la commande du message d'un utilisateur
	*/
	private String getCommand(String mess) {
		return (mess.split(":"))[0];
	}

	/**
	* Retourne le pseudo de l'utilisateur
	*/
	private String getPseudo(String mess) {
		return (mess.split(":"))[1];
	}

	/**
	* Retourne la partie de la requête
	*/
	private String getId(String mess) {
		return (mess.split(":"))[2];
	}

	/**
	* Retourne le nombre de joueurs de la requête de création de partie
	*/
	private int getNbJoueurs(String mess) {
		return Integer.parseInt((mess.split(":"))[3]);
	}

	/**
	* Retourne le nombre de manches de la requête de création de partie
	*/
	private int getNbManches(String mess) {
		return Integer.parseInt((mess.split(":"))[4]);
	}

	/**
	* Retourne le choix
	*/
	private String getChoix(String mess) {
		return (mess.split(":"))[0];
	}

	/**
	* Vérifie l'identifiant d'une partie
	*/
	private boolean checkId(String nom) {
		// TODO vérifier la map de parties
		return true;
	}

	/**
	* Vérifie que le message est correct et correspond à une des commandes utilisateur
	*/
	private boolean verifierMessage(String command) {
		return (command.equals("CREATE")
		|| command.equals("JOIN")
		|| command.equals("ROCK")
		|| command.equals("PAPER")
		|| command.equals("SCISSORS")
		|| command.equals("DISCONNECT")
		|| command.equals("CLOSE"));
	}

	/**
	* Créer la partie et envoie une message de retour selon le resultat
	*/
	private String creerPartie(String mess) {
		if(((mess.split(":")).length != 5) || (!checkId(getId(mess))) || (p != null))
			return "KO";
		p = new Partie(getPseudo(mess), getId(mess), getNbJoueurs(mess), getNbManches(mess));
		System.out.println("Partie créée.");
		status = "CONNEXION";
		return "OK";
	}

	/**
	* Rejoindre une partie
	*/
	private String rejoindrePartie(String mess) {
		if(((mess.split(":")).length != 3) || (!checkId(getId(mess))) || (p == null)) {
			return "ERROR";
		}
		p.setChallenger(getPseudo(mess));
		System.out.println("Le joueur s'est inscrit dans la partie.");
		System.out.println(p);
		return "READY";
	}

	/**
	*	L'utiliateur entre son choix
	*/
	private String choix(String mess) {
		if(((mess.split(":")).length != 3) || (!checkId(getId(mess))) || (p == null)) {
			return "ERROR";
		}

		System.out.println(p.getChoixJ1() + " / " + p.getChoixJ2());
		if(p.getChoixJ1() != null && p.getChoixJ2() != null) {
			return result(mess);
		}

		String j = getPseudo(mess);
		if(j.equals(p.getCreateur())) {
			if(p.getChoixJ1() == null) {
				p.setChoixJ1(getChoix(mess));
			}
			return "WAIT";
		}
		else if (j.equals(p.getChallenger())) {
			if(p.getChoixJ2() == null) {
				p.setChoixJ2(getChoix(mess));
			}
			return "WAIT";
		}
		else {
			return "ERROR";
		}
	}

	/**
	* On construit la requete de retour avec de la manche + les scores et on incremente les scores
	*/
	private String result(String mess) {
		if(getPseudo(mess).equals(p.getCreateur())) {
			if(p.getChoixJ1().equals("ROCK")) {
				if(p.getChoixJ2().equals("PAPER")) {
					p.setScoreJ2(p.getScoreJ2() + 1);
				}
				else if(p.getChoixJ2().equals("SCISSORS")) {
					p.setScoreJ1(p.getScoreJ1() + 1);
				}
			}
			else if(p.getChoixJ1().equals("PAPER")) {
				if(p.getChoixJ2().equals("SCISSORS")) {
					p.setScoreJ2(p.getScoreJ2() + 1);
				}
				else if(p.getChoixJ2().equals("ROCK")) {
					p.setScoreJ1(p.getScoreJ1() + 1);
				}
			}
			else {
				if(p.getChoixJ2().equals("ROCK")) {
					p.setScoreJ2(p.getScoreJ2() + 1);
				}
				else if(p.getChoixJ2().equals("PAPER")) {
					p.setScoreJ1(p.getScoreJ1() + 1);
				}
			}
		}
		String ret = p.getChoixJ1() + ":" + p.getChoixJ2() + ":" + p.getScoreJ1() + ":" + p.getScoreJ2() + ":" + p.getNbManches();

		if(getPseudo(mess).equals(p.getCreateur())) {
			p.setChoixJ1(null);
		}
		else {
			p.setChoixJ2(null);
		}

		return ret;
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

	public static void main(String[] args) throws NumberFormatException, IOException {
		Serveur serveur = new Serveur();
		serveur.go();
	}

}
