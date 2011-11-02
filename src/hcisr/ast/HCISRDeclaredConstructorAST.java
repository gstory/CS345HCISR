package hcisr.ast;

//this class represents a declared constructor
public class HCISRDeclaredConstructorAST extends HCISRConstructorAST{
	protected String[] retType;
	protected String retVar;
	protected String[] sig;
	protected HCISRReturnsDeclarationAST decRetType;
	
	public HCISRDeclaredConstructorAST(String returnVariable, String[] returnType,String[] methodSignature, HCISRReturnsDeclarationAST retDec){
		retVar = returnVariable;
		sig = methodSignature;
		decRetType = retDec;
		retType = returnType;
		isDefined = false;
	}
}