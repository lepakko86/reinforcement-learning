import java.awt.Color;
import java.awt.Graphics;


import javax.swing.JPanel;

public class MyPanel extends JPanel  {
	
	int r=0;
	Color color = new Color(255, 0, 0);
	
	public MyPanel() {
		// TODO Auto-generated constructor stub
		setBackground(Color.white);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawCircle(g ,(int)getAlignmentX()+getWidth()/2,(int)getAlignmentY()+getHeight()/2, r);
	}
	
	
	
	public void drawCircle(Graphics g ,int x, int y, int radius) {
			g.setColor(color);
		   g.fillOval(x-radius, y-radius, radius*2, radius*2);
	}
	
	public void setR(int r) {
		this.r = r;
	}
	
	public void setColor(Color color){
		this.color=color;
	}
	



	

}
