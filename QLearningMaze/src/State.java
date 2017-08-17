
public class State {
	private double leftActionReward;
	private double rightActionReward;
	private double upActionReward;
	private double downActionReward;
	private double loadActionReward;
	private int newStateLeft;
	private int newStateRight=-1;
	private int newStateUp=-1;
	private int newStateDown=-1;
	private int newStateLoad=-1;
	
	public State() {
		
	}
	
	public void setLeftActionReward(double leftActionReward) {
		this.leftActionReward = leftActionReward;
	}
	
	public void setDownActionReward(double downActionReward) {
		this.downActionReward = downActionReward;
	}
	
	public void setLoadActionReward(double loadActionReward) {
		this.loadActionReward = loadActionReward;
	}
	
	public void setRightActionReward(double rightActionReward) {
		this.rightActionReward = rightActionReward;
	}
	
	public void setUpActionReward(double upActionReward) {
		this.upActionReward = upActionReward;
	}
	
	public void setNewStateLeft(int newStateLeft) {
		this.newStateLeft = newStateLeft;
	}
	
	public void setNewStateDown(int newStateDown) {
		this.newStateDown = newStateDown;
	}
	
	public void setNewStateLoad(int newStateLoad) {
		this.newStateLoad = newStateLoad;
	}
	
	public void setNewStateRight(int newStateRight) {
		this.newStateRight = newStateRight;
	}
	
	public void setNewStateUp(int newStateUp) {
		this.newStateUp = newStateUp;
	}
	
	
	public double getDownActionReward() {
		return downActionReward;
	}
	
	public double getLeftActionReward() {
		return leftActionReward;
	}
	
	public double getLoadActionReward() {
		return loadActionReward;
	}
	
	public int getNewStateDown() {
		return newStateDown;
	}
	
	public int getNewStateLeft() {
		return newStateLeft;
	}
	
	public int getNewStateLoad() {
		return newStateLoad;
	}
	
	public int getNewStateRight() {
		return newStateRight;
	}
	
	public int getNewStateUp() {
		return newStateUp;
	}
	
	
	
	public double getRightActionReward() {
		return rightActionReward;
	}
	
	public double getUpActionReward() {
		return upActionReward;
	}
	
	
}
