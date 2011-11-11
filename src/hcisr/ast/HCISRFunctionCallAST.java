package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a function call
public class HCISRFunctionCallAST extends HCISRStatementAST{
	//the function signature
	public String[] argumentIDs;
	
	//what is the function to call
	protected HCISRFunctionAST toCall;
	//a description of where to find the arguments for the function
	protected boolean[] stackVar;
	protected int[] arrayIndex;
	
	//function calls do not define new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//just make a new copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRFunctionCallAST(argumentIDs);
	}
	
	public HCISRFunctionCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}