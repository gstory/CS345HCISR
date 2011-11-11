package hcisr.ast;

import java.util.*;

//this class represents a filled in method
public class HCISRDefinedMethodAST extends HCISRMethodAST{
	protected HCISRCodeBlockAST code;
	
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
}