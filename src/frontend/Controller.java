package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import javax.swing.Timer;
import javax.swing.table.TableColumn;

import UniversePackage.Galaxy;
import UniversePackage.Node;
import UniversePackage.Planet;
import UniversePackage.SupplyStation;
import player.HumanPlayer;

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

		galaxy = new Galaxy(940, 720, 50, 70);
		view = new View(galaxy);		
		galaxy.addObserver(view);
		
		for (int i = 1; i < galaxy.getStarBoard().length - 1; i++) {
			for (int j = 1; j < galaxy.getStarBoard()[0].length; j++) {
				if (galaxy.getStarBoard()[i][j] instanceof Planet) {
					((Planet)galaxy.getStarBoard()[i][j]).addObserver(((HumanPlayer) galaxy.getPlayer(0)));
				} else if (galaxy.getStarBoard()[i][j] instanceof SupplyStation) {
					((SupplyStation)galaxy.getStarBoard()[i][j]).addObserver(((HumanPlayer) galaxy.getPlayer(0)));
				}			
			}
		}
		galaxy.start();
		
		Timer t = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				view.nextStep();
			}
		});
		t.start();
		
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
				ready[i].setBounds(16, 600, 128, 40);
						
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
				ready[i].setBounds(1136, 600, 128, 40);
				
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
		
		// add mouse listener for each node in the universe
		view.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				Point mousePoint = event.getPoint();		
				click(mousePoint);		
			}
		});
	}
	
	public void click(Point mousePoint) {
		
		Node current = galaxy.getPlayer(0).getCurrentNode();
		Node targetNode = locatedNode(mousePoint.getX(), mousePoint.getY());
		if (targetNode != null && galaxy.areAdjacentNodes(current, targetNode)) {			
			targetNode.click();
			galaxy.buildEdge(current, targetNode);
			galaxy.getPlayer(0).setCurrentNode(targetNode);					
		}
	}
	
	/**
	 * locate node based on coordinates
	 * @param x horizontal coordinates of node
	 * @param y vertical...
	 * @return pointer to node if adjacent, null otherwise
	 */
	private Node locatedNode(double x, double y) {
		
		int col = (int)(x/galaxy.getGridLength());
		double remainder1 = x % galaxy.getGridLength();	
		if(remainder1 >= 35) col++;
		else if(remainder1 > 15) return null;
		int row = (int)(y/galaxy.getGridLength());
		double remainder2 = y % galaxy.getGridLength();
		if(remainder2 >= 35) row++;
		else if(remainder2 > 15) return null;
		return galaxy.getStarBoard()[row][col];
	}
}
