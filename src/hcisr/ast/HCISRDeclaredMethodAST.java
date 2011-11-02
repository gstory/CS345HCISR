package hcisr.ast;

//this class represents a declared method (abstract or external)
public class HCISRDeclaredMethodAST extends HCISRMethodAST{
	protected String instName;
	protected String[] sig;
	protected HCISRReturnsDeclarationAST emptyMeth;
	
	public HCISRDeclaredMethodAST(String instanceName,String[] methodSignature, HCISRReturnsDeclarationAST retType){
		instName = instanceName;
		sig = methodSignature;
		emptyMeth = retType;
		isDefined = false;
	}
}