package hcisr.ast;

import hcisr.*;

import java.util.*;

//this class represents a filled in method
public class HCISRDefinedMethodAST extends HCISRMethodAST{
	protected HCISRCodeBlockAST code;
	
	protected boolean referenceCompiled;
	
	//look for new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		for(String[] tpnm : typeRestrictions){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
		code.compileTemplates(imports, newClasses);
	}
	
	//create a deep copy with new types
	public HCISRMethodAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRDefinedMethodAST(this, bindings);
	}
	
	//anything that this calls, find it
	//this has return type, and a bunch of statements to compile
	public void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> classVars,HCISRClassAST callingClass){
		if(referenceCompiled){
			return;
		}
		referenceCompiled = true;
		//create a copy of the variables, and add in any arguments
		HashMap<String,VariableLocationDescription> methodVars = new HashMap<String,VariableLocationDescription>();
		methodVars.putAll(classVars);
		int curLoc = 0;
		//add in the calling variable
		methodVars.put(instName, new VariableLocationDescription(false,curLoc,callingClass.typeName));
		curLoc++;
		//and then add the rest
		for(int i = 1;i<sig.length;i++){
			//run through the signature looking for arguments (skip calling variable)
			if(HCISRFileAST.isIdentifier(sig[i])){
				methodVars.put(sig[i], new VariableLocationDescription(false,curLoc,typeRestrictions[i]));
				curLoc += 1;
			}
		}
		
		//now, with the new method vars, compile the code block
		stackSize = code.compileReferences(imports, methodVars, curLoc);
	}
	
	public String[] getReturnType(){
		return code.toReturn.retType;
	}
	
	public HCISRInstance run(HCISRStackFrame sf) throws HCISRException {
		//get the instance at location 0 (it is the heap location)
		HCISRInstance calling = sf.getLocation(0);
		//and run the code
		return code.run(sf, calling);
	}
	
	public HCISRDefinedMethodAST(String instanceName,String[] methodSignature, String[][] parameterRestrictions, HCISRCodeBlockAST commands){
		instName = instanceName;
		sig = methodSignature;
		code = commands;
		typeRestrictions = parameterRestrictions;
		isDefined = true;
	}
	
	public HCISRDefinedMethodAST(HCISRDefinedMethodAST origin, HashMap<String,String[]> bindings){
		//the instance name and signature do not change
		instName = origin.instName;
		sig = origin.sig;
		
		//but the type restrictions and return type do
		typeRestrictions = HCISRClassAST.replaceTypeRestrictions(origin.typeRestrictions, bindings);
		code = origin.code.copyWithParameters(bindings);
		
		isDefined = true;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting DefineMethodAST\n";
		tabCount++;
		//Add sub information here
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending DefineMethodAST\n";
		return result;
	}
}
