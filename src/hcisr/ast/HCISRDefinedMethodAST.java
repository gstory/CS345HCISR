package hcisr.ast;

//this class represents a filled in method
public class HCISRDefinedMethodAST extends HCISRMethodAST{
	protected String instName;
	protected String[] sig;
	protected HCISRCodeBlockAST code;
	
	public HCISRDefinedMethodAST(String instanceName,String[] methodSignature, HCISRCodeBlockAST commands){
		instName = instanceName;
		sig = methodSignature;
		code = commands;
		isDefined = true;
	}
}