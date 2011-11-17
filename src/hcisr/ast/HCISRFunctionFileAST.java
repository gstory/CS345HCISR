package hcisr.ast;
import hcisr.*;

import java.util.*;

//this class groups together functions and their data
public class HCISRFunctionFileAST implements HCISRHeapLocation{
	protected HCISRVariableAST[] functionGlobalData;
	protected HCISRFunctionAST[] functions;
	
	protected boolean hasCompiledTemplates = false;
	protected boolean hasCompiledReferences = false;
	//the global data
	protected HCISRInstance[] globalData;
	
	//look for new types
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		if(hasCompiledTemplates){
			return;
		}
		hasCompiledTemplates = true;
		for(int i = 0;i<functionGlobalData.length;i++){
			functionGlobalData[i].compileTemplates(imports, newClasses);
		}
		for(int i = 0;i<functions.length;i++){
			functions[i].compileTemplates(imports, newClasses);
		}
	}
	
	//figure out who has the requisite data
	public void compileReferences(HashMap<String,HCISRFileAST> imports){
		if(hasCompiledReferences){
			return;
		}
		hasCompiledReferences = true;
		//first, create a global scope
		HashMap<String,VariableLocationDescription> classVars = new HashMap<String,VariableLocationDescription>();
		for(int i = 0;i<functionGlobalData.length;i++){
			classVars.put(functionGlobalData[i].name, new VariableLocationDescription(true,i,functionGlobalData[i].type));
		}
		//then get the global data's initial values
		globalData = new HCISRInstance[functionGlobalData.length];
		for(int i = 0;i<globalData.length;i++){
			globalData[i] = HCISRVariableAST.getInitialValue(functionGlobalData[i].initType,functionGlobalData[i].initVal,imports);
		}
		//then compile the functions
		for(HCISRFunctionAST f : functions){
			f.compileReferences(imports, classVars, this);
		}
	}
	
	public HCISRInstance getLocation(int loc){
		return globalData[loc];
	}
	public void setLocation(HCISRInstance toAdd, int loc){
		globalData[loc] = toAdd;
	}
	
	public HCISRFunctionFileAST(HCISRVariableAST[] globalDataDeclaration, HCISRFunctionAST[] functionDeclarations){
		functionGlobalData = globalDataDeclaration;
		functions = functionDeclarations;
	}
}