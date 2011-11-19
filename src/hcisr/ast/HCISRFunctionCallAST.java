package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRHeapLocation;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a function call
public class HCISRFunctionCallAST extends HCISRStatementAST{
	//the function signature
	public String[] argumentIDs;
	
	//what is the function to call
	protected HCISRFunctionAST toCall;
	//a description of where to find the arguments for the function
	protected boolean[] specialVar;
	protected int[] arrayIndex;
	
	//function calls do not define new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		
	}
	
	//just make a new copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRFunctionCallAST(argumentIDs);
	}

	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	//look for the function and collect argument locations
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//construct argument types (should parallel argumentIDs
		VariableLocationDescription[] argumentTypes = new VariableLocationDescription[argumentIDs.length];
		int numArgs = 0;
		System.out.println("Finding arguments for :");
		for(String s : argumentIDs){
			System.out.print(s + " ");
		}
		System.out.println();
		for(int i = 0; i<argumentIDs.length;i++){
			if(HCISRFileAST.isIdentifier(argumentIDs[i])){
				numArgs++;
				System.out.print(argumentIDs[i] + " ");
				VariableLocationDescription curArg = currentScope.findVariable(argumentIDs[i]);
				argumentTypes[i] = curArg;
			}
		}
		System.out.println();
		//find the function to call
		toCall = HCISRFileAST.findFuntion(imports, argumentIDs, argumentTypes);
		//now, find out where the arguments are
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
		//first, make a stack frame
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
	
	public HCISRFunctionCallAST(String[] methodSignature){
		argumentIDs = methodSignature;
	}
}