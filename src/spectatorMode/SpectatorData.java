package spectatorMode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import elements.Element;

public class SpectatorData implements Runnable {
	private String ipAdresse;
	private ArrayList<Element> elementList;
	private ArrayList<Element> elementListMoving;
	private boolean firstConnection = true;

	public SpectatorData(String ipAdresse) {
		this.ipAdresse = ipAdresse;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		String hostName = ipAdresse;
		int port;
		Socket c = null;
		while (true) {
			try {
				Thread.sleep(1000 / 60);
				port = 4711;
				c = new Socket(ipAdresse, port);

				ObjectInputStream objectInput = new ObjectInputStream(c.getInputStream());

				PrintWriter socketOut = new PrintWriter(c.getOutputStream(), true);
				socketOut.println(1);

				if (firstConnection) {
					elementList = (ArrayList<Element>) objectInput.readObject();
				} else {
					elementListMoving = (ArrayList<Element>) objectInput.readObject();
				}
				firstConnection = false;
				c.close();
			} catch (UnknownHostException ue) {
				System.out.println("Kein DNS-Eintrag fuer " + hostName);
			} catch (NoRouteToHostException e) {
				System.err.println("Nicht erreichbar " + hostName);
			} catch (IOException e) {
				System.out.println("IO-Error");
				System.out.println(e);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public boolean isFirstConnection() {
		return firstConnection;
	}

	public void setFirstConnection(boolean firstConnection) {
		this.firstConnection = firstConnection;
	}

	public ArrayList<Element> getElementListMoving() {
		return elementListMoving;
	}

	public ArrayList<Element> getElementList() {
		return this.elementList;
	}
}
