package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a declared function
public class HCISRDeclaredFunctionAST extends HCISRFunctionAST{
	protected HCISRReturnsDeclarationAST emptyMeth;
	
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		emptyMeth.compileTemplates(imports, newClasses);
		for(String[] tpnm : typeRes){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
	}
	
	public HCISRDeclaredFunctionAST(String[] methodSignature, String[][] typeRestrictions, HCISRReturnsDeclarationAST retType){
		sig = methodSignature;
		emptyMeth = retType;
		typeRes = typeRestrictions;
		isDefined = false;
	}
}