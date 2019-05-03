import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Scanner;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
//used https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
//used https://docs.oracle.com/javase/tutorial/2d/images/drawimage.html
//https://www.baeldung.com/java-printstream-printf
//https://stackoverflow.com/questions/509076/how-do-i-address-unchecked-cast-warnings
//https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
//https://docs.oracle.com/javase/7/docs/api/java/io/PrintWriter.html
//http://home.wlu.edu/~lambertk/hsjava/whatsnew/input.html
//https://stackoverflow.com/questions/2168963/use-java-drawstring-to-achieve-the-following-text-alignment

public class MainMenu extends JPanel implements KeyListener{
	public static final int WIDTH = 800;
    public static final int HEIGHT = 850;
    public Color fontColor = new Color (153, 255, 0);
	public char currentScreen = 'm'; //m main menu; l leaderboard; h how to play; o game over
	
	public static void main(String[] args){
		/*
		JFrame frame = new JFrame("MazeGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainMenu Menu = new MainMenu();
        frame.setContentPane(Menu);
        frame.pack();
        frame.setVisible(true);
		*/
	}
	
		
	public MainMenu(){
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

    }
	
	public void paintComponent(Graphics g) {
		BufferedImage img = null;
		switch(currentScreen){
			case 'm' :
				try {
					img = ImageIO.read(new File("title.jpg"));
				} 
				catch (IOException e){};
				g.drawImage(img, 0, 0, this);
				break;
			
			case 'h' : //how to play
				try {
					img = ImageIO.read(new File("HowToPlay.jpg")); //change this to howtoplay.jpg
				} 
				catch (IOException e){};
				g.drawImage(img, 0, 0, this);
				break;
			
			case 'l' : //leaderboard
				try {
					img = ImageIO.read(new File("Leaderboard.jpg")); //change this to leaderboard.jpg
				} 
				catch (IOException e){};
				g.drawImage(img, 0, 0, this);
				printLeaderboard(g);
				break;
				
			case 'o' : //game over
				try {
				img = ImageIO.read(new File("gameover.jpg"));				
				} catch (IOException e){};
				g.drawImage(img, 0, 0, this);
				break;
		}
	}
	
	private void printLeaderboard(Graphics g){ //print a file on the screen
		
		try {
			Scanner inFile = new Scanner(new File("leaderboard.txt"));
			
			TableList board = new TableList();//this is the leaderboard
		
			while (inFile.hasNextLine()){
				String name = inFile.nextLine();
				int score = inFile.nextInt();
				if (inFile.hasNextLine())
					inFile.nextLine();
				board.add(name,score); //add to leaderboard
			}
			
			Font myFont = new Font("Impact", Font.PLAIN, 25);  //our aesthetic is impact

			g.setFont(myFont);
			g.setColor(fontColor);

			FontMetrics fontMetrics = g.getFontMetrics();
		
			for(int i = 0; i<board.size(); i++){
				int y = 200 + 35*i;
				Pair p = board.get(i);
				
				String s1 = String.format("%5d. %-16s",i+1,p.x); //name
				String s2 = "" + p.y; //score
				g.drawString(s1, 180, y); //draw s1 at 180
				g.drawString(s2, WIDTH - 200 - fontMetrics.stringWidth(s2), y); //draw s2 at right = width-200
			}
			inFile.close();
		} catch (FileNotFoundException E) {
			System.out.println("oopsies");
		}
		
	}
	
	public void keyPressed(KeyEvent e) { //interact with menu here
        char c=e.getKeyChar();
		if (currentScreen == 'm') {
			switch(c) {
				case '1' :
					//System.out.println("viewing how to play");
					currentScreen = 'h';
					repaint();
					break;
				case '2' :
					//System.out.println("Starting 2 minute game");
					MazeGame.startGame(c);
					break;
				case '3' :
					//System.out.println("Starting endless game");
					MazeGame.startGame(c);
					break;
				case '4' :
					//System.out.println("Viewing leaderboards");
					currentScreen = 'l';
					repaint();
					break;
				default :
					break;
			}
		} else if (currentScreen == 'h' || currentScreen == 'l'){ //at leaderboard or howtoplay
			if (c == 'b') { //go back
				currentScreen = 'm';
				repaint();
			}
		}
    }
    
    /*below: Used to read input from the user, calls startGame with the enter code (1,2,3,etc.). 
    keyPressed is the one that actually does something.*/
	
	public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
	
	public void addNotify() {
        super.addNotify();
        requestFocus();
    }
}

