package hcisr.ast;

import hcisr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents an if statement
public class HCISRIfCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] trueComs;
	protected HCISRStatementAST[] falseComs;
	
	//where to find the boolean
	protected boolean specialVar;
	protected int arrayIndex;
	
	//just compile the statement lists
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		for(int i = 0;i<trueComs.length;i++){
			trueComs[i].compileTemplates(imports, newClasses);
		}
		if(falseComs==null){
			return;
		}
		for(int i = 0;i<falseComs.length;i++){
			falseComs[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRIfCallAST(this, bindings);
	}
	

	//there are no goto statements here, but check the lower code
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		//get the true scope
		Scope trueScope = subScopes.next();
		//run through the statements, looking for goto
		HCISRFileAST.compileStatementGotoReferences(trueComs, trueScope, trueScope.subScopes.iterator());
		
		if(falseComs==null){
			return;
		}
		//then get the false scope
		Scope falseScope = subScopes.next();
		HCISRFileAST.compileStatementGotoReferences(falseComs, falseScope, falseScope.subScopes.iterator());
	}
	
	//look for the loop variable and the loop list
	//the methods to call on the list can be hard coded
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//first, find the boolean variable
		VariableLocationDescription listVarLoc = currentScope.findVariable(boolID);
		specialVar = listVarLoc.special;
		arrayIndex = listVarLoc.location;
		
		//then, make a new scope for the true stuff
		Scope trueScope = new Scope(currentScope);
		//then run through the code
		HCISRFileAST.compileStatementReferencesSansGoto(imports, trueComs, trueScope);
		
		if(falseComs==null){
			return;
		}
		//then, make a new scope for the false stuff
		Scope falseScope = new Scope(currentScope);
		HCISRFileAST.compileStatementReferencesSansGoto(imports, falseComs, falseScope);
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException,HCISRGotoException{
		boolean toRunTrue;
		if(specialVar){
			toRunTrue = ((HCISRInstanceBooleanVars)(hl.getLocation(arrayIndex).getExternalVariables())).value;
		}
		else{
			toRunTrue = ((HCISRInstanceBooleanVars)(sf.getLocation(arrayIndex).getExternalVariables())).value;
		}
		if(toRunTrue){
			int i = 0;
			while(i<trueComs.length){
				try{
					trueComs[i].run(sf, hl);
					i = i + 1;
				}
				catch(HCISRGotoException e){
					i = e.line;
					if(i >= trueComs.length || trueComs[i]!=e.target){
						throw e;
					}
				}
			}
		}
		else{
			if(falseComs==null){
				return null;
			}
			int i = 0;
			while(i<falseComs.length){
				try{
					falseComs[i].run(sf, hl);
					i = i + 1;
				}
				catch(HCISRGotoException e){
					i = e.line;
					if(i>=falseComs.length || falseComs[i]!=e.target){
						throw e;
					}
				}
			}
		}
		return null;
	}
	
	public HCISRIfCallAST(String booleanID, HCISRStatementAST[] trueCommands, HCISRStatementAST[] falseCommands){
		boolID = booleanID;
		trueComs = trueCommands;
		falseComs = falseCommands;
	}
	
	public HCISRIfCallAST(HCISRIfCallAST origin,HashMap<String,String[]> bindings){
		boolID = origin.boolID;
		
		HCISRStatementAST[] origTrue = origin.trueComs;
		trueComs = new HCISRStatementAST[origTrue.length];
		for(int i = 0;i<trueComs.length;i++){
			trueComs[i] = origTrue[i].copyWithParameters(bindings);
		}
		
		HCISRStatementAST[] origFalse = origin.falseComs;
		falseComs = new HCISRStatementAST[origFalse.length];
		for(int i = 0;i<falseComs.length;i++){
			falseComs[i] = origFalse[i].copyWithParameters(bindings);
		}
	}
}