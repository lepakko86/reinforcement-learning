
public class pState {
	int state,action;
	double p;
	
	public pState(int state,int action,double p) {
		this.state=state;
		this.action=action;
		this.p=p;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getState() {
		return state;
	}
	
	public double getP() {
		return p;
	}
}
