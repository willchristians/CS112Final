import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Subtile extends Tile {
	
	public boolean show;
	public Tile parent;
	public boolean isGoal = false;

	public static JFrame frame;

	public int blindness = 0; //0 for all bright, 1 for dim, 2 for black
	public boolean isLS = false; //is Light Subtile; lights up the whole maze


	public Color brickLight = new Color(100, 110, 110);
	public Color brickDark = new Color(90, 65, 62);
	public Color moss = new Color(19, 143, 91, 100);
	public Color luminate = new Color(255, 218, 35, 150);
	public Color hall1 = new Color(160,82,45);
	public Color hall2 = new Color(139,69,19);

	private int countTorch;

	Random rand = new Random();
	Random rand2 = new Random();
	public Subtile(Tile t, int x, int y,boolean b, JFrame frame){
		parent = t;
		xPos = x;
		yPos = y;
		show = b;
		this.frame = frame;
	}

	
public void drawSub(Graphics g, int size, int W, int H, BufferedImage brick, BufferedImage c1, BufferedImage c2, JFrame frame){
		
		int pwidth = H/size;
		int width = pwidth/3 + 1; //adjusts for empty pixels
		int xPix = parent.xPos*pwidth + xPos*width;
		int yPix = parent.yPos*pwidth + yPos*width;
		int lumwidth;
		rand.setSeed((int)((xPix+1)*(yPix+1)));
		if (!show && blindness != 2){ //graphics would happen here
			g.drawImage(brick, xPix, yPix, width, width, frame);
			g.setColor(moss);
			int xcoord; int ycoord; int widthOval;
			for(int i = 0; i<20; i++){
				widthOval = rand.nextInt(width/8);
				xcoord = xPix + rand.nextInt(width); ycoord = yPix + rand.nextInt(width);
				g.fillOval(xcoord, ycoord, widthOval, widthOval);
			}
		} 
		else if (isGoal && blindness != 2){
			drawCarpet(xPix, yPix, width, c1, c2, frame, g);
			BufferedImage head = null;
			try {
    			head = ImageIO.read(new File("Head2.png"));
			} 
			catch (IOException e){};
			g.drawImage(head, (int)(xPix-width/3), (int)(yPix-width/3), width+width/3, width+width/3, frame);
		}
		else if(isLS && blindness != 2){
			BufferedImage lSub = null;
			try {
    			lSub = ImageIO.read(new File("LSub.jpg"));
			} 
			catch (IOException e){};

			g.drawImage(lSub,xPix,yPix,width,width, frame);
		}
		else if (blindness != 2){
			drawCarpet(xPix, yPix, width, c1, c2, frame, g);
		}
	}
	
	public void makeGoal(){
		this.isGoal = true;
	}

	public void makeLS(){
		this.isLS = true;
	}

	public void drawTorches(Random rand, Random rand2, int xPix, int yPix, int lumwidth, Graphics g){
		g.setColor(Color.WHITE);
		g.fillOval(xPix + (int)(lumwidth*.25), yPix + (int)(lumwidth*.25), lumwidth/2, lumwidth/2);
		g.setColor(Color.BLACK);
		g.fillOval(xPix + (int)(lumwidth*.35), yPix + (int)(lumwidth*35), lumwidth/8, lumwidth/8);
		g.setColor(luminate);
		g.fillOval(xPix, yPix, lumwidth, lumwidth);
	}


	public void drawCarpet(int xPix, int yPix, int width, BufferedImage c1, BufferedImage c2, JFrame frame, Graphics g){
		int whereX = xPix;
		int whereY = yPix;
		BufferedImage carpet = c1;
		for(int i = 0; i<5; i++){
			whereX = xPix;
			for(int j = 0; j<5; j++){

				g.drawImage(carpet, whereX, whereY, width/5 +4, width/5+4, frame);
				carpet = changeCarpet(carpet, c1, c2);

				whereX += width/5;
			}
			whereY += width/5;
		}
	}


	public BufferedImage changeCarpet(BufferedImage carpet, BufferedImage c1, BufferedImage c2){
		if(carpet == c1) carpet = c2;
		else carpet = c1;
		return carpet;
	}
}

