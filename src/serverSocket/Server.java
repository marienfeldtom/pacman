package serverSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import elements.Element;

public class Server implements Runnable {
	private List<Element> elementList;
	private boolean firstConnection = true;
	private ServerSocket server;

	public Server() {

	}

	public void setElementList(List<Element> elementList) {
		this.elementList = elementList;
	}

	public void transact() {
		try {

			int port = 4711;

			server = new ServerSocket(port);

			System.out.println("Server gestartet!");

			while (true) {

				Socket socket = server.accept();
				Protocol protocol = new Protocol(socket, elementList, firstConnection);
				Thread thread = new Thread(protocol);
				thread.start();
				firstConnection = false;
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		transact();
	}

}
