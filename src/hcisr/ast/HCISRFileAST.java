package hcisr.ast;
import hcisr.*;
import java.util.*;
//import java.lang.*;

//this class holds import statements and the file contents
public class HCISRFileAST implements HCISRRunnable{
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
	public HCISRClassAST getDefinedClass(){
		return classDef;
	}
	public boolean isFunction(){
		return isF;
	}
	public boolean isProgram(){
		return isP;
	}
	
	//if it is a program, need to know the stack size
	protected int stackSize;
	
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
	
	//given a scope, run through all statements in an array and compile their references
	public static void compileStatementReferences(HashMap<String,HCISRFileAST> imports,HCISRStatementAST[] code,Scope mainScope){
		//take two passes through the code, one to find labels and assign variables, the second to figure out goto
		compileStatementReferencesSansGoto(imports, code, mainScope);
		Iterator<Scope> subScopes = mainScope.subScopes.iterator();
		compileStatementGotoReferences(code, mainScope, subScopes);
	}
	
	public static void compileStatementReferencesSansGoto(HashMap<String,HCISRFileAST> imports, HCISRStatementAST[] code, Scope mainScope){
		int lineNum = 0;
		for(HCISRStatementAST s : code){
			s.compileReferences(imports,mainScope, lineNum);
			lineNum++;
		}
	}
	
	public static void compileStatementGotoReferences(HCISRStatementAST[] code, Scope mainScope, Iterator<Scope> subScopes){
		for(HCISRStatementAST s : code){
			s.compileLabelReferences(mainScope,subScopes);
		}
	}
	
	public static HCISRConstructorAST findConstructor(HashMap<String,HCISRFileAST> imports,String[] signature,String[] type,VariableLocationDescription[] argumentTypes){
		//find the class
		HCISRClassAST toCreate = findBaseClass(imports,type[0]);
		if(toCreate.isTemplate()){
			toCreate = toCreate.getParameterizedClass(type);
		}
		//now search for the constructor
		return toCreate.findMatchingConstructor(imports, signature, argumentTypes);
	}
	
	public static int findMethodIndex(HashMap<String,HCISRFileAST> imports, String[] signature, String[] callingType, VariableLocationDescription[] argumentTypes){
		HCISRClassAST callingClass = HCISRFileAST.findBaseClass(imports, callingType[0]);
		if(callingClass.isTemplate()){
			callingClass = callingClass.getParameterizedClass(callingType);
		}
		//now search for the method
		return callingClass.findMatchingMethodLocation(imports, signature, argumentTypes);
	}
	
