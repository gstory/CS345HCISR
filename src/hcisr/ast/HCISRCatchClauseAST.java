package hcisr.ast;

//this class represents a catch clause
public class HCISRCatchClauseAST{
	protected String[] errType;
	protected String errVar;
	protected HCISRStatementAST[] toDo;
	
	public HCISRCatchClauseAST(String[] errorType, String errorVariable, HCISRStatementAST[] commandList){
		errType = errorType;
		errVar = errorVariable;
		toDo = commandList;
	}
}