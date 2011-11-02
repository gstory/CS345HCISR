package hcisr.ast;

//this class allows a user to describe what a function returns without defining the function
public class HCISRReturnsDeclarationAST{
	protected String[] typeID;
	
	public HCISRReturnsDeclarationAST(String[] typeDesc){
		typeID = typeDesc;
	}
}