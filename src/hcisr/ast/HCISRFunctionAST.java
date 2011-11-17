package hcisr.ast;
import hcisr.*;
import java.util.*;

public abstract class HCISRFunctionAST implements HCISRRunnable{
	public boolean isDefined;
	protected String[] sig;
	protected String[][] typeRes;
	protected int stackSize;
	public int getStackSize(){
		return stackSize;
	}
	
	//global data location
	protected HCISRFunctionFileAST globalDataLoc;
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> globalDataDescription, HCISRFunctionFileAST globalData);
}