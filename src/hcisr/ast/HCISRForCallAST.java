package hcisr.ast;

//this class represents a for loop
public class HCISRForCallAST extends HCISRStatementAST{
	protected String typeID;
	protected String varID;
	protected String listID;
	protected HCISRStatementAST[] loopComs;
	
	public HCISRForCallAST(String typeIndentifier, String variableIdentifier,String listIdentifier,HCISRStatementAST[] loopCommands){
		typeID = typeIndentifier;
		varID = variableIdentifier;
		listID = listIdentifier;
		loopComs = loopCommands;
	}
}