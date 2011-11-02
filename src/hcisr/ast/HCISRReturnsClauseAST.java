package hcisr.ast;

//this class represents a return clause
public class HCISRReturnsClauseAST{
	protected String retVar;
	protected String[] retType;
	
	public HCISRReturnsClauseAST(String returnVariable,String[] returnType){
		retVar = returnVariable;
		retType = returnType;
	}
}