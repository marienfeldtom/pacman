package mainGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import editor.LevelEditor;
import spectatorMode.Spectator;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3671326612015414153L;
	private GridBagLayout loginGridBagLayout = new GridBagLayout();
	private GridBagConstraints loginConstrains = new GridBagConstraints();
	private Font fontDefault = new Font("Arial", 25, 25);
	private JLabel background = new JLabel(new ImageIcon("data/login.jpg"));
	private JLabel playerNameLabel = new JLabel("Player:   ");
	private JLabel ipLabel = new JLabel("IP:");
	private JLabel warning = new JLabel(" ");
	private JPanel panelFront = new JPanel();
	private JPanel panelBorderStyle = new JPanel();
	private JTextField nameTextField = new JTextField();
	private JTextField ipTextField = new JTextField();
	private JButton startGameButton = new JButton("Start");
	private JButton startLevelEditorButton = new JButton("Level-Editor");
	private JButton startSpectatorModeButton = new JButton("Zuschauen");
	private JButton refreshButton = new JButton();
	private JComboBox<String> levelSelectionBox = new JComboBox<>();
	private JLabel levelSelectionLabel = new JLabel("Level: ");
	private ArrayList<String> levelNames;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Login login = new Login();
	}

	public Login() {
		getLevelNames();

		frameMain();
		setFrameElements();
		repaint();
		setVisible(true);
	}

	private void getLevelNames() {
		File f = new File("data/level");
		File[] fileArray = f.listFiles();
		levelNames = new ArrayList<>();
		for (int i = 0; i < fileArray.length; i++) {
			levelNames.add(fileArray[i].getName().split(".txt")[0]);
		}
		for (String name : levelNames) {
			levelSelectionBox.addItem(name);
		}

	}

	private void setFrameElements() {

		KeyListener exitKeyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
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

		ActionListener startSpectatorAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				Spectator spectator = new Spectator(ipTextField.getText());
			}
		};

		levelSelectionLabel.setFont(fontDefault);
		levelSelectionLabel.setForeground(Color.WHITE);
		loginConstrains.gridx = 0;
		loginConstrains.gridy = 0;
		panelFront.add(levelSelectionLabel, loginConstrains);

		loginConstrains.insets = new Insets(5, 5, 5, 5);
		levelSelectionBox.setPreferredSize(new Dimension(200, 30));
		levelSelectionBox.setFont(fontDefault);
		loginConstrains.gridx = 1;
		loginConstrains.gridy = 0;
		panelFront.add(levelSelectionBox, loginConstrains);

		refreshButton.setPreferredSize(new Dimension(30, 30));
		refreshButton.setIcon(new ImageIcon("data/refresh.png"));
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				levelSelectionBox.removeAllItems();
				getLevelNames();
			}
		});
		loginConstrains.gridx = 2;
		loginConstrains.gridy = 0;
		panelFront.add(refreshButton, loginConstrains);

		playerNameLabel.setForeground(Color.WHITE);
		playerNameLabel.setFont(fontDefault);
		loginConstrains.gridx = 0;
		loginConstrains.gridy = 1;
		panelFront.add(playerNameLabel, loginConstrains);

		ipLabel.setFont(fontDefault);
		ipLabel.setForeground(Color.WHITE);
		loginConstrains.gridx = 0;
		loginConstrains.gridy = 5;
		panelFront.add(ipLabel, loginConstrains);

		nameTextField.setFont(fontDefault);
		nameTextField.setPreferredSize(new Dimension(200, 30));
		nameTextField.addKeyListener(exitKeyListener);
		loginConstrains.gridy = 1;
		loginConstrains.gridx = 1;
		panelFront.add(nameTextField, loginConstrains);

		startGameButton.setFont(fontDefault);
		startGameButton.setPreferredSize(new Dimension(200, 30));
		loginConstrains.gridy = 2;
		panelFront.add(startGameButton, loginConstrains);

		startLevelEditorButton.setFont(fontDefault);
		startLevelEditorButton.setPreferredSize(new Dimension(200, 30));
		loginConstrains.gridy = 4;
		panelFront.add(startLevelEditorButton, loginConstrains);

		startLevelEditorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				LevelEditor leveleditor = new LevelEditor(levelSelectionBox.getSelectedItem().toString(), levelNames);
			}
		});

		ActionListener startGameAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!nameTextField.getText().equals("")) {
					@SuppressWarnings("unused")
					Game game = new Game(nameTextField.getText(), levelSelectionBox.getSelectedItem().toString());
					dispose();
				} else {
					warning.setText("Bitte eingeben");
				}
			}
		};

		nameTextField.addActionListener(startGameAction);
		startGameButton.addActionListener(startGameAction);
		startGameButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startGameButton.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				startGameButton.setForeground(Color.GREEN);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		startLevelEditorButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startLevelEditorButton.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				startLevelEditorButton.setForeground(Color.GREEN);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		warning.setFont(new Font("Arial", 15, 15));
		warning.setForeground(Color.RED);
		loginConstrains.gridy = 3;
		panelFront.add(warning, loginConstrains);
		nameTextField.setFocusable(true);
		nameTextField.requestFocus();

		ipTextField.setFont(fontDefault);
		ipTextField.addActionListener(startSpectatorAction);
		ipTextField.setPreferredSize(new Dimension(200, 30));
		loginConstrains.gridy = 5;
		panelFront.add(ipTextField, loginConstrains);

		startSpectatorModeButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startSpectatorModeButton.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				startSpectatorModeButton.setForeground(Color.GREEN);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		startSpectatorModeButton.addActionListener(startSpectatorAction);
		startSpectatorModeButton.setFont(fontDefault);
		startSpectatorModeButton.setPreferredSize(new Dimension(200, 30));
		loginConstrains.gridy = 6;
		panelFront.add(startSpectatorModeButton, loginConstrains);
	}

	private void frameMain() {
		setTitle("PACMAN - LOGIN");
		setSize(new Dimension(500, 400));
		setResizable(false);
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		getContentPane().add(background);

		panelBorderStyle.setPreferredSize(new Dimension(420, 320));
		panelBorderStyle.setBackground(new Color(40, 40, 200));
		panelBorderStyle.setLayout(loginGridBagLayout);
		panelBorderStyle.add(panelFront);

		panelFront.setBorder(BorderFactory.createEtchedBorder());
		panelFront.setBackground(Color.BLACK);
		panelFront.setPreferredSize(new Dimension(400, 300));
		panelFront.setLayout(loginGridBagLayout);

		background.add(panelBorderStyle);
		background.setLayout(loginGridBagLayout);
	}
}
