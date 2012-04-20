package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRHeapLocation;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a set command using a method
public class HCISRSetCallMethodAST extends HCISRStatementAST{
	//what variable needs setting
	protected String varID;
	//what value should it be set to
	protected HCISRMethodCallAST funcID;
	
	//these variables describe the location of the variable to be set
	//is it on the stack, or in an instance
	protected boolean specialVar;
	//what is its index
	protected int arrayLocation;
	
	// does not create new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		funcID.compileTemplates(imports, newClasses);
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRSetCallMethodAST(this, bindings);
	}
	
	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//compile the method call, and find which variable to set
		funcID.compileReferences(imports, currentScope, line);
		VariableLocationDescription toSet = currentScope.findVariable(varID);
		specialVar = toSet.special;
		arrayLocation = toSet.location;
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException{
		HCISRInstance newVal = funcID.run(sf, hl);
		if(specialVar){
			hl.setLocation(newVal, arrayLocation);
		}
		else{
			sf.setLocation(newVal, arrayLocation);
		}
		return null;
	}
	
	public HCISRSetCallMethodAST(String variableID, HCISRMethodCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
	
	public HCISRSetCallMethodAST(HCISRSetCallMethodAST origin, HashMap<String,String[]> bindings){
		varID = origin.varID;
		funcID = (HCISRMethodCallAST)(origin.funcID.copyWithParameters(bindings));
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting SetCallMethodAST\n";
		tabCount++;
			for(int i=0; i< tabCount;i++)
				result+="\t";
			result+="Recieving variable: "+varID+"\n";
			for(int i=0; i< tabCount;i++)
				result+="\t";
			result+="Set recieving variable to:\n";
			tabCount++;
				result+=funcID.toString(tabCount);
			tabCount--;
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending SetCallMethodAST\n";
		return result;
	}
}
