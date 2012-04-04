package hcisr;

public class HCISRInstanceIntegerVars implements HCISRInstanceMoreVars{
	public int value;
	
	public HCISRInstanceIntegerVars(){
		
	}
	public HCISRInstanceIntegerVars(int val){
		value = val;
	}

	public String toString(){
		return ""+value;
	}
}
