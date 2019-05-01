import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Map{
	public Tile[][] grid;
	public int size;
	public long seed;
	private Random rand;
	public int HEIGHT;
	public int WIDTH;
	public Player player;

	public static JFrame frame;
	
	public Map(int size, long seed, int W, int H, JFrame frame){
		this.size = size;
		grid = new Tile[size][size];
		this.seed = seed;
		rand = new Random(seed);
		WIDTH = W;
		HEIGHT = H;
		this.build();
		this.frame = frame;
	}
	
	private void build(){
		for(int i = 0; i<size; i++) //initiallizes all tiles
			for(int j = 0; j<size; j++)
				grid[j][i] = new Tile(j,i,-1, frame); //bla
			
		for(int i = 0; i<size; i++)
			for(int j = 0; j<size; j++){
					Tile t = grid[j][i];
					if (i-1>=0) t.north = grid[j][i-1];
					if (i+1<size) t.south = grid[j][i+1];
					if (j+1<size) t.east = grid[j+1][i];
					if (j-1>=0) t.west = grid[j-1][i];
			}

		Tile start = grid[size/2][size-1];
		start.type = 1;
		start.generate(rand,true);
		for(int i = 0;i<size/3;i++)
			this.pruneHallways();

		
		for(int i = 0; i<size; i++) //initiallizes all subtiles
			for(int j = 0; j<size; j++)
				grid[i][j].initializeSubtiles();
		
		player = new Player(this,size/2,size-1,1,1);
		
		genGoal();
	}
	
	private void genGoal(){
		boolean done = false;
		
		for (int i = 0;i<size; i++)			
			for(int j = 0; j<size; j++)
				if(rand.nextInt(size/4+1) == 0 && grid[i][j].type == 1){
					grid[i][j].subtiles[1][1].makeGoal();
					return;
				}
				
		genGoal();
	}
	
	public void draw(Graphics g, double time){
		for(int i = player.yCoord-2; i<player.yCoord+2 && i<size; i++)
			for(int j = player.xCoord-2; j<player.xCoord+2 && j<size; j++)
				if(i>=0 && j>=0) grid[j][i].draw(g,size,HEIGHT,WIDTH);
		player.initBlindness(g, HEIGHT/size/3);
		player.draw(g, frame);
		player.chaser.draw(g, frame);
		drawInfo(time, g);
	}
	
	private void pruneHallways(){
		for(int i = 0; i<size; i++)
			for(int j = 0; j<size; j++)
				if(grid[i][j].type == 2 && grid[i][j].neighbors()<2) {
					//System.out.println("Pruned: " + i + " " + j);
					grid[i][j].type = 0;
				}
	}
	public void print(){
		for(int i = 0; i<size; i++) {
			System.out.println();
			for(int j = 0; j<size; j++) {
				if (grid[i][j].type == -1)
					System.out.print(" ");
				else
					System.out.print(grid[i][j].type);
			}
		}
		System.out.println();
	}

	public void drawInfo(double t, Graphics g){
		BufferedImage bar = null;
		try {
    		bar = ImageIO.read(new File("ImageBar.png"));
		} 
		catch(IOException e){};
		g.drawImage(bar, 0, HEIGHT - 5, WIDTH, 55, this.frame);
		/*
		g.setColor(Color.ORANGE);
		g.fillRect(0, HEIGHT, WIDTH, 50);
		*/
		Font myFont = new Font("Helvetica", Font.PLAIN, 18);
		g.setFont(myFont);
		int minutes = 0;
		while(t>60){
			minutes++;
			t-=60;
		}
		String sTime = minutes + " minutes and " + (int)t + " seconds elapsed.";
		g.setColor(Color.BLACK);
		g.drawString(sTime, 10, HEIGHT + 30);
	}

}