package hcisr;
import hcisr.ast.*;
import hcisr.lib.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class HCISRInterpreter{
	public static final String stringClassLoc = "hcisr/lib/HCISRString.hcisr";
	public static final String intClassLoc = "hcisr/lib/HCISRInteger.hcisr";
	public static final String floatClassLoc = "hcisr/lib/HCISRFloat.hcisr";
	public static final String functionClassLoc = "hcisr/lib/HCISRFunction.hcisr";
	public static final String booleanClassLoc = "hcisr/lib/HCISRBoolean.hcisr";
	public static final String iterableLoc = "hcisr/lib/HCISRIterable.hcisr";
	public static final String iteratorLoc = "hcisr/lib/HCISRIterator.hcisr";
	public static final String stdioLoc = "hcisr/lib/HCISRStandardOutput.hcisr";
	public static final String usualMethLoc = "hcisr/lib/HCISRUsualMethods.hcisr";
	public static int verbose;
	
	HashMap<String,HCISRFileAST> loadedFiles = new HashMap<String,HCISRFileAST>();
	Class ctu;
	boolean externalsFilledIn = false;
	
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
	
	public void fillInInitialFiles() throws IOException,ParseException{
		if(externalsFilledIn){
			return;
		}
		
		HCISRFileAST stringClassF = loadFile(stringClassLoc);
		HCISRFileAST intClassF = loadFile(intClassLoc);
		HCISRFileAST floatClassF = loadFile(floatClassLoc);
		HCISRFileAST functionClassF = loadFile(functionClassLoc);
		HCISRFileAST booleanClassF = loadFile(booleanClassLoc);
		HCISRFileAST stdioFFF = loadFile(stdioLoc);
		HCISRFileAST usualMethFFF = loadFile(usualMethLoc);
		
		HCISRClassAST booleanClass = booleanClassF.getDefinedClass();
		HCISRClassAST intClass = intClassF.getDefinedClass();
		HCISRClassAST stringClass = stringClassF.getDefinedClass();
		
		//also load in iterable and iterator, though they are not external
		loadFile(iterableLoc);
		loadFile(iteratorLoc);
		
		String[] signature;
		VariableLocationDescription[] argumentTypes;
		
		//fill in usual methods blanks
		HCISRUsualMethodsDefinition usualMeths = new HCISRUsualMethodsDefinition(booleanClass);
		//the function to compare identity
		signature = new String[]{"are","A","and","B","the","same"};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"Object"}),null,new VariableLocationDescription(false,0,new String[]{"Object"}),null,null};
		HCISRDeclaredFunctionAST iequalsMeth = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFunction(loadedFiles, signature, argumentTypes));
		iequalsMeth.setInstructions(usualMeths.identityEqualsMeth);
		
		//fill in standard output's blanks
		HCISRStandardOutputDefinition stdioFuncs = new HCISRStandardOutputDefinition();

		//the function to print
//SO CONFUSED
		signature = new String[]{"print","S"};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"String"})};
//System.out.print("+");
		HCISRDeclaredFunctionAST printMeth = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFunction(loadedFiles, signature, argumentTypes));
		printMeth.setInstructions(stdioFuncs.printMeth);

		signature = new String[]{"print","I"};
		//argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"String"}), new VariableLocationDescription(false,0,new String[]{"Integer"})};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"Integer"})};
//System.out.print("+");
		//argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"String", "Integer"})};
		HCISRDeclaredFunctionAST printMeth2 = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFunction(loadedFiles, signature, argumentTypes));
		printMeth2.setInstructions(stdioFuncs.printMeth);

		signature = new String[]{"print","B"};
		//argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"String"}), new VariableLocationDescription(false,0,new String[]{"Integer"})};
		argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"Boolean"})};
		//argumentTypes = new VariableLocationDescription[]{null,new VariableLocationDescription(false,0,new String[]{"String", "Integer"})};
		HCISRDeclaredFunctionAST printMeth3 = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFunction(loadedFiles, signature, argumentTypes));
