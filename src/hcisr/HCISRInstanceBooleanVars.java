package hcisr;

public class HCISRInstanceBooleanVars implements HCISRInstanceMoreVars{
	public boolean value;
	
	public HCISRInstanceBooleanVars(){
		
	}
	public HCISRInstanceBooleanVars(boolean val){
		value = val;
	}

	public String toString(){
		if( value )
			return "True";
		else
			return "False";
	}
}
