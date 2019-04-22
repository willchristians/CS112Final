import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public abstract class Sprite{
	public int xCoord;
	public int yCoord;
	public int xSubCoord;
	public int ySubCoord;
	public Map m;

	public abstract void draw(Graphics g, JFrame frame);
	
}

class Player extends Sprite{
	
	public Player(Map m, int x, int y, int xSub, int ySub){
		xCoord = x;
		yCoord = y;
		ySubCoord = ySub;
		xSubCoord = xSub;
		this.m = m;
	}
	
	public void draw(Graphics g, JFrame frame){
		BufferedImage me = null;
		try {
    		me = ImageIO.read(new File("Sprite.png"));
		} 
		catch (IOException e){};

		int bigw = m.HEIGHT/m.size;
		int lilw = bigw/3;
		int xPix = xCoord*bigw + xSubCoord*lilw;
		int yPix = yCoord*bigw + ySubCoord*lilw;
		//g.setColor(Color.WHITE);
		//System.out.println(bigw + ", " + lilw);
		//System.out.println(xPix + ", " + yPix);
		//g.fillOval(xPix,yPix,lilw,lilw);
		g.drawImage(me, xPix, yPix, lilw, lilw, frame);
		drawBlindness(g,2);
	}
	
	private void drawBlindness(Graphics g, int n){
		int bigw = m.HEIGHT/m.size;
		int lilw = bigw/3;
		int xPix = xCoord*bigw + xSubCoord*lilw; 
		int yPix = yCoord*bigw + ySubCoord*lilw;
		g.setColor(Color.GRAY);
		g.fillRect(0,0,m.WIDTH,yPix-n*bigw); //topfog
		g.fillRect(0,0,xPix-n*bigw,m.HEIGHT); //leftfog
		g.fillRect(0,yPix+n*bigw+lilw,m.WIDTH,m.HEIGHT);
		g.fillRect(xPix+n*bigw+lilw,0,m.WIDTH,m.HEIGHT);
	}
	
	public void move(char c){
		int newYSub = ySubCoord;
		int newXSub = xSubCoord;
		int newY = yCoord;
		int newX = xCoord;
		switch (c) {
			
			case 'w' :
				newYSub = ySubCoord - 1;
				//System.out.println("Tried to move forward");
				if (newYSub<0) {
					newYSub = 2;
					newY -= 1;
				}
				if (newY<m.size && newY>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) {
					//System.out.println("Moved forward");
					yCoord = newY;
					ySubCoord = newYSub;
				}	
				break;
					
			case 'a' :
				newXSub = xSubCoord - 1;
				if (newXSub<0) {
					newXSub = 2;
					newX -= 1;
				}
				if (newX<m.size && newX>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) {
					xCoord = newX;
					xSubCoord = newXSub;
				}	
				break;
				
			case 's' :
				newYSub = ySubCoord + 1;
				if (newYSub>2) {
					newYSub = 0;
					newY += 1;
				}
				if (newY<m.size && newY>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) {
					yCoord = newY;
					ySubCoord = newYSub;
				}	
				break;
				
			case 'd' :
				newXSub = xSubCoord + 1;
				if (newXSub>2) {
					newXSub = 0;
					newX += 1;
				}
				if (newX<m.size && newX>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) {
					xCoord = newX;
					xSubCoord = newXSub;
				}	
				break;
		}
		
		if (m.grid[xCoord][yCoord].subtiles[xSubCoord][ySubCoord].isGoal)
			MazeGame.levelUp();
			
	}

}