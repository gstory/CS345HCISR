package hcisr.ast;

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
	
	public HCISRVariableAST(String[] typeDeclaration, String variableName, int initializedType, String initializedValue){
		type = typeDeclaration;
		name = variableName;
		initType = initializedType;
		initVal = initializedValue;
	}
}