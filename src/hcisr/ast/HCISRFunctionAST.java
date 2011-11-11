package hcisr.ast;

import java.util.*;

public abstract class HCISRFunctionAST{
	public boolean isDefined;
	protected String[] sig;
	protected String[][] typeRes;
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
}