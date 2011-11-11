package hcisr.ast;
import java.util.*;

//this class represents a hcisr type
public class HCISRClassAST{
	public static final int NORMAL = 0;
	public static final int ARCHTYPE = 1;
	public static final int EXTERNAL = 2;
	
	//unfiltered information about the type
	protected int classType;
	protected String[] typeName;
	protected String[][] typeRestrictions; //used for restrictions on parameterized types
	protected String[] supertypeName;
	protected HCISRVariableAST[] variableList;
	protected HCISRMethodAST[] methodList;
	protected HCISRConstructorAST[] constructorList;
	protected String methodThisName;
	protected String constructorThisName;
	
	//what has been done to the type
	protected boolean templateCompiled = false;
	
	//parameterized types, used for templates (the string is the full, concatenated name)
	HashMap<String,HCISRClassAST> parameterized;
	protected boolean filledIn = false;
	HashMap<String,String[]> typeReps;
	HCISRClassAST templateType;
	
	//is the type a template
	public boolean isTemplate(){
		return typeName.length > 1 && !filledIn;
	}
	
	//a helper method, takes an array of strings and creates on long space delimited string
	protected String getFullName(String[] fullName){
		String fullNewName = fullName[0];
		for(int i = 1; i<fullName.length; i++){
			fullNewName = fullNewName + " " + fullName[i];
		}
		return fullNewName;
	}
	
	//only useful for template types, create a clone class, and in the clone class replace all typename standins with the correct parameter
	//then add the class to the list of new classes
	//check that the class doesn't already exist
	public void parameterize(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses, String[] fullName){
		String fullNewName = getFullName(fullName);
		//if the class already exists, you're done
		if(parameterized.containsKey(fullNewName)){
			return;
		}
		//otherwise, create the type replacements and then make a clone
		HashMap<String,String[]> newTypeReps = new HashMap<String,String[]>();
		int curI = 2; //skip the first two strings in the thing, you already know your name
		for(int i = 2;i<typeName.length;i++){
			String typeVar = typeName[i];
			ArrayList<String> replacement = exhaustTemplateParameter(imports, fullName, curI);
			curI = curI + replacement.size();
			String[] repArray = new String[replacement.size()];
			for(int j = 0;j<repArray.length;j++){
				repArray[j] = replacement.get(j);
			}
			newTypeReps.put(typeVar, repArray);
		}
		HCISRClassAST newClass = parameterizeClone(newTypeReps);
		parameterized.put(fullNewName, newClass);
		newClasses.add(newClass);
	}
	
	//a helper method, used to figure out which set of parameters belongs to which type (for a java analogy, ArrayList<ArrayList<ArrayList<Integer>>>)
	protected ArrayList<String> exhaustTemplateParameter(HashMap<String,HCISRFileAST> imports, String[] fullName, int curI){
		if(isTemplate()){
			ArrayList<String> toRet = new ArrayList<String>();
			toRet.add(fullName[curI]);
			curI++;
			toRet.add(fullName[curI]);
			curI++;
			for(int i = 2;i<typeName.length;i++){
				//find the class and tell it to get its name from fullName
				String curClassName = fullName[curI];
				HCISRClassAST curClass = HCISRFileAST.findBaseClass(imports, curClassName);
				ArrayList<String> curClassFullName = curClass.exhaustTemplateParameter(imports, fullName, curI);
				curI = curI + curClassFullName.size();
				toRet.addAll(curClassFullName);
			}
			return toRet;
		}
		else{
			ArrayList<String> toRet = new ArrayList<String>();
			toRet.add(fullName[curI]);
			return toRet;
		}
	}
	
	//get a clone with type variables filled in
	protected HCISRClassAST parameterizeClone(HashMap<String,String[]> newTypeReps){
		return new HCISRClassAST(newTypeReps,this);
	}
	
