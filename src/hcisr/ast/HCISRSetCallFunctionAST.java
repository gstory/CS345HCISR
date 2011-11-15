package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a set statement using a function
public class HCISRSetCallFunctionAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRFunctionCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean specialVar;
	//what is its index
	protected int arrayLocation;
	
	//set does not, of itself, create new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		funcID.compileTemplates(imports, newClasses);
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRSetCallFunctionAST(this, bindings);
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
	
	public HCISRSetCallFunctionAST(String variableID, HCISRFunctionCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
	
	public HCISRSetCallFunctionAST(HCISRSetCallFunctionAST origin, HashMap<String,String[]> bindings){
		varID = origin.varID;
		funcID = (HCISRFunctionCallAST)(origin.funcID.copyWithParameters(bindings));
	}
}