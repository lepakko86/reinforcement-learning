import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Comparator;

public class PrioritizedSweeping {
	private static final int NUMBER_OF_ROW_COLUMN=10;
	private static final int ROWROW = NUMBER_OF_ROW_COLUMN*NUMBER_OF_ROW_COLUMN;
	private static final int LEFT = 0 ;
	private static final int RIGHT = 1 ;
	private static final int UP = 2 ;
	private static final int DOWN = 3 ;
	private static final int LOAD=4 ;
	private static final double gamma=0.95;
	private static final double alpha=0.1;
	private static final double theta=0;
	private static double epsilon=0.3;
	private static double x=0.3;
	private static double p=0;
	private static int[][] board = new int [NUMBER_OF_ROW_COLUMN][NUMBER_OF_ROW_COLUMN];
	private static final int NUMBER_OF_STATE=ROWROW*2;
	private static final int START_STATE=0;
	private static final int DESTINATION_STATE=199;
	private static final int LOAD_STATE=9;
	private static double[][] R = new double[NUMBER_OF_STATE][NUMBER_OF_STATE];
	private static double[][] Q = new double[NUMBER_OF_STATE][NUMBER_OF_STATE];
	private static State[] Model = new State[NUMBER_OF_STATE];
	private static double[][] stateAction = new double[NUMBER_OF_STATE][5];
	static int N=4;
	
	static List<Double> actions = new ArrayList<Double>();
	static PriorityQueue<pState> pQueue = new PriorityQueue<pState>(10000,new Comparator<pState>() {
		public int compare(pState a,pState b) {
			return Double.compare(a.getP(), b.getP());
		}
	});

	
	
	static int episode =0;
	static double tol=0.001;
	
	public static void main(String args[]) throws IOException {
		int count;
		Random r = new Random();
		
		int state;
		fillBoard();
		initialize();
		fillR();
		
		for(int i=0;i<NUMBER_OF_STATE;i++) {
			Model[i]=new State();
		}
		
		//print(R);
		//System.out.println("Q ");
		//print(Q);
		File fout = new File("solution.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));	
			
		while(episode<300 && !(isConverged())) {
			count=0;
			state=START_STATE;
			int rand;
			while(state!=DESTINATION_STATE) {
				
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
							control=state-NUMBER_OF_ROW_COLUMN;
						}
						if(rand==DOWN) {
							control=state+NUMBER_OF_ROW_COLUMN;
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
					control=state-NUMBER_OF_ROW_COLUMN;
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
					control=state+NUMBER_OF_ROW_COLUMN;
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
						control=state+NUMBER_OF_ROW_COLUMN*NUMBER_OF_ROW_COLUMN;
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
		//	bw.write("Episode "+episode + " Actions "+count);
		//	bw.newLine();
			System.out.println("Episode "+episode + " Actions "+count);
			actions.add((double)count);
			episode++;
			
			
			epsilon=x-(double)(episode*x)/10000;
			
			do { 
				pState node = pQueue.remove();
				int sstate=node.getState();
				int aaction=node.getAction();
				int sprime=0;
				double reward;
				
				if(aaction == LEFT) {
					sprime=Model[sstate].getNewStateLeft();
					reward=Model[sstate].getLeftActionReward();
				}
				else if(aaction == RIGHT) {
					sprime=Model[sstate].getNewStateRight();
					reward=Model[sstate].getRightActionReward();
				}
				else if(aaction == DOWN) {
					sprime=Model[sstate].getNewStateDown();
					reward=Model[sstate].getDownActionReward();
				}
				else if(aaction == UP) {
					sprime=Model[sstate].getNewStateUp();
					reward=Model[sstate].getUpActionReward();
				}
				else if(aaction == LOAD) {
					sprime=Model[sstate].getNewStateLoad();
					reward=Model[sstate].getLoadActionReward();
				}
				
				for(int i=0;i<NUMBER_OF_STATE;i++) {
					int sdprime,actiondprime;
					double rewarddoubleprime;
					if(Model[i].getNewStateDown()==sstate) {
						sdprime=sstate-NUMBER_OF_ROW_COLUMN;
						actiondprime=DOWN;
						rewarddoubleprime=Model[i].getDownActionReward();
						p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
						if(p>theta) {
							pQueue.add(new pState(sdprime,actiondprime,p));
						}
					}
					if(Model[i].getNewStateLeft()==sstate) {
						sdprime=sstate+1;
						actiondprime=LEFT;
						rewarddoubleprime=Model[i].getLeftActionReward();
						p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
						if(p>theta) {
							pQueue.add(new pState(sdprime,actiondprime,p));
						}
					}
					if(Model[i].getNewStateRight()==sstate) {
						sdprime=sstate-1;
						actiondprime=RIGHT;
						rewarddoubleprime=Model[i].getRightActionReward();
						p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
						if(p>theta) {
							pQueue.add(new pState(sdprime,actiondprime,p));
						}
					}
					if(Model[i].getNewStateUp()==sstate) {
						sdprime=sstate+NUMBER_OF_ROW_COLUMN;
						actiondprime=UP;
						rewarddoubleprime=Model[i].getUpActionReward();
						p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
						if(p>theta) {
							pQueue.add(new pState(sdprime,actiondprime,p));
						}
					}
					if(Model[i].getNewStateLoad()==sstate) {
						sdprime=sstate-ROWROW;
						actiondprime=LOAD;
						rewarddoubleprime=Model[i].getLoadActionReward();
						p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
						if(p>theta) {
							pQueue.add(new pState(sdprime,actiondprime,p));
						}
						
					}
				}
				
			}while(!pQueue.isEmpty() && count<N);
		
		//System.out.println(Model[198].getRightActionReward());
		//print(Q);
		bw.close();		
	}
	}
	
	
	public static void fillR(){
		for(int i =0 ; i<NUMBER_OF_ROW_COLUMN;i++){
			for(int j =0 ; j<NUMBER_OF_ROW_COLUMN ;j++) {
				
				if(board[i][j]==0) {
					
					if(j>0){//sola bak
						if(board[i][j-1]== 0){
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j-1]=p;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*i+j-1+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j-1]=-1;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*i+j-1+ROWROW]=-1;
						}
							
					}
					
					if(j<NUMBER_OF_ROW_COLUMN-1) {//saga bak
						if(board[i][j+1]== 0){
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j+1]=p;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*i+j+1+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j+1]=-1;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*i+j+1+ROWROW]=-1;
						}
							
					}
					
