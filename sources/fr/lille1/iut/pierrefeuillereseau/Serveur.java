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

	public Serveur() throws IOException {
		dgSocket = new DatagramSocket(_udpPort);
		p = null;
	}

	private void go() throws IOException {
		InetAddress address;
		int port;
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
		if(verifierMessage(getCommand(mess)) && (mess.split(":")).length >= 3) {
			String ret = null;
			switch(getCommand(mess)) {
				case "CREATE":
					ret = creerPartie(mess);
					break;
				case "JOIN":
					ret = rejoindrePartie(mess);
					break;
				case "ROCK":
					ret = "ROCK";
					break;
				case "PAPER":
					ret = "PAPER";
					break;
				case "SCISSCORS":
					ret = "SCISSCORS";
					break;
				case "DISCONNECT":
					ret = "DISCONNECT";
					break;
				case "CLOSE":
					ret = "CLOSE";
					break;
				default :
					ret = "ERROR";
			}
			return ret;
		}
		return "ERROR";
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
		|| command.equals("SCISSCORS")
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
		return "OK";
	}

	/**
	* Rejoindre une partie
	*/
	private String rejoindrePartie(String mess) {
		if(((mess.split(":")).length != 3) || (!checkId(getId(mess))) || (p == null))
			return "ERROR";
		return "READY";
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
