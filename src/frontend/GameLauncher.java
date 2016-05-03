package frontend;

import javax.swing.SwingUtilities;

public class GameLauncher {

public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {		
			public void run() {		
				Controller c = new Controller();
				c.login();
			}
		});      
	}	
}
