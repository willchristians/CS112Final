import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class Map{
	public Tile[][] grid;
	public int size;
	public long seed;
	private Random rand;
	public int HEIGHT;
	public int WIDTH;
	public Player player;
	
	public Map(int size, long seed, int W, int H){
		this.size = size;
		grid = new Tile[size][size];
		this.seed = seed;
		rand = new Random(seed);
		WIDTH = W;
		HEIGHT = H;
		this.build();
	}
	
	private void build(){
		for(int i = 0; i<size; i++) //initiallizes all tiles
			for(int j = 0; j<size; j++)
				grid[j][i] = new Tile(j,i,-1);
			
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
				if(rand.nextInt(size/2) == 0 && grid[j][i].type == 1){
					grid[j][i].subtiles[1][1].makeGoal();
					return;
				}
				
		genGoal();
	}
	
	public void draw(Graphics g){
		for(int i = 0; i<size; i++)
			for(int j = 0; j<size; j++)
				grid[j][i].draw(g,size,HEIGHT,WIDTH);
			
		player.draw(g);
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
}