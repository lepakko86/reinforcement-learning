import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;





public class Softmax {
	private static final int NUMBER_OF_ROW_COLUMN=5;
	private static final int NUMBER_OF_STATE=NUMBER_OF_ROW_COLUMN*NUMBER_OF_ROW_COLUMN;
	private static final int DESTINATION_STATE=NUMBER_OF_STATE-1;
	private static final int LEFT = 0 ;
	private static final int RIGHT = 1 ;
	private static final int UP = 2 ;
	private static final int DOWN = 3 ;
	private static final int UPLEFT=4;
	private static final int UPRIGHT=5;
	private static final int DOWNLEFT=6;
	private static final int DOWNRIGHT=7;
	private static int[][] board = new int [NUMBER_OF_ROW_COLUMN][NUMBER_OF_ROW_COLUMN];
	private static int[][] R = new int[NUMBER_OF_STATE][NUMBER_OF_STATE];
	private static double[][] Q = new double[NUMBER_OF_STATE][NUMBER_OF_STATE];
	private static final double gamma=0.95;
	private static final double alpha=0.1;
	private static double epsilon=0.05;
	private static double x=0.05; 
		
	static List<Double> actions = new ArrayList<Double>();
	static int episode =0;
	static double tol=0.0005;
	
	public static void main(String args[]) throws IOException {
		int count;
		Random r = new Random();
		
		int state;
		fillBoard();
		initialize();
		fillR();
		//print(R);
		//System.out.println("Q ");
		//print(Q);
		
		File fout = new File("solution.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		
		
		
		while(episode<10000000 && !(isConverged())) {
			count=0;
			state=0;
			int rand;
			while(state!=DESTINATION_STATE) {
				
				double random = r.nextDouble();
				int control=-1;
				if(random<epsilon) {
					do{
						rand = r.nextInt(8);
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
						if(rand==UPLEFT) {
							control=state-NUMBER_OF_ROW_COLUMN-1;
						}
						if(rand==UPRIGHT) {
							control=state-NUMBER_OF_ROW_COLUMN+1;
						}
						if(rand==DOWNLEFT) {
							control=state+NUMBER_OF_ROW_COLUMN-1;
						}
						if(rand==DOWNRIGHT) {
							control=state+NUMBER_OF_ROW_COLUMN+1;
						}
					}while(!(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1));
					
					state=Q(state,rand);
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
					
					control=state-NUMBER_OF_ROW_COLUMN-1;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(UPLEFT);
						}
						else if(Q[state][control]==max){
							choose.add(number,UPLEFT);
							number++;
						}
							
					}
					
					control=state-NUMBER_OF_ROW_COLUMN+1;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(UPRIGHT);
						}
						else if(Q[state][control]==max){
							choose.add(number,UPRIGHT);
							number++;
						}
							
					}
					
					control=state+NUMBER_OF_ROW_COLUMN-1;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(DOWNLEFT);
						}
						else if(Q[state][control]==max){
							choose.add(number,DOWNLEFT);
							number++;
						}
							
					}
					
					control=state+NUMBER_OF_ROW_COLUMN+1;
					if(control<=NUMBER_OF_STATE-1 && control >=0 && R[state][control]!=-1){
						if(Q[state][control]>max){
							max=Q[state][control];
							number=1;
							choose.clear();
							choose.add(DOWNRIGHT);
						}
						else if(Q[state][control]==max){
							choose.add(number,DOWNRIGHT);
							number++;
						}
							
					}
					
					int selection =r.nextInt(choose.size());
					state=Q(state,choose.get(selection));
					count++;
					
					
					
					
					
				}
				
			}
			bw.write("Episode "+episode + " Actions "+count);
			bw.newLine();
			System.out.println("Episode "+episode + " Actions "+count);
			actions.add((double)count);
			episode++;
			
			while (epsilon > 0.005) epsilon=epsilon-(double)(episode*epsilon)/1000;
			
			
			
			
			
		}
		bw.close();
		
		
		
	}
	
	public static boolean isConverged() {
		if(episode<10)
			return false;
		
		double mean=0;
		int length=actions.size();
		
		
		for(int i =length-10 ; i<length;i++)
			mean +=actions.get(i);
		mean=mean/10;
		
		
		double variance=0;
		
		for(int i =length-10 ; i<length;i++)
			variance +=Math.pow((actions.get(i)-mean), 2);
		variance=variance/10;
		
		
		
		if(variance<tol)
			return true;
		else
			return false;
			
		
	}
	
	public static void fillR(){
		for(int i =0 ; i<NUMBER_OF_ROW_COLUMN;i++){
			for(int j =0 ; j<NUMBER_OF_ROW_COLUMN ;j++) {
				
				if(board[i][j]==0) {
					
					if(j>0){//sola bak
						if(board[i][j-1]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j-1]=0;
						else 
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j-1]=-1;
					}
					
					if(j<NUMBER_OF_ROW_COLUMN-1) {//saga bak
						if(board[i][j+1]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j+1]=0;
						else 
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*i+j+1]=-1;
					}
					
					if(i>0) {//yukar� bak
						if(board[i-1][j]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j]=0;
						else 
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j]=-1;
					}
					
					if(i<NUMBER_OF_ROW_COLUMN-1) {//asag� bak
						if(board[i+1][j]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j]=0;
						else 
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j]=-1;
					}
					
					if(j>0 && i>0) {//sol yukar�
						if(board[i][j-1]== 0 && board[i-1][j]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j-1]=0;
						else
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j-1]=-1;
					}
					
					if(j>0 && i<NUMBER_OF_ROW_COLUMN-1) {//sol asag�
						if(board[i][j-1]== 0 && board[i+1][j]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j-1]=0;
						else
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j-1]=-1;
					}
					
					if(j<NUMBER_OF_ROW_COLUMN-1 && i>0) { //sag yukar�
						if(board[i][j+1]== 0 && board[i-1][j]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j+1]=0;
						else
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i-1)+j+1]=-1;
					}
					
					if(j<NUMBER_OF_ROW_COLUMN-1 && i<NUMBER_OF_ROW_COLUMN-1 ) { //sag asag�
						if(board[i][j+1]== 0 && board[i+1][j]== 0)
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j+1]=0;
						else
							R[NUMBER_OF_ROW_COLUMN*i+j][NUMBER_OF_ROW_COLUMN*(i+1)+j+1]=-1;
					}
					
					
				}
			}
		}
		
		for(int i =0 ; i< NUMBER_OF_STATE ; i++) {//reward 
			if(R[i][DESTINATION_STATE]==0)
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
	
	public static void fillBoard() {
		
		for(int i=0;i<NUMBER_OF_ROW_COLUMN;i++) {
			for(int j=0;j<NUMBER_OF_ROW_COLUMN;j++) {
				board[i][j]=0;
			}
		}
		
	}
	
	public static void print(int a[][]) {
		System.out.println();
		for(int i =0 ; i<NUMBER_OF_STATE ; i++) {
			for(int j =0 ; j<NUMBER_OF_STATE ; j++) {
				System.out.print(a[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public static int Q(int state , int action){
		int newstate;
		int control;
		double max=-10000;
		Random r =new Random();
		double random = r.nextDouble();
		
		
		if(action==LEFT){
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state-1;
				else if(random<0.9   )
					newstate=state-NUMBER_OF_ROW_COLUMN-1;
				else 
					newstate= state+NUMBER_OF_ROW_COLUMN-1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
			
		}
			
		else if(action==RIGHT){
			do{
				random=r.nextDouble();
				if(random<0.8)//right
					newstate= state+1;
				else if(random<0.9   )//yukar� sa�
					newstate=state-NUMBER_OF_ROW_COLUMN+1;
				else 
					newstate= state+NUMBER_OF_ROW_COLUMN+1;//asa�� sa�
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
		}
			
		else if(action==UP){
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state-NUMBER_OF_ROW_COLUMN;
				else if(random<0.9   )
					newstate=state-NUMBER_OF_ROW_COLUMN-1;
				else 
					newstate= state-NUMBER_OF_ROW_COLUMN+1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
		}
		
		else if(action==DOWN) {
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state+NUMBER_OF_ROW_COLUMN;
				else if(random<0.9   )
					newstate=state+NUMBER_OF_ROW_COLUMN-1;
				else 
					newstate= state+NUMBER_OF_ROW_COLUMN+1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
		}
			
		else if(action==UPLEFT) {
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state-NUMBER_OF_ROW_COLUMN-1;
				else if(random<0.9   )
					newstate=state-NUMBER_OF_ROW_COLUMN;
				else 
					newstate= state-1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
		}
		
		else if(action==UPRIGHT) {
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state-NUMBER_OF_ROW_COLUMN-1;
				else if(random<0.9   )
					newstate=state-NUMBER_OF_ROW_COLUMN;
				else 
					newstate= state+1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
		}
		
		else if(action==DOWNLEFT) {
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state+NUMBER_OF_ROW_COLUMN-1;
				else if(random<0.9   )
					newstate=state+NUMBER_OF_ROW_COLUMN;
				else 
					newstate= state-1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
		}
		
		else  {
			do{
				random=r.nextDouble();
				if(random<0.8)
					newstate= state+NUMBER_OF_ROW_COLUMN+1;
				else if(random<0.9   )
					newstate=state+NUMBER_OF_ROW_COLUMN;
				else 
					newstate= state+1;
				
			}while(!(newstate<=NUMBER_OF_STATE-1 && newstate >=0 && R[state][newstate]!=-1));
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
		
		control=newstate-NUMBER_OF_ROW_COLUMN-1;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate-NUMBER_OF_ROW_COLUMN+1;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+NUMBER_OF_ROW_COLUMN-1;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		control=newstate+NUMBER_OF_ROW_COLUMN+1;
		if(control<=NUMBER_OF_STATE-1 && control >=0 && R[newstate][control]!=-1){
			if(Q[newstate][control]>max){
				max=Q[newstate][control];
			}
		}
		
		Q[state][newstate]= (Q[state][newstate] +alpha*(R[state][newstate] + gamma*max-Q[state][newstate]));
		
		return newstate;
		
		
		
		
			
		

	}
	
	
}
