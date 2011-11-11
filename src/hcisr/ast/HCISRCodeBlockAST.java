package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

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
}