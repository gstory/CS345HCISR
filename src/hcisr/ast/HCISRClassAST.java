package hcisr.ast;
import java.util.*;
import hcisr.*;

//this class represents a hcisr type
//How overloading is done
//	starting from the left, look for the method that has the best match (same signature, and argument type closest to the signature)
//	note that it is only possible to upcast for methods, you cannot downcast
public class HCISRClassAST{
	public static final int NORMAL = 0;
	public static final int ARCHTYPE = 1;
	public static final int EXTERNAL = 2;
	
	//unfiltered information about the type
	protected int classType;
	protected String[] typeName; public String[] getFullName(){return typeName;}
	protected String[][] typeRestrictions; //used for restrictions on parameterized types
	protected String[] supertypeName;
	protected HCISRVariableAST[] variableList;
	protected HCISRMethodAST[] methodList;
	protected HCISRConstructorAST[] constructorList;
	protected String methodThisName;
	protected String constructorThisName;
	
	//compiled information about the type
	protected HCISRClassAST stype;
	
	//initial values for the variables
	HCISRInstance[] initVals;
	
	//what has been done to the type
	protected boolean templateCompiled = false;
	protected boolean hierarchyCompiled = false;
	protected boolean referencesCompiled = false;
	
	//parameterized types, used for templates (the string is the full, concatenated name)
	HashMap<String,HCISRClassAST> parameterized;
	protected boolean filledIn = false;
	HashMap<String,String[]> typeReps;
	HCISRClassAST templateType;
	
	//get the correctly parameterized type
	public HCISRClassAST getParameterizedClass(String[] fullTypeName){
		return parameterized.get(getFullName(fullTypeName));
	}
	
	//look for the location of the best method
	public int findMatchingMethodLocation(HashMap<String,HCISRFileAST> imports, String[] signature, VariableLocationDescription[] argumentTypes){
		HCISRMethodAST match = findMatchingMethod(imports, signature, argumentTypes);
		for(int i = 0;i<methodList.length;i++){
			if(methodList[i] == match){
				return i;
			}
		}
		return -1;
	}
	
	//look for the best method
	public HCISRMethodAST findMatchingMethod(HashMap<String,HCISRFileAST> imports,String[] signature,VariableLocationDescription[] argumentTypes){
		ArrayList<HCISRMethodAST> possible = new ArrayList<HCISRMethodAST>();
		for(HCISRMethodAST m : methodList){
			possible.add(m);
		}
		//first, find all methods that could match (skip the first identifier, it is a guaranteed match
		for(int i = 1;i < signature.length;i++){
			ArrayList<HCISRMethodAST> best = new ArrayList<HCISRMethodAST>();
			String curSig = signature[i];
			//in the part of the constructor with arguments
			if(HCISRFileAST.isIdentifier(curSig)){
				//is an identifier, check type
				for(HCISRMethodAST m : possible){
					if(m.sig.length>i && HCISRFileAST.isIdentifier(m.sig[i])){
						//both are identifiers, look at hierarchy
						String[] potentialMatchRestrictions = m.typeRestrictions[i]; //there will be a typeRes
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
						int dist = findUpcastDistance(callingTypeClass, potentialMatchType);
						if(dist > -1){
							best.add(m);
						}
					}
				}
			}
			else{
				//is a random string, check equality
				for(HCISRMethodAST m : possible){
					if(m.sig.length>i && m.sig[i].equals(curSig)){
						best.add(m);
					}
				}
			}
			possible = best;
		}
		//then find the best fit
		for(int i = 1;i < signature.length;i++){
			ArrayList<HCISRMethodAST> best = new ArrayList<HCISRMethodAST>();
			String curSig = signature[i];
			//in the part of the constructor with arguments
			if(HCISRFileAST.isIdentifier(curSig)){
				//is an identifier, check type
				int shortestUpcast = Integer.MAX_VALUE;
				for(HCISRMethodAST m : possible){
					if(m.sig.length>i && HCISRFileAST.isIdentifier(m.sig[i])){
						//both are identifiers, look at hierarchy
						String[] potentialMatchRestrictions = m.typeRestrictions[i]; //there will be a typeRes
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
						int dist = findUpcastDistance(callingTypeClass, potentialMatchType);
						if(dist > -1){
							if(dist == shortestUpcast){
								best.add(m);
							}
							else if(dist < shortestUpcast){
								best = new ArrayList<HCISRMethodAST>();
								best.add(m);
								shortestUpcast = dist;
							}
						}
					}
				}
			}
			else{
				//is a random string, check equality
				for(HCISRMethodAST m : possible){
					if(m.sig.length>i && m.sig[i].equals(curSig)){
						best.add(m);
					}
				}
			}
			possible = best;
		}
		//there is only one match
		if(possible.size() == 1){
			return possible.get(0);
		}
		else{
			System.out.println("asdf " + possible.size() );
			return null;
			//there is an error, throw exception
		}
	}
	
