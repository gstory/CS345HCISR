package hcisr.ast;

//this class represents a set statement using a function
public class HCISRSetCallFunctionAST extends HCISRStatementAST{
	protected String varID;
	protected HCISRFunctionCallAST funcID;
	
	public HCISRSetCallFunctionAST(String variableID, HCISRFunctionCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
}