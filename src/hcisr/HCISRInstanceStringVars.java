package hcisr;

public class HCISRInstanceStringVars implements HCISRInstanceMoreVars{
	public String value;
	
	public HCISRInstanceStringVars(){
		
	}
	public HCISRInstanceStringVars(String val){
		value = val;
	}

	public String toString(){
		return value;
	}
}
