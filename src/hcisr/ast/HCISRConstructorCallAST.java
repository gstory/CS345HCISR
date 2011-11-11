package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a function call
public class HCISRConstructorCallAST extends HCISRStatementAST{
	//the function signature
	public String[] argumentIDs;
	//the class
	protected String[] typeID;
	
	//what is the function to call
	protected HCISRConstructorAST toCall;
	//a description of where to find the arguments for the function
	protected boolean[] stackVar;
	protected int[] arrayIndex;
	
	//a constructor call may define a new type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, typeID);
	}
	
	//just make a new copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRConstructorCallAST(this, bindings);
	}
	
	public HCISRConstructorCallAST(String[] methodSignature,String[] typeIdentifier){
		argumentIDs = methodSignature;
		typeID = typeIdentifier;
	}
	
	public HCISRConstructorCallAST(HCISRConstructorCallAST origin,HashMap<String,String[]> bindings){
		argumentIDs = origin.argumentIDs;
		typeID = HCISRClassAST.replaceTypeNames(origin.typeID, bindings);
	}
}