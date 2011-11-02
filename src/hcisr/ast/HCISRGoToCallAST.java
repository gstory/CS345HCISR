package hcisr.ast;

//a class that represents a goto statement
public class HCISRGoToCallAST extends HCISRStatementAST{
	//what is the destination
	protected String locationID;
	
	//a more concrete description (the statement in question and the location in its array, unroll loops/compound statements until you find the statement)
	protected HCISRStatementAST toGoTo;
	protected int statementIndex;
	
	public HCISRGoToCallAST(String locationIdentifier){
		locationID = locationIdentifier;
	}
}