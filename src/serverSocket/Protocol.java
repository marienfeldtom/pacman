package serverSocket;

import java.io.*;
import java.net.*;
import java.util.*;

import elements.Element;
import elements.Wall;

public class Protocol implements Runnable {

	private Socket socket;

	private BufferedReader socketIn;

	private ObjectOutputStream oos;

	private List<Element> elementList;
	private boolean firstConnection;

	public Protocol(Socket s, List<Element> elementList, boolean firstConnection) {
		try {
			this.socket = s;
			this.firstConnection = firstConnection;
			this.elementList = elementList;
			socketIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			System.out.println("IO-Error");
			e.printStackTrace();
		}
	}

	public void transact() {

		try {
			try {
				ArrayList<Element> elementList2 = new ArrayList<Element>(elementList);
				if (!firstConnection) {
					elementList2.removeIf((Element o) -> o instanceof Wall);
				}
				oos.writeObject(elementList2);
				oos.flush();
				socketIn.read();
				oos.close();
			} catch (NumberFormatException e) {
				System.out.println(e);
			} finally {
				socket.close();
			}

		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		transact();

	}
}
