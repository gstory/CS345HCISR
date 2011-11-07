package hcisr.ast;

//this class groups together functions and their data
public class HCISRFunctionFileAST{
	protected HCISRVariableAST[] functionGlobalData;
	protected HCISRFunctionAST[] functions;
	
	public HCISRFunctionFileAST(HCISRVariableAST[] globalDataDeclaration, HCISRFunctionAST[] functionDeclarations){
		functionGlobalData = globalDataDeclaration;
		functions = functionDeclarations;
	}
}