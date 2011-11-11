package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//a class that represents a goto statement
public class HCISRGoToCallAST extends HCISRStatementAST{
	//what is the destination
	protected String locationID;
	
	//a more concrete description (the statement in question and the location in its array, unroll loops/compound statements until you find the statement)
	protected HCISRStatementAST toGoTo;
	protected int statementIndex;
	
	//goto does not make new classes
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//shallow copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRGoToCallAST(locationID);
	}
	
	public HCISRGoToCallAST(String locationIdentifier){
		locationID = locationIdentifier;
	}
}