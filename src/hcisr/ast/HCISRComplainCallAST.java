package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a complain/throw call in a program
public class HCISRComplainCallAST extends HCISRStatementAST{
	//the name of the given error
	protected String errorID;
	
	//directions on where to find the error to be thrown
	protected boolean stackVar;
	protected int arrayIndex;
	
	//does not introduce a new type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//just make a copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRComplainCallAST(errorID);
	}
	
	public HCISRComplainCallAST(String errorIdentifier){
		errorID = errorIdentifier;
	}
}