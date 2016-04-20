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
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

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
	private JLabel[] wealth;
	private JButton ready;
	private JLabel readyLabel;
	private JComboBox<String> selectList;
	
	private JButton buildEdge;
	private JButton capture;
	private JButton travel;
	
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
		
		/*
		for (int i = 1; i < galaxy.getStarBoard().length - 1; i++) {
			for (int j = 1; j < galaxy.getStarBoard()[0].length; j++) {
				if (galaxy.getStarBoard()[i][j] instanceof Planet) {
					((Planet)galaxy.getStarBoard()[i][j]).addObserver(((HumanPlayer) galaxy.getPlayer(0)));
				} else if (galaxy.getStarBoard()[i][j] instanceof SupplyStation) {
					((SupplyStation)galaxy.getStarBoard()[i][j]).addObserver(((HumanPlayer) galaxy.getPlayer(0)));
				}			
			}
		}
		*/
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
		playerName = new JLabel[2];
		wealth = new JLabel[2];
		table = new JTable[2];
		scrollPane = new JScrollPane[2];
		messageBoard = new JTextArea[2];		
		selectList = new JComboBox<String>();
		ready = new JButton();
		readyLabel = new JLabel();
		
		// a bunch of buttons for human players 
		buildEdge = new JButton("Build Edge");
		buildEdge.setEnabled(false);
		capture = new JButton("Capture");
		capture.setEnabled(false);
		travel = new JButton("Travel");
		travel.setEnabled(false);
		
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
			table[i] = new JTable(data, columnNames);
			scrollPane[i] = new JScrollPane(table[i]);
			messageBoard[i] = new JTextArea();
			messageBoard[i].setBackground(Color.BLACK);
			wealth[i] = new JLabel();
			if (i == 0) {
				playerName[i].setText("Player1");
				playerName[i].setForeground(Color.GREEN);
				playerName[i].setBounds(16, 5, 120, 20);	
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_fox128x96.jpg")));
				player[i].setBounds(16, 40, 128, 96);
				
				wealth[i].setBounds(16, 200, 128, 30);
				wealth[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
				wealth[i].setText("Current Wealth: \n");
				
				// selectList only for player1 (computer)
				selectList.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
				selectList.setBounds(10, 150, 140, 30);
				
				scrollPane[i].setBounds(10, 550, 140, 60);
				messageBoard[i].setBounds(10, 250, 140, 180);
				
				readyLabel.setBounds(16, 620, 128, 35);
				control[i].add(selectList);
				control[i].add(readyLabel);
				
			} else {
				playerName[i].setText("Player2");
				playerName[i].setForeground(Color.ORANGE);
				playerName[i].setBounds(1136, 5, 120, 20);
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_judy128x96.jpg")));
				player[i].setBounds(1136, 40, 128, 96);
				
				wealth[i].setBounds(1136, 200, 128, 30);
				wealth[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
				wealth[i].setText("Current Wealth: \n");
				
				buildEdge.setBounds(1140, 450, 120, 30);
				capture.setBounds(1140, 480, 120, 30);
				travel.setBounds(1140, 510, 120, 30);
				
				scrollPane[i].setBounds(1130, 550, 140, 60);
				messageBoard[i].setBounds(1130, 250, 140, 180);
				ready.setBounds(1136, 620, 128, 35);
				ready.setFont(new Font("Lucida Grande", Font.PLAIN, 18));

				control[i].add(ready);
				control[i].add(buildEdge);
				control[i].add(capture);
				control[i].add(travel);
			}
			playerName[i].setHorizontalAlignment(SwingConstants.CENTER);
			playerName[i].setFont(new Font("Lucida Grande", Font.BOLD, 20));
						
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
			control[i].add(wealth[i]);
			control[i].add(scrollPane[i]);
			control[i].add(messageBoard[i]);
			
		}	
	}
	
	/**
	 * 
	 */
	private void attachListenersToComponents() {
		
		// add mouse click listener to view
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				Point mousePoint = event.getPoint();		
				Node node = click(mousePoint);	
				if (node != null) {
					buildEdge.setEnabled(true);
					capture.setEnabled(true);
					travel.setEnabled(true);
				}
			}
		});
		
		ready.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	galaxy.start();
            }
        });
		
		
		buildEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	Node current = galaxy.getPlayer(0).getCurrentNode();
//            	galaxy.buildEdge(current, targetNode);
            }
        });
		
		capture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
            }
        });
		
		travel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
            }
        });
		
		// add mouse motion listener to view
		// this is used to help user keeping track
		// of the current location of the cursor
		view.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent event) {
				Point p = event.getPoint();
				Node cursor = locateNode(p.getX(), p.getY());
				if(cursor == null) {
					
				}
				else {
					
				}
			}
		});
	}
	
	/**
	 * locate node based on coordinates
	 * @param mousePoint
	 * @return pointer to node if adjacent, null otherwise
	 */

	public Node click(Point mousePoint) {
		
		double x = mousePoint.getX();
		double y = mousePoint.getY();
		
		return locateNode(x, y);
	}
	
	/**
	 * locate node based on coordinates
	 * @param x horizontal coordinates of node
	 * @param y vertical...
	 * @return pointer to node if adjacent, null otherwise
	 */
	private Node locateNode(double x, double y) {
		
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
