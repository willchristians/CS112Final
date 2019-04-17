import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class Subtile extends Tile {
	
	public boolean show;
	public Tile parent;
	public boolean isGoal = false;

	//public Subtile north;
	/*
	public Tile south;
	public Tile east;
	public Tile west;
	*/

	public Color brickLight = new Color(100, 110, 110);
	public Color brickDark = new Color(90, 65, 62);
	public Color moss = new Color(19, 143, 91);
	public Color luminate = new Color(255, 218, 35, 150);
	public Color hall1 = new Color(160,82,45);
	public Color hall2 = new Color(139,69,19);

	private int countTorch;

	Random rand = new Random();
	Random rand2 = new Random();

	public Subtile(Tile t, int x, int y,boolean b){
		parent = t;
		xPos = x;
		yPos = y;
		show = b;
		//north = t.north;
	}
	
public void drawSub(Graphics g, int size, int W, int H){
		
		int pwidth = H/size;
		int width = pwidth/3 + 1; //adjusts for empty pixels
		int xPix = parent.xPos*pwidth + xPos*width;
		int yPix = parent.yPos*pwidth + yPos*width;
		int lumwidth;
		rand.setSeed((int)(xPix*yPix));
		if (!show){ //graphics would happen here
			g.setColor(brickDark);
			g.fillRect(xPix,yPix,width,width);
			g.setColor(brickLight);
			g.fillRect(xPix+1, yPix+1, width-2, width-2);
			g.setColor(moss);
			int xcoord; int ycoord; int widthOval;
			for(int i = 0; i<20; i++){
				widthOval = rand.nextInt(width/8);
				xcoord = xPix + rand.nextInt(width); ycoord = yPix + rand.nextInt(width);
				g.fillOval(xcoord, ycoord, widthOval, widthOval);
			}
		} 
		else if (isGoal){
			g.setColor(Color.YELLOW);
			g.fillRect(xPix,yPix,width,width);
		}
		else{
			int whereX = xPix;
			int whereY = yPix;
			for(int i = 0; i<5; i++){
				whereX = xPix;
				for(int j = 0; j<5; j++){
					g = changeColor(g);
					g.fillRect(whereX, whereY, width/5 + 4, width/5 + 4); // adjusts so no black lines
					whereX += width/5;
				}
				whereY += width/5;
			}
			/*
			if(this.isWallTile() && rand.nextInt(6) == 1){
				drawTorches(rand, rand2, xPix, yPix, pwidth/8 + rand2.nextInt(5), g);
			}
			*/
			//^this will work much better once the neighbors function has been worked out
		}
	}
	
	public void makeGoal(){
		this.isGoal = true;
	}

	public void drawTorches(Random rand, Random rand2, int xPix, int yPix, int lumwidth, Graphics g){
		g.setColor(Color.WHITE);
		g.fillOval(xPix + (int)(lumwidth*.25), yPix + (int)(lumwidth*.25), lumwidth/2, lumwidth/2);
		g.setColor(Color.BLACK);
		g.fillOval(xPix + (int)(lumwidth*.35), yPix + (int)(lumwidth*35), lumwidth/8, lumwidth/8);
		g.setColor(luminate);
		g.fillOval(xPix, yPix, lumwidth, lumwidth);
	}

	public Graphics changeColor(Graphics g){
		if(g.getColor() == hall1) g.setColor(hall2);
		else g.setColor(hall1);
		return g;
	}
/*
	public boolean isWallTile(){
		int count = 0;
		if(this.south.show)count ++;
		if(this.north.show)count++;
		if(this.west.show)count++;
		if(this.east.show)count++;
		if(count<4) return true;
	}
	*/
}

