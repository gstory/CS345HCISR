package hcisr.ast;
import hcisr.*;
import java.util.*;

public abstract class HCISRConstructorAST implements HCISRRunnable{
	public boolean isDefined;
	protected String[] createdVariableType;
	protected String createdVariable;
	protected String[] sig; // this is the signature of the constructor, with make a new and the type
	protected String[][] typeRes;
	
	//something required to make a new instance
	protected HCISRClassAST toConstruct;
	protected int stackSize;
	public int getStackSize(){
		return stackSize;
	}
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract HCISRConstructorAST copyWithParameters(HashMap<String,String[]> bindings);
	public abstract void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> classVars,HCISRClassAST toCreate);
	public abstract String toString(int tabCount);
}
