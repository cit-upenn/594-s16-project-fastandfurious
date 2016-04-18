package frontend;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

public class Controller {

	private JFrame frame;
	private JPanel imagePanel;
	private JLabel background;
	private JPanel[] control;
	private JLabel[] player;
	private JLabel[] playerName;
	private JButton[] ready;
	private JComboBox<String> selectList;
	private JScrollPane[] scrollPane;
	private JTable[] table;
	
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
		
	}
	
	
	private void display() {
		layOutComponents();
		attachListenersToComponents();
		frame.setBounds(10, 10, 1280, 740);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	
	private void layOutComponents() {
		frame = new JFrame("Conquer the Universe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		imagePanel = new JPanel();
		imagePanel.setBounds(160, 0, 960, 740);
		frame.add(imagePanel);
		
		background = new JLabel();
		background.setIcon(new ImageIcon(this.getClass().getResource("/resources/universe960x720.jpg")));
		imagePanel.add(background);
		
		control = new JPanel[2];
		for(int i = 0; i < control.length; i++) {
			control[i] = new JPanel();
			control[i].setLayout(null);			
			control[i].setVisible(true);
		}
		control[0].setBounds(0, 0, 160, 740);
		frame.add(control[0]);
		control[1].setBounds(1120, 0, 160, 740);
		frame.add(control[1]);
		
		player = new JLabel[2];
		ready = new JButton[2];
		playerName = new JLabel[2];
		table = new JTable[2];
		scrollPane = new JScrollPane[2];
		selectList = new JComboBox<String>();
		
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
			if (i == 0) {
				playerName[i].setText("Player1");
				playerName[i].setBounds(16, 5, 120, 20);	
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_fox128x96.jpg")));
				player[i].setBounds(16, 30, 128, 96);
				ready[i].setBounds(16, 650, 128, 50);
						
				selectList.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
				selectList.setBounds(10, 140, 140, 30);
				control[i].add(selectList);
				
				scrollPane[i].setBounds(10, 400, 140, 150);
				
			} else {
				playerName[i].setText("Player2");
				playerName[i].setBounds(1136, 5, 120, 20);
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_judy128x96.jpg")));
				player[i].setBounds(1136, 30, 128, 96);
				ready[i].setBounds(1136, 650, 128, 50);
				
				scrollPane[i].setBounds(1130, 400, 140, 150);
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
			control[i].add(scrollPane[i]);
			
		}	
	
	}
	
	private void attachListenersToComponents() {
		
	}
}
