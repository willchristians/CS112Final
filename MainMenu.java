import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
//used https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
//used https://docs.oracle.com/javase/tutorial/2d/images/drawimage.html

public class MainMenu extends JPanel implements KeyListener{
	public static final int WIDTH = 800;
    public static final int HEIGHT = 850;
	
	public static void main(String[] args){
		JFrame frame = new JFrame("MazeGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainMenu Menu = new MainMenu();
        frame.setContentPane(Menu);
        frame.pack();
        frame.setVisible(true);
	}
	
		
	public MainMenu(){
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		//Thread mainThread = new Thread(new Runner());
		//mainThread.start();
    }
	
	public void paintComponent(Graphics g) {
	BufferedImage img = null;
	try {
    	img = ImageIO.read(new File("title.jpg"));
	} 
	catch (IOException e){};
	g.drawImage(img, 0, 0, this);


		/*
        g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		String[] options = new String[] {"[1] 1-minute mode","[2] 3-minute mode","[3] endless mode","[4] leaderboard"};
		int buttonWidth = WIDTH*3/4;
		int centerX = WIDTH/2;
		
		for(int i = 0; i<options.length; i++){
			int y = HEIGHT/4 + i*HEIGHT/8;
			g.setColor(Color.WHITE);
			g.fillRect(centerX-buttonWidth/2,y,buttonWidth,HEIGHT/10);
			g.setColor(Color.BLACK);
			g.drawString(options[i],centerX-(buttonWidth/8),y+HEIGHT/20);
		}
		*/
	}
	
	public void keyPressed(KeyEvent e) {
        char c=e.getKeyChar();
		switch(c) {
			case '1' :
				System.out.println("Starting 1 minute game (NOT IMPLEMENTED)");
				break;
			case '2' :
				System.out.println("Starting 3 minute game (NOT IMPLEMENTED)");
				break;
			case '3' :
				System.out.println("Starting endless game");
				MazeGame.startGame(c);
				break;
			case '4' :
				System.out.println("Viewing leaderboards (NOT IMPLEMENTED)");
				break;
			default :
				break;
		}
    }
	
	public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
	
	public void addNotify() {
        super.addNotify();
        requestFocus();
    }
	
	
	
	
}
