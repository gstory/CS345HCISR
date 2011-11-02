package hcisr.ast;

//this class holds import statements and the file contents
public class HCISRFileAST{
	protected String[] importFiles;
	protected HCISRClassAST classDef;
	protected HCISRFunctionFileAST functionDef;
	protected HCISRStatementAST[] programDef;
	
	public HCISRFileAST(String[] filesToImport,HCISRClassAST classDefinition){
		importFiles = filesToImport;
		classDef = classDefinition;
	}
	public HCISRFileAST(String[] filesToImport,HCISRFunctionFileAST functionDefinition){
		importFiles = filesToImport;
		functionDef = functionDefinition;
	}
	public HCISRFileAST(String[] filesToImport,HCISRStatementAST[] programDefinition){
		importFiles = filesToImport;
		programDef = programDefinition;
	}
}