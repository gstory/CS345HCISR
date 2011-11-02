package hcisr.ast;

//this class represents a set statement using a function
public class HCISRSetCallFunctionAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRFunctionCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean stackVar;
	//what is its index
	protected int arrayLocation;
	
	public HCISRSetCallFunctionAST(String variableID, HCISRFunctionCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
}