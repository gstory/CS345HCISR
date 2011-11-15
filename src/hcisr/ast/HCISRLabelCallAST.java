package hcisr.ast;

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
	
	public HCISRLabelCallAST(String labelName){
		name = labelName;
	}
}