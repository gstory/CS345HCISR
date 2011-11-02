package hcisr.ast;

//this class groups together functions and their data
public class HCISRFunctionFileAST{
	protected HCISRPermanentVariableAST[] functionGlobalData;
	protected HCISRFunctionAST[] functions;
	
	public HCISRFunctionFileAST(HCISRPermanentVariableAST[] globalDataDeclaration, HCISRFunctionAST[] functionDeclarations){
		functionGlobalData = globalDataDeclaration;
		functions = functionDeclarations;
	}
}