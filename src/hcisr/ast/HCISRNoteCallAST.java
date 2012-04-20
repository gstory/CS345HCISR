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
	
	//function added to help print out AST TC
	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting NoteCallAST\n";
		tabCount++;
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "A comment which does not get put on the AST\n";
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending NoteCallAST\n";
		return result;
	}
}
