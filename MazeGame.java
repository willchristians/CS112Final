import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Scanner;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class MazeGame extends JPanel implements KeyListener{
	
	public static final int WIDTH = 800;
    public static final int HEIGHT = 850;
    public static final int FPS = 60;
	public static Map map; //map we load. Updated at start and in levelup
	public static Player player; //me
	public static Random rand = new Random();
	public static JFrame frame = new JFrame("DUNGEON CRAWL");
	public static double time; //either elapsed or remaining, in seconds
	public static double timechange; //used in run in runner. change in time per frame
	public static int score;
	private static int chaserspeed = 35; //number of frames b/t chaser's moves, changes upon levelup
	private static Scanner keyboard = new Scanner(System.in);
	
	class Runner implements Runnable{ //from lab8
		
		public void run(){ //repeatedly called to update graphics
			while(true) {
				repaint();
				try {
					Thread.sleep(1000/FPS);
					time += timechange;
					if((int)(time*FPS)%chaserspeed==0){
						player.chaser.update(); //chaser moved here
						score += 5;
					}
					if (time<0){ //game ends here
						MainMenu Menu = new MainMenu();
						Menu.currentScreen = 'o';
						frame.setContentPane(Menu);	
						frame.pack();					
						Thread.sleep(3000);
						frame.setVisible(false);
						
						MazeGame.addToLeaderboard(score);
					}
						
				}
				catch(InterruptedException e){}
			}
		}	
	}
	
	/* Below three methods:
	All methods used when implementing keyListener so the program can read input and move the player sprite. 
	keyTyped is the method that actually moves the player sprite. */

	public void keyPressed(KeyEvent e) {
        char c=e.getKeyChar();
		player.move(c);
    }
	
	public void keyReleased(KeyEvent e) {
        char c=e.getKeyChar();
		//System.out.println("\tYou let go of: " + c);
	
    }

    public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		//System.out.println("You typed: " + c);
    }
	
	public void addNotify() {
        super.addNotify();
        requestFocus();
    }
	
	public static void main(String[] args){ //displays main menu and implements graphics
		
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainMenu Menu = new MainMenu();
        frame.setContentPane(Menu);
        frame.pack();
        frame.setVisible(true);
		
		//map.print();
		
    }

	public static void startGame(char enterCode){ //starts based on type of game '2' timed '3' endless. Called statically
		switch (enterCode) {
			case '2' :
				time = 120;
				timechange = -1.0/FPS;
				break;
				
			case '3' :
				time = 0;
				timechange = 1.0/FPS;
				break;	
		}
			
		int size = 3;
		long seed = rand.nextInt(1000);
		map = new Map(size,seed,WIDTH,HEIGHT-50, frame);
		player = map.player;
		
		MazeGame mainInstance = new MazeGame();
        frame.setContentPane(mainInstance);
        frame.pack();
	}
	
	public void paintComponent(Graphics g) { //sets background; paints map
        g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		map.draw(g);
	}

	public MazeGame(){
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		Thread mainThread = new Thread(new Runner());
		mainThread.start();
    }

	public static void levelUp(){ //makes bigger map at new level
		score += (map.size)*(map.size)*100;
		map = new Map(map.size+1,rand.nextInt(100000),WIDTH,HEIGHT-50, frame);
		player = map.player;
		chaserspeed -= 1;
	}
	
	private static void addToLeaderboard(int points){ //called at end of game; what it sounds like
		try {
			Scanner inFile = new Scanner(new File("leaderboard.txt"));
			
			TableList board = new TableList(); //leaderboard stored here
		
			while (inFile.hasNextLine()){ //reads from file to tablelist
				String name = inFile.nextLine();
				int score = inFile.nextInt();
				if (inFile.hasNextLine())
					inFile.nextLine();
				board.add(name,score);
			}
			
			System.out.println("You scored " + points + " points!");
			System.out.println("Add score to leaderboard? (y/n)");
			char answer = keyboard.nextLine().charAt(0);
			if (answer == 'y'){
				System.out.print("Enter name: ");
				String name = keyboard.nextLine();
				board.add(name,points);
				board.sort();
			}
			System.out.println("Thanks for playing!");
			
			PrintWriter outFile = new PrintWriter(new File("leaderboard.txt"));
			for(int i = 0; i<board.size(); i++){
				Pair p = board.get(i);
				outFile.println(p.x);
				outFile.println(p.y);
			}
			outFile.close();
			
		} catch (FileNotFoundException E) {
			System.out.println("oopsies");
		}
		System.exit(1);
	}
}