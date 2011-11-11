package hcisr.ast;

import java.util.*;

public abstract class HCISRConstructorAST{
	public boolean isDefined;
	protected String[] retType;
	protected String retVar;
	protected String[] sig;
	protected String[][] typeRes;
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract HCISRConstructorAST copyWithParameters(HashMap<String,String[]> bindings);
}