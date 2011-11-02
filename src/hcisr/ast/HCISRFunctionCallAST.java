package hcisr.ast;

//this class represents a function call
public class HCISRFunctionCallAST extends HCISRStatementAST{
	//the function signature
	public String[] argumentIDs;
	
	//what is the function to call
	protected HCISRFunctionAST toCall;
	//a description of where to find the arguments for the function
	protected boolean[] stackVar;
	protected int[] arrayIndex;
	
	public HCISRFunctionCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}