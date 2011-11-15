package hcisr.ast;

import java.util.*;

//this class represents a catch clause
public class HCISRCatchClauseAST{
	protected String[] errType;
	protected String errVar;
	protected HCISRStatementAST[] toDo;
	
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, errType);
		for(int i = 0;i<toDo.length;i++){
			toDo[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRCatchClauseAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRCatchClauseAST(this, bindings);
	}
	
	public void compileReferences(HashMap<String,HCISRFileAST> imports, Scope mainScope){
		//first, make a new scope (these variables are only visible in the exception
		Scope exceptScope = new Scope(mainScope);
		//add the exception to the list
		exceptScope.addStackVariable(errVar,errType);
		//and run through the code
		HCISRFileAST.compileStatementReferencesSansGoto(imports, toDo, exceptScope);
	}
	
	public void compileLabelReferences(Scope currentScope,Iterator<Scope> subScopes){
		//catch clauses start a new scope
		Scope curScope = subScopes.next();
		//run through the statements, looking for goto
		HCISRFileAST.compileStatementGotoReferences(toDo, curScope, curScope.subScopes.iterator());
	}
	
	public HCISRCatchClauseAST(String[] errorType, String errorVariable, HCISRStatementAST[] commandList){
		errType = errorType;
		errVar = errorVariable;
		toDo = commandList;
	}
	
	public HCISRCatchClauseAST(HCISRCatchClauseAST origin, HashMap<String,String[]> bindings){
		errType = HCISRClassAST.replaceTypeNames(origin.errType, bindings);
		errVar = origin.errVar;
		HCISRStatementAST[] origStat = origin.toDo;
		toDo = new HCISRStatementAST[origStat.length];
		for(int i = 0;i<toDo.length;i++){
			toDo[i] = origStat[i].copyWithParameters(bindings);
		}
	}
}