//System.out.println(printMeth2);
//System.out.println(loadedFiles.size());
//for( HCISRFileAST a : loadedFiles.values() ) System.out.println(a.getParameterizedClass(a.getDefinedClass()));
		printMeth3.setInstructions(stdioFuncs.printMeth);


		//the function to println
		signature = new String[]{"println"};
		argumentTypes = new VariableLocationDescription[]{null};
		HCISRDeclaredFunctionAST printlnMeth = (HCISRDeclaredFunctionAST)(HCISRFileAST.findFunction(loadedFiles, signature, argumentTypes));
		printlnMeth.setInstructions(stdioFuncs.printlnMeth);

		//fill in String's blanks
		HCISRStringDefinition stringMeths = new HCISRStringDefinition(intClass,booleanClass,stringClass);
		//the method for charAt
		signature = new String[]{"S","charAt","B"};
		argumentTypes = new VariableLocationDescription[]{null,null,new VariableLocationDescription(false,0,new String[]{"Integer"})};
		HCISRDeclaredMethodAST charAtMeth = (HCISRDeclaredMethodAST)(stringClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		charAtMeth.setInstructions(stringMeths.charAtMeth);

		//the method for charAt
		signature = new String[]{"S","concatenates","R"};
		argumentTypes = new VariableLocationDescription[]{null,null,new VariableLocationDescription(false,0,new String[]{"String"})};
		HCISRDeclaredMethodAST concatenatesMeth = (HCISRDeclaredMethodAST)(stringClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		concatenatesMeth.setInstructions(stringMeths.concatenatesMeth);

		
		
		//fill in integer's blanks
		HCISRIntegerDefinition intMeths = new HCISRIntegerDefinition(intClass,booleanClass,stringClass);
		//the method for add
		signature = new String[]{"I","+","B"};
		argumentTypes = new VariableLocationDescription[]{null,null,new VariableLocationDescription(false,0,new String[]{"Integer"})};
		HCISRDeclaredMethodAST addMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		addMeth.setInstructions(intMeths.addMeth);
		//the method for subtract
		signature = new String[]{"I","-","B"};
		argumentTypes = new VariableLocationDescription[]{null,null,new VariableLocationDescription(false,0,new String[]{"Integer"})};
		HCISRDeclaredMethodAST subMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		subMeth.setInstructions(intMeths.subMeth);
		//the method for less than
		signature = new String[]{"I","<","B"};
		HCISRDeclaredMethodAST ltMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		ltMeth.setInstructions(intMeths.ltMeth);
		//the method for is equal
		signature = new String[]{"I","=","B"};
		HCISRDeclaredMethodAST eqMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		eqMeth.setInstructions(intMeths.eqlMeth);
		//the method for as string
		signature = new String[]{"I","as","string"};
		argumentTypes = new VariableLocationDescription[]{null,null,null};
		HCISRDeclaredMethodAST asStringMeth = (HCISRDeclaredMethodAST)(intClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		asStringMeth.setInstructions(intMeths.asStrMeth);
		
		
		
		//fill in boolean's blanks
		HCISRBooleanDefinition boolMeths = new HCISRBooleanDefinition(booleanClass, stringClass);
		//the method for as string
		signature = new String[]{"B","as","string"};
		argumentTypes = new VariableLocationDescription[]{null,null,null};
		HCISRDeclaredMethodAST boolAsStringMeth = (HCISRDeclaredMethodAST)(booleanClass.findMatchingMethod(loadedFiles, signature, argumentTypes));
		boolAsStringMeth.setInstructions(boolMeths.asStrMeth);
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
	
	protected HCISRFileAST parseFile(String resourceName) throws IOException, ParseException{
//System.out.println(resourceName);
		//first load in the main file (that will be returned)
		URL floc = ctu.getResource(resourceName);
		if(floc == null){
			throw new FileNotFoundException(resourceName);
		}
		InputStream fin = floc.openStream();
		HCISRFileAST toRet = HCISRParser.readFile(fin);
		loadedFiles.put(resourceName, toRet);
		return toRet;
	}
	
	public HCISRFileAST loadFile(String resourceName) throws IOException, ParseException{
		if(loadedFiles.containsKey(resourceName)){
			return loadedFiles.get(resourceName);
		}
		
		//a list of files to load
		ArrayList<String> toLoad = new ArrayList<String>();
		
		HCISRFileAST toRet = parseFile(resourceName);
		
		//load in some preliminary stuff
		toLoad.add(HCISRInterpreter.stringClassLoc);
		toLoad.add(HCISRInterpreter.intClassLoc);
		toLoad.add(HCISRInterpreter.floatClassLoc);
		toLoad.add(HCISRInterpreter.functionClassLoc);
		toLoad.add(HCISRInterpreter.booleanClassLoc);
		toLoad.add(HCISRInterpreter.iterableLoc);
		toLoad.add(HCISRInterpreter.iteratorLoc);
		toLoad.add(HCISRInterpreter.stdioLoc);
		toLoad.add(HCISRInterpreter.usualMethLoc);
		
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
				HCISRFileAST toAdd = parseFile(toImp);
				for(String s:toAdd.getImports()){
					toLoad.add(s);
				}
			}
		}
//System.out.println(loadedFiles.size());
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
		
		//now update the primitives, if necessary
		fillInInitialFiles();
		if(verbose>0){
			System.out.println("<===============>Printing Out AST<===============>\n");
			System.out.println(toRet.toString(0));
			System.out.println("\n<===============>End of AST<===============>");
			if(verbose>1){
				System.out.println("\n<===============>Linked Methods<===============>");
				int index=0;
				for(HCISRFileAST f:loadedFiles.values()){
					System.out.println("Linked File with index of: "+index);
					index++;
					System.out.println(f.toString(0));
				}
				System.out.println("\n<===============>End of Methods<===============>");
			}
		}
		return toRet;
	}
	
	public HCISRInterpreter(Class classToUse, int verbose1){
		ctu = classToUse;
		verbose = verbose1;
	}
}
