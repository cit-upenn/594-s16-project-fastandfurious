package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import UniversePackage.Galaxy;

public class Controller {

	private View view;
	private Galaxy galaxy;
	
	private JFrame frame;
	private JPanel[] control;
	private JLabel[] player;
	private JLabel[] playerName;
	private JButton[] ready;
	private JComboBox<String>[] selectList;
	private JScrollPane[] scrollPane;
	private JTable[] table;
	private JTextArea[] messageBoard;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Controller c = new Controller();
                c.init();
                c.display();
            }
        });        
	}

	private void init() {

		galaxy = new Galaxy(940, 720, 90, 30);
		view = new View(galaxy);		
		galaxy.addObserver(view);
	}
	
	
	private void display() {
		
		layOutComponents();
		attachListenersToComponents();
			
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);				
	}
	
	
	@SuppressWarnings("unchecked")
	private void layOutComponents() {
		frame = new JFrame("Conquer the Universe");
		frame.setPreferredSize(new Dimension(1280, 740));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		view.setBounds(160, 0, 960, 740);
		frame.add(view);

			
		control = new JPanel[2];
		for(int i = 0; i < control.length; i++) {
			control[i] = new JPanel();
			control[i].setBackground(Color.LIGHT_GRAY);
			control[i].setLayout(null);			
			control[i].setVisible(true);
		}
		control[0].setBounds(0, 0, 160, 740);
		control[1].setBounds(1120, 0, 160, 740);
		frame.getContentPane().add(control[0]);
		frame.getContentPane().add(control[1]);
		
		player = new JLabel[2];
		ready = new JButton[2];
		playerName = new JLabel[2];
		table = new JTable[2];
		scrollPane = new JScrollPane[2];
		selectList = new JComboBox[2];
		messageBoard = new JTextArea[2];
		
		// setup a table to display the statistics
		String[] columnNames = {"Name", "Status"};
		Object[][] data = {
			    {"GiantPlanet", new Integer(5)},
			    {"HugePlanet", new Integer(3)},
			    {"MediumPlanet", new Integer(2)},
			    {"SmallPlanet", new Integer(20)},
			    {"Star", new Integer(10)}
			};
			
		for(int i = 0; i < player.length; i++) {
			player[i] = new JLabel();	
			playerName[i] = new JLabel();
			ready[i] = new JButton("Ready!");
			table[i] = new JTable(data, columnNames);
			scrollPane[i] = new JScrollPane(table[i]);
			messageBoard[i] = new JTextArea();
			messageBoard[i].setBackground(Color.BLACK);;
			selectList[i] = new JComboBox<String>();
			if (i == 0) {
				playerName[i].setText("Player1");
				playerName[i].setForeground(Color.GREEN);
				playerName[i].setBounds(16, 5, 120, 20);	
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_fox128x96.jpg")));
				player[i].setBounds(16, 40, 128, 96);
				ready[i].setBounds(16, 650, 128, 50);
						
				selectList[i].setFont(new Font("Lucida Grande", Font.PLAIN, 18));
				selectList[i].setBounds(10, 150, 140, 30);
				
				scrollPane[i].setBounds(10, 400, 140, 150);
				messageBoard[i].setBounds(10, 250, 140, 120);
				
			} else {
				playerName[i].setText("Player2");
				playerName[i].setForeground(Color.ORANGE);
				playerName[i].setBounds(1136, 5, 120, 20);
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_judy128x96.jpg")));
				player[i].setBounds(1136, 40, 128, 96);
				ready[i].setBounds(1136, 650, 128, 50);
				
				selectList[i].setFont(new Font("Lucida Grande", Font.PLAIN, 18));
				selectList[i].setBounds(1130, 150, 140, 30);
				
				scrollPane[i].setBounds(1130, 400, 140, 150);
				messageBoard[i].setBounds(1130, 250, 140, 120);
			}
			playerName[i].setHorizontalAlignment(SwingConstants.CENTER);
			playerName[i].setFont(new Font("Lucida Grande", Font.BOLD, 20));
			ready[i].setFont(new Font("Lucida Grande", Font.PLAIN, 18));
			
			TableColumn column = null;
			for (int j = 0; j < 2; j++) {
			    column = table[i].getColumnModel().getColumn(j);
			    if (j == 0) {
			        column.setPreferredWidth(100); //third column is bigger
			    } else {
			        column.setPreferredWidth(40);
			    }
			}		
			control[i].add(playerName[i]);
			control[i].add(player[i]);					
			control[i].add(ready[i]);
			control[i].add(selectList[i]);
			control[i].add(scrollPane[i]);
			control[i].add(messageBoard[i]);
			
		}	
	}
	
	private void attachListenersToComponents() {
		
	}
}
