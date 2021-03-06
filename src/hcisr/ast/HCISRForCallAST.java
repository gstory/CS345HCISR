package hcisr.ast;

import hcisr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

//this class represents a for loop
public class HCISRForCallAST extends HCISRStatementAST{
	public static final String iterableLoc = HCISRInterpreter.iterableLoc;
	public static final String iteratorLoc = HCISRInterpreter.iteratorLoc;
	public static final String[] getIterator = new String[]{"I","get","iterator"};
	public static final String[] hasNext = new String[]{"I","has","next"};
	public static final String[] getNext = new String[]{"I","get","next"};
	
	protected String[] typeID;
	protected String varID;
	protected String listID;
	protected HCISRStatementAST[] loopComs;
	
	//where will the temporary variable be (it will be on the stack)
	protected int tmpArrayIndex;
	//where is the list
	protected boolean specialVar;
	protected int arrayIndex;
	//what should be called to get the iterator
	protected int getIteratorIndex;
	protected int getNextIndex;
	protected int hasNextIndex;
	
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
		
		//now, find the class for the list, and find the location for get iterator
		String[] listClassName = listVarLoc.typeNm;
		HCISRClassAST listClass = HCISRFileAST.findBaseClass(imports, listClassName[0]);
		if(listClass.isTemplate()){
			listClass = listClass.getParameterizedClass(listClassName);
		}
		//the template type for list's type
		HCISRClassAST listSuperTemp = imports.get(iterableLoc).classDef;
		//crawl up the hierarchy until the origin is the iterable type
		HCISRClassAST listSuperClass = listClass;
		while(listSuperClass!=null && listSuperClass.templateType!=listSuperTemp){
			listSuperClass = listSuperClass.stype;
		}
		if(listSuperClass == null){
			//we have a problem
		}
		//now, find the get iterator method location
		VariableLocationDescription[] getIteratorArgs = new VariableLocationDescription[]{listVarLoc,null,null};
		getIteratorIndex = listSuperClass.findMatchingMethodLocation(imports, getIterator, getIteratorArgs);
		
		//now, find the iterator type, and find methods for it
		HCISRMethodAST getIteratorMethod = listSuperClass.findMatchingMethod(imports, getIterator, getIteratorArgs);
		String[] iteratorType = getIteratorMethod.getReturnType();
		HCISRClassAST iteratorClass = HCISRFileAST.findBaseClass(imports, iteratorType[0]);
		if(iteratorClass.isTemplate()){
			iteratorClass = iteratorClass.getParameterizedClass(iteratorType);
		}
		VariableLocationDescription[] iteratorArgs = new VariableLocationDescription[]{new VariableLocationDescription(false,0,iteratorType),null,null};
		//find the get next method
		getNextIndex = iteratorClass.findMatchingMethodLocation(imports, getNext, iteratorArgs);
		hasNextIndex = iteratorClass.findMatchingMethodLocation(imports, hasNext, iteratorArgs);
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException,HCISRGotoException{
		//first, get the list
		HCISRInstance list;
		if(specialVar){
			list = hl.getLocation(arrayIndex);
		}
		else{
			list = sf.getLocation(arrayIndex);
		}
		//then, get it's iterator
		HCISRMethodAST getIteratorMeth = list.getHCISRClass().methodList[getIteratorIndex];
		//set up the stack frame
		HCISRStackFrame getIteratorStackFrame = new HCISRStackFrame(getIteratorMeth.getStackSize());
		getIteratorStackFrame.setLocation(list, 0);
		HCISRInstance iterator = getIteratorMeth.run(getIteratorStackFrame);
		
		//now get the iterator's two methods
		HCISRMethodAST getNextMeth = iterator.getHCISRClass().methodList[getNextIndex];
		HCISRMethodAST hasNextMeth = iterator.getHCISRClass().methodList[hasNextIndex];
		//and set up a stack frame
		HCISRStackFrame getNextSF = new HCISRStackFrame(getNextMeth.getStackSize());
		HCISRStackFrame hasNextSF = new HCISRStackFrame(hasNextMeth.getStackSize());
		
		boolean running = true;
		while(running){
			//set up stack frames
			hasNextSF.setLocation(iterator, 0);
			HCISRInstance hasMore = hasNextMeth.run(hasNextSF);
			if(((HCISRInstanceBooleanVars)(hasMore.getExternalVariables())).value){
				//still have more, run get next, then run commands
				getNextSF.setLocation(iterator, 0);
				HCISRInstance nextVal = getNextMeth.run(getNextSF);
				//then set the loop variable to next val
				sf.setLocation(nextVal, tmpArrayIndex);
				//then run the loop commands
				int curCom = 0;
				while(curCom<loopComs.length){
					try{
						loopComs[curCom].run(sf, hl);
						curCom = curCom + 1;
					}
					catch(HCISRGotoException e){
						curCom = e.line;
						if(loopComs[curCom] != e.target){
							throw e;
						}
					}
				}
			}
			else{
				running = false;
			}
		}
		return null;
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
		listID = origin.listID;
		
		HCISRStatementAST[] origStat = origin.loopComs;
		loopComs = new HCISRStatementAST[origStat.length];
		for(int i = 0;i<origStat.length;i++){
			loopComs[i] = origStat[i].copyWithParameters(bindings);
		}
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting ForCallAST\n";
		tabCount++;
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Variable ID: "+varID+"\n";
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Type ID: "+Arrays.toString(typeID)+"\n";
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "List ID: "+listID+"\n";
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Starting Loop Statements\n";	
			tabCount++;
				for(int i=0; i< loopComs.length; i++)
					result+=loopComs[i].toString(tabCount);
			tabCount--;
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending ForCallAST\n";
		return result;
	}
}
