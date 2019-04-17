import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class Tile{
	public int xPos;
	public int yPos;
	public int type;
	//public int width;
	
	public Tile north;
	public Tile south;
	public Tile east;
	public Tile west;
	
	public Tile(){}
	
	public Subtile[][] subtiles;
	
	public Tile(int x,int y,int t){
		xPos = x;
		yPos = y;
		type = t;
		subtiles = new Subtile[3][3];
	}
	
	public void generate(Random rand, boolean start){
		if(type == -1) { //has not been generated yet
				type = rand.nextInt(3); //0=empty, 1=room, 2=hall (-1 = unitiallized)
				if(type != 0){
					if (north != null)
						this.north.generate(rand,false);
					if (south != null)
						this.south.generate(rand,false);
					if (east != null)
						this.east.generate(rand,false);
					if (west != null)
						this.west.generate(rand,false);
				}
			}
		else if (start) {
			this.north.generate(rand,false);
			this.east.generate(rand,false);
			this.west.generate(rand,false);
			//this.south.generate(rand,false);
		}
	}
	
	public void initializeSubtiles(){
		switch (type) {
			case 1 : //room
				for (int j = 0; j<3; j++)
					for (int i = 0; i<3; i++)
						subtiles[i][j] = new Subtile(this,i,j,true);
					
				break;
					
			case 2 : //hallway
				for (int j = 0; j<3; j++)
					for (int i = 0; i<3; i++)
						subtiles[i][j] = new Subtile(this,i,j,false);
				subtiles[1][1].show = true;
				
				if(this.south != null && this.south.type >0) subtiles[1][2].show = true;
				if(this.north != null && this.north.type >0) subtiles[1][0].show = true;
				if(this.east != null && this.east.type >0) subtiles[2][1].show = true;
				if(this.west != null && this.west.type >0) subtiles[0][1].show = true;
				
				break;
				
			default : // blank/empty
				for (int j = 0; j<3; j++)
					for (int i = 0; i<3; i++)
						this.subtiles[i][j] = new Subtile(this,i,j,false);
		}
			
	}
	
	public void draw(Graphics g, int size, int W, int H){
		//g.setColor(Color.RED);
		//g.drawString(xPos + ", " + yPos,xPos*W/size + 5,yPos*W/size + 5);
		for (int j = 0; j<3; j++)
			for (int i = 0; i<3; i++)
					if (subtiles[i][j] != null)
						subtiles[i][j].drawSub(g, size, W, H);
	}
	
	public int neighbors(){
		int around = 0;
		if(this.south != null && this.south.type >0) around++;
		if(this.north != null && this.north.type >0) around++;
		if(this.east != null && this.east.type >0) around++;
		if(this.west != null && this.west.type >0) around++;
		return around;
	}
}