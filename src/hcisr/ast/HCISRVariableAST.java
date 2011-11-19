package hcisr.ast;
import hcisr.*;
import java.util.*;

//this class represents a variable in either a class or a function block
public class HCISRVariableAST{
	public static final int NOINIT = -1;
	public static final int STRING = 0;
	public static final int INT = 1;
	public static final int FLOAT = 2;
	public static final int FUNCTION = 3;
	public static final int BOOL = 4;
	
	public static final String stringClassLoc = HCISRInterpreter.stringClassLoc;
	public static final String intClassLoc = HCISRInterpreter.intClassLoc;
	public static final String floatClassLoc = HCISRInterpreter.floatClassLoc;
	public static final String functionClassLoc = HCISRInterpreter.functionClassLoc;
	public static final String booleanClassLoc = HCISRInterpreter.booleanClassLoc;
	
	protected String[] type;
	protected String name;
	protected int initType;
	protected String initVal;
	
	//see if type is already parameterized
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		HCISRFileAST.checkForTemplateClass(imports, newClasses, type);
	}
	
	//get an instance for an initial variable
	public static HCISRInstance getInitialValue(int type, String val, HashMap<String,HCISRFileAST> imports){
		HCISRFileAST toUse;
		HCISRInstance toRet;
		switch(type){
		case NOINIT: 
			return null;
		case STRING: 
			//get the string class
			toUse = imports.get(stringClassLoc);
			toRet = new HCISRInstance(toUse.classDef);
			toRet.addExternalVariables(new HCISRInstanceStringVars(val.substring(7)));
			return toRet;
		case INT: 
			toUse = imports.get(intClassLoc);
			toRet = new HCISRInstance(toUse.classDef);
			toRet.addExternalVariables(new HCISRInstanceIntegerVars(Integer.parseInt(val)));
			return toRet;
		case FLOAT: 
			toUse = imports.get(floatClassLoc);
			toRet = new HCISRInstance(toUse.classDef);
			//don't do any initialization, skip floats for now
			return toRet;
		case FUNCTION: 
			toUse = imports.get(functionClassLoc);
			toRet = new HCISRInstance(toUse.classDef);
			//don't do any initialization, will skip first class functions for now
			return toRet;
		case BOOL: 
			toUse = imports.get(booleanClassLoc);
			toRet = new HCISRInstance(toUse.classDef);
			if(val.equals("true")){
				toRet.addExternalVariables(new HCISRInstanceBooleanVars(true));
			}
			else{
				toRet.addExternalVariables(new HCISRInstanceBooleanVars(false));
			}
			return toRet;
		}
		return null;
	}
	
	public HCISRVariableAST(String[] typeDeclaration, String variableName, int initializedType, String initializedValue){
		type = typeDeclaration;
		name = variableName;
		initType = initializedType;
		initVal = initializedValue;
	}
	
	//this constructor is used to create a copy of a variable with type blanks filled in
	public HCISRVariableAST(HCISRVariableAST origin,HashMap<String,String[]> bindings){
		type = HCISRClassAST.replaceTypeNames(origin.type, bindings);
		name = origin.name;
		initType = origin.initType;
		initVal = origin.initVal;
	}
}