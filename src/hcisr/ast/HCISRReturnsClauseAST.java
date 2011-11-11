package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a return clause
public class HCISRReturnsClauseAST{
	protected String retVar;
	protected String[] retType;
	
	//check the return type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, retType);
	}
	
	//deep copy for a template
	public HCISRReturnsClauseAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRReturnsClauseAST(this,bindings);
	}
	
	public HCISRReturnsClauseAST(String returnVariable,String[] returnType){
		retVar = returnVariable;
		retType = returnType;
	}
	
	public HCISRReturnsClauseAST(HCISRReturnsClauseAST origin, HashMap<String,String[]> bindings){
		retVar = origin.retVar;
		retType = HCISRClassAST.replaceTypeNames(origin.retType, bindings);
	}
}