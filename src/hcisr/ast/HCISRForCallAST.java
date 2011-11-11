package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a for loop
public class HCISRForCallAST extends HCISRStatementAST{
	protected String[] typeID;
	protected String varID;
	protected String listID;
	protected HCISRStatementAST[] loopComs;
	
	//where will the temporary variable be (it will be on the stack)
	protected int tmpArrayIndex;
	//where is the list
	protected boolean stackVar;
	protected int arrayIndex;
	
	//this does introduce a new type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, typeID);
		for(HCISRStatementAST s : loopComs){
			s.compileTemplates(imports, newClasses);
		}
	}
	
	//deep copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRForCallAST(this, bindings);
	}
	
	public HCISRForCallAST(String[] typeIndentifier, String variableIdentifier,String listIdentifier,HCISRStatementAST[] loopCommands){
		typeID = typeIndentifier;
		varID = variableIdentifier;
		listID = listIdentifier;
		loopComs = loopCommands;
	}
	
	public HCISRForCallAST(HCISRForCallAST origin,HashMap<String,String[]> bindings){
		varID = origin.varID;
		typeID = HCISRClassAST.replaceTypeNames(origin.typeID, bindings);
		
		HCISRStatementAST[] origStat = origin.loopComs;
		loopComs = new HCISRStatementAST[origStat.length];
		for(int i = 0;i<origStat.length;i++){
			loopComs[i] = origStat[i].copyWithParameters(bindings);
		}
	}
}