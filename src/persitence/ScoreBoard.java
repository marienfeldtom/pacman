package persitence;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ScoreBoard extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -247631310285681728L;
	private JLabel testLabel = new JLabel("Bestenliste");
	private GridBagLayout gblayout = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	private JTable table;
	private String[][] list;
	String[] columnNames = { "Name", "Punkte" };

	public ScoreBoard(String levelPath) {
		this.list = SQL.receiveAll(levelPath);
		setTitle("Bestenliste");
		setSize(1100, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setUndecorated(true);
		setVisible(true);
		setLayout(gblayout);

		table = new JTable(list, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		getContents();

	}

	private void getContents() {
		KeyListener escape = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		};
		getContentPane().setBackground(Color.GREEN);
		testLabel.setFont(new Font("Schriftart", 50, 50));
		testLabel.setSize(100, 300);
		c.gridx = 1;
		c.gridy = 1;
		add(testLabel, c);
		c.gridy = 2;
		table.getTableHeader().setFont(new Font("Liste", 30, 30));
		table.setRowHeight(30);
		table.setFont(new Font("Liste", 25, 25));
		table.addKeyListener(escape);
		add(new JScrollPane(table), c);
		pack();
		setVisible(true);

		this.addKeyListener(escape);
	}
}
