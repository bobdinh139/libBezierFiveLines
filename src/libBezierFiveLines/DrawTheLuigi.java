//Main stuff here
package libBezierFiveLines;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import javax.swing.*;

public class DrawTheLuigi  extends Component implements MouseListener, MouseMotionListener  {
	private static final long serialVersionUID = 1L;

	public boolean toggle_perl = false;
	private static double coordsx = 400;
	private static double coordsy = 400;
	private double dapathx = 400;
	public boolean nightmode = false;

	//private static double dapathx =400;
	private final int supperBigNumber = 500000;
	private boolean check = true;

	public static Timer timer;
	private JFrame frame;
	private ActionListener animation;
	private WriteLogF wl = new WriteLogF();

	public static double toX =400;
	public static double toY= 400;

	public static double mouseXC =0;
	public static double mouseYC =0;

	public static boolean ball_go = false;
	private static boolean ball_kgo = false;

	public static int ball_y =395;
	private static int theSub =1;
	public static int ball_x =395;

	private int stick_sx = 500;
	private static int stick_cy = 500;
	private static int the_sub =1;

	public double w = DrawAsk.w;
	public double h = DrawAsk.h;
	double halfw = (w%200!=0) ? 200-(-1)*( w/2-200) : w/2;
	double startpoint = 200;
	double top =400-h;
	double bottom =400;

	private int count =0;

	private double top_curve =0;
	private double top_tri =0;
	private int c =0;

	private int initial =0;
	private int initialY=0;

	private boolean clicked = false;	
	public boolean hideHelpBox = false;

