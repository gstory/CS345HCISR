package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a declared constructor
public class HCISRDeclaredConstructorAST extends HCISRConstructorAST{
	protected HCISRReturnsDeclarationAST decRetType;

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
	
	public HCISRDeclaredConstructorAST(String returnVariable, String[] returnType,String[] methodSignature,String[][] typeRestrictions, HCISRReturnsDeclarationAST retDec){
		retVar = returnVariable;
		sig = methodSignature;
		decRetType = retDec;
		retType = returnType;
		typeRes = typeRestrictions;
		isDefined = false;
	}
	
	public HCISRDeclaredConstructorAST(HCISRDeclaredConstructorAST origin, HashMap<String,String[]> bindings){
		retVar = origin.retVar;
		sig = origin.sig;
		retType = HCISRClassAST.replaceTypeNames(origin.retType, bindings);
		typeRes = HCISRClassAST.replaceTypeRestrictions(origin.typeRes, bindings);
		decRetType = origin.decRetType.copyWithParameters(bindings);
		isDefined = false;
	}
}