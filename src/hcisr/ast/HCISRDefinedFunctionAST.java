package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a filled in function
public class HCISRDefinedFunctionAST extends HCISRFunctionAST{
	protected HCISRCodeBlockAST code;
	
	//look for new types
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		code.compileTemplates(imports, newClasses);
		for(String[] tpnm : typeRes){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
	}
	
	//anything that this calls, find it
	//this has return type, and a bunch of statements to compile
	public void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> globalVars, HCISRFunctionFileAST globalDataLink){
		//create a copy of the variables, and add in any arguments
		HashMap<String,VariableLocationDescription> methodVars = new HashMap<String,VariableLocationDescription>();
		methodVars.putAll(globalVars);
		int curLoc = 0;
		for(int i = 0;i<sig.length;i++){
			//run through the signature looking for arguments (it is assumed that sig[0] is the calling variable)
			if(HCISRFileAST.isIdentifier(sig[i])){
				methodVars.put(sig[i], new VariableLocationDescription(false,curLoc,typeRes[i]));
				curLoc += 1;
			}
		}
		
		//now, with the new method vars, compile the code block
		stackSize = code.compileReferences(imports, methodVars, curLoc);
		
		globalDataLoc = globalDataLink;
	}
	
	public HCISRInstance run(HCISRStackFrame sf) throws HCISRException{
		//use the global data
		return code.run(sf, globalDataLoc);
	}
	
	public HCISRDefinedFunctionAST(String[] methodSignature, String[][] typeRestrictions, HCISRCodeBlockAST commands){
		sig = methodSignature;
		code = commands;
		typeRes = typeRestrictions;
		isDefined = true;
	}
}