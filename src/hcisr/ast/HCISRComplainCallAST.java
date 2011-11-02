package hcisr.ast;

//this class represents a complain/throw call in a program
public class HCISRComplainCallAST extends HCISRStatementAST{
	//the name of the given error
	protected String errorID;
	
	//directions on where to find the error to be thrown
	protected boolean stackVar;
	protected int arrayIndex;
	
	public HCISRComplainCallAST(String errorIdentifier){
		errorID = errorIdentifier;
	}
}