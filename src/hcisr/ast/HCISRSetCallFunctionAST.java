package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a set statement using a function
public class HCISRSetCallFunctionAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRFunctionCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean stackVar;
	//what is its index
	protected int arrayLocation;
	
	//set does not, of itself, create new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		funcID.compileTemplates(imports, newClasses);
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRSetCallFunctionAST(this, bindings);
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