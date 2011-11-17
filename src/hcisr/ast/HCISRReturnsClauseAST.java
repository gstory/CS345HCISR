package hcisr.ast;

import hcisr.HCISRHeapLocation;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a return clause
public class HCISRReturnsClauseAST{
	protected String retVar;
	protected String[] retType;
	
	//return variable location
	protected boolean retNull = false;
	protected int arrayIndex;
	protected boolean specialVar;
	
	//check the return type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		//skip Nothing
		if(retType.length==1){
			if(retType[0].equals("Nothing")){
				return;
			}
		}
		HCISRFileAST.checkForTemplateClass(imports, newClasses, retType);
	}
	
	//deep copy for a template
	public HCISRReturnsClauseAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRReturnsClauseAST(this,bindings);
	}
	
	//find out where everything is
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope){
		if(retType.length==1){
			if(retType[0].equals("Nothing")){
				retNull = true;
				return;
			}
		}
		VariableLocationDescription var = currentScope.findVariable(retVar);
		arrayIndex = var.location;
		specialVar = var.special;
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		if(retNull){
			return null;
		}
		if(specialVar){
			return hl.getLocation(arrayIndex);
		}
		else{
			return sf.getLocation(arrayIndex);
		}
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