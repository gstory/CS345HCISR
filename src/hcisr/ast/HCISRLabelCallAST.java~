package hcisr.ast;

import hcisr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a target for a goto statement
public class HCISRLabelCallAST extends HCISRStatementAST{
	protected String name;
	
	//no new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//shallow copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRLabelCallAST(name);
	}
	
	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	//add a label to the scope
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		currentScope.addLabel(name, line, this);
	}
	
	//this code does nothing
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		return null;
	}
	
	public HCISRLabelCallAST(String labelName){
		name = labelName;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting LabelCallAST with label name: "+name+"\n";
		tabCount++;
		//Add sub information here
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending LabelCallAST\n";
		return result;
	}
}
