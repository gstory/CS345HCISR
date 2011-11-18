package hcisr;
import hcisr.ast.*;
import hcisr.lib.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class HCISRInterpreter{
	public static final String stringClassLoc = "hcisr.lib.HCISRString.hcisr";
	public static final String intClassLoc = "hcisr.lib.HCISRString.hcisr";
	public static final String floatClassLoc = "hcisr.lib.HCISRFloat.hcisr";
	public static final String functionClassLoc = "hcisr.lib.HCISRFloat.hcisr";
	public static final String booleanClassLoc = "hcisr.lib.HCISRBoolean.hcisr";
	public static final String iterableLoc = "hcisr.lib.HCISRIterable.hcisr";
	public static final String iteratorLoc = "hcisr.lib.HCISRIterator.hcisr";
	public static final String stdioLoc = "hcisr.lib.HCISRStandardOutput.hcisr";
	public static final String usualMethLoc = "hcisr.lib.HCISRUsualMethods.hcisr";
	
	HashMap<String,HCISRFileAST> loadedFiles = new HashMap<String,HCISRFileAST>();
	
	//run a program
	public HCISRInstance runProgram(HCISRFileAST toRun) throws HCISRException{
		//first, create a stack frame
		HCISRStackFrame sf = new HCISRStackFrame(toRun.getStackSize());
		//then, run program and return
		return toRun.run(sf);
	}
	
	//run a method or constructor (have to provide arguments) (can also run a program this way, arguments will be ignored)
	public HCISRInstance runFunction(HCISRRunnable toRun, HCISRStackFrame sf) throws HCISRException{
		return toRun.run(sf);
	}
	
	public void loadInitialFiles(Class ctu) throws IOException{
		HCISRFileAST stringClassF = loadFile(ctu, stringClassLoc);
		HCISRFileAST intClassF = loadFile(ctu, intClassLoc);
		HCISRFileAST floatClassF = loadFile(ctu, floatClassLoc);
		HCISRFileAST functionClassF = loadFile(ctu, functionClassLoc);
		HCISRFileAST booleanClassF = loadFile(ctu, booleanClassLoc);
		HCISRFileAST stdioFFF = loadFile(ctu, stdioLoc);
		HCISRFileAST usualMethFFF = loadFile(ctu, usualMethLoc);
		
		HCISRClassAST booleanClass = booleanClassF.getDefinedClass();
		HCISRClassAST intClass = intClassF.getDefinedClass();
		HCISRClassAST stringClass = stringClassF.getDefinedClass();
		
		//also load in iterable and iterator, though they are not external
		loadFile(ctu, iterableLoc);
		loadFile(ctu, iteratorLoc);
		
		String[] signature;
		VariableLocationDescription[] argumentTypes;
		
		//fill in usual methods blanks
		HCISRUsualMethodsDefinition usualMeths = new HCISRUsualMethodsDefinition(booleanClass);
		//the function to compare identity
		signature = new String[]{"are","A","and","B","the","same"};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"Object"}),null,new VariableLocationDescription(false,0,new String[]{"Object"}),null,null};
		HCISRDeclaredFunctionAST iequalsMeth = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFuntion(loadedFiles, signature, argumentTypes));
		iequalsMeth.setInstructions(usualMeths.identityEqualsMeth);
		
		//fill in standard output's blanks
		HCISRStandardOutputDefinition stdioFuncs = new HCISRStandardOutputDefinition();
		//the function to print
		signature = new String[]{"print","S"};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"String"})};
		HCISRDeclaredFunctionAST printMeth = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFuntion(loadedFiles, signature, argumentTypes));
		printMeth.setInstructions(stdioFuncs.printMeth);
		//the function to println
		signature = new String[]{"println"};
		argumentTypes = new VariableLocationDescription[]{null};
		HCISRDeclaredFunctionAST printlnMeth = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFuntion(loadedFiles, signature, argumentTypes));
		printlnMeth.setInstructions(stdioFuncs.printlnMeth);
		
		//fill in integer's blanks
		HCISRIntegerDefinition intMeths = new HCISRIntegerDefinition(intClass,booleanClass,stringClass);
		//the method for add
		signature = new String[]{"I","+","B"};
		argumentTypes = new VariableLocationDescription[]{null,null,new VariableLocationDescription(false,0,new String[]{"Integer"})};
		HCISRDeclaredMethodAST addMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		addMeth.setInstructions(intMeths.addMeth);
		//the method for less than
		signature = new String[]{"I","<","B"};
		HCISRDeclaredMethodAST ltMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		ltMeth.setInstructions(intMeths.ltMeth);
		//the method for as string
		signature = new String[]{"I","as","string"};
		argumentTypes = new VariableLocationDescription[]{null,null,null};
		HCISRDeclaredMethodAST asStringMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		asStringMeth.setInstructions(intMeths.asStrMeth);
		
		//fill in boolean's blanks
		HCISRBooleanDefinition boolMeths = new HCISRBooleanDefinition(booleanClass);
		//the method for and
		signature = new String[]{"B","&","A"};
		argumentTypes = new VariableLocationDescription[]{null,null,new VariableLocationDescription(false,0,new String[]{"Boolean"})};
		HCISRDeclaredMethodAST andMeth = (HCISRDeclaredMethodAST)(booleanClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		andMeth.setInstructions(boolMeths.andMeth);
		//the method for or
		signature = new String[]{"B","|","A"};
		HCISRDeclaredMethodAST orMeth = (HCISRDeclaredMethodAST)(booleanClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		orMeth.setInstructions(boolMeths.orMeth);
		//the method for not
		signature = new String[]{"B","~"};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"Boolean"})};
		HCISRDeclaredMethodAST notMeth = (HCISRDeclaredMethodAST)(booleanClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		notMeth.setInstructions(boolMeths.notMeth);
	}
	
	public HCISRFileAST loadFile(Class ctu,String resourceName) throws IOException{
		if(loadedFiles.containsKey(resourceName)){
			return loadedFiles.get(resourceName);
		}
		
		//a list of files to load
		ArrayList<String> toLoad = new ArrayList<String>();
		
		//first load in the main file (that will be returned)
		URL floc = ctu.getResource(resourceName);
		if(floc == null){
			throw new FileNotFoundException(resourceName);
		}
		InputStream fin = floc.openStream();
		HCISRFileAST toRet = HCISRParser.readFile(fin);
		loadedFiles.put(resourceName, toRet);
		//now load in linked files
		for(String toImp : toRet.getImports()){
			toLoad.add(toImp);
		}
		while(toLoad.size()>0){
			String toImp = toLoad.remove(toLoad.size()-1);
			//if it's already loaded, do not load the new file
			if(loadedFiles.containsKey(toImp)){}
			else{
				//otherwise, load
				floc = ctu.getResource(toImp);
				if(floc == null){
					throw new FileNotFoundException(toImp);
				}
				fin = floc.openStream();
				HCISRFileAST toAdd = HCISRParser.readFile(fin);
				loadedFiles.put(toImp, toAdd);
				for(String s:toAdd.getImports()){
					toLoad.add(s);
				}
			}
		}
		
		//now run through anything that's not a template and look for parameterized types
		ArrayList<HCISRClassAST> toFurtParam = new ArrayList<HCISRClassAST>();
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileTemplates(loadedFiles, toFurtParam);
		}
		while(toFurtParam.size()>0){
			HCISRClassAST c = toFurtParam.remove(toFurtParam.size()-1);
			c.compileTemplates(loadedFiles, toFurtParam);
		}
		
		//now resolve the class hierarchy
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileHierarchy(loadedFiles);
		}
		
		//now resolve references
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileReferences(loadedFiles);
		}
		
		//now check types
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileTypeChecking(loadedFiles);
		}
		
		return toRet;
	}
}