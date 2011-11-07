package hcisr.ast;
import java.util.*;

//this class represents a hcisr type
public class HCISRClassAST{
	public static final int NORMAL = 0;
	public static final int ARCHTYPE = 1;
	public static final int EXTERNAL = 2;
	
	//unfiltered information about the type
	protected int classType;
	protected String[] typeName;
	protected String[][] typeRestrictions; //used for restrictions on parameterized types
	protected String[] supertypeName;
	protected HCISRVariableAST[] variableList;
	protected HCISRMethodAST[] methodList;
	protected HCISRConstructorAST[] constructorList;
	protected String methodThisName;
	protected String constructorThisName;
	
	//parameterized types, used for templates (the string is the full, concatenated name)
	HashMap<String,HCISRClassAST> parameterized;
	HCISRClassAST templateType;
	
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		
	}
	
	public HCISRClassAST(int classClassification, String[] fullTypeName, String[] fullSuperTypeName, HCISRVariableAST[] instanceVariableList,HCISRMethodAST[] instanceMethodList,HCISRConstructorAST[] fullConstructorList,String methodSelfReference,String constructorSelfReference){
		classType = classClassification;
		typeName = fullTypeName;
		supertypeName = fullSuperTypeName;
		variableList = instanceVariableList;
		methodList = instanceMethodList;
		constructorList = fullConstructorList;
		methodThisName = methodSelfReference;
		constructorThisName = constructorSelfReference;
	}
}