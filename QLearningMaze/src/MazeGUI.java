import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Delayed;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.xml.bind.ValidationEvent;

public class MazeGUI {

	private static JFrame frame;
	private static int[][] R = new int[25][25];
	private static int[][] Q = new int[25][25];
	private static final int BLOCK = -1 ;
	private static final int LEFT = 0 ;
	private static final int RIGHT = 1 ;
	private static final int UP = 2 ;
	private static final int DOWN = 3 ;
	private static final double gamma=0.8;
	private static final double alpha=0.2;
	private static final double epsilon=0.15;
	static JLabel[] labels = new JLabel[25];
	static int[][] board = new int[5][5];
	static Random r = new Random();
	static long ms = 1;
	static int state=0;
	static int count=0;
	static int episode=0;
	
	static ActionListener actionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			
			
			labels[state].setBackground(Color.RED);
			int rand;
			
				
				double random = r.nextDouble();
				int control=-1;
				if(random<epsilon) {
					do{
						rand = r.nextInt(4);
						if(rand==LEFT) {
							control=state-1;
						}
						if(rand==RIGHT) {
							control=state+1;
						}
						if(rand==UP) {
							control=state-5;
						}
						if(rand==DOWN) {
							control=state+5;
						}
					}while(!(control<=24 && control >=0 && R[state][control]!=-1));
					labels[state].setBackground(Color.WHITE);
					state=Q(state,rand);
					labels[state].setBackground(Color.RED);
					count++;
					
							
				}
				else {
					List<Integer> choose = new ArrayList<Integer>();
					int number =-1;
					int max =-1;
					control=state-1;
					if(control<=24 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							choose.add(LEFT);
							number=1;
						}
							
					}
					control=state+1;
					if(control<=24 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(RIGHT);
						}
						else if(Q[state][control]==max){
							choose.add(number,RIGHT);
							number++;
						}
							
					}
					control=state-5;
					if(control<=24 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(UP);
						}
						else if(Q[state][control]==max){
							choose.add(number,UP);
							number++;
						}
							
					}
					control=state+5;
					if(control<=24 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(DOWN);
						}
						else if(Q[state][control]==max){
							choose.add(number,DOWN);
							number++;
						}
							
					}
					
					int selection =r.nextInt(choose.size());
					labels[state].setBackground(Color.WHITE);
					state=Q(state,choose.get(selection));
					labels[state].setBackground(Color.RED);
					count++;
					
					
					
					
					
				}
			
			
			
		}
	};
	
	Timer timer = new Timer(300, actionListener);
	
	
	ActionListener controlListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(state==24){
				timer.stop();
				System.out.println("Episode: "+ episode + " Actions: "+count);
				labels[24].setBackground(Color.green);
				labels[0].setBackground(Color.red);
				state=0;
				count=0;
				episode++;
			}
			
			if(episode<20){
				
				
				timer.start();
			}
				
				
				
				
			
		}
	};
	
	Timer timer2 = new Timer(300, controlListener);
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MazeGUI window = new MazeGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		
		
		
		
		
		
		
		
	}

	/**
	 * Create the application.
	 */
	public MazeGUI() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 2));
		
		JPanel boardPanel = new JPanel();
		
		JPanel infoPanel = new JPanel();
		
		boardPanel.setLayout(new GridLayout(5, 5));
		
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		
		
		
		for(int i =0 ; i< 25 ;i++) {
			labels[i] = new JLabel("");
			labels[i].setOpaque(true);
			labels[i].setBackground(Color.white);
			labels[i].setBorder(BorderFactory.createLineBorder(Color.black));
			
			
			
			boardPanel.add(labels[i]);
			
		}
		MouseListener listener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				JLabel source = (JLabel)e.getSource();
				source.setBackground(Color.BLACK);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		for(int i=0;i<25;i++) {
			labels[i].addMouseListener(listener);
		}
		
		
		labels[24].setBackground(Color.green);
		labels[0].setBackground(Color.red);
		
		
		frame.add(boardPanel);
		frame.add(infoPanel);
		
		JButton start = new JButton("Start");
		
		infoPanel.add(start);
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				Random r = new Random();
				
				
				
				for(int i =0 ;i < 25 ; i++ ) {
					
					if(labels[i].getBackground().equals(Color.black))
						board[i/5][i%5]=-1;
				}
				
				for(int i=0 ; i<25 ; i++) {//initiliaze R
					for(int j=0; j<25 ; j++) {
						R[i][j]=-1;
					}
				}
				
				for(int i =0 ; i<5;i++){//fillR
					for(int j =0 ; j<5 ;j++) {
						
						if(board[i][j]==0) {
							
							if(j>0){//sola bak
								if(board[i][j-1]== 0)
									R[5*i+j][5*i+j-1]=0;
								else 
									R[5*i+j][5*i+j-1]=-1;
							}
							
							if(j<4) {//saga bak
								if(board[i][j+1]== 0)
									R[5*i+j][5*i+j+1]=0;
								else 
									R[5*i+j][5*i+j+1]=-1;
							}
							
							if(i>0) {//yukarý bak
								if(board[i-1][j]== 0)
									R[5*i+j][5*(i-1)+j]=0;
								else 
									R[5*i+j][5*(i-1)+j]=-1;
							}
							
							if(i<4) {//asagý bak
								if(board[i+1][j]== 0)
									R[5*i+j][5*(i+1)+j]=0;
								else 
									R[5*i+j][5*(i+1)+j]=-1;
							}
							
							
							
						}
					}
				}
				
				for(int i =0 ; i< 25 ; i++) {//reward 
					if(R[i][24]==0)
						R[i][24]=100;
				}
				
				
				
				timer.start();
				timer2.start();
				
				
				
				
			}
		});
		
		
		
		
		
	}
	
	public static int Q(int state , int action){
		int newstate;
		int control;
		int max=-10000;
		if(action==LEFT)
			newstate= state-1;
		else if(action==RIGHT)
			newstate= state+1;
		else if(action==UP)
			newstate= state-5;
		else 
			newstate= state+5;
		
		
		control=newstate-1;
		if(control<=24 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+1;
		if(control<=24 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate-5;
		if(control<=24 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+5;
		if(control<=24 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		Q[state][newstate]=R[state][newstate] + (int)Math.round(gamma*max);
		
		return newstate;
		
		
		
		
			
		

	}

}
