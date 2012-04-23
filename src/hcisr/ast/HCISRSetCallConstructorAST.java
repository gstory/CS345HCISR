package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRHeapLocation;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

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
	
	public HCISRSetCallConstructorAST(String variableID, HCISRConstructorCallAST functionID){
		varID = variableID;
		funcID = functionID;
	}
	
	public HCISRSetCallConstructorAST(HCISRSetCallConstructorAST origin, HashMap<String,String[]> bindings){
		varID = origin.varID;
		funcID = (HCISRConstructorCallAST)(origin.funcID.copyWithParameters(bindings));
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting SetCallConstructorAST\n";
		tabCount++;
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Variable being set: "+varID+"\n";
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Value its being set to:\n";
			tabCount++;
				result+=funcID.toString(tabCount);
			tabCount--;
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending SetCallConstructorAST\n";
		return result;
	}
}
