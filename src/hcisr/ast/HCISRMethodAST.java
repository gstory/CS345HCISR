package hcisr.ast;

import java.util.*;

public abstract class HCISRMethodAST{
	public boolean isDefined;
	protected String instName;
	protected String[] sig;
	protected String[][] typeRestrictions;
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract HCISRMethodAST copyWithParameters(HashMap<String,String[]> bindings);
}