package hcisr;
import hcisr.ast.*;

//a repository for the data for an instance
public class HCISRInstance implements HCISRHeapLocation{
	protected HCISRInstance[] contents;
	protected HCISRClassAST type;
	protected HCISRInstanceMoreVars supplemental;
	
	public HCISRClassAST getHCISRClass(){
		return type;
	}
	
	public void addExternalVariables(HCISRInstanceMoreVars supplement){
		supplemental = supplement;
	}
	
	public HCISRInstanceMoreVars getExternalVariables(){
		return supplemental;
	}
	
	public HCISRInstance getLocation(int loc){
		return contents[loc];
	}
	
	public void setLocation(HCISRInstance toAdd, int loc){
		contents[loc] = toAdd;
	}
	
	public HCISRInstance(HCISRClassAST tp){
		type = tp;
		contents = new HCISRInstance[type.getNumberOfVariables()]; //initially, everything is nothing
	}
}