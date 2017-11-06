package other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import elements.BigCoin;
import elements.Coin;
import elements.Enemy;
import elements.Element;
import elements.Player;
import elements.Wall;

public class LevelReader {

	private int spielerKoordinaten[] = new int[2];
	private transient File file;
	private String name;
	private int counter = 1;
	ArrayList<Element> elementList = new ArrayList<>();
	ArrayList<Integer[]> wallCoordinates = new ArrayList<>();

	public LevelReader(String levelPath, String name) {
		this.name = name;
		File f = new File("data/level/" + levelPath + ".txt");
		if (!f.exists() || !f.canRead()) {
			System.out.println("Leveldatei \"" + f + "\" wurde nicht gefunden.");
			System.exit(0);
		}

		if (f.isDirectory()) {
			System.out.println("Inhalt von Verzeichnis \"" + f + "\":");
			String[] datei = f.list();
			for (int i = 0; i < datei.length; i++)
				System.out.println(datei[i]);
			System.exit(0);
		}

		this.file = f;
	}

	public int[] getSpielerPos() {
		return spielerKoordinaten;
	}

	public void getLevelData() {
		ArrayList<String> levelData = new ArrayList<>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				levelData.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		int xPos = 0, yPos = 0;
		for (String s : levelData) {
			for (int i = 0; i < s.length(); i++) {
				Integer coordinates[] = { xPos, yPos };
				if (s.charAt(i) == 'x') {
					elementList.add(new Wall(xPos, yPos));
					wallCoordinates.add(coordinates);
				} else if (s.charAt(i) == 'p') {
					elementList.add(new Player(name, xPos, yPos));
				} else if (s.charAt(i) == 'o') {
					elementList.add(new Coin(xPos + 18, yPos + 18));
				} else if (s.charAt(i) == 'g') {
					elementList.add(new Coin(xPos + 18, yPos + 18));
					elementList.add(new Enemy(xPos, yPos, "data/ghosts/ghost" + counter + ".png"));
					counter++;
				} else if (s.charAt(i) == 'b') {
					elementList.add(new BigCoin(xPos + 12, yPos + 12));
				}
				xPos += 50;
			}
			xPos = 0;
			yPos += 50;
		}
	}

	public ArrayList<Integer[]> getWallCoordinates() {
		return wallCoordinates;
	}

	public ArrayList<Element> getElementList() {
		return elementList;
	}

}
