package hcisr.ast;
import hcisr.*;
import java.util.ArrayList;
import java.util.HashMap;

//this class represents a declared method (abstract or external)
public class HCISRDeclaredMethodAST extends HCISRMethodAST{
	protected HCISRReturnsDeclarationAST emptyMeth;
	
	protected boolean referenceCompiled = false;
	
	//for external types, this may be filled in by another program
	HCISRExternalCodeBlock theCode;
	public void setInstructions(HCISRExternalCodeBlock instructions){
		theCode = instructions;
	}
	
	//look at type restrictions and the return declaration, and create any new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		for(String[] tpnm : typeRestrictions){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
		emptyMeth.compileTemplates(imports, newClasses);
	}
	
	public HCISRMethodAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRDeclaredMethodAST(this,bindings);
	}
	
	//anything that this calls, find it
	//this really has nothing to call, just find the stack size
	public void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> classVars,HCISRClassAST callingClass){
		if(referenceCompiled){
			return;
		}
		referenceCompiled = true;
		for(int i = 0;i<sig.length;i++){
			if(HCISRFileAST.isIdentifier(sig[i])){
				stackSize = stackSize + 1;
			}
		}
	}
	
	public HCISRInstance run(HCISRStackFrame sf){
		//get the instance at location 0 (it is the heap location)
		HCISRInstance calling = sf.getLocation(0);
		//run the filled in stuff (or shit a brick if it's undefined)
		if(theCode==null){
			String toAdd = sig[0];
			for(int i = 1;i<sig.length;i++){
				toAdd = toAdd + sig[i];
			}
			throw new UnsupportedOperationException("External or Abstract method not filled in: "+toAdd);
		}
		return theCode.run(sf, calling);
	}
	
	public String[] getReturnType(){
		return emptyMeth.typeID;
	}
	
	public HCISRDeclaredMethodAST(String instanceName,String[] methodSignature, String[][] parameterRestrictions,HCISRReturnsDeclarationAST retType){
		instName = instanceName;
		sig = methodSignature;
		emptyMeth = retType;
		typeRestrictions = parameterRestrictions;
		isDefined = false;
	}
	
	public HCISRDeclaredMethodAST(HCISRDeclaredMethodAST origin, HashMap<String,String[]> bindings){
		//the instance name and signature do not change
		instName = origin.instName;
		sig = origin.sig;
		
		//but the type restrictions and return type do
		typeRestrictions = HCISRClassAST.replaceTypeRestrictions(origin.typeRestrictions, bindings);
		emptyMeth = origin.emptyMeth.copyWithParameters(bindings);
		
		isDefined = false;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting DeclaredMethodAST\n";
		tabCount++;
		//Add sub information here
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending DeclaredMethodAST\n";
		return result;
	}
}
