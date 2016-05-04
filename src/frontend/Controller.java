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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import UniversePackage.Galaxy;
import UniversePackage.Navigator;
import UniversePackage.Node;
import UniversePackage.StarCluster;
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
	private JTextArea[] messageBoard;
	
	private JButton Build;
	private JButton Attack;
	private JButton Move;
	
	private HashMap<Integer, String> playerMap;
	private HashMap<Integer, String> playerNames;
	private HashMap<Integer, Integer> playerIcon;
	private MyActionListener myAL = new MyActionListener();
	private JComboBox<String> type1;
	private JComboBox<String> type2;

	private String[] imageNames = { "iron-man", "captain", "zootopia_fox", "zootopia_judy"};
	
	/**
	 * Login to start the game.
	 */
	protected void login() {
		
		playerMap = new HashMap<>();
		playerNames = new HashMap<>();
		playerIcon = new HashMap<>();
		
		JFrame loginFrame = new JFrame("Login");
		loginFrame.setPreferredSize(new Dimension(800, 600));
		loginFrame.setLayout(null);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton startGame = new JButton("Start");
		startGame.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		startGame.setForeground(Color.BLUE);
		startGame.setBounds(300, 400, 200, 80);
		loginFrame.add(BorderLayout.CENTER, startGame);

		SingleSelectJSplitPane splitPane = new SingleSelectJSplitPane();
		JSplitPane p = splitPane.getSplitPane();
		p.setBounds(60, 150, 300, 150);
		loginFrame.getContentPane().add(p);
		
		SingleSelectJSplitPane splitPane2 = new SingleSelectJSplitPane();
		JSplitPane p2 = splitPane2.getSplitPane();
		p2.setBounds(440, 150, 300, 150);
		loginFrame.getContentPane().add(p2);
		
		JLabel player1 = new JLabel("Player1");
		JLabel selectPlayer1 = new JLabel("Please select player1:");
		player1.setFont(new Font("SansSerif", Font.BOLD, 22));
		player1.setBounds(160, 10, 100, 30);
		selectPlayer1.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		selectPlayer1.setBounds(60, 50, 200, 30);
		
		JLabel player2 = new JLabel("Player2");
		JLabel selectPlayer2 = new JLabel("Please select player2:");
		player2.setFont(new Font("SansSerif", Font.BOLD, 22));
		player2.setBounds(540, 10, 100, 30);
		selectPlayer2.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		selectPlayer2.setBounds(440, 50, 200, 30);
		
		JLabel l1 = new JLabel("Player Type:");
		JLabel l2 = new JLabel("Player Type:");
		l1.setBounds(60, 85, 100, 20);
		l2.setBounds(440, 85, 100, 20);
		l1.setFont(new Font("SansSerif", Font.PLAIN, 16));
		l2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		loginFrame.add(l1);
		loginFrame.add(l2);
		
		type1 = new JComboBox<String>();
		type2 = new JComboBox<String>();
		ArrayList<String> type = new ArrayList<>();
		type.add(" ");
		type.add("Computer");
		type.add("Human");
		for (String s : type) {
			type1.addItem(s);
			type2.addItem(s);
		}
		
		type1.setFont(new Font("SansSerif", Font.PLAIN, 16));
		type1.setBounds(160, 85, 150, 20);
		loginFrame.add(type1);
		
		type2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		type1.setBounds(160, 85, 150, 20);
		type2.setBounds(540, 85, 150, 20);
		loginFrame.add(type1);
		loginFrame.add(type2);
		
		type1.addActionListener(myAL); 
		type2.addActionListener(myAL);
		
		JLabel lbl1 = new JLabel("Player1's name: ");
		JLabel lbl2 = new JLabel("Player2's name: ");
		lbl1.setBounds(60, 320, 140, 40);
		lbl2.setBounds(440, 320, 140, 40);
		lbl1.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lbl2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		loginFrame.add(lbl1);
		loginFrame.add(lbl2);
		
		JTextField text1 = new JTextField(20);
		JTextField text2 = new JTextField(20);
		text1.setBounds(190, 320, 150, 40);
		text2.setBounds(570, 320, 150, 40);
		loginFrame.add(text1);
		loginFrame.add(text2);
		
		loginFrame.getContentPane().add(player1);
		loginFrame.getContentPane().add(player2);
		loginFrame.getContentPane().add(selectPlayer1);
		loginFrame.getContentPane().add(selectPlayer2);
		
		loginFrame.pack();
		loginFrame.setVisible(true);

		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = text1.getText();
				playerNames.put(0, s);
				s = text2.getText();
				playerNames.put(1, s);		
				playerIcon.put(0, splitPane.getSelectedIndex());
				playerIcon.put(1, splitPane2.getSelectedIndex());
				loginFrame.dispose();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						init();
						display();
					}
				});   		
			}
		});
	}
	
	private void init() {

		galaxy = new Galaxy(940, 720, 50, 70, 3600);	
		galaxy.build(playerNames.get(0), Color.yellow, playerMap.get(0), playerNames.get(1), Color.cyan, playerMap.get(1));
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
		
		frame.getContentPane().add(control[0]);
		frame.getContentPane().add(control[1]);
		
		player = new JLabel[2];
		playerName = new JLabel[2];
		wealth = new JLabel[2];
		messageBoard = new JTextArea[2];		
		
		// a bunch of buttons for human players 
		Build = new JButton("Build");
		Build.setEnabled(false);
		Attack = new JButton("Attack");
		Attack.setEnabled(false);
		Move = new JButton("Move");
		Move.setEnabled(false);
		
		for(int i = 0; i < player.length; i++) {
			
			player[i] = new JLabel();	
			playerName[i] = new JLabel();
			messageBoard[i] = new JTextArea();
			messageBoard[i].setBackground(Color.BLACK);
			wealth[i] = new JLabel();
			if (i == 0) {
				
				String player1name = playerNames.get(i);
				
				playerName[i].setText(player1name);				
				playerName[i].setForeground(galaxy.getPlayer(0).getPlayerColor());
				playerName[i].setBounds(16, 5, 120, 20);	
				playerName[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 14));	
				
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/" + imageNames[playerIcon.get(i)] + ".jpg")));
				player[i].setBounds(16, 40, 128, 96);
				
				wealth[i].setBounds(16, 200, 128, 30);
				wealth[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
				wealth[i].setText("Current Wealth: \n");
				
				messageBoard[i].setBounds(10, 250, 140, 180);
				
			} else {
				
				String player2name = playerNames.get(i);
				
				playerName[i].setText(player2name);
				playerName[i].setForeground(galaxy.getPlayer(1).getPlayerColor());
				playerName[i].setBounds(1136, 5, 120, 20);
				
				player[i].setIcon(new ImageIcon(this.getClass().getResource("/resources/" + imageNames[playerIcon.get(i)] + ".jpg")));
				player[i].setBounds(1136, 40, 128, 96);
				
				wealth[i].setBounds(1136, 200, 128, 30);
				wealth[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
				wealth[i].setText("Current Wealth: \n");
				
				Build.setBounds(1140, 450, 120, 30);
				Attack.setBounds(1140, 480, 120, 30);
				Move.setBounds(1140, 510, 120, 30);
				
				messageBoard[i].setBounds(1130, 250, 140, 180);

				control[i].add(Build);
				control[i].add(Attack);
				control[i].add(Move);
			}
			playerName[i].setHorizontalAlignment(SwingConstants.CENTER);
			playerName[i].setFont(new Font("Lucida Grande", Font.BOLD, 20));
						
			control[i].add(playerName[i]);
			control[i].add(player[i]);					
			control[i].add(wealth[i]);
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
					if(!galaxy.getHumanPlayer().inMotion()) { galaxy.getHumanPlayer().getSelections().clear(); }
					galaxy.getHumanPlayer().setSelected(node);			
					if (node != null) {		
						Build.setEnabled(true);
						Attack.setEnabled(true);
						Move.setEnabled(true);	
					}
				}
			}
		});
		
		
		Build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	HumanPlayer hp = (HumanPlayer)galaxy.getHumanPlayer();
            	hp.buildPath();
            }
        });
		
		Attack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Player human = galaxy.getHumanPlayer();
                if(human != null) {
                	Node target = human.getSelected();
                	Node cur = human.getCurrentNode();
                	if(galaxy.areAdjacentNodes(cur, target)&&target.getRuler()!=human&&target.getRuler()!=null){
                		human.getDestinations().add(target);
                		human.getSelections().add(cur);
                		human.getSelections().add(target);
                	}
                }
            }
        });
		
		Move.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {     	
            	Player humanPlayer = galaxy.getHumanPlayer();
            	if(humanPlayer == null) return;
            	Node source = humanPlayer.getCurrentNode();
            	Node dest = humanPlayer.getSelected();
            	if(StarCluster.find(source) != StarCluster.find(dest)) {
            		System.err.println("Unreachable");
            	}
            	else{
                	List<Node> path = Navigator.buildDijkstraPath(source, dest, galaxy);
                	if(path == null) return;	
                	else humanPlayer.getDestinations().addAll(path);
            	}
            }
        });
		
		// add mouse motion listener to view
		// this is used to help user keeping track
		// of the current location of the cursor
		view.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent event) {
				Player human = galaxy.getHumanPlayer();
				if(human != null) { human.setFocus(click(event.getPoint()));}
			}
		});
		
		// drag to select a sequence of nodes
		view.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				
				Player human = galaxy.getHumanPlayer();
				if(human == null) return;
				Node node = click(event.getPoint());
				if(node != null&&(node.getRuler() == null||node.getRuler()==human)) {
					int size = human.getSelections().size();
					if(size == 0||galaxy.areAdjacentNodes(human.getSelections().getLast(), node)) {
						if(!human.getSelections().contains(node) &&(size == 0||
							!galaxy.hasEdge(human.getSelections().getLast(), node))) {
							human.getSelections().add(node);
						}
						if(size> 2) {
							if(node == human.getSelections().get(size - 2)) { human.getSelections().removeLast();}
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
		if(remainder1 >= 30) col++;
		else if(remainder1 > 10) return null;
		int row = (int)(y/galaxy.getGridLength());
		double remainder2 = y % galaxy.getGridLength();
		if(remainder2 >= 30) row++;
		else if(remainder2 > 10) return null;
		return galaxy.getStarBoard()[row][col];
	}
	
	/**
	 * provide access to message boards
	 */
	public void updateMessageBoard(int i, String msg) {
		
		this.messageBoard[i].setText(msg);
	}
	
	
	/**
	 * A class for setting up the JSplitPane on the login screen.
	 *
	 */
	@SuppressWarnings("serial")
	class SingleSelectJSplitPane extends JPanel implements ListSelectionListener {
		
		private JLabel picture;
		private JList<String> list;
		private JSplitPane splitPane;
		

		public SingleSelectJSplitPane() {
			//Create the list of images and put it in a scroll pane.
			list = new JList<String>(imageNames);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setSelectedIndex(0);
			list.addListSelectionListener(this);

			JScrollPane listScrollPane = new JScrollPane(list);
			picture = new JLabel();
			picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
			picture.setHorizontalAlignment(JLabel.CENTER);

			JScrollPane pictureScrollPane = new JScrollPane(picture);

			//Create a split pane with the two scroll panes in it.
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					listScrollPane, pictureScrollPane);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(100);

			//Provide minimum sizes for the two components in the split pane.
			Dimension minimumSize = new Dimension(100, 50);
			listScrollPane.setMinimumSize(minimumSize);
			pictureScrollPane.setMinimumSize(minimumSize);

			//Provide a preferred size for the split pane.
			splitPane.setPreferredSize(new Dimension(400, 200));
			updateLabel(imageNames[list.getSelectedIndex()]);
		}


		//Renders the selected image
		protected void updateLabel (String name) {
			ImageIcon icon = createImageIcon("/resources/" + name + ".jpg");
			picture.setIcon(icon);
			if  (icon != null) {
				picture.setText(null);
			} else {
				picture.setText("Image not found");
			}
		}

		/** Returns an ImageIcon, or null if the path was invalid. */
		protected ImageIcon createImageIcon(String path) {
			URL imgURL = this.getClass().getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			 JList list = (JList)e.getSource();
		     updateLabel(imageNames[list.getSelectedIndex()]);	
		}

		public JList<String> getImageList() {
	        return list;
	    }
	 
	    public JSplitPane getSplitPane() {
	        return splitPane;
	    }

	    public int getSelectedIndex() {
	    	return list.getSelectedIndex();
	    }
	}
	
	/**
	 * 
	 *
	 */
	class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == type1) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String selected = (String)cb.getSelectedItem();
		        if (selected.equals("Human")) { 	        	
		        	type2.removeItem("Human");
		        }else{
		        	if(type2.getItemCount() == 2) {
		        		type2.addItem("Human");
		        	}
		        }
		        playerMap.put(0, selected);	
		        
			} else if (e.getSource() == type2) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String selected = (String) cb.getSelectedItem();
				if (selected.equals("Human")) {
		        	type1.removeItem("Human");
				}else {
		        	if(type1.getItemCount() == 2) {
		        		type1.addItem("Human");
		        	}
				}
				playerMap.put(1, selected);
			}
		}	
	}
	
}
