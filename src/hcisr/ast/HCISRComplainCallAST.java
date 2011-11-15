package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a complain/throw call in a program
public class HCISRComplainCallAST extends HCISRStatementAST{
	//the name of the given error
	protected String errorID;
	
	//directions on where to find the error to be thrown
	protected boolean specialVar; //if the variable is special, look for it in the heap data, otherwise, stack
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
	
	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	//figure out who to throw
	public void compileReferences(HashMap<String,HCISRFileAST> imports, Scope currentScope, int line) {
		VariableLocationDescription loc = currentScope.findVariable(errorID);
		specialVar = loc.special;
		arrayIndex = loc.location;
	}
}