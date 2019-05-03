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
	
	public boolean show; //shown are hallways
	public Tile parent; //my mommy tile
	public boolean isGoal = false;

	public static JFrame frame;

	public int blindness = 1; //0 for all bright, 1 for dim, 2 for black
	public boolean isLS = false; //is Light Subtile; lights up the whole maze

	public Color moss = new Color(19, 143, 91, 100);

	Random rand = new Random();

	public Subtile(Tile t, int x, int y,boolean b, JFrame frame){
		parent = t;
		xPos = x;
		yPos = y;
		show = b;
		this.frame = frame;
	}

	
public void drawSub(Graphics g, int size, int W, int H, BufferedImage brick, BufferedImage c1, BufferedImage c2, JFrame frame){
		
		int pwidth = H/size; //tile width
		int width = pwidth/3 + 1; //subtile width; adjusts for empty pixels
		int xPix = parent.xPos*pwidth + xPos*width; //where I'm at
		int yPix = parent.yPos*pwidth + yPos*width;

		rand.setSeed((int)((xPix+1)*(yPix+1))); //seed set for moss based on location of subtile

		if (!show && blindness != 2){ //walls
			g.drawImage(brick, xPix, yPix, width, width, frame); //draws a brick
			g.setColor(moss);//disperses moss below
			int xcoord; int ycoord; int widthOval;
			for(int i = 0; i<20; i++){
				widthOval = rand.nextInt(width/8);
				xcoord = xPix + rand.nextInt(width); ycoord = yPix + rand.nextInt(width);
				g.fillOval(xcoord, ycoord, widthOval, widthOval);
			}
		} 
		else if (isGoal && blindness != 2){ //if at goal
			drawCarpet(xPix, yPix, width, c1, c2, frame, g); //draw carpet under head
			BufferedImage head = null;
			try {
    			head = ImageIO.read(new File("Head2.png"));
			} 
			catch (IOException e){};
			g.drawImage(head, (int)(xPix-width/3), (int)(yPix-width/3), width+width/3, width+width/3, frame);
		}
		else if(isLS && blindness != 2){ //draw light tile
			BufferedImage lSub = null;
			try {
    			lSub = ImageIO.read(new File("LSub.jpg"));
			} 
			catch (IOException e){};

			g.drawImage(lSub,xPix,yPix,width,width, frame);
		}
		else if (blindness != 2){ //catpet
			drawCarpet(xPix, yPix, width, c1, c2, frame, g);
		}
	}
	
	public void makeGoal(){ //make the goal
		this.isGoal = true;
	}

	public void makeLS(){ //make a light tile
		this.isLS = true;
	}



	private void drawCarpet(int xPix, int yPix, int width, BufferedImage c1, BufferedImage c2, JFrame frame, Graphics g){ //draws a carpet
		int whereX = xPix; //where teeny carpet tile is at
		int whereY = yPix;
		BufferedImage carpet = c1; //first teeny carpet tile
		for(int i = 0; i<5; i++){ //each carpet 5x5
			whereX = xPix;
			for(int j = 0; j<5; j++){

				g.drawImage(carpet, whereX, whereY, width/5 +4, width/5+4, frame); //draws teeny carpet tile and adjust for empty areas
				carpet = changeCarpet(carpet, c1, c2);

				whereX += width/5;
			}
			whereY += width/5;
		}
	}


	private BufferedImage changeCarpet(BufferedImage carpet, BufferedImage c1, BufferedImage c2){ //just changes image we're drawing for carpet
		if(carpet == c1) carpet = c2;
		else carpet = c1;
		return carpet;
	}
}

