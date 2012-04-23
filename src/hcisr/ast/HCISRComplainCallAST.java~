package hcisr.ast;

import hcisr.*;

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

	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	//figure out who to throw
	public void compileReferences(HashMap<String,HCISRFileAST> imports, Scope currentScope, int line) {
		VariableLocationDescription loc = currentScope.findVariable(errorID);
		specialVar = loc.special;
		arrayIndex = loc.location;
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException{
		//get the variable to throw
		HCISRInstance toThrow;
		if(specialVar){
			toThrow = hl.getLocation(arrayIndex);
		}
		else{
			toThrow = sf.getLocation(arrayIndex);
		}
		HCISRException toThrowJava = new HCISRException("User defined throw.",toThrow);
		throw toThrowJava;
	}
	
	public HCISRComplainCallAST(String errorIdentifier){
		errorID = errorIdentifier;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting ComplainCallAST\n";
		tabCount++;
		//Add sub information here
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending ComplainCallAST\n";
		return result;
	}
}
