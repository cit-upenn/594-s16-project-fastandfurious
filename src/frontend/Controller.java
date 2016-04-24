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
import java.util.List;
import javax.swing.BorderFactory;
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
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

import UniversePackage.Galaxy;
import UniversePackage.Navigator;
import UniversePackage.Node;
import UniversePackage.Planet;
import UniversePackage.StarCluster;
import UniversePackage.SupplyStation;
import player.HumanPlayer;
import player.Player;


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
	
	private JButton buildPath;
	private JButton capture;
	private JButton travel;
	
	private JScrollPane[] scrollPane;
	private JTable[] table;
	private JTextArea[] messageBoard;

	private void init() {

		galaxy = new Galaxy(940, 720, 50, 70);
		view = new View(galaxy, this);		
		galaxy.addObserver(view);
		galaxy.start();
		
	}
	
	
	private void display() {
		
		layOutComponents();
		attachListeners();
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
		
		control[0].setBackground(Color.BLACK);
		control[1].setBackground(Color.BLACK);
		
		Border border = BorderFactory.createLineBorder(Color.cyan);
		
		control[0].setBorder(border);
		control[1].setBorder(border);
		
		
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
		buildPath = new JButton("Build Path");
		buildPath.setEnabled(false);
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
				
				String player1name = "PLAYER1";
				if(galaxy.getPlayer(0) instanceof HumanPlayer) {
					player1name += " (HUMAN) " ;
				}else {
					player1name += " (CPU) ";
				}
				
				playerName[i].setText(player1name);				
				playerName[i].setForeground(galaxy.getPlayer(0).getPlayerColor());
				playerName[i].setBounds(16, 5, 120, 20);	
				playerName[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 15));	
				
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/rsz_marvel-iron-man-mark-xlvi-sixth-scale-captain-america-civil-war-hot-toys-thumb-902622.jpg")));
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
				
				String player2name = "PLAYER2";
				
				if(galaxy.getPlayer(1) instanceof HumanPlayer) {
					player2name += " (HUMAN) " ;
				}else {
					player2name += " (CPU) ";
				}

				playerName[i].setText(player2name);
				playerName[i].setForeground(galaxy.getPlayer(1).getPlayerColor());
				playerName[i].setBounds(1136, 5, 120, 20);
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/rsz_captain.jpg")));
				player[i].setBounds(1136, 40, 128, 96);
				
				wealth[i].setBounds(1136, 200, 128, 30);
				wealth[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
				wealth[i].setText("Current Wealth: \n");
				
				buildPath.setBounds(1140, 450, 120, 30);
				capture.setBounds(1140, 480, 120, 30);
				travel.setBounds(1140, 510, 120, 30);
				
				scrollPane[i].setBounds(1130, 550, 140, 60);
				messageBoard[i].setBounds(1130, 250, 140, 180);
				ready.setBounds(1136, 620, 128, 35);
				ready.setFont(new Font("Lucida Grande", Font.PLAIN, 18));

				control[i].add(ready);
				control[i].add(buildPath);
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
		
		messageBoard[0].setForeground(Color.GREEN);
		messageBoard[1].setForeground(Color.GREEN);
		
		messageBoard[0].setFont(new Font("Lucida Grande", Font.BOLD, 15));
		messageBoard[1].setFont(new Font("Lucida Grande", Font.BOLD, 15));
		
		messageBoard[0].setText("hello");
		messageBoard[1].setText("hello");
	}
	
	/**
	 * 
	 */
	private void attachListeners() {
		
		view.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent event) {
				Point mousePoint = event.getPoint();		
				Node node = click(mousePoint);
				
				if(galaxy.getHumanPlayer() != null) {
					
					if(!galaxy.getHumanPlayer().inMotion()) {
						galaxy.getHumanPlayer().getSelections().clear();
					}
					galaxy.getHumanPlayer().setSelected(node);			
					if (node != null) {		
						buildPath.setEnabled(true);
						capture.setEnabled(true);
						travel.setEnabled(true);	
					}
				}
			}
		});
		
		ready.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	galaxy.start();
            }
        });
		
		
		buildPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	HumanPlayer hp = (HumanPlayer)galaxy.getHumanPlayer();
            	hp.buildPath();
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
            	
            	Player humanPlayer = galaxy.getHumanPlayer();
            	if(humanPlayer == null) return;
            	Node source = humanPlayer.getCurrentNode();
            	Node dest = humanPlayer.getSelected();
            	if(StarCluster.find(source) != StarCluster.find(dest)) {
            		System.out.println("Target and source not connected");
            		return;
            	}
            	List<Node> targets = Navigator.findSimplePath(source, dest);
            	for(Node target: targets) {
            		humanPlayer.addTarget(target);
            	}
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
				Player human = galaxy.getHumanPlayer();
				if(human != null) {
					human.setFocus(cursor);
				}
			}
		});
		
		view.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				
				Point mousePoint = event.getPoint();	
				Node node = locateNode(mousePoint.getX(), mousePoint.getY());
				Player human = galaxy.getHumanPlayer();
				if(node != null) {
					int size = human.getSelections().size();
					if(size == 0||galaxy.areAdjacentNodes(human.getSelections().getLast(), node)) {
						if(!human.getSelections().contains(node) &&(size == 0||
							!galaxy.hasEdge(human.getSelections().getLast(), node))) {
							human.getSelections().add(node);
						}
						if(size> 2) {
							if(node == human.getSelections().get(size - 2)) {
								human.getSelections().removeLast();
							}
						}
					}
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
	
	/**
	 * provide access to message boards
	 */
	public void updateMessageBoard(int i, String msg) {
		
		this.messageBoard[i].setText(msg);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Controller c = new Controller();
                c.init();
                c.display();
            }
        });        
	}
}
