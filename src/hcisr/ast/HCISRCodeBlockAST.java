package hcisr.ast;

//this class represents a block of code with its return and catch clauses
public class HCISRCodeBlockAST{
	protected HCISRStatementAST[] codeContents;
	protected HCISRReturnsClauseAST toReturn;
	protected HCISRCatchClauseAST[] errorCorrect;
	
	public HCISRCodeBlockAST(HCISRStatementAST[] codeConts,HCISRReturnsClauseAST toRet,HCISRCatchClauseAST[] errCor){
		codeContents = codeConts;
		toReturn = toRet;
		errorCorrect = errCor;
	}
}