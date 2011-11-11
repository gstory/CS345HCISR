package hcisr.ast;

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
	
	public HCISRDefinedFunctionAST(String[] methodSignature, String[][] typeRestrictions, HCISRCodeBlockAST commands){
		sig = methodSignature;
		code = commands;
		typeRes = typeRestrictions;
		isDefined = true;
	}
}