import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Dimension;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Tile{
	public int xPos;
	public int yPos;
	public int type;
	//public int width;
	
	public Tile north;
	public Tile south;
	public Tile east;
	public Tile west;

	public static JFrame frame; //added this
	
	public Tile(){}
	
	public Subtile[][] subtiles;
	
	public Tile(int x,int y,int t, JFrame frame){
		xPos = x;
		yPos = y;
		type = t;
		this.frame = frame;
		subtiles = new Subtile[3][3];
	}
	
	public void generate(Random rand, boolean start){
		if(type == -1) { //has not been generated yet
				int roll = rand.nextInt(20);
				if (roll < 6) type = 0; //empty 30%
				else if (roll < 10) type = 1; //room 20%
				else type = 2; //hall 50%
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
						subtiles[i][j] = new Subtile(this,i,j,true, frame);
					
				break;
					
			case 2 : //hallway
				for (int j = 0; j<3; j++)
					for (int i = 0; i<3; i++)
						subtiles[i][j] = new Subtile(this,i,j,false, frame);
				subtiles[1][1].show = true;
				
				if(this.south != null && this.south.type >0) subtiles[1][2].show = true;
				if(this.north != null && this.north.type >0) subtiles[1][0].show = true;
				if(this.east != null && this.east.type >0) subtiles[2][1].show = true;
				if(this.west != null && this.west.type >0) subtiles[0][1].show = true;
				
				break;
				
			default : // blank/empty
				for (int j = 0; j<3; j++)
					for (int i = 0; i<3; i++)
						this.subtiles[i][j] = new Subtile(this,i,j,false, frame);
		}
			
	}
	
	public void draw(Graphics g, int size, int W, int H){
		//g.setColor(Color.RED);
		//g.drawString(xPos + ", " + yPos,xPos*W/size + 5,yPos*W/size + 5);

		BufferedImage brick = null;
		try {
    		brick = ImageIO.read(new File("brick.jpg"));
		} 
		catch (IOException e){};
		
		BufferedImage c1 = null;
		try {
    		c1 = ImageIO.read(new File("Carpet1.jpg"));
		} 
		catch (IOException e){};
	
		BufferedImage c2 = null;
		try {
    		c2 = ImageIO.read(new File("Carpet2.jpg"));
		} 
		catch (IOException e){};

		for (int j = 0; j<3; j++)
			for (int i = 0; i<3; i++)
					if (subtiles[i][j] != null)
						subtiles[i][j].drawSub(g, size, W, H, brick, c1, c2, frame);
	}
	
	public int neighbors(){
		int around = 0;
		if(this.south != null && this.south.type >0) around++;
		if(this.north != null && this.north.type >0) around++;
		if(this.east != null && this.east.type >0) around++;
		if(this.west != null && this.west.type >0) around++;
		return around;
	}
	/*
		public BufferedImage changeCarpet(BufferedImage carpet, BufferedImage c1, BufferedImage c2){
		if(carpet == c1) carpet = c2;
		else carpet = c1;
		return carpet;
	}
	*/
}