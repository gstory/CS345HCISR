package hcisr.ast;
import hcisr.*;
import java.util.*;

//this class represents a block of code with its return and catch clauses
public class HCISRCodeBlockAST{
	protected HCISRStatementAST[] codeContents;
	protected HCISRReturnsClauseAST toReturn;
	protected HCISRCatchClauseAST[] errorCorrect;
	
	//look for new types in the code
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		for(int i = 0;i<codeContents.length;i++){
			codeContents[i].compileTemplates(imports, newClasses);
		}
		toReturn.compileTemplates(imports, newClasses);
		for(int i = 0;i<errorCorrect.length;i++){
			errorCorrect[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRCodeBlockAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRCodeBlockAST(this,bindings);
	}
	
	//find out where everything is and return the number of things on the stack
	public int compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> methodVars, int initStackSize){
		//first, make a basic scope
		Scope mainScope = new Scope(methodVars,initStackSize);
		//take two passes through the code, one to find labels and assign variables, the second to figure out goto
		//first go through the code
		int lineNum = 0;
		for(HCISRStatementAST s : codeContents){
			s.compileReferences(imports,mainScope,lineNum);
			lineNum++;
		}
		//figure out where the return variable is
		toReturn.compileReferences(imports, mainScope);
		//and go straight to the catch clauses
		for(HCISRCatchClauseAST c : errorCorrect){
			c.compileReferences(imports, mainScope);
		}
		//and now compile goto
		Iterator<Scope> subScopes = mainScope.subScopes.iterator();
		for(HCISRStatementAST s : codeContents){
			s.compileLabelReferences(mainScope, subScopes);
		}
		for(HCISRCatchClauseAST c : errorCorrect){
			c.compileLabelReferences(mainScope, subScopes);
		}
		int numStackVars = mainScope.numStackVars;
		//remember to free up memory
		mainScope.kill();
		//and return stack size
		return numStackVars;
	}
	
	//actually run the code
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException{
		int i = 0;
		while(i<codeContents.length){
			//run through the code, and either return or catch an exception
			try{
				//run code
				codeContents[i].run(sf, hl);
				i = i + 1;
			}
			catch(HCISRException e){
				//check exceptions, and if any match, run them, otherwise, rethrow e
				for(int j = 0;j<errorCorrect.length;j++){
					if(errorCorrect[j].matchesError(e)){
						try{
							return errorCorrect[j].run(sf, hl);
						}
						catch(HCISRGotoException e2){
							i = e2.line;
							if(codeContents[i]!=e2.target){
								//code blocks cannot throw goto errors, things are bad
								throw new UnsupportedOperationException("Tried to goto outside a code block");
							}
						}
					}
				}
				throw e;
			}
			catch(HCISRGotoException e){
				i = e.line;
				if(codeContents[i]!=e.target){
					//code blocks cannot throw goto errors, things are bad
					throw new UnsupportedOperationException("Tried to goto outside a code block");
				}
			}
		}
		//then return
		return toReturn.run(sf, hl);
	}
	
	public HCISRCodeBlockAST(HCISRStatementAST[] codeConts,HCISRReturnsClauseAST toRet,HCISRCatchClauseAST[] errCor){
		codeContents = codeConts;
		toReturn = toRet;
		errorCorrect = errCor;
	}
	
	public HCISRCodeBlockAST(HCISRCodeBlockAST origin, HashMap<String,String[]> bindings){
		HCISRStatementAST[] origStat = origin.codeContents;
		codeContents = new HCISRStatementAST[origStat.length];
		for(int i = 0; i < origStat.length; i++){
			codeContents[i] = origStat[i].copyWithParameters(bindings);
		}
		toReturn = origin.toReturn.copyWithParameters(bindings);
		HCISRCatchClauseAST[] origCat = origin.errorCorrect;
		errorCorrect = new HCISRCatchClauseAST[origCat.length];
		for(int i = 0;i < origCat.length; i++){
			errorCorrect[i] = origCat[i].copyWithParameters(bindings);
		}
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting CodeBlockAST\n";
		tabCount++;
			for(int i=0; i <tabCount; i++)
				result+="\t";
			result+= "This Code block contains the following statements:\n";
			tabCount++;
				for(int i=0; i<codeContents.length;i++){
					result+=codeContents[i].toString(tabCount);
				}
			tabCount--;
			result+=toReturn.toString(tabCount);
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending CodeBlockAST\n";
		return result;
	}
}
