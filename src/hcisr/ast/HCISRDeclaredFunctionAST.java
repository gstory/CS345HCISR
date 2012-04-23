package hcisr.ast;

import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;
import hcisr.HCISRHeapLocation;
import hcisr.HCISRException;

import java.util.ArrayList;
import java.util.Arrays;
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
//System.out.println("---");System.out.println(theCode == null);
		//if(theCode==null){
		if( sf == null ){
			throw new UnsupportedOperationException("External or Abstract method not filled in");
		}
		return theCode.run(sf, globalDataLoc);
	}

//gs testing
	public HCISRInstance run(HCISRStackFrame sf, HCISRHeapLocation hl) throws HCISRException{
		//use the global data
		//run the filled in stuff (or shit a brick if it's undefined)
//		System.out.println("---");System.out.println(theCode == null);
		//if(theCode==null){
		if( sf == null || hl == null ){
			throw new UnsupportedOperationException("StackFrame or HeapLocation null");
		}
		return theCode.run(sf, hl);
	}
	
	public HCISRDeclaredFunctionAST(String[] methodSignature, String[][] typeRestrictions, HCISRReturnsDeclarationAST retType){
		sig = methodSignature;
		emptyMeth = retType;
		typeRes = typeRestrictions;
		isDefined = false;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting DeclaredFunctionAST\n";
		tabCount++;
			if(sig!=null){
				for(int i =0; i<tabCount;i++)
					result+="\t";
				result+="Method signature of: "+Arrays.toString(sig)+"\n";
			}
			if(typeRes!=null){
				for(int i =0; i<tabCount;i++)
					result+="\t";
				result+="Type Restriction of: "+Arrays.deepToString(typeRes)+"\n";
			}
			result+=emptyMeth.toString(tabCount);
			
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending DeclaredFunctionAST\n";
		return result;
	}
}
