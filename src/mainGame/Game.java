package mainGame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import elements.BigCoin;
import elements.Coin;
import elements.Enemy;
import elements.Moving;
import elements.Element;
import elements.Player;
import elements.Wall;
import other.Config;
import other.LevelReader;
import persitence.SQL;
import persitence.ScoreBoard;
import serverSocket.Server;

public class Game extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1413004896987171707L;
	private JPanel spielRaum;
	private JPanel header;
	
	private Thread thread;
	private Server server;
	
	private GridBagLayout gbLayout = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private Player player;
	
	private String levelPath;
	private String playerName;
	
	private boolean secondTick = false;
	private boolean eatingMode = false;
	private boolean playedIntermission = false;
	private boolean status = true;
	
	private int tickCount = 0;
	private Integer counter = null;
	
	private double lastTime;
	private double fps = 0;
	
	
	private List<Element> elementList = new Vector<>();
	private Vector<Element> tempElementListe = new Vector<>();

	public Game(String name, String levelPath) {
		playerName = name;
		this.levelPath = levelPath;
		this.server = new Server();
		Thread serverThread = new Thread(server);
		serverThread.start();
		newLevel(3, true);
		setTitle("Pacman");
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setVisible(true);
		setLayout(gbLayout);
		getContentPane().setBackground(new Color(40, 80, 100));
		fllen();
	}

	private synchronized void start() {
		if (status)
			return;
		status = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if (!status)
			return;
		status = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void playSound(String yourfile) {
		try {
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			Clip clip;

			stream = AudioSystem.getAudioInputStream(new File(yourfile));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public boolean isCollision(Player spieler, Element o) {

		if (spieler.getxPos() < o.getxPos() + 30 && spieler.getxPos() + 30 > o.getxPos()
				&& spieler.getyPos() < o.getyPos() + 30 && 30 + spieler.getyPos() > o.getyPos()) {
			return true;
		} else {
		}
		return false;
	}

	public boolean picksUp() {
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof BigCoin && player.getxPos() < elementList.get(i).getxPos() + 25
					&& player.getxPos() + 25 > elementList.get(i).getxPos()
					&& player.getyPos() < elementList.get(i).getyPos() + 25
					&& 25 + player.getyPos() > elementList.get(i).getyPos()) {
				elementList.remove(i);
				eatingMode = true;
				tickCount = 0;
				for (Element o : elementList) {
					if (o instanceof Enemy) {
						((Enemy) o).setEatingMode(true);
					}
				}

			}
		}

		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof Coin && player.getxPos() < elementList.get(i).getxPos() + 25
					&& player.getxPos() + 25 > elementList.get(i).getxPos()
					&& player.getyPos() < elementList.get(i).getyPos() + 25
					&& 25 + player.getyPos() > elementList.get(i).getyPos()) {
				elementList.remove(i);
				player.getPoints(player.getPoints() + 10);
				if (!secondTick) {
					playSound("data/sounds/pacman_chomp.wav");
					secondTick = true;
				} else {
					secondTick = false;
				}

				boolean coinExists = false;
				for (Element o : elementList) {
					if (o instanceof Coin) {
						coinExists = true;
					}
				}

				if (coinExists == false) {
					repaint();
					playSound("data/sounds/win.wav");
					newLevel(player.getLifes(), false);
				}
			}
		}

		return true;
	}

	private void newLevel(int leben, boolean neuStart) {
		LevelReader lr = new LevelReader(levelPath, playerName);
		lr.getLevelData();
		elementList = lr.getElementList();
		server.setElementList(elementList);
		int oldPointCount = 0;
		if (player != null) {
			oldPointCount = player.getPoints();
		}
		for (Element o : elementList) {
			if (o instanceof Player) {
				this.player = (Player) o;
				player.getPoints(oldPointCount);
			}
			if (o instanceof Player || o instanceof Enemy) {
				o.setWallCoordinates(lr.getWallCoordinates());
			}
		}

		for (Element o : elementList) {
			if (o instanceof Player) {
				((Player) o).setLifes(leben);
			}

		}
		status = false;
		if (!neuStart) {
			timer(3);
		}
		if (neuStart) {
			start();
		}
	}

	@SuppressWarnings("deprecation")
	public void tick() {
		playSound("data/sounds/pacman_beginning.wav");
		timer(3);
		int fpsTemp = 0;
		double fpsZeit;
		while (true) {

			fpsZeit = 1000000.0 / (lastTime - (lastTime = System.nanoTime()));
			if (fpsTemp == 0) {
				fps = fpsZeit;
			}
			fpsTemp++;
			if (fpsTemp == 10) {
				fpsTemp = 0;
			}
			tickCount++;
			if (tickCount > 350) {
				for (Element o : elementList) {
					if (o instanceof Enemy) {
						((Enemy) o).setEatingMode(false);
					}
					eatingMode = false;
					playedIntermission = false;
					tickCount = 0;

				}
			}
			if (eatingMode == true && playedIntermission == false) {
				playSound("data/sounds/pacman_intermission.wav");
				playedIntermission = true;
			}

			if (player.getLifes() == 0) {
				repaint();
				SQL.send(playerName, player.getPoints(), levelPath);
				@SuppressWarnings("unused")
				ScoreBoard scoreBoard = new ScoreBoard(levelPath);
				thread.stop();

			}

			for (Element o : elementList) {
				if (isCollision(player, o) && o instanceof Enemy && !((Enemy) o).isEatingMode()) {
					playSound("data/sounds/pacman_death.wav");
					player.removeLife();
					player.setxPos(player.getxSpawn());
					player.setyPos(player.getySpawn());
					eatingMode = false;
					if (player.getLifes() > 0) {
						timer(3);
					}
				}

				if (isCollision(player, o) && o instanceof Enemy && ((Enemy) o).isEatingMode()) {
					o.setxPos(((Moving) o).getxSpawn());
					o.setyPos(((Moving) o).getySpawn());
					((Enemy) o).setEatingMode(false);

					player.getPoints(player.getPoints() + 100);
				}
			}

			for (Element o : elementList) {
				if (o instanceof Player || o instanceof Enemy) {
					o.tick();
				}
			}

			picksUp();
			repaint();
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void fllen() {

		c.insets = new Insets(5, 5, 5, 5);

		header = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2423366795938903261L;

			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1200, 100);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", 20, 20));
				g.drawString("Spieler: " + playerName, 10, 40);
				g.drawString("Punkte: " + player.getPoints(), 10, 70);
				g.setColor(Color.YELLOW);
				g.drawString("FPS: " + (int) (fps * -1000), 1120, 20);

				if (player.getLifes() == 3) {
					g.drawImage(new ImageIcon(Config.HeartImage).getImage(), 1100, 50, 40, 40, null);
				}
				if (player.getLifes() >= 2) {
					g.drawImage(new ImageIcon(Config.HeartImage).getImage(), 1050, 50, 40, 40, null);
				}
				if (player.getLifes() >= 1) {
					g.drawImage(new ImageIcon(Config.HeartImage).getImage(), 1000, 50, 40, 40, null);
				}
				g.setColor(Color.RED);
				if (player.getLifes() == 0) {
					g.drawString("GAME OVER", 1000, 50);
					g.drawString("PRESS ENTER FOR RESTART", 450, 50);
				}

				if (!(counter == null) && !(player.getLifes() == 0)) {
					g.setFont(new Font("Arial", 80, 80));
					g.drawString("" + counter, 550, 80);
				}
			}
		};
		header.setPreferredSize(new Dimension(1201, 101));
		c.gridx = 1;
		c.gridy = 0;
		add(header, c);

		spielRaum = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8493300945351638252L;

			@Override
			protected void paintComponent(Graphics g) {

				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1200, 700);

				g.setColor(Color.BLUE);
				
				
				tempElementListe.removeAllElements();
				
				for (Element o : elementList){
					tempElementListe.add(o);
				}
				
				for (Element o : tempElementListe) {
					if (o instanceof Player) {
						if (player.getDirection() == 1) {
							g.drawImage(new ImageIcon(Config.PlayerImage1).getImage(), o.getxPos(), o.getyPos(), 50, 50,
									null);
						}
						if (player.getDirection() == 2) {
							g.drawImage(new ImageIcon(Config.PlayerImage1).getImage(), o.getxPos() + 50, o.getyPos(),
									-50, 50, null);
						}
						if (player.getDirection() == 3) {
							g.drawImage(new ImageIcon(Config.PlayerImage2).getImage(), o.getxPos(), o.getyPos(), 50, 50,
									null);
						}
						if (player.getDirection() == 4) {
							g.drawImage(new ImageIcon(Config.PlayerImage2).getImage(), o.getxPos(), o.getyPos() + 50,
									50, -50, null);
						}
					} else {

						if (o instanceof Wall) {
							g.drawImage(new ImageIcon(Config.WallImage).getImage(), o.getxPos(), o.getyPos(),
									Config.Wall, Config.Wall, null);
						}

						else if (o instanceof Coin) {
							g.drawImage(new ImageIcon(Config.CoinImage).getImage(), o.getxPos(), o.getyPos(),
									Config.Coin, Config.Coin, null);
						} else if (o instanceof BigCoin) {
							g.drawImage(new ImageIcon(Config.BigCoinImage).getImage(), o.getxPos(), o.getyPos(),
									Config.BigCoin, Config.BigCoin, null);
						} else if (o instanceof Enemy) {
							g.drawImage(new ImageIcon(((Enemy) o).getImage()).getImage(), o.getxPos(), o.getyPos(),
									Config.Enemy, Config.Enemy, null);
						}

					}
				}

			}
		};

		KeyListener bewegen = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				player.setWishedMove(0);
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (player.canMove(1)) {
						player.setDirection(1);
					} else {
						player.setWishedMove(1);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (player.canMove(2)) {
						player.setDirection(2);
					} else {
						player.setWishedMove(2);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (player.canMove(3)) {
						player.setDirection(3);
					} else {
						player.setWishedMove(3);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (player.canMove(4)) {
						player.setDirection(4);
					} else {
						player.setWishedMove(4);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER && player.getLifes() < 1) {
					player.getPoints(0);
					newLevel(3, true);
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					int result = JOptionPane.showConfirmDialog(null, "Möchtest du Pacman wirklich beenden?", "ESC",
							JOptionPane.YES_NO_OPTION);

					switch (result) {
					case JOptionPane.YES_OPTION:
						System.exit(0);
					}
				}
			}

		};

		spielRaum.setFocusable(true);
		spielRaum.requestFocus();
		spielRaum.addKeyListener(bewegen);
		spielRaum.setPreferredSize(new Dimension(1201, 701));
		c.gridx = 1;
		c.gridy = 1;
		add(spielRaum, c);
		start();

	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void removeCounter() {
		this.counter = null;
	}

	public void timer(int sekunden) {
		for (int i = sekunden; i > 0; i--) {
			try {
				setCounter(i);
				repaint();
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		removeCounter();
	}

	@Override
	public void run() {
		while (status) {
			tick();
			repaint();
		}
		stop();

	}
}
