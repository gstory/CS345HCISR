package hcisr.ast;

import hcisr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//a class that represents a goto statement
public class HCISRGoToCallAST extends HCISRStatementAST{
	//what is the destination
	protected String locationID;
	
	//a more concrete description (the statement in question and the location in its array, unroll loops/compound statements until you find the statement)
	protected HCISRStatementAST toGoTo;
	protected int statementIndex;
	
	//goto does not make new classes
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//shallow copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRGoToCallAST(locationID);
	}
	
	//the goto statement in all its glory
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		GotoTargetLocationDescription target = currentScope.findLabel(locationID);
		toGoTo = target.target;
		statementIndex = target.index;
	}
	
	//goto targets labels, it does not add things
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException,HCISRGotoException{
		throw new HCISRGotoException(statementIndex,toGoTo);
	}
	
	public HCISRGoToCallAST(String locationIdentifier){
		locationID = locationIdentifier;
	}
}