package hcisr.ast;

//this class represents a set command using a method
public class HCISRSetCallMethodAST extends HCISRStatementAST{
	protected String varID;
	protected HCISRMethodCallAST funcID;
	
	public HCISRSetCallMethodAST(String variableID, HCISRMethodCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
}