package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a set statement using a function
public class HCISRSetCallConstructorAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRConstructorCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean specialVar;
	//what is its index
	protected int arrayLocation;
	
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		funcID.compileTemplates(imports, newClasses);
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRSetCallConstructorAST(this,bindings);
	}
	
	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//compile the function call, nothing more
		funcID.compileReferences(imports, currentScope, line);
		VariableLocationDescription toSet = currentScope.findVariable(varID);
		specialVar = toSet.special;
		arrayLocation = toSet.location;
	}
	
	public HCISRSetCallConstructorAST(String variableID, HCISRConstructorCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
	
	public HCISRSetCallConstructorAST(HCISRSetCallConstructorAST origin, HashMap<String,String[]> bindings){
		varID = origin.varID;
		funcID = (HCISRConstructorCallAST)(origin.funcID.copyWithParameters(bindings));
	}
}