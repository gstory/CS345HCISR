package hcisr.ast;
import java.util.*;

//this class holds import statements and the file contents
public class HCISRFileAST{
	protected String[] importFiles;						public String[] getImports(){return importFiles;};
	protected HCISRClassAST classDef;
	protected HCISRFunctionFileAST functionDef;
	protected HCISRStatementAST[] programDef;
	
	//what type of file is it?
	protected boolean isC;
	protected boolean isP;
	protected boolean isF;
	public boolean isClass(){
		return isC;
	}
	public boolean isFunction(){
		return isF;
	}
	public boolean isProgram(){
		return isP;
	}
	
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		
	}
	
	public void compileHierarchy(HashMap<String,HCISRFileAST> imports){
		
	}
	
	public void compileReferences(HashMap<String,HCISRFileAST> imports){
		
	}
	
	public void compileTypeChecking(HashMap<String,HCISRFileAST> imports){
		
	}
	
	public HCISRFileAST(String[] filesToImport,HCISRClassAST classDefinition){
		importFiles = filesToImport;
		classDef = classDefinition;
		isC = true;
	}
	public HCISRFileAST(String[] filesToImport,HCISRFunctionFileAST functionDefinition){
		importFiles = filesToImport;
		functionDef = functionDefinition;
		isF = true;
	}
	public HCISRFileAST(String[] filesToImport,HCISRStatementAST[] programDefinition){
		importFiles = filesToImport;
		programDef = programDefinition;
		isP = true;
	}
}