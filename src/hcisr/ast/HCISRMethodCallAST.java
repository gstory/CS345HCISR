package hcisr.ast;

//this class represents a method call
public class HCISRMethodCallAST extends HCISRStatementAST{
	public String[] argumentIDs;
	
	public HCISRMethodCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}