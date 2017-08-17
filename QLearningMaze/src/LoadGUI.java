import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.IllegalBlockSizeException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.xml.soap.Text;

import org.omg.PortableServer.THREAD_POLICY_ID;

public class LoadGUI {
    
	
	
	private static final int NUMBER_OF_ROW=15;
	private static final int ROWROW=NUMBER_OF_ROW*NUMBER_OF_ROW;
	private static final int NUMBER_OF_STATE=ROWROW*2;
	private static final int START_STATE=0;
	private static final int GOAL_STATE=199;
	private static final int DEST_STATE=GOAL_STATE-ROWROW;
	private static int LOAD_STATE;
	private static double[][] R = new double[NUMBER_OF_STATE][NUMBER_OF_STATE];
	private static double[][] Q = new double[NUMBER_OF_STATE][NUMBER_OF_STATE];
	static int[][] board = new int[NUMBER_OF_ROW][NUMBER_OF_ROW];
	private static double p=0;
	private static final int LEFT = 0 ;
	private static final int RIGHT = 1 ;
	private static final int UP = 2 ;
	private static final int DOWN = 3 ;
	private static final int LOAD = 4;
	private static final double gamma=0.95;
	private static final double alpha=0.1;
	private static double epsilon=0.5;
	private static double x=0.5; 
	static long ms = 1000;
	static int state=0;
	static int count=0;
	static int episode=0;
	static Random r = new Random();
	private static boolean loaded=false;
	
	static JFrame frame = new JFrame();
	static JLabel[] labels = new JLabel[ROWROW];
	static MyPanel[] qPanels = new MyPanel[ROWROW];
	static MyPanel[] lPanels = new MyPanel[ROWROW];
	
	
	static JPanel boardPanel = new JPanel();
	
	static JPanel infoPanel = new JPanel();
	static JPanel qPanel = new JPanel();
	
	static JPanel lPanel = new JPanel();
 	
static ActionListener actionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			if(loaded)
				labels[state-ROWROW].setBackground(Color.RED);
			else
			    labels[state].setBackground(Color.RED);
			int rand;
			
				
				double random = r.nextDouble();
				int control=-1;
				if(random<epsilon) {
					do{
						rand = r.nextInt(5);
						if(rand==LEFT) {
							control=state-1;
						}
						if(rand==RIGHT) {
							control=state+1;
						}
						if(rand==UP) {
							control=state-NUMBER_OF_ROW;
						}
						if(rand==DOWN) {
							control=state+NUMBER_OF_ROW;
						}
						if(rand==LOAD)
							control=state;
						
					}while(!(control<=NUMBER_OF_STATE-1 && control >=0 && (R[state][control]!=-1 || rand==LOAD)));
					if(loaded)
						labels[state-ROWROW].setBackground(Color.WHITE);
					else 
						labels[state].setBackground(Color.WHITE);
					state=Q(state,rand);
					if(loaded)
						labels[state-ROWROW].setBackground(Color.RED);
					else
						labels[state].setBackground(Color.RED);
					count++;
					
							
				}
				else {
					List<Integer> choose = new ArrayList<Integer>();
					int number =-1;
					double max =-1;
					control=state-1;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							choose.add(LEFT);
							number=1;
						}
							
					}
					control=state+1;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
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
					control=state-NUMBER_OF_ROW;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
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
					control=state+NUMBER_OF_ROW;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
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
					
					if(state==LOAD_STATE)
						control=state+ROWROW;
					else
						control=state;
					
					if(control<=NUMBER_OF_STATE-1 && control >=0 && (R[state][control]!=-1 || control==state)){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(LOAD);
						}
						else if(Q[state][control]==max){
							choose.add(number,LOAD);
							number++;
						}						
					}
					
					int selection =r.nextInt(choose.size());
					if(loaded)
						labels[state-ROWROW].setBackground(Color.WHITE);
					else
						labels[state].setBackground(Color.WHITE);
					state=Q(state,choose.get(selection));
					if(loaded)
						labels[state-ROWROW].setBackground(Color.RED);
					else 
						labels[state].setBackground(Color.RED);
					count++;
					
					
					
					
					
				}
				
			if(!loaded) {
				labels[LOAD_STATE].setBackground(Color.BLUE);
				labels[DEST_STATE].setBackground(Color.GREEN);
			}	
			else
				labels[LOAD_STATE].setBackground(Color.WHITE);
			
		}
	};
	
	Timer timer = new Timer(100, actionListener);
	
	ActionListener controlListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(state==GOAL_STATE && loaded){
				timer.stop();
				System.out.println("Episode: "+ episode + " Actions: "+count);
				labels[DEST_STATE].setBackground(Color.green);
				labels[START_STATE].setBackground(Color.red);
				state=START_STATE;
				count=0;
				//epsilon=x-(double)(episode*x)/15000;
				fillTables();
				episode++;
				loaded=false;
			}
			
			if(episode<20000){
				
				//System.out.println(epsilon);
				timer.start();
			}
				
				
				
				
			
		}
	};
	
	Timer timer2 = new Timer(100, controlListener);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoadGUI window = new LoadGUI();
					window.frame.setVisible(true);
					//window.Qframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoadGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		
		frame.setBounds(100, 100, 1200, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 4));
		
		
		
		JPanel boardPanel = new JPanel();
		
		JPanel infoPanel = new JPanel();
		JPanel qPanel = new JPanel();
		
		JPanel lPanel = new JPanel();
		
		boardPanel.setLayout(new GridLayout(NUMBER_OF_ROW,NUMBER_OF_ROW));
		
		boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		qPanel.setLayout(new GridLayout(NUMBER_OF_ROW,NUMBER_OF_ROW));
		
		qPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		lPanel.setLayout(new GridLayout(NUMBER_OF_ROW,NUMBER_OF_ROW));
		
		lPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		for(int i =0 ; i< ROWROW ;i++) {
			labels[i] = new JLabel("");
			labels[i].setOpaque(true);
			labels[i].setBackground(Color.white);
			labels[i].setBorder(BorderFactory.createLineBorder(Color.black));
			
			
			
			boardPanel.add(labels[i]);
			
			qPanels[i] = new MyPanel();
			
			qPanels[i].setBackground(Color.white);
			qPanels[i].setBorder(BorderFactory.createLineBorder(Color.black));
			
			qPanel.add(qPanels[i]);
			
			lPanels[i] = new MyPanel();
			
			lPanels[i].setBackground(Color.white);
			lPanels[i].setBorder(BorderFactory.createLineBorder(Color.black));
			
			lPanel.add(lPanels[i]);
			
			
			
		}
		
		
