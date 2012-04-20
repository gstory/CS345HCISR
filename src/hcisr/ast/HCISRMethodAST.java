package hcisr.ast;
import hcisr.*;
import java.util.*;

public abstract class HCISRMethodAST implements HCISRRunnable{
	public boolean isDefined;
	protected String instName;
	protected String[] sig;
	protected String[][] typeRestrictions;
	
	//how many things are on the stack
	public int stackSize;
	public int getStackSize(){
		return stackSize;
	}
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract HCISRMethodAST copyWithParameters(HashMap<String,String[]> bindings);
	public abstract void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> classVars,HCISRClassAST callingClass);
	public abstract String[] getReturnType();
}