	//look for the best constructor
	public HCISRConstructorAST findMatchingConstructor(HashMap<String,HCISRFileAST> imports,String[] signature,VariableLocationDescription[] argumentTypes){
		ArrayList<HCISRConstructorAST> possible = new ArrayList<HCISRConstructorAST>();
		for(HCISRConstructorAST c : constructorList){
			possible.add(c);
		}
		//first find all possible matches
		boolean crossedWith = false;
		for(int i = 0;i < signature.length;i++){
			ArrayList<HCISRConstructorAST> best = new ArrayList<HCISRConstructorAST>();
			String curSig = signature[i];
			if(crossedWith){
				//in the part of the constructor with arguments
				if(HCISRFileAST.isIdentifier(curSig)){
					//is an identifier, check type
					for(HCISRConstructorAST c : possible){
						if(c.sig.length>i && HCISRFileAST.isIdentifier(c.sig[i])){
							//both are identifiers, look at hierarchy
							String[] potentialMatchRestrictions = c.typeRes[i]; //there will be a typeRes
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
							int dist = findUpcastDistance(callingTypeClass, potentialMatchType);
							if(dist > -1){
								best.add(c);
							}
						}
					}
				}
				else{
					//is a random string, check equality
					for(HCISRConstructorAST c : possible){
						if(c.sig.length>i && c.sig[i].equals(curSig)){
							best.add(c);
						}
					}
				}
			}
			else{
				//still in the first part of the constructor name, just check equality
				for(HCISRConstructorAST c : possible){
					if(c.sig.length>i && c.sig[i].equals(curSig)){
						best.add(c);
					}
				}
				if(curSig.equals("with")){
					crossedWith = true;
				}
			}
			possible = best;
		}
		//then find the best match
		crossedWith = false;
		for(int i = 0;i < signature.length;i++){
			ArrayList<HCISRConstructorAST> best = new ArrayList<HCISRConstructorAST>();
			String curSig = signature[i];
			if(crossedWith){
				//in the part of the constructor with arguments
				if(HCISRFileAST.isIdentifier(curSig)){
					//is an identifier, check type
					int shortestUpcast = Integer.MAX_VALUE;
					for(HCISRConstructorAST c : possible){
						if(c.sig.length>i && HCISRFileAST.isIdentifier(c.sig[i])){
							//both are identifiers, look at hierarchy
							String[] potentialMatchRestrictions = c.typeRes[i]; //there will be a typeRes
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
							int dist = findUpcastDistance(callingTypeClass, potentialMatchType);
							if(dist > -1){
								if(dist == shortestUpcast){
									best.add(c);
								}
								else if(dist < shortestUpcast){
									best = new ArrayList<HCISRConstructorAST>();
									best.add(c);
									shortestUpcast = dist;
								}
							}
						}
					}
				}
				else{
					//is a random string, check equality
					for(HCISRConstructorAST c : possible){
						if(c.sig.length>i && c.sig[i].equals(curSig)){
							best.add(c);
						}
					}
				}
			}
			else{
				//still in the first part of the constructor name, just check equality
				for(HCISRConstructorAST c : possible){
					if(c.sig.length>i && c.sig[i].equals(curSig)){
						best.add(c);
					}
				}
				if(curSig.equals("with")){
					crossedWith = true;
				}
			}
			possible = best;
		}
		//there is only one match
		if(possible.size() == 1){
			return possible.get(0);
		}
		else{
			return null;
			//there is an error, throw exception
		}
	}
	
	//find out how many classes the suspectedSubclass is beneath the suspectedSuperClass (-1 if there is no connection)
	public static int findUpcastDistance(HCISRClassAST suspectedSubclass, HCISRClassAST suspectedSuperclass){
		return findUpcastDist(suspectedSubclass, suspectedSuperclass, 0);
	}
	private static int findUpcastDist(HCISRClassAST suspectedSubclass, HCISRClassAST suspectedSuperclass,int cur){
		//if the subclass is null, is not a child of superclass
		if(suspectedSubclass == null){
			return -1;
		}
		if(suspectedSubclass==suspectedSuperclass){
			return cur;
		}
		return findUpcastDist(suspectedSubclass.stype, suspectedSuperclass, cur+1);
	}
	
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
			String replaceBase = fullName[curI];
			//get the base class
			HCISRClassAST toExhaust = HCISRFileAST.findBaseClass(imports, replaceBase);
			ArrayList<String> replacement = toExhaust.exhaustTemplateParameter(imports, fullName, curI);
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
		if(supertypeName.length==1){
			if(supertypeName[0].equals("Nothing")){
				
			}
			else{
				HCISRFileAST.checkForTemplateClass(imports, newClasses, supertypeName);
			}
		}
		else{
			HCISRFileAST.checkForTemplateClass(imports, newClasses, supertypeName);
		}
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
	
