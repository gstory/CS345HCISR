package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a defined constructor
public class HCISRDefinedConstructorAST extends HCISRConstructorAST{
	protected HCISRCodeBlockAST code;

	//look at type restrictions and the return declaration, and create any new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		for(String[] tpnm : typeRes){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
		code.compileTemplates(imports, newClasses);
	}
	
	public HCISRConstructorAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRDefinedConstructorAST(this,bindings);
	}
	public HCISRDefinedConstructorAST(String returnVariable, String[] returnType,String[] methodSignature,String[][] typeRestrictions, HCISRCodeBlockAST commands){
		retVar = returnVariable;
		sig = methodSignature;
		code = commands;
		retType = returnType;
		typeRes = typeRestrictions;
		isDefined = true;
	}
	
	public HCISRDefinedConstructorAST(HCISRDefinedConstructorAST origin, HashMap<String,String[]> bindings){
		retVar = origin.retVar;
		sig = origin.sig;
		retType = HCISRClassAST.replaceTypeNames(origin.retType, bindings);
		typeRes = HCISRClassAST.replaceTypeRestrictions(origin.typeRes, bindings);
		code = origin.code.copyWithParameters(bindings);
		isDefined = true;
	}
}