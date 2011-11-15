package hcisr.ast;

public class GotoTargetLocationDescription{
	public int index;
	public HCISRLabelCallAST target;
	
	public GotoTargetLocationDescription(int statementLocation, HCISRLabelCallAST statementID){
		index = statementLocation;
		target = statementID;
	}
}