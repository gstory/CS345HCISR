package hcisr.ast;

//this class represents a set command using a method
public class HCISRSetCallMethodAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRMethodCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean stackVar;
	//what is its index
	protected int arrayLocation;
	
	public HCISRSetCallMethodAST(String variableID, HCISRMethodCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
}