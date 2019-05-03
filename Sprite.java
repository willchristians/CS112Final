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
	public int xCoord; //tile coordinates w/in matrix
	public int yCoord;
	public int xSubCoord; //subtile coordinates w/in matrix
	public int ySubCoord;
	public Map m;

	public Color dim = new Color(10, 10, 10, 135);
	public Color blind = new Color(10, 10, 10);

	public abstract void draw(Graphics g, JFrame frame); //makes sure each sprite has a draw method
	
	public void moveSprite(char c){ //takes user input to move sprite
		
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
				if (newY<m.size && newY>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) { //can we move there?
					//System.out.println("Moved forward");
					//if we can move there, move there
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
				if (newX<m.size && newX>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) { //can move there?
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
				if (newY<m.size && newY>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) { //can move there?
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
				if (newX<m.size && newX>= 0 && m.grid[newX][newY].subtiles[newXSub][newYSub].show) { //can move there?
					xCoord = newX;
					xSubCoord = newXSub;
				}	
				break;
		}
	}
	
}

class Player extends Sprite{
	
	public Chaser chaser; //this our chasey boy
	
	public Player(Map m, int x, int y, int xSub, int ySub){
		xCoord = x;
		yCoord = y;
		ySubCoord = ySub;
		xSubCoord = xSub;
		this.m = m;
		this.chaser = new Chaser(m, x, y, xSub, ySub);
	}
	
	public void draw(Graphics g, JFrame frame){
		BufferedImage me = null;
		try {
    		me = ImageIO.read(new File("Sprite.png")); //dis me
		} 
		catch (IOException e){};

		int bigw = m.HEIGHT/m.size; //tile width
		int lilw = bigw/3; //subtile width
		int xPix = xCoord*bigw + xSubCoord*lilw; //where we are
		int yPix = yCoord*bigw + ySubCoord*lilw;

		g.drawImage(me, xPix, yPix, lilw, lilw, frame);//draws me
		drawBlindness(g, lilw); //draws my blindness
	}
	
	public void initBlindness(Graphics g, int width){

		int widthConst = m.grid.length; // helps for sizing

		int locX; int locY; double distFrom; // will calculate for each tile

		int subCount = 0;

		int myLocX = m.grid[xCoord][0].xPos*(m.WIDTH/widthConst) + m.grid[xCoord][yCoord].subtiles[xSubCoord][0].xPos*width; // my location as coordinates
		int myLocY = m.grid[xCoord][yCoord].yPos*(m.WIDTH/widthConst) + m.grid[xCoord][yCoord].subtiles[xSubCoord][ySubCoord].yPos*width; //my location as coordinates

		boolean atChaser = chaser.getChaser() == m.grid[xCoord][yCoord].subtiles[xSubCoord][ySubCoord]; //am I at the max?

		for(Tile [] tileArray : m.grid){
			for(Tile t : tileArray){
				for(Subtile[] subArray : t.subtiles){
					for(Subtile sub : subArray){ //this series of for-each loops accesses every subtile

						locX = t.xPos*(m.WIDTH/widthConst) + sub.xPos*width; //each subtile's location
						locY = t.yPos*(m.HEIGHT/widthConst) + sub.yPos*width;
						distFrom = Math.sqrt((locX - myLocX)*(locX - myLocX) + (locY - myLocY)*(locY - myLocY)); //gets distance from every tile to me
						
						if(this.isAtLS()) sub.blindness = 0; //if at LS everything bright
						else if(!atChaser && distFrom < width*2){//calculates levels of blindness for everything
							if(sub.show) sub.blindness = 0;
							else sub.blindness = 1;
						}
						else if(!atChaser && distFrom < width*2.5){
							if(!sub.show && distFrom > width*2);
							else sub.blindness = 1;
						}
						else sub.blindness = 2;
					}
				}
			}
		}

	}

	private void drawBlindness(Graphics g, int width){

		int widthConst = m.grid.length; // helps for sizing

		int locX; int locY; int bigX; int bigY;

		for(Tile [] tileArray : m.grid){
			for(Tile t : tileArray){
				for(Subtile[] subArray : t.subtiles){
					for(Subtile sub : subArray){ //this series of for-each loops accesses every subtile

						locX = t.xPos*(m.WIDTH/widthConst) + sub.xPos*width;
						locY = t.yPos*(m.HEIGHT/widthConst) + sub.yPos*width;

						if(sub.blindness == 1) g.setColor(dim);
						else if(sub.blindness == 2) g.setColor(blind);
						//^At first I didn't draw any of the blind rectangles, but the processing delay made the graphics look wonky

						if(sub.blindness !=0 ) g.fillRect(locX, locY, width + 2, width + 2);
					}
				}
			}	
		}

	}

	public boolean isAtLS(){ //true if at a light subtile
		return m.grid[xCoord][yCoord].subtiles[xSubCoord][ySubCoord].isLS;
	}
	
	public void move(char c){ 
		if(!(c == 'w' || c == 'a' || c == 's' || c == 'd')) //do nothing if not one of these
			return;
		
		chaser.moveList.add(c); //adds movement to move list
		moveSprite(c);//move sprite based on char
		chaser.doMove = true;
		
		if (m.grid[xCoord][yCoord].subtiles[xSubCoord][ySubCoord].isGoal) //if at goal level up
			MazeGame.levelUp();

		if (chaser.xCoord == xCoord && chaser.yCoord == yCoord && chaser.xSubCoord == xSubCoord && chaser.ySubCoord == ySubCoord)
			MazeGame.score -= 200*m.size;		//if at chaser reduce score
	}

}

class Chaser extends Sprite{
	
	public Queue<Character> moveList = new Queue<Character>();/*
	^A list of moves in order, so that the chaser can exactly replicate the player’s movement. 
	Each time the player moves, their movement is added to the queue.*/
	
	public boolean doMove = false; //false until the player moves, then true
	
	public Chaser(Map m, int x, int y, int xSub, int ySub){
		xCoord = x;
		yCoord = y;
		ySubCoord = ySub+1;
		xSubCoord = xSub;
		this.m = m;
		moveList.add('w');
	}
	
	public void draw(Graphics g, JFrame frame){

		BufferedImage me = null;
		try {
    		me = ImageIO.read(new File("Chaser.png"));
		} 
		catch (IOException e){};


		int bigw = m.HEIGHT/m.size; //tile width
		int lilw = bigw/3;//subtile width
		int xPix = xCoord*bigw + xSubCoord*lilw;//where I'm at
		int yPix = yCoord*bigw + ySubCoord*lilw;
		//System.out.println(bigw + ", " + lilw);
		//System.out.println(xPix + ", " + yPix);
		g.drawImage(me, xPix, yPix, lilw, lilw, frame);//draw me
	}
	
	public void update(){

		/*desc:
		Called every so often by the run class in Runner. 
		Basically calls moveSprite with the character that is first in the queue. 
		If the chaser doesn’t actually move (if it hits a wall), then it tries to move again*/
		
		try {
			int ySubStart = ySubCoord;
			int xSubStart = xSubCoord;
			while(doMove && ySubStart == ySubCoord && xSubStart == xSubCoord){
				char c = moveList.pop();
				moveSprite(c);
			}
			
		} catch (NullPointerException e) {}
		
		Player p = m.player;
		if (p.xCoord == xCoord && p.yCoord == yCoord && p.xSubCoord == xSubCoord && p.ySubCoord == ySubCoord)//if at chaser, remove from score
			MazeGame.score -= 200*m.size;
	}

	public Subtile getChaser(){return m.grid[xCoord][yCoord].subtiles[xSubCoord][ySubCoord];}//returns the subtile the chaser is on
}