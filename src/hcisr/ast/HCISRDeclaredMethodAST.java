package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a declared method (abstract or external)
public class HCISRDeclaredMethodAST extends HCISRMethodAST{
	protected HCISRReturnsDeclarationAST emptyMeth;
	
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
}