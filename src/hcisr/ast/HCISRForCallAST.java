package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a for loop
public class HCISRForCallAST extends HCISRStatementAST{
	protected String[] typeID;
	protected String varID;
	protected String listID;
	protected HCISRStatementAST[] loopComs;
	
	//where will the temporary variable be (it will be on the stack)
	protected int tmpArrayIndex;
	//where is the list
	protected boolean specialVar;
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
	
	//there are no goto statements here, but check the lower code
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		//catch clauses start a new scope
		Scope curScope = subScopes.next();
		//run through the statements, looking for goto
		HCISRFileAST.compileStatementGotoReferences(loopComs, curScope, curScope.subScopes.iterator());
	}
	
	//look for the loop variable and the loop list
	//the methods to call on the list can be hard coded
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//first, make a new scope (these variables are only visible in the loop
		Scope loopScope = new Scope(currentScope);
		//add the loop variable
		loopScope.addStackVariable(varID, typeID);
		//then run through the code
		HCISRFileAST.compileStatementReferencesSansGoto(imports, loopComs, loopScope);
		//now find variable locations
		tmpArrayIndex = loopScope.findVariable(varID).location;
		VariableLocationDescription listVarLoc = currentScope.findVariable(listID);
		specialVar = listVarLoc.special;
		arrayIndex = listVarLoc.location;
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