package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRGotoException;
import hcisr.HCISRHeapLocation;
import hcisr.HCISRInstance;
import hcisr.HCISRInstanceBooleanVars;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a while loop
public class HCISRWhileCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] loopComs;
	
	//where to find the boolean
	protected boolean specialVar;
	protected int arrayIndex;

	//while itself has no templates, though inner code might
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		for(int i = 0;i<loopComs.length;i++){
			loopComs[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRWhileCallAST(this, bindings);
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
		//the loop variable is already defined
		//then run through the code
		HCISRFileAST.compileStatementReferencesSansGoto(imports, loopComs, loopScope);
		//now find variable locations
		VariableLocationDescription listVarLoc = currentScope.findVariable(boolID);
		specialVar = listVarLoc.special;
		arrayIndex = listVarLoc.location;
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException,HCISRGotoException{
		boolean toRunTrue;
		if(specialVar){
			toRunTrue = ((HCISRInstanceBooleanVars)(hl.getLocation(arrayIndex).getExternalVariables())).value;
		}
		else{
			toRunTrue = ((HCISRInstanceBooleanVars)(sf.getLocation(arrayIndex).getExternalVariables())).value;
		}
		while(toRunTrue){
			int i = 0;
			while(i<loopComs.length){
				try{
					loopComs[i].run(sf, hl);
					i = i + 1;
				}
				catch(HCISRGotoException e){
					i = e.line;
					if(loopComs[i]!=e.target){
						throw e;
					}
				}
			}
			if(specialVar){
				toRunTrue = ((HCISRInstanceBooleanVars)(hl.getLocation(arrayIndex).getExternalVariables())).value;
			}
			else{
				toRunTrue = ((HCISRInstanceBooleanVars)(sf.getLocation(arrayIndex).getExternalVariables())).value;
			}
		}
		return null;
	}
	
	public HCISRWhileCallAST(String booleanID, HCISRStatementAST[] loopCommands){
		boolID = booleanID;
		loopComs = loopCommands;
	}
	
	public HCISRWhileCallAST(HCISRWhileCallAST origin, HashMap<String,String[]> bindings){
		boolID = origin.boolID;
		HCISRStatementAST[] origStat = origin.loopComs;
		loopComs = new HCISRStatementAST[origStat.length];
		for(int i = 0;i<loopComs.length;i++){
			loopComs[i] = origStat[i].copyWithParameters(bindings);
		}
	}
}