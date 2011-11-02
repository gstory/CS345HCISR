package hcisr.ast;

//this class represents a hcisr type
public class HCISRClassAST{
	public static final int NORMAL = 0;
	public static final int ARCHTYPE = 1;
	public static final int EXTERNAL = 2;
	
	protected int classType;
	protected String[] typeName;
	protected String[] supertypeName;
	protected HCISRPermanentVariableAST[] variableList;
	protected HCISRMethodAST[] methodList;
	protected HCISRConstructorAST[] constructorList;
	
	public HCISRClassAST(int classClassification, String[] fullTypeName, String[] fullSuperTypeName, HCISRPermanentVariableAST[] instanceVariableList,HCISRMethodAST[] instanceMethodList,HCISRConstructorAST[] fullConstructorList){
		classType = classClassification;
		typeName = fullTypeName;
		supertypeName = fullSuperTypeName;
		variableList = instanceVariableList;
		methodList = instanceMethodList;
		constructorList = fullConstructorList;
	}
}