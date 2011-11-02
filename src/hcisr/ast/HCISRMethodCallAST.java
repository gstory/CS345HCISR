package hcisr.ast;

//this class represents a method call
public class HCISRMethodCallAST extends HCISRStatementAST{
	//the method signature
	protected String[] argumentIDs;
	
	//where inside the class for the calling object is the definition of the method
	protected int methodIndex;
	//a description of where to find the arguments for the method (starting with the calling instance)
	protected boolean[] stackVar;
	protected int[] arrayIndex;
	
	public HCISRMethodCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}