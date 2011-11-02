package hcisr.ast;

//this class represents a function call
public class HCISRFunctionCallAST extends HCISRStatementAST{
	public String[] argumentIDs;
	
	public HCISRFunctionCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}