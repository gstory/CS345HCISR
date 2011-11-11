package hcisr.ast;
import java.util.*;

public abstract class HCISRStatementAST{
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings);
}