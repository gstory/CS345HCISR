package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents an if statement
public class HCISRIfCallAST extends HCISRStatementAST{
	protected String boolID;
	protected HCISRStatementAST[] trueComs;
	protected HCISRStatementAST[] falseComs;
	
	//where to find the boolean
	protected boolean stackVar;
	protected int arrayIndex;
	
	//just compile the statement lists
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		for(int i = 0;i<trueComs.length;i++){
			trueComs[i].compileTemplates(imports, newClasses);
		}
		for(int i = 0;i<falseComs.length;i++){
			falseComs[i].compileTemplates(imports, newClasses);
		}
	}
	
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRIfCallAST(this, bindings);
	}
	
	public HCISRIfCallAST(String booleanID, HCISRStatementAST[] trueCommands, HCISRStatementAST[] falseCommands){
		boolID = booleanID;
		trueComs = trueCommands;
		falseComs = falseCommands;
	}
	
	public HCISRIfCallAST(HCISRIfCallAST origin,HashMap<String,String[]> bindings){
		boolID = origin.boolID;
		
		HCISRStatementAST[] origTrue = origin.trueComs;
		trueComs = new HCISRStatementAST[origTrue.length];
		for(int i = 0;i<trueComs.length;i++){
			trueComs[i] = origTrue[i].copyWithParameters(bindings);
		}
		
		HCISRStatementAST[] origFalse = origin.falseComs;
		falseComs = new HCISRStatementAST[origFalse.length];
		for(int i = 0;i<falseComs.length;i++){
			falseComs[i] = origFalse[i].copyWithParameters(bindings);
		}
	}
}