	public DrawTheLuigi(JFrame frame) {
		this.frame = frame;
		addMouseListener(this);
		addMouseMotionListener(this);
		// for animation
		animation = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				repaint();
			}
		};
		timer = new Timer(50, animation);

	}

	//draw on screen button
	private void initComponents(Graphics g) {
		g.setColor(checkhover(680,430));
		g.fillRect(680, 430, 50, 50);
		g.setColor(checkhover(630,480));
		g.fillRect(630, 480, 50, 50);
		g.setColor(checkhover(680,480));
		g.fillRect(680, 480, 50, 50);
		g.setColor(checkhover(730,480));
		g.fillRect(730, 480, 50, 50);
		g.setColor(checkhover(680,560));
		g.fillOval(680, 560, 50, 50);
		g.setColor(Color.BLACK);
		g.drawRect(680, 430, 50, 50);
		g.drawRect(630, 480, 50, 50);
		g.drawRect(680, 480, 50, 50);
		g.drawRect(730, 480, 50, 50);
		g.drawOval(680, 560, 50, 50);
		g.fillPolygon(new int[] {695 -5, 705 ,715+5}, new int[] {460 +5, 440 , 460+5}, 3);
		g.fillPolygon(new int[] {695 -5, 705 ,715+5}, new int[] {505 -5, 525, 505-5}, 3);
		g.fillPolygon(new int[] {(630+(630+50))/2 -20 +5, (630+(630+50))/2 +5,(630+(630+50))/2 +5}, new int[] {505, 525, 485}, 3);
		g.fillPolygon(new int[] {(730+(730+50))/2 -5, (730+(730+50))/2 -5,(730+(730+50))/2 + 20-5}, new int[] {485, 525, 505}, 3);	
		g.setColor(Color.BLUE);
		g.fillOval(140,490,20,10);
		g.setColor(Color.GREEN);
		g.fillOval(140,505,20,10);
		g.setColor(Color.ORANGE);
		g.fillOval(140,535,20,10);
		g.setColor(Color.RED);
		g.fillOval(140,550,20,10);
		g.setColor(Color.PINK);
		g.fillOval(140,575,20,10);
	}

	//draw bunch of stuff
	@Override
	public void paint(Graphics g) {
		getTopCurve();
		drawInformation(g);
		drawCurve(g);
		drawCoords(g);    	
		drawBall(g);
		drawNormalLineAndPathBall(g); 
		drawPathBounceBack(g);       
		drawStick(g);
		drawTangentLine(g);
		initComponents(g);
		ballAndStickLogic();
		getPathSlope();
		writedalog();


	}

	//from here down are logic stuff
	//check color when hover onscreen button
	private Color checkhover(int x, int y) {
		if (((mouseXC > x) && (mouseXC < x +50)) && ((mouseYC > y) && (mouseYC < y +50))) {
			if (clicked) {
				clicked = false;
				return Color.DARK_GRAY;

			} else
				return Color.LIGHT_GRAY;
		}
		else {
			return  Color.GRAY;
		}
	}
	// calculate the slope of the ball and the cue --> find the path of the ball and the cue
	private void ballAndStickLogic() {
		//if you click g
		if(ball_go) {
			// if the cue touch the ball at origin
			if (stick_cy <= 400) {
				// ball starts going; if ball touch the curve
				if ((ball_y) <=toY) {
					ball_go= false;
					ball_kgo = true;
				}else {
					//ball keep going til it hits the curve
					ball_y -= theSub; theSub+=1;
					// equation to find x according to y; shift right 395 and up 395 bc it is the ball's origin (using y = a(x+c)+b)
					ball_x =  (int) (((ball_y-395)*(400-toX))/Math.abs(400-toY)) + 395;
				}    
			} else {stick_cy -= the_sub; the_sub +=2;} // it will keep going until it hits the ball
		} else if (ball_kgo) {
			// if ball passes the origin in y; stops.
			if (ball_y > 400) {
				ball_kgo= false;
			}
			// ball bounce back -- this is the equation to find x; but i am not sure if this is the correct one
			ball_y+=theSub; theSub -=1;
			ball_x =  (int) (((ball_y - toY)/((coordsy-toY)/(coordsx - toX))) + toX-7);
		}
	}
	// reset everything, stop the timer -- animation.
	public static void reset() {
		timer.stop();
		ball_go = false;
		ball_kgo = false;
		ball_y = 395;
		ball_x = 395;
		stick_cy = 500;
		the_sub =1;
		theSub =1;
		toX =400;
		toY=400;
		//dapathx = 400;
	}
	public void resetC() {
		w=400;
		h=300;
	}
	public void resetB() {
		ball_y = 395;
		ball_x = 395;
	}
	//find the vertex (y)
	private void getTopCurve() {
		if (DrawAsk.toogle_top) {
			top_curve = h/2;
			top_tri = h;
		} else {
			if (c ==0) {
				top_curve = h/2;
				top_tri= h;
				c=1;
			}

		}
	}
	private void writedalog() {
		// write log
		if (ball_go && check) {
			System.out.println("Equation of the curve: y = " + (4)*top_curve/Math.pow(w,2) + "(x-400)^2 + " + (400 - top_curve));
			wl.writeLog("Equation of the curve: y = " + (4)*top_curve/Math.pow(w,2) + "(x-400)^2 + " + (400 - top_curve));
			System.out.println("dapathx appears to be: " + dapathx);
			wl.writeLog("dapathx appears to be: " + dapathx);
			System.out.println("coordsx appears to be: " + coordsx);
			wl.writeLog("coordsx appears to be: " + coordsx);
			System.out.println("coordsy appears to be: " + coordsy);
			wl.writeLog("coordsy appears to be: " + coordsy);
			check = false;
		}
		if (!ball_go) {
			check = true;
		}
	}
	private int getCueX() {
		return (int) (((stick_sx)*(400-toX))/Math.abs(400-toY));
	}

	// find y of the curve
	private double the_functino(double x) {
		return ((4)*top_curve/Math.pow(w,2))*Math.pow((x-400),2) + (400 - top_curve);
	}
	//calculate the derivative of the curve
	private double find_derivative(double x) {
		return (4)*(top_curve/Math.pow(w,2))*2*(x-400);
	}
	//calculate the line perpendicular to the tangent line
	private double find_perpend_tan(double x) {
		return -1/((4)*(top_curve/Math.pow(w,2))*2*(x-400));
	}
	// calculate the reflection line by finding the length between perpend line and projected line

	private void getPathSlope() {
		//calculating the intersection of the path and 400 (y)
		dapathx = (400 - toY)/( (supperBigNumber-toY)/((supperBigNumber/find_perpend_tan(toX))-toX)) + toX -4;

		double fpt = ( (supperBigNumber-toY)/((supperBigNumber/find_perpend_tan(toX))-toX));
		double fd = ( (supperBigNumber-toY)/((supperBigNumber/find_derivative(toX))-toX));

		double testx = (fpt*(toX-4) - fd*(395) + 395 - toY)/(fpt - fd);
		double testy = fd*(testx - 395) + 395;
		// calculating the length 400,400 to the normal line
		double length = Math.sqrt( Math.pow( (395 - testx), 2) + Math.pow( (395 - testy), 2));
		if (dapathx >= 400) {
			coordsx = testx + length*Math.sqrt(1/(1+ Math.pow(fd, 2)));
			coordsy = testy + fd*length*Math.sqrt(1/(1+ Math.pow(fd, 2)));
		}
		else {
			coordsx = testx - length*Math.sqrt(1/(1+ Math.pow(fd, 2)));
			coordsy = testy - fd*length*Math.sqrt(1/(1+ Math.pow(fd, 2)));	
		}

		//return dapathx+ (dapathx -400);
	}

	//draw the tangent line to the curve
	private void drawTangentLine(Graphics g) {
		//check if mouse touch the curve
		if(nightmode) {
			g.setColor(Color.WHITE);

		}else
			g.setColor(Color.BLACK);
		double suppose_y = the_functino(toX);
		if ((suppose_y >= (toY-2)) && (suppose_y <= (toY+2)) && toggle_perl) {		
			// perpendicular line
			g.setColor(Color.blue);
			((Graphics2D) g).draw(new Line2D.Double(toX,toY, supperBigNumber/find_perpend_tan(toX), supperBigNumber ));
			//draw from x,y to infi
			g.setColor(Color.RED);
			((Graphics2D) g).draw(new Line2D.Double(toX,toY, supperBigNumber/find_derivative(toX), supperBigNumber ));
			//draw from x,y to -infi
			((Graphics2D) g).draw(new Line2D.Double(toX,toY, -supperBigNumber/find_derivative(toX), -supperBigNumber ));
		}
	}
	//from here down are graphics stuff

	private void drawInformation(Graphics g) {
		g.setColor(Color.RED);
		if(!hideHelpBox) {
			g.drawString("Use arrow keys or drag (mousechange) its height, width", 30,450);
			g.drawString("type \"a\", \"s\" to add, remove lines and \"c\" to close", 30, 470);
			g.drawString(" type \"g\" or click at the origin to animate the ball", 30,485);
			g.drawString(" type \"t\" or click        to toggle changing height", 30,500);
			g.drawString(" type \"d\" or click        to toggle debug mode", 30,515);
			g.drawString(" type \"m\" to toggle mouse change", 30,530);
			g.drawString(" type \"n\" or click        to toggle nightmode", 30,545);
			g.drawString(" type \"r\" or click        to reset curve", 30,560);
			g.drawString(" type \"h\" or click        to hide help box", 30,585);
		}
		if (toggle_perl) {
			// equation of the two sides of the triangle
			g.drawString("coordsx, coordsy appears to be: " + Math.round(coordsx) +", "+Math.round(coordsy), 10,15 );
			g.drawString("dapathx appears to be: " + Math.round(dapathx), 10,30 );

			g.drawString("Top of the triangle (from 400 y): "+top_tri , 10,60);
			g.drawString("Top of the curve (from 400 y): "+ top_curve , 10,80);
			// mouse Coords
			g.drawString("X: "+mouseXC, 700,60);
			g.drawString("Y: "+mouseYC, 700,80);

			//equation of the curve and equation of the line 
			g.drawString("y = " + (4)*top_curve/Math.pow(w,2) + "(x-400)^2 + " + (400 - top_curve), 10,100 );
			g.drawString("y= "+ Math.abs(400-toY)/(400-toX) + "(x-400)" + "+ 400",10,115);
		}
	}

	private void drawCoords(Graphics g) {
		if(nightmode) {
			g.setColor(Color.GRAY);

		}else
			g.setColor(Color.BLACK);

		//x and y axis
		g.drawLine(10, 400, 790, 400);
		if(toggle_perl) {
			g.drawLine(400, 10, 400, 790);
			//Mid triangle
			g.drawLine(200, 400, 400, 100);
			g.drawLine(400, 100, 600, 400);
		}
	}

	//draw the ball
	private void drawBall(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(ball_x,ball_y, 10,10);
	}

	private void drawNormalLineAndPathBall(Graphics g) {
		if(nightmode) {
			g.setColor(Color.WHITE);

		}else
			g.setColor(Color.BLACK);
		//path of the ball
		((Graphics2D) g).draw(new Line2D.Double(400,400,toX,toY));
		g.setColor(Color.LIGHT_GRAY);
		//line that is perpendicular to the path of the ball before it hits the curve -- using very large number to create 90 degree angle ( it will be less 90 if the number goes down)
		if(toggle_perl) {
			((Graphics2D) g).draw(new Line2D.Double(toX,toY, supperBigNumber/((-1)*(400 - toX)/Math.abs(400 - toY)) , supperBigNumber));
			((Graphics2D) g).draw(new Line2D.Double(toX,toY, -supperBigNumber/((-1)*(400 - toX)/Math.abs(400 - toY)) , -supperBigNumber));
		}
	}

	//draw the pink path -- when the ball hits the curve and bounce back
	private void drawPathBounceBack(Graphics g) {
		// this line responsible for the bounce back of the ball, but i am not sure if it is correct
		//double the_slop= (toX*Math.pow(w,2))/(8*(h/2));
		g.setColor(Color.pink);
		//check if mouse touch the curve
		double suppose_y = the_functino(toX);
		if ((suppose_y >= (toY-2)) && (suppose_y <= (toY+2)))
			((Graphics2D) g).draw(new Line2D.Double(toX,toY, coordsx , coordsy));
		// ((Graphics2D) g).draw(new Line2D.Double(toX,toY,500000/the_slop , 500000));
	}

	//draw the cue using bunch of lines instead of rect because cant rotate
	private void drawStick(Graphics g) {
		// using the same slope as the path of the ball to the curve 
		stick_sx = (int) (((stick_cy-395)*(400-toX))/Math.abs(400-toY)) + 395;
		g.setColor(Color.ORANGE);
		for (int i=0; i< 5; i++)
			g.drawLine(stick_sx +i, stick_cy+i,  getCueX() + 395+i, stick_cy + 200+i);

	}

	// draw the curve using lines
	private void drawCurve(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; 
		double i =top; 
		double j =bottom;
		g.setColor(Color.MAGENTA);
		// generate the curve by generating bunch of line; each line is i and j distance from each other
		while (i <bottom && j>top) {

			g2.draw( new Line2D.Double( ((-1)*(halfw*(i-400)/h) + 200+(-1)*(w/2-200))  ,(i+=DrawAsk.dis) , ((halfw*(j-400)/h)+400+(400-(200+(-1)*(w/2-200))))   , 	(j-=DrawAsk.dis) ));
			count ++;
		}
		//count number of lines
		g.setColor(Color.BLUE); 
		g.drawString("Number of Lines: "+count, 10, 45);
		count = 0;

	}

	//down here are listeners for the mouse
	@Override
	public void mouseDragged(MouseEvent arg0) {
		//find the coords of the mouse to display on the screen
		mouseXC = arg0.getX();
		mouseYC = arg0.getY();

		//if user click mouse change
		if(DrawAsk.mousechange) {
			// allow for changing the curve horizontally using mouse
			if (arg0.getX() - initial <0) {
				w += 5;
				frame.repaint();
			} else {
				w -= 5;
				frame.repaint();
			}
			// changing the curve vertically
			if (arg0.getY() - initialY >0) {
				if (DrawAsk.toogle_top)  top =400-h; //if they toggle changing up and down
				h-=5;
			} else {
				if (DrawAsk.toogle_top)  top =400-h; // if not, change how the endpoints are met
				h+=5;
			}
		}	else if ( arg0.getY() <=400){			
			//if user does not toggle mousechange --> allow doing simulation
			toX = arg0.getX();
			toY = arg0.getY();

		}
		//refresh the frame
		frame.repaint();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		//get the coords in realtime
		mouseXC = arg0.getX();
		mouseYC = arg0.getY();
		frame.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		resetB();
		clicked = true;
		//if user toggle mouse change
		if(DrawAsk.mousechange) {
			//click near the blue part to add/remove lines 
			DrawAsk.toogle_top = !DrawAsk.toogle_top;
			//reset when mouse is clicked but not on origin and when mousechange is toggle
			if (!((Math.abs(400 - arg0.getX()) <=5)&&(Math.abs(400 - arg0.getY()) <=5)))
				reset();
		}
		else {
			if(((Math.abs(400 - arg0.getX()) <=5)&&(Math.abs(400 - arg0.getY()) <=5)) || (  ((arg0.getX() > 680) && (arg0.getX()  < 680 +50)) && ((arg0.getY()  > 560) && (arg0.getY()  < 560 +50))    )   ) {
				ball_go = true;
				timer.start();
			} 
			else if (((arg0.getX() > 680) && (arg0.getX() < 680 +50)) && ((arg0.getY() > 430) && (arg0.getY() < 430 +50))) {
				if (DrawAsk.toogle_top) {
					h +=  10;
					top = 400 - h;	
				} else {
					h +=  10;
				}

			}else if (((arg0.getX() > 680) && (arg0.getX() < 680 +50)) && ((arg0.getY() > 480) && (arg0.getY() < 480 +50))) {
				if (DrawAsk.toogle_top) {
					h -=  10;
					top = 400 - h;	
				} else {
					h -=  10;
				}

			}else if (((arg0.getX() > 630) && (arg0.getX() < 630 +50)) && ((arg0.getY() > 480) && (arg0.getY() < 480 +50))) {
				w-=10;
			}else if (((arg0.getX() > 730) && (arg0.getX() < 730 +50)) && ((arg0.getY() > 480) && (arg0.getY() < 480 +50))) {
				w+=10;
			}
			else if((arg0.getButton() == MouseEvent.BUTTON1) && arg0.getY() <=400) {
				//if user does not toggle mouse change; if user left-click
				//get the coordinates of the left-click
				toX = arg0.getX();
				toY = arg0.getY();
			} else if (arg0.getButton() == MouseEvent.BUTTON3){
				//if user click something else that is not left click --> reset the coords of the line to the curve back to origin
				toX = 400;
				toY = 400;
			}

		}

		if (arg0.getY() < 50 && arg0.getY()> 10 && arg0.getX() < 50 && arg0.getX() > 0) {
			DrawAsk.dis --;
			if (DrawAsk.dis <1) DrawAsk.dis =1;
		}
		else if (arg0.getY() < 50 && arg0.getY()> 10 && arg0.getX() > 50 && arg0.getX() < 100)
			DrawAsk.dis ++;

		if((Math.abs(arg0.getX() -150) <=15) && (Math.abs(arg0.getY() - 515) <=10) ) {
			System.out.println("Clicked d makes debug mode = " + toggle_perl);
			wl.writeLog("Clicked d makes debug mode = "+ toggle_perl);
			toggle_perl = !toggle_perl;
		}

		if((Math.abs(arg0.getX() -150) <=15) && (Math.abs(arg0.getY() - 500) <=10) ) {
			System.out.println("toggle top = "+ DrawAsk.toogle_top);
			wl.writeLog("toggle top = "+ DrawAsk.toogle_top);
			DrawAsk.toogle_top = !DrawAsk.toogle_top;
		}

		if((Math.abs(arg0.getX() -150) <=15) && (Math.abs(arg0.getY() - 560) <=10) ) {
			System.out.println("Clicked r makes w, h = " + w + ", "+h);
			wl.writeLog("Clicked r makes w, h = " + w + ", "+h);
			resetC();
		}

		if((Math.abs(arg0.getX() -150) <=15) && (Math.abs(arg0.getY() - 585) <=10) ) {
			hideHelpBox = !hideHelpBox;
		}

		if((Math.abs(arg0.getX() -150) <=15) && (Math.abs(arg0.getY() - 545) <=10) ) {
			System.out.println("Clicked n makes nightmode = "+ nightmode);
			wl.writeLog("Clicked n makes nightmode = "+ nightmode);
			nightmode = !nightmode;
			if(nightmode) {
				frame.getContentPane().setBackground(Color.BLACK);  
			} else {
				frame.getContentPane().setBackground(Color.WHITE);  

			}
		}

		frame.repaint();
	}

	//kinda forget what this does
	@Override
	public void mousePressed(MouseEvent arg0) {
		clicked = true;
		if(DrawAsk.mousechange) {
			initial = arg0.getX();
			initialY = arg0.getY();
		}


	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}




}