	//look for any parameterized types in the class or its variables
	public void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		if(templateCompiled){
			//already compiled, do nothing
			return;
		}
		templateCompiled = true;
		if(filledIn){
			//this type is a filled in template type
			//first, check your assignments
			for(String[] tpnms : typeReps.values()){
				HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnms);
			}
			//then, check the restrictions
			for(String[] tpnms : typeRestrictions){
				HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnms);
			}
			//run through all things and compile templates
			reallyCompileTemplates(imports,newClasses);
			return;
		}
		if(isTemplate()){
			//this type is a template, don't do anything
			return;
		}
		else{
			reallyCompileTemplates(imports,newClasses);
		}
	}
	
	//the above asks the question do I need to compile, this actually carries it out
	protected void reallyCompileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses){
		//first, check supertypeName to see if it calls a generic type
		HCISRFileAST.checkForTemplateClass(imports, newClasses, supertypeName);
		//now, run through the variables, looking at types
		for(HCISRVariableAST v:variableList){
			v.compileTemplates(imports, newClasses);
		}
		//next, run through the methods
		for(HCISRMethodAST m:methodList){
			m.compileTemplates(imports, newClasses);
		}
		//last, run through constructors
		for(HCISRConstructorAST c:constructorList){
			c.compileTemplates(imports, newClasses);
		}
	}
	
	//the usual constructor to use straight from the parser
	public HCISRClassAST(int classClassification, String[] fullTypeName, String[][] parameterLimiters, String[] fullSuperTypeName, HCISRVariableAST[] instanceVariableList,HCISRMethodAST[] instanceMethodList,HCISRConstructorAST[] fullConstructorList,String methodSelfReference,String constructorSelfReference){
		classType = classClassification;
		typeName = fullTypeName;
		typeRestrictions = parameterLimiters;
		supertypeName = fullSuperTypeName;
		variableList = instanceVariableList;
		methodList = instanceMethodList;
		constructorList = fullConstructorList;
		methodThisName = methodSelfReference;
		constructorThisName = constructorSelfReference;
		if(typeName.length>1){
			parameterized = new HashMap<String,HCISRClassAST>();
		}
	}
	
	//this version of the constructor is used to create a parameterized class
	//it creates a deep copy of everything fed into it
	public HCISRClassAST(HashMap<String,String[]> bindings,HCISRClassAST origin){
		//this doesn't change
		classType = origin.classType;
		
		//first, change the full name
		typeName = replaceTypeNames(origin.typeName, bindings);
		
		//now fill in parameter limiters
		typeRestrictions = replaceTypeRestrictions(origin.typeRestrictions,bindings);
		
		//now fill in the super type blanks
		supertypeName = replaceTypeNames(origin.supertypeName, bindings);
		
		//now fill in the variable blanks
		HCISRVariableAST[] origVarList = origin.variableList;
		variableList = new HCISRVariableAST[origVarList.length];
		for(int i = 0;i<variableList.length;i++){
			variableList[i] = new HCISRVariableAST(origVarList[i],bindings);
		}
		
		//now fill in method blanks
		HCISRMethodAST[] origMeth = origin.methodList;
		methodList = new HCISRMethodAST[origMeth.length];
		for(int i = 0;i<methodList.length;i++){
			methodList[i] = origMeth[i].copyWithParameters(bindings);
		}
		
		//now fill in constructor blanks
		HCISRConstructorAST[] origCons = origin.constructorList;
		constructorList = new HCISRConstructorAST[origCons.length];
		for(int i = 0;i<constructorList.length;i++){
			constructorList[i] = origCons[i].copyWithParameters(bindings);
		}
		
		//the this names do not change
		methodThisName = origin.methodThisName;
		constructorThisName = origin.constructorThisName;
		
		//this is a parameterized class, so it needs to know some things
		filledIn = true;
		typeReps = bindings;
		templateType = origin;
	}
	
	//given the parameterization, replace the variables in a type name with the full deal
	public static String[] replaceTypeNames(String[] original, HashMap<String,String[]> bindings){
		ArrayList<String> ftpnm = new ArrayList<String>();
		for(int i = 0;i<original.length;i++){
			if(bindings.containsKey(original[i])){
				String[] nextnm = bindings.get(original[i]);
				for(String s : nextnm){
					ftpnm.add(s);
				}
			}
			else{
				ftpnm.add(original[i]);
			}
		}
		String[] toRet = new String[ftpnm.size()];
		for(int i = 0;i<toRet.length;i++){
			toRet[i] = ftpnm.get(i);
		}
		return toRet;
	}
	
	//given the parameterization, replace several variables
	public static String[][] replaceTypeRestrictions(String[][] original, HashMap<String,String[]> bindings){
		String[][] typeRes = new String[original.length][];
		for(int i = 0;i<typeRes.length;i++){
			if(original[i]==null){}
			else{
				typeRes[i] = replaceTypeNames(original[i],bindings);
			}
		}
		return typeRes;
	}
}