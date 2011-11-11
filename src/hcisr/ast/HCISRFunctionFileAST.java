package hcisr.ast;
import java.util.*;

//this class groups together functions and their data
public class HCISRFunctionFileAST{
	protected HCISRVariableAST[] functionGlobalData;
	protected HCISRFunctionAST[] functions;
	
	//look for new types
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		for(int i = 0;i<functionGlobalData.length;i++){
			functionGlobalData[i].compileTemplates(imports, newClasses);
		}
		for(int i = 0;i<functions.length;i++){
			functions[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRFunctionFileAST(HCISRVariableAST[] globalDataDeclaration, HCISRFunctionAST[] functionDeclarations){
		functionGlobalData = globalDataDeclaration;
		functions = functionDeclarations;
	}
}