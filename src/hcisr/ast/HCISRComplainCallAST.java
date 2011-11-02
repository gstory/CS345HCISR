package hcisr.ast;

//this class represents a complain/throw call in a program
public class HCISRComplainCallAST extends HCISRStatementAST{
	protected String errorID;
	
	public HCISRComplainCallAST(String errorIdentifier){
		errorID = errorIdentifier;
	}
}