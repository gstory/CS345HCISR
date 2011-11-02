package hcisr.ast;

//this class represents a while loop
public class HCISRWhileCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] loopComs;
	
	//where to find the boolean
	protected boolean stackVar;
	protected int arrayIndex;
	
	public HCISRWhileCallAST(String booleanID, HCISRStatementAST[] loopCommands){
		boolID = booleanID;
		loopComs = loopCommands;
	}
}