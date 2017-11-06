package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import other.Config;

public class LevelEditor extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GridBagLayout gbLayout = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();

	private JPanel headerPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel itemPanel;
	private JPanel rasterPanel;
	private JComboBox<String> levelBox;

	private Font fontDefault = new Font("Test", 25, 25);

	private boolean playerSet = false;
	private boolean ghost1Set = false;
	private boolean ghost2Set = false;
	private boolean ghost3Set = false;
	private boolean ghost4Set = false;
	private boolean ghost5Set = false;

	private Image coinImage = new ImageIcon(Config.CoinImage).getImage();
	private Image wallIcon = new ImageIcon(Config.WallImage).getImage();
	private Image pacmanIcon = new ImageIcon(Config.PlayerImage1).getImage();
	private Image ghost1Icon = new ImageIcon("data/ghosts/ghost1.png").getImage();
	private Image ghost2Icon = new ImageIcon("data/ghosts/ghost2.png").getImage();
	private Image ghost3Icon = new ImageIcon("data/ghosts/ghost3.png").getImage();
	private Image ghost4Icon = new ImageIcon("data/ghosts/ghost4.png").getImage();
	private Image ghost5Icon = new ImageIcon("data/ghosts/ghost5.png").getImage();

	private ArrayList<Image> ghostImagesList = new ArrayList<>();
	private ArrayList<String> levelNames;

	private String[][] grid = new String[24][14];
	private String selectedItem = "";
	private String levelPath;

	public LevelEditor(String levelPath, ArrayList<String> levelNames) {
		this.levelNames = levelNames;
		this.levelPath = levelPath;
		setVisible(true);
		setSize(1300, 1000);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Level-Editor");
		setLocationRelativeTo(getParent());
		setLayout(gbLayout);

		getContentPane().setBackground(new Color(25, 34, 66));

		c.insets = new Insets(2, 2, 2, 2);
		c.gridx = 0;
		c.gridy = 0;
		add(headerPanel, c);
		c.gridy = 1;
		add(centerPanel, c);
		c.gridy = 2;
		add(bottomPanel, c);

		loadingLevelData();

		headerBuild();
		centerBuild();
		bottomBuild();
	}

	private void save() {

		if (levelNames.contains(levelBox.getSelectedItem())) {
			int result = JOptionPane.showConfirmDialog(null,
					"\"" + levelBox.getSelectedItem() + "\"" + " existiert bereits. Überschreiben?", "Speichern",
					JOptionPane.YES_NO_OPTION);

			switch (result) {
			case JOptionPane.YES_OPTION:
				File file = new File("data/level/" + levelBox.getSelectedItem() + ".txt");
				file.delete();
				levelSet();
			}
		} else {
			levelSet();
			File f = new File("data/level");
			File[] fileArray = f.listFiles();
			levelNames = new ArrayList<>();
			for (int i = 0; i < fileArray.length; i++) {
				levelNames.add(fileArray[i].getName().split(".txt")[0]);
			}
		}

		fillLevelBox();
		centerPanel.repaint();

	}

	private void fillLevelBox() {
		levelBox.removeAllItems();
		for (String s : levelNames) {
			levelBox.addItem(s);
		}
	}

	private void levelSet() {
		String[][] out = new String[24][14];
		for (int x = 0; x < 24; x++) {
			for (int y = 0; y < 14; y++) {
				if (grid[x][y].equals("wall")) {
					out[x][y] = "x";
				} else if (grid[x][y].equals("player")) {
					out[x][y] = "p";
				} else if (grid[x][y].equals("smallCoin")) {
					out[x][y] = "o";
				} else if (grid[x][y].equals("ghost1") || grid[x][y].equals("ghost2") || grid[x][y].equals("ghost3")
						|| grid[x][y].equals("ghost4") || grid[x][y].equals("ghost5")) {
					out[x][y] = "g";
				} else if (grid[x][y].equals("bigCoin")) {
					out[x][y] = "b";
				}
			}
		}

		BufferedWriter output;

		try {
			output = new BufferedWriter(new FileWriter("data/level/" + levelBox.getSelectedItem() + ".txt", true));
			for (int i = 0; i < 14; i++) {
				output.write((out[0][i] + out[1][i] + out[2][i] + out[3][i] + out[4][i] + out[5][i] + out[6][i]
						+ out[7][i] + out[8][i] + out[9][i] + out[10][i] + out[11][i] + out[12][i] + out[13][i]
						+ out[14][i] + out[15][i] + out[16][i] + out[17][i] + out[18][i] + out[19][i] + out[20][i]
						+ out[21][i] + out[22][i] + out[23][i]));
				output.newLine();
			}

			output.close();

		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

	}

	private void loadingLevelData() {

		for (int x = 0; x < 24; x++) {
			for (int y = 0; y < 14; y++) {
				grid[x][y] = "";
			}
		}

		playerSet = false;
		ghost1Set = false;
		ghost2Set = false;
		ghost3Set = false;
		ghost4Set = false;
		ghost5Set = false;

		headerPanel.repaint();

		selectedItem = "wall";
		ghostImagesList.add(ghost1Icon);
		ghostImagesList.add(ghost2Icon);
		ghostImagesList.add(ghost3Icon);
		ghostImagesList.add(ghost4Icon);
		ghostImagesList.add(ghost5Icon);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("data/level/" + levelPath + ".txt")));
			String line = null;
			int spalte = 0;
			int ghostNum = 1;
			while ((line = br.readLine()) != null) {

				char[] zeile = line.toCharArray();

				for (int i = 0; i < line.length(); i++) {
					if (zeile[i] == 'x') {
						grid[i][spalte] = "wall";
					} else if (zeile[i] == 'o') {
						grid[i][spalte] = "smallCoin";
					} else if (zeile[i] == 'b') {
						grid[i][spalte] = "bigCoin";
					} else if (zeile[i] == 'p') {
						grid[i][spalte] = "player";
						playerSet = true;
					} else if (zeile[i] == 'g') {
						grid[i][spalte] = ("ghost" + ghostNum);
						if (ghostNum == 1) {
							ghost1Set = true;
						} else if (ghostNum == 2) {
							ghost2Set = true;
						} else if (ghostNum == 3) {
							ghost3Set = true;
						} else if (ghostNum == 4) {
							ghost4Set = true;
						} else if (ghostNum == 5) {
							ghost5Set = true;
						}
						ghostNum++;
					}
				}
				spalte++;
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

	}

	private void bottomBuild() {
		bottomPanel.setBackground(Color.BLACK);
		bottomPanel.setPreferredSize(new Dimension(1000, 80));

		levelBox = new JComboBox<>();
		levelBox.setEditable(true);
		for (String s : levelNames) {
			levelBox.addItem(s);
		}
		levelBox.setFont(fontDefault);
		JButton loadButton = new JButton("Laden");
		loadButton.setFont(fontDefault);
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				levelPath = (String) levelBox.getSelectedItem();
				loadingLevelData();
				for (int i = 0; i < levelNames.size(); i++) {
					if (levelBox.getItemAt(i).equals(levelPath)) {
						levelBox.setSelectedIndex(i);
					}
				}
				centerPanel.repaint();

			}
		});

		JButton saveButton = new JButton("Speichern");
		saveButton.setFont(fontDefault);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		JButton delButton = new JButton("Entfernen");
		delButton.setFont(fontDefault);
		delButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null,
						"\"" + levelBox.getSelectedItem() + "\"" + " wirklich entfernen?", "Entfernen",
						JOptionPane.YES_NO_OPTION);

				switch (result) {
				case JOptionPane.YES_OPTION:

					File file = new File("data/level/" + levelBox.getSelectedItem() + ".txt");
					file.delete();
					File f = new File("data/level");
					File[] fileArray = f.listFiles();
					levelNames = new ArrayList<>();
					for (int i = 0; i < fileArray.length; i++) {
						levelNames.add(fileArray[i].getName().split(".txt")[0]);
					}
					levelPath = levelNames.get(0);
					loadingLevelData();

					for (int i = 0; i < levelNames.size(); i++) {
						if (levelBox.getItemAt(i).equals(levelPath)) {
							levelBox.setSelectedIndex(i);
						}
					}

					fillLevelBox();
					centerPanel.repaint();
				}

			}
		});

		JButton clearButton = new JButton("Neu");
		clearButton.setFont(fontDefault);
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int x = 0; x < 24; x++) {
					for (int y = 0; y < 14; y++) {
						grid[x][y] = "wall";
					}
				}
				playerSet = false;
				ghost1Set = false;
				ghost2Set = false;
				ghost3Set = false;
				ghost4Set = false;
				ghost5Set = false;
				repaint();
			}
		});

		JLabel fileNameText = new JLabel("             Name: ");
		fileNameText.setFont(fontDefault);
		fileNameText.setForeground(Color.WHITE);
		for (int x = 0; x < levelNames.size(); x++) {
			if (levelNames.get(x).equals(levelPath)) {
				levelBox.setSelectedIndex(x);
			}
		}
		
		bottomPanel.add(levelBox);
		bottomPanel.add(loadButton);
		bottomPanel.add(clearButton);
		bottomPanel.add(saveButton);
		bottomPanel.add(delButton);
	}

	private void centerBuild() {
		centerPanel.setBackground(Color.BLACK);
		centerPanel.setPreferredSize(new Dimension(1200, 710));

		rasterPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6458149684037284614L;

			@Override
			protected void paintComponent(Graphics g) {
				for (int yStart = 0; yStart < 750; yStart += 50) {
					g.drawLine(0, yStart, 1200, yStart);
				}
				for (int xStart = 0; xStart < 1250; xStart += 50) {
					g.drawLine(xStart, 0, xStart, 700);
				}

				for (int x = 0; x < 24; x++) {
					for (int y = 0; y < 14; y++) {
						if (grid[x][y].equals("wall")) {
							g.drawImage(wallIcon, x * 50, y * 50, 50, 50, null);
						} else if (grid[x][y].equals("smallCoin")) {
							g.drawImage(coinImage, x * 50 + 20, y * 50 + 20, 12, 12, null);
						} else if (grid[x][y].equals("bigCoin")) {
							g.drawImage(coinImage, x * 50 + 14, y * 50 + 14, 24, 24, null);
						} else if (grid[x][y].equals("player")) {
							g.drawImage(pacmanIcon, x * 50, y * 50, 50, 50, null);
						} else if (grid[x][y].equals("ghost1")) {
							g.drawImage(ghostImagesList.get(0), x * 50, y * 50, 50, 50, null);
						} else if (grid[x][y].equals("ghost2")) {
							g.drawImage(ghostImagesList.get(1), x * 50, y * 50, 50, 50, null);
						} else if (grid[x][y].equals("ghost3")) {
							g.drawImage(ghostImagesList.get(2), x * 50, y * 50, 50, 50, null);
						} else if (grid[x][y].equals("ghost4")) {
							g.drawImage(ghostImagesList.get(3), x * 50, y * 50, 50, 50, null);
						} else if (grid[x][y].equals("ghost5")) {
							g.drawImage(ghostImagesList.get(4), x * 50, y * 50, 50, 50, null);

						}
					}
				}

			}
		};
		rasterPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				Integer[] selection = { e.getX(), e.getY() };
				Integer[] selectionConverted = { selection[0] / 50, selection[1] / 50 };
				if (e.getButton() == 3) {
					checkUp(selectionConverted[0], selectionConverted[1]);
					grid[selectionConverted[0]][selectionConverted[1]] = "smallCoin";
					repaint();
				}

				if (e.getButton() == 1) {
					checkUp(selectionConverted[0], selectionConverted[1]);
					if (selectedItem.equals("wall")) {
						checkUp(selectionConverted[0], selectionConverted[1]);
						grid[selectionConverted[0]][selectionConverted[1]] = "wall";
					} else if (selectedItem.equals("bigCoin")) {
						checkUp(selectionConverted[0], selectionConverted[1]);
						grid[selectionConverted[0]][selectionConverted[1]] = "bigCoin";
					} else if (selectedItem.equals("player") && !playerSet) {
						grid[selectionConverted[0]][selectionConverted[1]] = "player";
						playerSet = true;
					} else if (selectedItem.equals("ghost1") && !ghost1Set) {
						grid[selectionConverted[0]][selectionConverted[1]] = "ghost1";
						ghost1Set = true;
					} else if (selectedItem.equals("ghost2") && !ghost2Set) {
						grid[selectionConverted[0]][selectionConverted[1]] = "ghost2";
						ghost2Set = true;
					} else if (selectedItem.equals("ghost3") && !ghost3Set) {
						grid[selectionConverted[0]][selectionConverted[1]] = "ghost3";
						ghost3Set = true;
					} else if (selectedItem.equals("ghost4") && !ghost4Set) {
						grid[selectionConverted[0]][selectionConverted[1]] = "ghost4";
						ghost4Set = true;
					} else if (selectedItem.equals("ghost5") && !ghost5Set) {
						grid[selectionConverted[0]][selectionConverted[1]] = "ghost5";
						ghost5Set = true;
					}

					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		rasterPanel.setPreferredSize(new Dimension(1200, 710));
		centerPanel.add(rasterPanel);
	}

	private void headerBuild() {
		headerPanel.setBackground(Color.BLACK);
		headerPanel.setPreferredSize(new Dimension(1000, 100));
		headerPanel.setLayout(gbLayout);

		c.gridx = 0;
		c.gridy = 0;

		itemPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4104518865850221831L;

			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(66, 32, 33));
				g.fillRect(0, 0, 700, 70);
				g.setColor(new Color(220, 242, 211));
				g.fillRect(330, 5, 350, 60);
				g.setColor(Color.BLUE);
				if (selectedItem.equals("bigCoin")) {
					g.fillRect(65, 5, 60, 60);
				} else if (selectedItem.equals("wall")) {
					g.fillRect(145, 5, 60, 60);
				} else if (selectedItem.equals("player")) {
					g.fillRect(255, 5, 60, 60);
				} else if (selectedItem.equals("ghost1")) {
					g.fillRect(335, 5, 60, 60);
				} else if (selectedItem.equals("ghost2")) {
					g.fillRect(405, 5, 60, 60);
				} else if (selectedItem.equals("ghost3")) {
					g.fillRect(475, 5, 60, 60);
				} else if (selectedItem.equals("ghost4")) {
					g.fillRect(545, 5, 60, 60);
				} else if (selectedItem.equals("ghost5")) {
					g.fillRect(615, 5, 60, 60);
				}

				g.drawImage(coinImage, 70, 10, 50, 50, null);
				g.drawImage(wallIcon, 150, 10, 50, 50, null);
				if (!playerSet) {
					g.drawImage(pacmanIcon, 260, 10, 50, 50, null);
				}
				if (!ghost1Set) {
					g.drawImage(ghost1Icon, 340, 10, 50, 50, null);
				}
				if (!ghost2Set) {
					g.drawImage(ghost2Icon, 410, 10, 50, 50, null);
				}
				if (!ghost3Set) {
					g.drawImage(ghost3Icon, 480, 10, 50, 50, null);
				}
				if (!ghost4Set) {
					g.drawImage(ghost4Icon, 550, 10, 50, 50, null);
				}
				if (!ghost5Set) {
					g.drawImage(ghost5Icon, 620, 10, 50, 50, null);
				}

			}
		};

		itemPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getX() >= 70 && e.getX() < 120 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "bigCoin";
				} else if (e.getX() >= 150 && e.getX() < 200 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "wall";
				} else if (e.getX() >= 260 && e.getX() < 310 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "player";
				} else if (e.getX() >= 340 && e.getX() < 390 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "ghost1";
				} else if (e.getX() >= 410 && e.getX() < 460 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "ghost2";
				} else if (e.getX() >= 480 && e.getX() < 530 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "ghost3";
				} else if (e.getX() >= 550 && e.getX() < 600 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "ghost4";
				} else if (e.getX() >= 620 && e.getX() < 670 && e.getY() >= 10 && e.getY() < 60) {
					selectedItem = "ghost5";
				}

				itemPanel.repaint();
				e.getY();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		itemPanel.setPreferredSize(new Dimension(700, 70));
		headerPanel.add(itemPanel, c);

	}

	private void checkUp(int x, int y) {
		if (grid[x][y].equals("player")) {
			playerSet = false;
		} else if (grid[x][y].equals("ghost1")) {
			ghost1Set = false;
		} else if (grid[x][y].equals("ghost2")) {
			ghost2Set = false;
		} else if (grid[x][y].equals("ghost3")) {
			ghost3Set = false;
		} else if (grid[x][y].equals("ghost4")) {
			ghost4Set = false;
		} else if (grid[x][y].equals("ghost5")) {
			ghost5Set = false;
		}
	}
}
