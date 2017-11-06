package spectatorMode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import elements.BigCoin;
import elements.Coin;
import elements.Enemy;
import elements.Element;
import elements.Player;
import elements.Wall;
import other.Config;

public class Spectator extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3080814768687680669L;
	public ArrayList<Element> elementList;
	public ArrayList<Element> elementListMoving;
	private SpectatorData data;
	GridBagLayout gblayout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	JPanel midPanel = new JPanel();
	Timer timer = new Timer((1000 / 60), this);
	private Player player;

	public Spectator(String ipAdresse) {
		timer.start();
		data = new SpectatorData(ipAdresse);
		Thread thread = new Thread(data);
		thread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSize(1300, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Zuschauer Modus");
		setLocationRelativeTo(getParent());
		getContentPane().setLayout(gblayout);
		getContentPane().setBackground(new Color(20, 40, 76));
		setVisible(true);

		JLabel topLabel = new JLabel("Zuschauen bei: " + ipAdresse);
		topLabel.setForeground(Color.WHITE);
		topLabel.setFont(new Font("Teset", 30, 30));
		c.gridx = 0;
		c.gridy = 0;
		add(topLabel, c);

		midPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4116536887064347680L;

			@Override
			protected void paintComponent(Graphics g) {

				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 1200, 700);

				for (Element o : elementList) {
					if (o instanceof Wall) {
						g.drawImage(new ImageIcon(Config.WallImage).getImage(), o.getxPos(), o.getyPos(), Config.Wall,
								Config.Wall, null);
					}
				}

				for (Element o : elementListMoving) {
					if (o instanceof Player) {
						player = (Player) o;
						topLabel.setText("Zuschauen bei: " + player.getName() + " (" + ipAdresse + ")"
								+ "            Leben: " + player.getLifes() + "      Punkte: " + player.getPoints());

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
						if (o instanceof Coin) {
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

		midPanel.setPreferredSize(new Dimension(1202, 710));
		c.gridy = 1;
		add(midPanel, c);
		repaint();
		midPanel.repaint();
	}

	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == timer) {
			this.elementList = data.getElementList();
			this.elementListMoving = data.getElementListMoving();
			repaint();// this will call at every 1 second
		}
	}
}
