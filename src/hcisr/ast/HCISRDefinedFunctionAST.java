package hcisr.ast;

//this class represents a filled in function
public class HCISRDefinedFunctionAST extends HCISRFunctionAST{
	protected String[] sig;
	protected HCISRCodeBlockAST code;
	
	public HCISRDefinedFunctionAST(String[] methodSignature, HCISRCodeBlockAST commands){
		sig = methodSignature;
		code = commands;
		isDefined = true;
	}
}