					if(i>0) {//yukarý bak
						if(board[i-1][j]== 0){
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j]=p;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*(i-1)+j+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j]=-1;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*(i-1)+j+ROWROW]=-1;
						}
							
					}
					
					if(i<NUMBER_OF_ROW_COLUMN-1) {//asagý bak
						if(board[i+1][j]== 0){
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j]=p;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*(i+1)+j+ROWROW]=p;
						}
							
						else {
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j]=-1;
							R[NUMBER_OF_ROW_COLUMN*i+j+ROWROW][NUMBER_OF_ROW_COLUMN*(i+1)+j+ROWROW]=-1;
						}
							
					}
					
					if(i*NUMBER_OF_ROW_COLUMN+j == LOAD_STATE){
						R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j+ROWROW]=p;
						
					}
						
					
					
					
				}
			}
		}
		
		for(int i =0 ; i< NUMBER_OF_STATE ; i++) {//reward 
			if(R[i][DESTINATION_STATE]==p)
				R[i][DESTINATION_STATE]=100;
		}
	}
	public static void initialize () {
		for(int i=0 ; i<NUMBER_OF_STATE ; i++) {
			for(int j=0; j<NUMBER_OF_STATE ; j++) {
				R[i][j]=-1;
			}
		}
	}
	
	public static boolean isConverged() {
		if(episode<15)
			return false;
		
		double mean=0;
		int length=actions.size();
		
		
		for(int i =length-15 ; i<length;i++)
			mean +=actions.get(i);
		mean=mean/15;
		
		
		double variance=0;
		
		for(int i =length-15 ; i<length;i++)
			variance +=Math.pow((actions.get(i)-mean), 2);
		variance=variance/15;
		
		
		
		if(variance<tol)
			return true;
		else
			return false;
			
		
	}
	
	public static void fillBoard() {
		
		for(int i=0;i<NUMBER_OF_ROW_COLUMN;i++) {
			for(int j=0;j<NUMBER_OF_ROW_COLUMN;j++) {
				board[i][j]=0;
			}
		}
	}
	
	public static int Q(int state , int action){
		int newstate;
		int control;
		int count=0;
		double p;
		double max=-10000;
		if(action==LEFT){
			newstate= state-1;
			Model[state].setNewStateLeft(newstate);
			Model[state].setLeftActionReward(R[state][newstate]);		
		}
			
		else if(action==RIGHT){
			newstate= state+1;
			Model[state].setNewStateRight(newstate);
			Model[state].setRightActionReward(R[state][newstate]);
		}
			
		else if(action==UP){
			newstate= state-NUMBER_OF_ROW_COLUMN;
			Model[state].setNewStateUp(newstate);
			Model[state].setUpActionReward(R[state][newstate]);
		}

			
		else if(action==LOAD ){
			if(state==LOAD_STATE){
				newstate=state+NUMBER_OF_ROW_COLUMN*NUMBER_OF_ROW_COLUMN;
				Model[state].setNewStateLoad(newstate);
				Model[state].setLoadActionReward(R[state][newstate]);
			}
				
			else
				return state;	
		}
			
		else {
			newstate= state+NUMBER_OF_ROW_COLUMN;
			Model[state].setNewStateDown(newstate);
			Model[state].setDownActionReward(R[state][newstate]);
		}
			
		
		if(newstate==LOAD_STATE){
			control=newstate+NUMBER_OF_ROW_COLUMN*NUMBER_OF_ROW_COLUMN;
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
		
		control=newstate-NUMBER_OF_ROW_COLUMN;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+NUMBER_OF_ROW_COLUMN;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		p=Math.abs(R[state][newstate] + gamma*max-Q[state][newstate]);
		
		if(p>theta) {
			stateAction[state][action]=p;
			pQueue.add(new pState(state, action, p));
		}
		
	/*	while(!pQueue.isEmpty() && count<N) {
			pState node = pQueue.remove();
			int sstate=node.getState();
			int aaction=node.getAction();
			int sprime=0;
			double reward;
			
			if(aaction == LEFT) {
				sprime=Model[sstate].getNewStateLeft();
				reward=Model[sstate].getLeftActionReward();
			}
			else if(aaction == RIGHT) {
				sprime=Model[sstate].getNewStateRight();
				reward=Model[sstate].getRightActionReward();
			}
			else if(aaction == DOWN) {
				sprime=Model[sstate].getNewStateDown();
				reward=Model[sstate].getDownActionReward();
			}
			else if(aaction == UP) {
				sprime=Model[sstate].getNewStateUp();
				reward=Model[sstate].getUpActionReward();
			}
			else if(aaction == LOAD) {
				sprime=Model[sstate].getNewStateLoad();
				reward=Model[sstate].getLoadActionReward();
			}*/
			
			Q[state][newstate]= (Q[state][newstate] +alpha*(R[state][newstate] + gamma*max(newstate)-Q[state][newstate]));
			
		/*	for(int i=0;i<NUMBER_OF_STATE;i++) {
				int sdprime,actiondprime;
				double rewarddoubleprime;
				if(Model[i].getNewStateDown()==sstate) {
					sdprime=sstate-NUMBER_OF_ROW_COLUMN;
					actiondprime=DOWN;
					rewarddoubleprime=Model[i].getDownActionReward();
					p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
					if(p>theta) {
						pQueue.add(new pState(sdprime,actiondprime,p));
					}
				}
				if(Model[i].getNewStateLeft()==sstate) {
					sdprime=sstate+1;
					actiondprime=LEFT;
					rewarddoubleprime=Model[i].getLeftActionReward();
					p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
					if(p>theta) {
						pQueue.add(new pState(sdprime,actiondprime,p));
					}
				}
				if(Model[i].getNewStateRight()==sstate) {
					sdprime=sstate-1;
					actiondprime=RIGHT;
					rewarddoubleprime=Model[i].getRightActionReward();
					p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
					if(p>theta) {
						pQueue.add(new pState(sdprime,actiondprime,p));
					}
				}
				if(Model[i].getNewStateUp()==sstate) {
					sdprime=sstate+NUMBER_OF_ROW_COLUMN;
					actiondprime=UP;
					rewarddoubleprime=Model[i].getUpActionReward();
					p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
					if(p>theta) {
						pQueue.add(new pState(sdprime,actiondprime,p));
					}
				}
				if(Model[i].getNewStateLoad()==sstate) {
					sdprime=sstate-ROWROW;
					actiondprime=LOAD;
					rewarddoubleprime=Model[i].getLoadActionReward();
					p=Math.abs(rewarddoubleprime + gamma*max(sstate)-Q[sdprime][sstate]);
					if(p>theta) {
						pQueue.add(new pState(sdprime,actiondprime,p));
					}
				}
			}
			
		//count++;	
		}*/
		
		
		
		return newstate;	
	}
	
	public static void print(double a[][]) {
		System.out.println();
		for(int i =0 ; i<NUMBER_OF_STATE ; i++) {
			for(int j =0 ; j<NUMBER_OF_STATE ; j++) {
				System.out.print(a[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public static double max(int newstate) {
		int control;
		double max=-1000;
		
		if(newstate==LOAD_STATE){
			control=newstate+NUMBER_OF_ROW_COLUMN*NUMBER_OF_ROW_COLUMN;
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
		
		control=newstate-NUMBER_OF_ROW_COLUMN;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+NUMBER_OF_ROW_COLUMN;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		return max;
	}
}

