package hcisr.ast;

//this class represents a defined constructor
public class HCISRDefinedConstructorAST extends HCISRConstructorAST{
	protected String[] retType;
	protected String retVar;
	protected String[] sig;
	protected HCISRCodeBlockAST code;
	
	public HCISRDefinedConstructorAST(String returnVariable, String[] returnType,String[] methodSignature, HCISRCodeBlockAST commands){
		retVar = returnVariable;
		sig = methodSignature;
		code = commands;
		retType = returnType;
		isDefined = true;
	}
}