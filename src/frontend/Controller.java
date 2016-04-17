package frontend;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Controller {

	private JFrame frame;
	private JPanel imagePanel;
	private JLabel background;
	private JPanel[] control;
	private JLabel[] player;
	private JButton[] ready;
	
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
		frame.setSize(1280, 740);
		frame.setVisible(true);
	}
	
	
	private void layOutComponents() {
		frame = new JFrame("Conquer the Universe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		imagePanel = new JPanel();
		frame.add(BorderLayout.CENTER, imagePanel);
		
		background = new JLabel();
		background.setIcon(new ImageIcon(this.getClass().getResource("/resources/universe960x720.jpg")));
		background.setSize(960, 720);
		imagePanel.add(background);
		
		control = new JPanel[2];
		for(int i = 0; i < control.length; i++) {
			control[i] = new JPanel();
			control[i].setLayout(new BorderLayout());
		}
		frame.add(BorderLayout.WEST, control[0]);
		frame.add(BorderLayout.EAST, control[1]);
		player = new JLabel[2];
		for(int i = 0; i < player.length; i++) {
			player[i] = new JLabel();
		}
		player[0].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_fox160x120.jpg")));
		player[1].setIcon(new ImageIcon(this.getClass().getResource("/resources/zootopia_judy160x120.jpg")));
		
		control[0].add(BorderLayout.NORTH, player[0]);
		control[1].add(BorderLayout.NORTH, player[1]);
		
		ready = new JButton[2];
		for (int i = 0; i < ready.length; i++) {
			ready[i] = new JButton("Ready!");
			control[i].add(BorderLayout.SOUTH, ready[i]);
		}
		
	}
	
	private void attachListenersToComponents() {
		
	}
}
