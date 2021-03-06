package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRHeapLocation;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a method call
public class HCISRMethodCallAST extends HCISRStatementAST{
	//the method signature
	protected String[] argumentIDs;
	HCISRClassAST callingClass;

	//where inside the class for the calling object is the definition of the method
	protected int methodIndex;
	//a description of where to find the arguments for the method (starting with the calling instance)
	protected boolean[] specialVar;
	protected int[] arrayIndex;

	//methods do not introduce new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {

	}

	//just do a shallow copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRMethodCallAST(argumentIDs);
	}


	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {

	}

	//look for the method and collect argument locations
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//construct argument types (should parallel argumentIDs
		VariableLocationDescription[] argumentTypes = new VariableLocationDescription[argumentIDs.length];
		int numArgs = 0;
		for(int i = 0; i<argumentIDs.length;i++){
			if(HCISRFileAST.isIdentifier(argumentIDs[i])){
				numArgs++;
				VariableLocationDescription curArg = currentScope.findVariable(argumentIDs[i]);
				argumentTypes[i] = curArg;
			}
		}
		//first, find the type of the calling variable
		String[] callingType = argumentTypes[0].typeNm;
		callingClass = HCISRFileAST.findBaseClass(imports, callingType[0]);
		if(callingClass.isTemplate()){
			callingClass = callingClass.getParameterizedClass(callingType);
		}
		//find the method to call
		methodIndex = HCISRFileAST.findMethodIndex(imports, argumentIDs, callingType, argumentTypes);
		//now, find out where the arguments are
		//note that the calling variable is always in slot 0, and the arguments are in order on the stack
		specialVar = new boolean[numArgs];
		arrayIndex = new int[numArgs];
		int curArgNum = 0;
		for(int i = 0; i<argumentIDs.length;i++){
			if(HCISRFileAST.isIdentifier(argumentIDs[i])){
				VariableLocationDescription curArg = currentScope.findVariable(argumentIDs[i]);
				specialVar[curArgNum] = curArg.special;
				arrayIndex[curArgNum] = curArg.location;
				curArgNum++;
			}
		}
	}

	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException{
		if(methodIndex==-1)
		{
			System.out.println("Undefined Method Call" );
			System.exit(-1);
			return(null);
		}
		else
		{	
			//first, find the method to run
			HCISRInstance calling;
			if(specialVar[0]){
				calling = hl.getLocation(arrayIndex[0]);
			}
			else{
				calling = sf.getLocation(arrayIndex[0]);
			}
			if(calling==null){
				throw new NullPointerException("Variable " + argumentIDs[0] + " is null/Nothing.");
			}
			HCISRMethodAST toCall = calling.getHCISRClass().methodList[methodIndex];
			//make a stack frame
			HCISRStackFrame nsf = new HCISRStackFrame(toCall.getStackSize());
			//and add values to it
			for(int i = 0;i<specialVar.length;i++){
				if(specialVar[i]){
					nsf.setLocation(hl.getLocation(arrayIndex[i]), i);
				}
				else{
					nsf.setLocation(sf.getLocation(arrayIndex[i]), i);
				}
			}
			//now run the function and return
			return toCall.run(nsf);
		}
	}

	public HCISRMethodCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting MethodCallAST\n";
		tabCount++;
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Method Index:"+methodIndex+"\n";
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Argument IDs:"+Arrays.toString(argumentIDs)+"\n";
			for(int i=0; i < tabCount; i++)
				result +="\t";
			result+= "Class:\n";
			tabCount++;
				result+= callingClass.toString(tabCount);
			tabCount--;		
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending MethodCallAST\n";
		return result;
	}
}
