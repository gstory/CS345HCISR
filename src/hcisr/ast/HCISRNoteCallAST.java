package hcisr.ast;
import hcisr.*;
import java.util.*;

//a do nothing class, for a do nothing statement
public class HCISRNoteCallAST extends HCISRStatementAST{
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		
	}
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings){
		return this;
	}
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line){
		
	}
	public void compileLabelReferences(Scope currentScope,Iterator<Scope> subScopes){
		
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException,HCISRGotoException{
		return null;
	}
	public HCISRNoteCallAST(){
		
	}
}