	//look for your super class and take it's methods and variables from it
	public void compileHierarchy(HashMap<String,HCISRFileAST> imports){
		//if the class is a template, compile the lower stuff and return
		if(isTemplate()){
			for(HCISRClassAST params : parameterized.values()){
				params.compileHierarchy(imports);
			}
			return;
		}
		
		if(hierarchyCompiled){
			return; //already found super class, do nothing
		}
		hierarchyCompiled = true;
		
		//find the super class
		//if supertypeName is Nothing, then don't find superclass, and methods are as they should be
		if(supertypeName.length==1){
			if(supertypeName[0].equals("Nothing")){
				return;
			}
		}
		HCISRClassAST superClassInstance = HCISRFileAST.findBaseClass(imports, supertypeName[0]);
		if(superClassInstance.isTemplate()){
			//the super class is a template, get the parameterized version
			superClassInstance = superClassInstance.getParameterizedClass(supertypeName);
		}
		stype = superClassInstance;
		
		//now, super type must be compiled before following steps
		stype.compileHierarchy(imports);
		
		//now, get a full list of variables and methods
		//variables are easy, just tack on the new stuff at the end
		HCISRVariableAST[] superVars = stype.variableList;
		HCISRVariableAST[] newVarList = new HCISRVariableAST[superVars.length + variableList.length];
		for(int i = 0;i<superVars.length;i++){
			newVarList[i] = superVars[i];
		}
		for(int i = 0;i<variableList.length;i++){
			newVarList[i + superVars.length] = variableList[i];
		}
		variableList = newVarList;
		
		//methods require more care, as overriding must be watched (exact same method signature, including restrictions)
		HCISRMethodAST[] superMeth = stype.methodList;
		ArrayList<HCISRMethodAST> newMethList = new ArrayList<HCISRMethodAST>();
		for(int i = 0;i<superMeth.length;i++){
			newMethList.add(i, superMeth[i]);
		}
		//for every method currently in the class, run through the array list
		//if any method signature matches, replace
		for(int i = 0;i<methodList.length;i++){
			HCISRMethodAST curM = methodList[i];
			boolean matched = false;
			for(int j = 0;j<newMethList.size();j++){
				if(areMethodSignaturesEqual(curM,newMethList.get(j))){
					newMethList.set(j, curM);
					matched = true;
					break;
				}
				else{}
			}
			if(!matched){
				newMethList.add(curM);
			}
		}
		//now, turn array list to an array
		methodList = new HCISRMethodAST[newMethList.size()];
		for(int i = 0;i<methodList.length;i++){
			methodList[i] = newMethList.get(i);
		}
		
		//done, don't need constructors (it wouldn't make sense)
	}
	
	public boolean areMethodSignaturesEqual(HCISRMethodAST m1, HCISRMethodAST m2){
		String[] m1sig = m1.sig;
		String[][] m1tr = m1.typeRestrictions;
		String[] m2sig = m2.sig;
		String[][] m2tr = m2.typeRestrictions;
		//first, check lengths
		if(m1sig.length != m2sig.length){
			return false;
		}
		//now, skip first identifier
		for(int i = 1;i<m1sig.length;i++){
			String m1v = m1sig[i];
			String m2v = m2sig[i];
			if(HCISRFileAST.isIdentifier(m1v)){
				if(HCISRFileAST.isIdentifier(m2v)){
					//both are identifiers, check their types, if not equal, quit, otherwise continue
					String[] m1vt = m1tr[i];
					String[] m2vt = m2tr[i];
					if(m1vt.length!=m2vt.length){
						return false;
					}
					for(int j = 0;j<m1vt.length;j++){
						if(!(m1vt[j].equals(m2vt[j]))){
							//if any types are unequal, they are not the same signature
							return false;
						}
					}
				}
				else{
					//one is an identifier, and one is not, so not equal
					return false;
				}
			}
			else{
				if(!(m1v.equals(m2v))){
					return false;
				}
			}
		}
		//it failed to fail, so it must pass
		return true;
	}
	
