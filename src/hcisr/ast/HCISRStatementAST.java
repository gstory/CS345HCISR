package hcisr.ast;
import hcisr.*;
import java.util.*;

public abstract class HCISRStatementAST{
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings);
	public abstract void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line);
	public abstract void compileLabelReferences(Scope currentScope,Iterator<Scope> subScopes);
	public abstract HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException,HCISRGotoException;
	public abstract String toString(int tabCount);
}
