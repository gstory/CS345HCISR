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
	
	//find a class in the collection of imports
	public static HCISRClassAST findBaseClass(HashMap<String,HCISRFileAST> imports,String name){
		for(HCISRFileAST f : imports.values()){
			if(f.isC){
				if(f.classDef.typeName[0].equals(name)){
					return f.classDef;
				}
			}
		}
		return null;
	}
	//see if a class needs parameterization
	public static void checkForTemplateClass(HashMap<String,HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses, String[] typeName){
		if(typeName == null){
			return;
		}
		if(HCISRFileAST.findBaseClass(imports, typeName[0]).isTemplate()){
			HCISRClassAST superT = HCISRFileAST.findBaseClass(imports, typeName[0]);
			superT.parameterize(imports, newClasses, typeName);
		}
	}
	
	//run through contents looking for all parameterized types, and tell the base type to make it
	//provided that this file is not itself a template
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		if(isC){
			classDef.compileTemplates(imports, newClasses);
		}
		else if(isF){
			functionDef.compileTemplates(imports, newClasses);
		}
		else{
			for(HCISRStatementAST s:programDef){
				s.compileTemplates(imports, newClasses);
			}
		}
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