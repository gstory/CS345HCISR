package hcisr.ast;

//this class represents a declared function
public class HCISRDeclaredFunctionAST extends HCISRFunctionAST{
	protected String[] sig;
	protected HCISRReturnsDeclarationAST emptyMeth;
	
	public HCISRDeclaredFunctionAST(String[] methodSignature, HCISRReturnsDeclarationAST retType){
		sig = methodSignature;
		emptyMeth = retType;
		isDefined = false;
	}
}