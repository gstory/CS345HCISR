package hcisr.ast;

import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

//this class represents a declared constructor
public class HCISRDeclaredConstructorAST extends HCISRConstructorAST{
	protected HCISRReturnsDeclarationAST decRetType;
	
	//for external types, this may be filled in by another program
	HCISRExternalCodeBlock theCode;
	public void setInstructions(HCISRExternalCodeBlock instructions){
		theCode = instructions;
	}
	
	//look at type restrictions and the return declaration, and create any new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		for(String[] tpnm : typeRes){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
		decRetType.compileTemplates(imports, newClasses);
	}
	
	public HCISRConstructorAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRDeclaredConstructorAST(this,bindings);
	}
	
	//anything that this calls, find it
	//this really only has the stack to worry about (and the class to create)
	public void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> classVars,HCISRClassAST toCreate){
		toConstruct = toCreate;
		stackSize = stackSize + 1;
		for(int i = 0;i<sig.length;i++){
			if(HCISRFileAST.isIdentifier(sig[i])){
				stackSize = stackSize + 1;
			}
		}
	}
	
	public HCISRInstance run(HCISRStackFrame sf){
		//make the new instance
		HCISRInstance newInst = new HCISRInstance(toConstruct);
		sf.setLocation(newInst, 0);
		//run the filled in stuff (or shit a brick if it's undefined)
		if(theCode==null){
			throw new UnsupportedOperationException("External or Abstract method not filled in");
		}
		return theCode.run(sf, newInst);
	}
	
	public HCISRDeclaredConstructorAST(String createdVariable, String[] createdType,String[] methodSignature,String[][] typeRestrictions, HCISRReturnsDeclarationAST retDec){
		this.createdVariable = createdVariable;
		sig = methodSignature;
		decRetType = retDec;
		createdVariableType = createdType;
		typeRes = typeRestrictions;
		isDefined = false;
	}
	
	public HCISRDeclaredConstructorAST(HCISRDeclaredConstructorAST origin, HashMap<String,String[]> bindings){
		createdVariable = origin.createdVariable;
		createdVariableType = HCISRClassAST.replaceTypeNames(origin.createdVariableType, bindings);
		typeRes = HCISRClassAST.replaceTypeRestrictions(origin.typeRes, bindings);
		sig = HCISRClassAST.replaceConstructorTypeNames(createdVariableType, origin.sig);
		typeRes = HCISRClassAST.resizeTypeRestrictions(typeRes, origin.sig, sig);
		decRetType = origin.decRetType.copyWithParameters(bindings);
		isDefined = false;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting DeclaredConstructorAST\n";
		tabCount++;
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Signature: "+Arrays.toString(sig)+"\n";	
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending DeclaredConstructorAST\n";
		return result;
	}
}