	//tell all sub nodes to figure out who has their shit
	public void compileReferences(HashMap<String,HCISRFileAST> imports){
		//methods and constructors still have loose ends, and parameterized types need to compile, but everything else is resolved
		if(isTemplate()){
			//if it is a template, do not find references, only let parameterized types compile
			for(HCISRClassAST c : parameterized.values()){
				c.compileReferences(imports);
			}
		}
		else{
			if(referencesCompiled){
				return;
			}
			referencesCompiled = true;
			
			//first order of business is to compile the supertype (I'd like methods to be compiled with their original type first)
			if(stype==null){
				
			}
			else{
				stype.compileReferences(imports);
			}
			//next, get variable initial values
			initVals = new HCISRInstance[variableList.length];
			for(int i = 0;i<initVals.length;i++){
				initVals[i] = HCISRVariableAST.getInitialValue(variableList[i].initType, variableList[i].initVal, imports);
			}
			//next, create a map of variable names and variables, and then compile methods and constructors
			HashMap<String,VariableLocationDescription> classVars = new HashMap<String,VariableLocationDescription>();
			for(int i = 0;i<variableList.length;i++){
				classVars.put(variableList[i].name, new VariableLocationDescription(true,i,variableList[i].type));
			}
			for(HCISRMethodAST m : methodList){
				m.compileReferences(imports,classVars,this);
			}
			for(HCISRConstructorAST c : constructorList){
				c.compileReferences(imports, classVars, this);
			}
		}
	}
	
	//how many variables does this thing have, used at runtime
	public int getNumberOfVariables(){
		return variableList.length;
	}
	
	//get the variables, to initialize
	public HCISRInstance[] getVariables(){
		return initVals;
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
		//now, tell all constructors whom they make
		for(int i = 0;i<constructorList.length;i++){
			constructorList[i].toConstruct = this;
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
			constructorList[i].toConstruct = this;
		}
		
		//the this names do not change
		methodThisName = origin.methodThisName;
		constructorThisName = origin.constructorThisName;
		
		//this is a parameterized class, so it needs to know some things
		filledIn = true;
		typeReps = bindings;
		templateType = origin;
	}
	
	//resize a constructor type restriction to match the new signature
	public static String[][] resizeTypeRestrictions(String[][] oldRestrictions,String[] oldSignature,String[] newSignature){
		ArrayList<String[]> toBuild = new ArrayList<String[]>();
		//first, add in nulls until with for the new signature
		int curNIN = 0;
		boolean skipped = false;
		while(!skipped && curNIN<newSignature.length){
			toBuild.add(null);
			if(newSignature[curNIN].equals("with")){
				skipped = true;
			}
			else{
				curNIN = curNIN + 1;
			}
		}
		//now, run through the old signature, adding everything after with
		skipped = false;
		for(int i = 0;i<oldSignature.length;i++){
			if(skipped){
				toBuild.add(oldRestrictions[i]);
			}
			else{
				if(oldSignature[i].equals("with")){
					skipped = true;
				}
			}
		}
		String[][] toRet = new String[toBuild.size()][];
		for(int i = 0;i<toRet.length;i++){
			toRet[i] = toBuild.get(i);
		}
		return toRet;
	}
	
	//given the parameterization, replace variables in a constructor type name with the new deal
	public static String[] replaceConstructorTypeNames(String[] newConstructorTypeName,String[] fullMethodSignature){
		ArrayList<String> toBuild = new ArrayList<String>();
		toBuild.add(fullMethodSignature[0]);//make a new
		for(int i = 0;i<newConstructorTypeName.length;i++){
			toBuild.add(newConstructorTypeName[i]); //the new type name
		}
		//now, skip to with and replace the rest as is
		boolean skipped = false;
		for(int i = 0;i<fullMethodSignature.length;i++){
			if(!skipped){
				if(fullMethodSignature[i].equals("with")){
					toBuild.add(fullMethodSignature[i]);
					skipped = true;
				}
			}
			else{
				toBuild.add(fullMethodSignature[i]);
			}
		}
		//now, create the string []
		String[] toRet = new String[toBuild.size()];
		for(int i = 0;i<toRet.length;i++){
			toRet[i] = toBuild.get(i);
		}
		return toRet;
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
	
	//function added to help print out AST TC
	public String toString(int tabCount)
	{
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+="Starting ClassAST: type: ";
		if(classType==0){
			result+="normal class\n";
		}
		if(classType==1){
			result+="archtype class\n";
		}
		if(classType==2){
			result+="external class\n";
		}
		tabCount++;
			for(int i=0; i<tabCount; i++)
				result+="\t";
			result+="Type name: "+Arrays.toString(typeName)+"\n";
			for(int i=0; i<tabCount; i++)
				result+="\t";
			result+="Variable List:\n";
			tabCount++;
				for(int i=0; i<variableList.length;i++)
					result+= variableList[i].toString(tabCount);
			tabCount--;
			for(int i=0; i<tabCount; i++)
				result+="\t";
			result+="Method List:\n";
			tabCount++;
				for(int i=0; i<methodList.length;i++)
					result+=methodList[i].toString(tabCount);
			tabCount--;
			for(int i=0; i<tabCount; i++)
				result+="\t";
			result+="Constructor List:\n";
			tabCount++;
				for(int i=0; i<constructorList.length;i++)
					result+= constructorList[i].toString(tabCount);
			tabCount--;
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending ClassAST\n";
		return result;
	}
}
