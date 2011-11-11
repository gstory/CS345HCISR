package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public HCISRLabelCallAST(String labelName){
		name = labelName;
	}
}