MouseListener listener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				JLabel source = (JLabel)e.getSource();
				source.setBackground(Color.BLUE);
				
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
		
		for(int i=0;i<ROWROW;i++) {
			labels[i].addMouseListener(listener);
		}
		
		labels[DEST_STATE].setBackground(Color.green);
		labels[START_STATE].setBackground(Color.red);
		
		frame.add(boardPanel);
		frame.add(infoPanel);
		frame.add(qPanel);
		frame.add(lPanel);
		
		JButton start = new JButton("Start");
		JButton trainAgent = new JButton("Train");
		//JTextField numberOfTrain = new JTextField("Sayý giriniz");
		infoPanel.add(start);
		infoPanel.add(trainAgent);
		//infoPanel.add(numberOfTrain);
		
		trainAgent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for(int i=0;i<NUMBER_OF_ROW;i++)
					for(int j=0;j<NUMBER_OF_ROW;j++)
						board[i][j]=0;
				
				Random r = new Random();
				
				
				for(int i =0 ;i < ROWROW ; i++ ) {
					
					if(labels[i].getBackground().equals(Color.BLUE))
						LOAD_STATE=i;
				}
				
				for(int i=0 ; i<NUMBER_OF_STATE ; i++) {//initiliaze R
					for(int j=0; j<NUMBER_OF_STATE ; j++) {
						R[i][j]=-1;
					}
				}
				fillR();
				
				while(episode<10000) {
					count=0;
					state=START_STATE;
					int rand;
					while(state!=GOAL_STATE) {
						
						double random = r.nextDouble();
						int control=-1;
						if(random<epsilon) {
							do{
								rand = r.nextInt(5);
								if(rand==LEFT) {
									control=state-1;
								}
								if(rand==RIGHT) {
									control=state+1;
								}
								if(rand==UP) {
									control=state-NUMBER_OF_ROW;
								}
								if(rand==DOWN) {
									control=state+NUMBER_OF_ROW;
								}
								if(rand==LOAD) {
									control=state;
								}
							}while(!(control<=NUMBER_OF_STATE-1 && control >=0 && (R[state][control]!=-1 || rand==LOAD)));
							
							state=Q(state,rand);
							count++;			
									
						}
						else {
							List<Integer> choose = new ArrayList<Integer>();
							int number =-1;
							double max =-100000000;
							control=state-1;
							if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
								if(Q[state][control]>max){
									max=Q[state][control];
									choose.add(LEFT);
									number=1;
								}
									
							}
							control=state+1;
							if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
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
							control=state-NUMBER_OF_ROW;
							if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
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
							control=state+NUMBER_OF_ROW;
							if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
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
							
							if(state==LOAD_STATE)
								control=state+ROWROW;
							else
								control=state;
							
							if(control<=NUMBER_OF_STATE-1 && control >=0 && (R[state][control]!=-1 || control==state)){
								if(Q[state][control]>max){
									max=Q[state][control];
									number=1;
									choose.clear();
									choose.add(LOAD);
								}
								else if(Q[state][control]==max){
									choose.add(number,LOAD);
									number++;
								}						
							}
							
							
							
							
							int selection =r.nextInt(choose.size());
							state=Q(state,choose.get(selection));
							count++;																								
						}
						
					}
					episode++;
					
					
					epsilon=x-(double)(episode*x)/10000;						
				}
							
			}
			
		});
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for(int i=0;i<NUMBER_OF_ROW;i++)
					for(int j=0;j<NUMBER_OF_ROW;j++)
						board[i][j]=0;
				
				Random r = new Random();
				
				
				for(int i =0 ;i < ROWROW ; i++ ) {
					
					if(labels[i].getBackground().equals(Color.BLUE))
						LOAD_STATE=i;
				}
				
				for(int i=0 ; i<NUMBER_OF_STATE ; i++) {//initiliaze R
					for(int j=0; j<NUMBER_OF_STATE ; j++) {
						R[i][j]=-1;
					}
				}
				fillR();
				
				
				
				timer.start();
				timer2.start();
				
				
				
				
			}
		});
		
		
		
		
	}
	public static void fillR(){
		for(int i =0 ; i<NUMBER_OF_ROW;i++){
			for(int j =0 ; j<NUMBER_OF_ROW ;j++) {
				
				if(board[i][j]==0) {
					
					if(j>0){//sola bak
						if(board[i][j-1]== 0){
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*i+j-1]=p;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*i+j-1+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*i+j-1]=-1;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*i+j-1+ROWROW]=-1;
						}
							
					}
					
					if(j<NUMBER_OF_ROW-1) {//saga bak
						if(board[i][j+1]== 0){
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*i+j+1]=p;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*i+j+1+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*i+j+1]=-1;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*i+j+1+ROWROW]=-1;
						}
							
					}
					
					if(i>0) {//yukarý bak
						if(board[i-1][j]== 0){
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*(i-1)+j]=p;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*(i-1)+j+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*(i-1)+j]=-1;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*(i-1)+j+ROWROW]=-1;
						}
							
					}
					
					if(i<NUMBER_OF_ROW-1) {//asagý bak
						if(board[i+1][j]== 0){
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*(i+1)+j]=p;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*(i+1)+j+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*(i+1)+j]=-1;
							R[NUMBER_OF_ROW*i+j+ROWROW][NUMBER_OF_ROW*(i+1)+j+ROWROW]=-1;
						}
							
					}
					
					if(i*NUMBER_OF_ROW+j == LOAD_STATE){
						R[NUMBER_OF_ROW*i+j][NUMBER_OF_ROW*i+j+ROWROW]=p;
						
					}
						
					
					
					
				}
			}
		}
		
		for(int i =0 ; i< NUMBER_OF_STATE ; i++) {//reward 
			if(R[i][GOAL_STATE]==p)
				R[i][GOAL_STATE]=10000;
		}
	}
	
	public static int Q(int state , int action){
		int newstate;
		int control;
		double max=-10000;
		if(action==LEFT)
			newstate= state-1;
		else if(action==RIGHT)
			newstate= state+1;
		else if(action==UP)
			newstate= state-NUMBER_OF_ROW;
		else if(action==LOAD ){
			if(state==LOAD_STATE) {
				newstate=state+ROWROW;
				loaded=true;
			}	
			else
				return state;	
		}
			
		else 
			newstate= state+NUMBER_OF_ROW;
		
		if(newstate==LOAD_STATE){
			control=newstate+ROWROW;
			if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
				if(Q[newstate][control]>max){
					max=Q[newstate][control];
				}
			}
		}

		control=newstate-1;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+1;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate-NUMBER_OF_ROW;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+NUMBER_OF_ROW;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		Q[state][newstate]= (Q[state][newstate] +alpha*(R[state][newstate] + gamma*max-Q[state][newstate]));
		
		return newstate;	
	}
	
	public static void fillTables() {
		
		for(int i =0 ; i< NUMBER_OF_STATE ; i++) {
			for(int j =0 ; j< NUMBER_OF_STATE ; j++) {
				double max=0;
				if(Q[i][j]!=0) {
					for(int b=0;b<NUMBER_OF_STATE;b++)
						if(Q[i][b]>max)
							max=Q[i][b];
					if(i<ROWROW) {
						int r =map(max);
						int colorMap=colorMap(r);
						qPanels[i].setR(r);
						qPanels[i].setColor(new Color(colorMap, colorMap, colorMap));
						qPanels[i].repaint();
						
					}	
					
					
					else {
						int r =map(max);
						int colorMap=colorMap(r);
						lPanels[i-ROWROW].setR(r);
						lPanels[i-ROWROW].setColor(new Color(colorMap, colorMap, colorMap));
						lPanels[i-ROWROW].repaint();
						
						
						
					}
					
					
					
					break;
					
				}
			}
		}
	}
	
	public static int map(double max) {
		return (int)(10*max/10000);
	}
	
	public static int colorMap(int r) {
		return (int)(240-r*200/10);
	}
	
	public static void drawCenteredCircle(Graphics g, int x, int y, int r) {
		  x = x-(r/2);
		  y = y-(r/2);
		  g.fillOval(x,y,r,r);
	}
	
	
}



