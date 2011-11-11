package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a set command using a method
public class HCISRSetCallMethodAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRMethodCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean stackVar;
	//what is its index
	protected int arrayLocation;
	
	// does not create new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		funcID.compileTemplates(imports, newClasses);
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRSetCallMethodAST(this, bindings);
	}
	
	public HCISRSetCallMethodAST(String variableID, HCISRMethodCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
	
	public HCISRSetCallMethodAST(HCISRSetCallMethodAST origin, HashMap<String,String[]> bindings){
		varID = origin.varID;
		funcID = (HCISRMethodCallAST)(origin.funcID.copyWithParameters(bindings));
	}
}