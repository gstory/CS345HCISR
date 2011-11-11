package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a method call
public class HCISRMethodCallAST extends HCISRStatementAST{
	//the method signature
	protected String[] argumentIDs;
	
	//where inside the class for the calling object is the definition of the method
	protected int methodIndex;
	//a description of where to find the arguments for the method (starting with the calling instance)
	protected boolean[] stackVar;
	protected int[] arrayIndex;
	
	//methods do not introduce new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//just do a shallow copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRMethodCallAST(argumentIDs);
	}
	
	public HCISRMethodCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}