package hcisr.ast;

import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a declared function
public class HCISRDeclaredFunctionAST extends HCISRFunctionAST{
	protected HCISRReturnsDeclarationAST emptyMeth;
	
	//for external types, this may be filled in by another program
	HCISRExternalCodeBlock theCode;
	public void setInstructions(HCISRExternalCodeBlock instructions){
		theCode = instructions;
	}
	
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		emptyMeth.compileTemplates(imports, newClasses);
		for(String[] tpnm : typeRes){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
	}
	
	//anything that this calls, find it
	//this really has nothing to call, just find the stack size (and record global variable location)
	public void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> globalVars, HCISRFunctionFileAST globalDataLink){
		for(int i = 0;i<sig.length;i++){
			if(HCISRFileAST.isIdentifier(sig[i])){
				stackSize = stackSize + 1;
			}
		}
		globalDataLoc = globalDataLink;
	}
	
	public HCISRInstance run(HCISRStackFrame sf){
		//use the global data
		//run the filled in stuff (or shit a brick if it's undefined)
		if(theCode==null){
			throw new UnsupportedOperationException("External or Abstract method not filled in");
		}
		return theCode.run(sf, globalDataLoc);
	}
	
	public HCISRDeclaredFunctionAST(String[] methodSignature, String[][] typeRestrictions, HCISRReturnsDeclarationAST retType){
		sig = methodSignature;
		emptyMeth = retType;
		typeRes = typeRestrictions;
		isDefined = false;
	}
}