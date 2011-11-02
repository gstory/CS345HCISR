package hcisr.ast;

//this class represents a target for a goto statement
public class HCISRLabelCallAST extends HCISRStatementAST{
	protected String name;
	
	public HCISRLabelCallAST(String labelName){
		name = labelName;
	}
}