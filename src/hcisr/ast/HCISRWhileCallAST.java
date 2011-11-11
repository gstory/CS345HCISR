package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a while loop
public class HCISRWhileCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] loopComs;
	
	//where to find the boolean
	protected boolean stackVar;
	protected int arrayIndex;

	//while itself has no templates, though inner code might
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		for(int i = 0;i<loopComs.length;i++){
			loopComs[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRWhileCallAST(this, bindings);
	}
	
	public HCISRWhileCallAST(String booleanID, HCISRStatementAST[] loopCommands){
		boolID = booleanID;
		loopComs = loopCommands;
	}
	
	public HCISRWhileCallAST(HCISRWhileCallAST origin, HashMap<String,String[]> bindings){
		boolID = origin.boolID;
		HCISRStatementAST[] origStat = origin.loopComs;
		loopComs = new HCISRStatementAST[origStat.length];
		for(int i = 0;i<loopComs.length;i++){
			loopComs[i] = origStat[i].copyWithParameters(bindings);
		}
	}
}