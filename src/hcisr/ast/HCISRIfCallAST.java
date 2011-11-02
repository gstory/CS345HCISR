package hcisr.ast;

//this class represents an if statement
public class HCISRIfCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] trueComs;
	protected HCISRStatementAST[] falseComs;
	
	//where to find the boolean
	protected boolean stackVar;
	protected int arrayIndex;
	
	public HCISRIfCallAST(String booleanID, HCISRStatementAST[] trueCommands, HCISRStatementAST[] falseCommands){
		boolID = booleanID;
		trueComs = trueCommands;
		falseComs = falseCommands;
	}
}