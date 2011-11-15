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
	
	//figure out who has the requisite data
	public void compileReferences(HashMap<String,HCISRFileAST> imports){
		//first, create a global scope
		HashMap<String,VariableLocationDescription> classVars = new HashMap<String,VariableLocationDescription>();
		for(int i = 0;i<functionGlobalData.length;i++){
			classVars.put(functionGlobalData[i].name, new VariableLocationDescription(true,i,functionGlobalData[i].type));
		}
		//then compile the functions
		for(HCISRFunctionAST f : functions){
			f.compileReferences(imports, classVars, this);
		}
	}
	
	public HCISRFunctionFileAST(HCISRVariableAST[] globalDataDeclaration, HCISRFunctionAST[] functionDeclarations){
		functionGlobalData = globalDataDeclaration;
		functions = functionDeclarations;
	}
}