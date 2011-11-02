package hcisr.ast;

//this class represents a while loop
public class HCISRWhileCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] loopComs;
	
	public HCISRWhileCallAST(String booleanID, HCISRStatementAST[] loopCommands){
		boolID = booleanID;
		loopComs = loopCommands;
	}
}