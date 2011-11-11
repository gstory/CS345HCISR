package hcisr.ast;

import java.util.*;

//this class represents a variable in either a class or a function block
public class HCISRVariableAST{
	public static final int STRING = 0;
	public static final int INT = 1;
	public static final int FLOAT = 2;
	public static final int FUNCTION = 3;
	public static final int BOOL = 4;
	
	protected String[] type;
	protected String name;
	protected int initType;
	protected String initVal;
	
	//see if type is already parameterized
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		HCISRFileAST.checkForTemplateClass(imports, newClasses, type);
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