	public static HCISRFunctionAST findFunction(HashMap<String,HCISRFileAST> imports, String[] signature,VariableLocationDescription[] argumentTypes){
		//collect all functions
		ArrayList<HCISRFunctionAST> possible = new ArrayList<HCISRFunctionAST>();
		for(HCISRFileAST file : imports.values()){
			if(file.isF){
				HCISRFunctionFileAST ffile = file.functionDef;
				for(HCISRFunctionAST f : ffile.functions){
					possible.add(f);
				}
			}
		}
		//first, find all functions that can match the signature
		for(int i = 0;i < signature.length;i++){
			ArrayList<HCISRFunctionAST> best = new ArrayList<HCISRFunctionAST>();
			String curSig = signature[i];
			//in the part of the constructor with arguments
			if(HCISRFileAST.isIdentifier(curSig)){
				//is an identifier, check type
				for(HCISRFunctionAST f : possible){
					if(f.sig.length>i && HCISRFileAST.isIdentifier(f.sig[i])){
						//both are identifiers, look at hierarchy
						String[] potentialMatchRestrictions = f.typeRes[i]; //there will be a typeRes
						String[] callingType = argumentTypes[i].typeNm;
						//get the types
						HCISRClassAST potentialMatchType = HCISRFileAST.findBaseClass(imports, potentialMatchRestrictions[0]);
						if(potentialMatchType.isTemplate()){
							potentialMatchType = potentialMatchType.getParameterizedClass(potentialMatchRestrictions);
						}
						HCISRClassAST callingTypeClass = HCISRFileAST.findBaseClass(imports, callingType[0]);
						if(callingTypeClass.isTemplate()){
							callingTypeClass = callingTypeClass.getParameterizedClass(callingType);
						}
						//and see how far it takes to go from calling type class to potential match restrictions
						int dist = HCISRClassAST.findUpcastDistance(callingTypeClass, potentialMatchType);
						if(dist > -1){
							best.add(f);
						}
					}
				}
			}
			else{
				//is a random string, check equality
				for(HCISRFunctionAST f : possible){
					if(f.sig.length>i && f.sig[i].equals(curSig)){
						best.add(f);
					}
				}
			}
			possible = best;
		}
		//then find the best
		for(int i = 0;i < signature.length;i++){
			ArrayList<HCISRFunctionAST> best = new ArrayList<HCISRFunctionAST>();
			String curSig = signature[i];
			//in the part of the constructor with arguments
			if(HCISRFileAST.isIdentifier(curSig)){
				//is an identifier, check type
				int shortestUpcast = Integer.MAX_VALUE;
				for(HCISRFunctionAST f : possible){
					if(f.sig.length>i && HCISRFileAST.isIdentifier(f.sig[i])){
						//both are identifiers, look at hierarchy
						String[] potentialMatchRestrictions = f.typeRes[i]; //there will be a typeRes
						String[] callingType = argumentTypes[i].typeNm;
						//get the types
						HCISRClassAST potentialMatchType = HCISRFileAST.findBaseClass(imports, potentialMatchRestrictions[0]);
						if(potentialMatchType.isTemplate()){
							potentialMatchType = potentialMatchType.getParameterizedClass(potentialMatchRestrictions);
						}
						HCISRClassAST callingTypeClass = HCISRFileAST.findBaseClass(imports, callingType[0]);
						if(callingTypeClass.isTemplate()){
							callingTypeClass = callingTypeClass.getParameterizedClass(callingType);
						}
						//and see how far it takes to go from calling type class to potential match restrictions
						int dist = HCISRClassAST.findUpcastDistance(callingTypeClass, potentialMatchType);
						if(dist > -1){
							if(dist == shortestUpcast){
								best.add(f);
							}
							else if(dist < shortestUpcast){
								best = new ArrayList<HCISRFunctionAST>();
								best.add(f);
								shortestUpcast = dist;
							}
						}
					}
				}
			}
			else{
				//is a random string, check equality
				for(HCISRFunctionAST f : possible){
					if(f.sig.length>i && f.sig[i].equals(curSig)){
						best.add(f);
					}
				}
			}
			possible = best;
		}
		//there is only one match
		if(possible.size() == 1){
//System.out.println("+"+possible.get(0));(possible.get(0)).print();
			return possible.get(0);
		}
		else{  
//System.out.println("-"+possible.size());
			return null;
			//there is an error, throw exception
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
		if(isC){
			classDef.compileHierarchy(imports);
		}
		else{
			//don't do anything, this does not inherit anything
		}
	}
	
	public void compileReferences(HashMap<String,HCISRFileAST> imports){
		if(isC){
			classDef.compileReferences(imports);
		}
		else if(isF){
			functionDef.compileReferences(imports);
		}
		else{
			//for a program, make an empty scope and run through it
			Scope mainScope = new Scope(new HashMap<String,VariableLocationDescription>(),0);
			compileStatementReferences(imports,programDef,mainScope);
			//note the stack size
			stackSize = mainScope.getNumberOfStackVariables();
			//remember to kill scope
			mainScope.kill();
		}
	}
	
	public void compileTypeChecking(HashMap<String,HCISRFileAST> imports){
		
	}
	
	public HCISRInstance run(HCISRStackFrame sf) throws HCISRException{
		if(!isProgram()){
			throw new UnsupportedOperationException("This file is not a program.");
		}
		for(int i = 0; i<programDef.length; i++){
			//run through the code, running the statements
			try{  
//System.out.println();System.out.println("---");System.out.println("asdf sf");sf.print();
				programDef[i].run(sf, null);
			}
			catch(HCISRGotoException e){
				//try to go to the line
				i = e.line;
				if(i>=programDef.length || e.target != programDef[i]){
					//there is a bad error
					throw new UnsupportedOperationException("Malformed goto");
				}
			}
		}
		return null; //programs do not return anything
	}
	
	public int getStackSize(){
		return stackSize;
	}
	
	public static boolean isIdentifier(String s){
		char c = s.charAt(0);
		if(Character.isUpperCase(c)){
			return true;
		}
		return false;
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

	//Added to print out AST TC
	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+="Starting FileAST: ";
		tabCount++;
		if(isC){
			result+="it is a class\n";
			result+=classDef.toString(tabCount);
		}
		if(isF){
			result+="it is a function\n";
			result+=functionDef.toString(tabCount);
		}
		if(isP){
			result+="it is a program with a list of statements\n";
			for(int i=0; i<programDef.length;i++)
			{
				result+=programDef[i].toString(tabCount);
			}
		}
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending FileAST\n";
		return result;
	}
}
