package hcisr.ast;

//a class that represents a goto statement
public class HCISRGoToCallAST extends HCISRStatementAST{
	protected String locationID;
	
	public HCISRGoToCallAST(String locationIdentifier){
		locationID = locationIdentifier;
	}
}