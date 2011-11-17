package hcisr;

//a class that represents a stack frame
public class HCISRStackFrame{
	HCISRInstance[] contents;
	
	public HCISRStackFrame(int size){
		contents = new HCISRInstance[size];
	}
	
	public HCISRInstance getLocation(int loc){
		return contents[loc];
	}
	
	public void setLocation(HCISRInstance toAdd, int loc){
		contents[loc] = toAdd;
	}
}