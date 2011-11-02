package hcisr.ast;

//this class represents a define call in HCISR
public class HCISRDefineCallAST extends HCISRStatementAST{
	public static final int STRING = 0;
	public static final int INT = 1;
	public static final int FLOAT = 2;
	public static final int FUNCTION = 3;
	public static final int BOOL = 4;
	
	protected String[] typeName;
	protected String varName;
	protected String initVal;
	protected int initType;
	
	public HCISRDefineCallAST(String[] typeIdentifier, String variableName, String initialValue, int initialType){
		typeName = typeIdentifier;
		varName = variableName;
		initVal = initialValue;
		initType = initialType;
	}
}