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
	public Tile[][] grid; //all tiles
	public int size; //side length of tile matrix
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

		/*desc:
		Initializes all the tiles in the map by calling the generate method in the tile class, 
		pruning dead-end hallways, and constructing the player. Instance method because it uses
		 instance variables.*/

		for(int i = 0; i<size; i++) //initiallizes all tiles
			for(int j = 0; j<size; j++)
				grid[j][i] = new Tile(j,i,-1, frame);
			
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
		genLumTiles(0);
	}
	
	private void genGoal(){ //generates goal probabilistically at each tile
		boolean done = false;
		
		for (int i = 0;i<size; i++)			
			for(int j = 0; j<size; j++)
				if(rand.nextInt(size/4+1) == 0 && grid[j][i].type == 1){
					grid[j][i].subtiles[1][1].makeGoal();
					return;
				}
				
		genGoal();
	}

	private void genLumTiles(int count){

		if(count < 1 + (int)(.1*size)){ //count < max number of LS based on size of map
			for(Tile [] tileArray : grid)
				for(Tile t : tileArray)
					for(Subtile[] subArray : t.subtiles)
						for(Subtile sub : subArray) //series of for-each loops calls each subtile
							if(sub.show && rand.nextInt(size*size*9) == 1){ //if at hallway & low-probability
								sub.makeLS();
								genLumTiles(count+1);
								return;
							}
		}
		
	}
	
	public void draw(Graphics g){
		player.initBlindness(g, HEIGHT/size/3);//blindness informs later drawing decisions
		if(player.isAtLS()){ //look at entire map if we're at an LS
			for(int i = 0; i<size; i++)
				for(int j = 0; j<size; j++)
					if(i>=0 && j>=0) grid[j][i].draw(g,size,HEIGHT,WIDTH);
		}
		else{ //if not at LS, for speed, only look at some of map
			for(int i = player.yCoord-2; i<player.yCoord+2 && i<size; i++)
				for(int j = player.xCoord-2; j<player.xCoord+2 && j<size; j++)
					if(i>=0 && j>=0) grid[j][i].draw(g,size,HEIGHT,WIDTH);
		}
		player.draw(g, frame);
		player.chaser.draw(g, frame);
		drawInfo(g);
	}
	
	private void pruneHallways(){ //prunes dead-end hallways
		for(int i = 0; i<size; i++)
			for(int j = 0; j<size; j++)
				if(grid[i][j].type == 2 && grid[i][j].neighbors()<2) {
					//System.out.println("Pruned: " + i + " " + j);
					grid[i][j].type = 0;
				}
	}

	public void print(){ //very helpful in debugging. Prints the map.
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

	private void drawInfo(Graphics g){ //draws our info bar
		double t = MazeGame.time;

		BufferedImage bar = null;
		try {
    		bar = ImageIO.read(new File("ImageBar.png"));
		} 
		catch(IOException e){};

		g.drawImage(bar, 0, HEIGHT - 5, WIDTH, 55, this.frame);

		Font myFont = new Font("Helvetica", Font.PLAIN, 18);
		g.setFont(myFont);
		int minutes = (int)(t/60); //calculates time
		t = t%60;
		String ifZero = "";
		if (t<10)
			ifZero = "0";
		String sTime = "Time: " + minutes + ":" + ifZero + (int)t;
		g.setColor(Color.BLACK);
		g.drawString(sTime, 10, HEIGHT + 30);
		g.drawString("Level: " + (size - 2), 350, HEIGHT + 30);
		g.drawString("Score: " + MazeGame.score, 650, HEIGHT + 30);
	}

}