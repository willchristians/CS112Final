import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class MazeGame extends JPanel implements KeyListener{
	
	public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int FPS = 60;
	public static Map map;
	public static Player player;
	public static Random rand = new Random();
	public static JFrame frame = new JFrame("MazeGame");
	
	class Runner implements Runnable{ //from lab8
		
		public void run(){
			while(true) {
				repaint();
				try {
					Thread.sleep(1000/FPS);
				}
				catch(InterruptedException e){}
			}
		}	
	}
	
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
	
	public static void main(String[] args){
		
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainMenu Menu = new MainMenu();
        frame.setContentPane(Menu);
        frame.pack();
        frame.setVisible(true);
		
		//map.print();
		
    }

	public static void startGame(char enterCode){
		int size = 3;
		long seed = rand.nextInt(1000);
		map = new Map(size,seed,WIDTH,HEIGHT);
		player = map.player;
		
		MazeGame mainInstance = new MazeGame();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
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

	public static void levelUp(){
		map = new Map(map.size+1,rand.nextInt(100000),WIDTH,HEIGHT);
		player = map.player;
	}
}