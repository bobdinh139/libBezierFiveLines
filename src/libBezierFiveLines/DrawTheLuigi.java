package libBezierFiveLines;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import javax.swing.*;

public class DrawTheLuigi  extends Component implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	   Timer timer;
		  ActionListener animation;

	static double toX =400;
	static double toY= 400;
	
	double mouseXC =0;
	double mouseYC =0;
	
	boolean ball_go = false;
	boolean ball_kgo = false;
	
	static int ball_y =395;
	static int ball_x =395;
	
	JFrame frame;
	public DrawTheLuigi(JFrame frame) {
		this.frame = frame;
	    addMouseListener(this);
	    addMouseMotionListener(this);
	     animation = new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent ae) {
	                repaint();
	            }
	        };
	        timer = new Timer(50, animation);
	}
	private static final long serialVersionUID = 1L;
	double w = DrawAsk.w;
    double h = DrawAsk.h;
    double halfw = (w%200!=0) ? 200-(-1)*( w/2-200) : w/2;
    double startpoint = 200;
    double top =400-h;
    double bottom =400;
    
    int count =0;
    
    double top_curve =0;
    double top_tri =0;
    int c =0;
    
    public void paint(Graphics g) {

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
              g.drawLine(10, 400, 790, 400);
              g.drawLine(400, 10, 400, 790);
              // B(t)=(1-t)^2.P0 + 2(1-t).t.P1 + t^2.P2
              // x = (1-t)^2x1 + 2(1-t)tx2 + t^2x3
              //y = (1-t)^2y1 + 2(1-t)ty2 + t^2y3
              // 400 = ((1-t)^2)*(200+(-1)*(w/2-200)) + 2(1-t)t400 +(t^2)*(400-(200+(-1)*(w/2-200)))
              //t = (-(-2x1-2x2) +- sqrt((-2x1-2x2)^2 - 4(x1-2x2+x3)(x1-x))/2(x1-2x2+x3)
              //y (i) = 3/2(x + 200) + 400; y' (j) = -3/2(x-200) + 400 
              // i = 3/2(x+w/2) + 400
              // w = 400; h = 300
              g.setColor(Color.RED);
              g.drawString("i = ("+ h/100 +"/"+w/200+")(200-x) +"+w, 10,15 );
              g.drawString("j = ("+ h/100 +"/"+w/200+")(x+600) +"+ w, 10,30 );
              g.drawString("Use arrow keys or drag the screen up/down to change its height, width", 250,650);
              g.drawString("type \"a\" or click near the blue text (add/remove) to add more lines, \"s\" to remove lines and \"c\" to close the window", 150, 670);
              g.drawString(" type \"g\" to animate the ball", 300,685);
              g.drawString(" type \"t\" or click once to toggle changing height", 280,700);
              
              g.drawString("Top of the triangle (from 400 y): "+top_tri , 10,60);
              g.drawString("Top of the curve (from 400 y): "+ top_curve , 10,80);
              g.drawString("X: "+mouseXC, 700,60);
              g.drawString("Y: "+mouseYC, 700,80);

              g.drawString("y = " + (4)*top_curve/Math.pow(w,2) + "(x-400)^2 + " + (400 - top_curve), 10,100 );
              g.drawString("y= "+ Math.abs(400-toY)/(400-toX) + "(x-400)" + "+ 400",10,110);

              g.setColor(Color.BLACK);
              g.drawLine(200, 400, 400, 100);
              g.drawLine(400, 100, 600, 400);
              
              g.setColor(Color.GREEN);
              g.fillOval(ball_x,ball_y, 10,10);
              g.setColor(Color.BLACK);
              ((Graphics2D) g).draw(new Line2D.Double(400,400,toX,toY));
          
               // infinite big number
              ((Graphics2D) g).draw(new Line2D.Double(toX,toY, 500000/((-1)*(400 - toX)/Math.abs(400 - toY)) , 500000));

     
         
              double the_slop= (toX*Math.pow(w,2))/(8*(h/2));
              g.setColor(Color.pink);
              ((Graphics2D) g).draw(new Line2D.Double(toX,toY,500000/the_slop , 500000));
              g.setColor(Color.BLACK);

              
              Graphics2D g2 = (Graphics2D) g; 
             double i =top; 
             double j =bottom;
             
             while (i <bottom && j>top) {
            	 i+=DrawAsk.dis;
            	 j-=DrawAsk.dis;
            	 double x1 = (-1)*(halfw*(i-400)/h) + 200+(-1)*(w/2-200);
            	 double x2 =  (halfw*(j-400)/h)+400+(400-(200+(-1)*(w/2-200)));
            	 double y1 = i;
            	 double y2 = j;
            	        
                       g2.draw( new Line2D.Double( x1 , y1, x2, y2));
                       count ++;
             }
             g.setColor(Color.BLUE); 
             g.drawString("Number of Lines: "+count, 10, 45);
                  count = 0;
            
           		if(ball_go) {
       		     if ((ball_y) <=toY) {
       		    	ball_go= false;
       		    	ball_kgo = true;
       		     }else {
       			 ball_y --;
       		     ball_x =  (int) (((ball_y-395)*(400-toX))/Math.abs(400-toY)) + 395;
       		     }    
              } else if (ball_kgo) {
            	  if (ball_y > 400) {
            		  ball_kgo= false;
            	  }
            	  ball_y++;
            	  ball_x =  (int) (((ball_y - toX)/ (Math.pow(w,2))/(8*(h/2))) + toX);
            	  
              }
                  
     }
int initial =0;
int initialY=0;
	@Override

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseXC = arg0.getX();
		mouseYC = arg0.getY();
	       ball_y = 395;
	        ball_x = 395;
	if(DrawAsk.mousechange) {
	  if (arg0.getX() - initial <0) {
		  w += 5;
		  frame.repaint();
	  } else {
		  w -= 5;
		  frame.repaint();
	  }
	  
	  if (arg0.getY() - initialY >0) {
		  if (DrawAsk.toogle_top)  top =400-h;
		  h-=5;
	  } else {
		  if (DrawAsk.toogle_top)  top =400-h;
		  h+=5;
	  }
	}	else {
		toX = arg0.getX();
		toY = arg0.getY();
		
		}
	  
	frame.repaint();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseXC = arg0.getX();
		mouseYC = arg0.getY();
		frame.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		timer.stop();
		ball_go = false;
	       ball_y = 395;
	        ball_x = 395;
		if(DrawAsk.mousechange) {
		  if (arg0.getY() < 50 && arg0.getY()> 10 && arg0.getX() < 50 && arg0.getX() > 0) {
			  DrawAsk.dis  --;
		  if (DrawAsk.dis <1) DrawAsk.dis =1;
		  }
			  else if (arg0.getY() < 50 && arg0.getY()> 10 && arg0.getX() > 50 && arg0.getX() < 100)
			  DrawAsk.dis ++;
		  
		  else 
          DrawAsk.toogle_top = !DrawAsk.toogle_top;
		}
		else {
	        if(arg0.getButton() == MouseEvent.BUTTON1) {
	        	toX = arg0.getX();
	    		toY = arg0.getY();
	          } else {
	        	  	toX = 400;
		    		toY = 400;
	          }
	
		}
		frame.repaint();

	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(DrawAsk.mousechange) {
		initial = arg0.getX();
		initialY = arg0.getY();
		}
	}
	

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	      	
	}
	

}
