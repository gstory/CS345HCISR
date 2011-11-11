package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class allows a user to describe what a function returns without defining the function
public class HCISRReturnsDeclarationAST{
	protected String[] typeID;
	
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, typeID);
	}
	
	public HCISRReturnsDeclarationAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRReturnsDeclarationAST(this, bindings);
	}
	
	public HCISRReturnsDeclarationAST(String[] typeDesc){
		typeID = typeDesc;
	}
	
	public HCISRReturnsDeclarationAST(HCISRReturnsDeclarationAST origin, HashMap<String,String[]> bindings){
		typeID = HCISRClassAST.replaceTypeNames(origin.typeID, bindings);
	}
}