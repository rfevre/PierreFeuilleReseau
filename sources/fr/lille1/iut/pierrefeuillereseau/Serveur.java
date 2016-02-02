package fr.lille1.iut.pierrefeuillereseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Serveur {
	private final static int _dgLength = 50;
	private int _udpPort;
	private DatagramSocket dgSocket;
	private DatagramPacket dgPacket;

	public ServeurUDP(int _udpPort) throws IOException {
		this._udpPort = _udpPort;
		dgSocket = new DatagramSocket(_udpPort);
	}

	private void go() throws IOException {
		InetAddress address;
		int port;
		while ( true ) {
			// Attente de réception d'un datagramme
			String msg = receive();
			msg = msg + "World !";
		// 	Récupération de l'adresse et du port du client
			address = dgPacket.getAddress();
			port = dgPacket.getPort();
			send(msg, address, port);
		}

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
		ServeurUDP serveur = new ServeurUDP(Integer.parseInt(args[0]));
		serveur.go();
	}

}
