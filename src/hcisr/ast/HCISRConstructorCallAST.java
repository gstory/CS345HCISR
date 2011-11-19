package hcisr.ast;

import hcisr.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//this class represents a function call
public class HCISRConstructorCallAST extends HCISRStatementAST{
	//the function signature
	public String[] argumentIDs; //includes typeID
	//the class
	protected String[] typeID;
	
	//what is the function to call
	protected HCISRConstructorAST toCall;
	//a description of where to find the arguments for the function
	protected boolean[] specialVar; //if a variable is special, it is located on the attached list
	protected int[] arrayIndex;
	
	//a constructor call may define a new type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, typeID);
	}
	
	//just make a new copy
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRConstructorCallAST(this, bindings);
	}
	
	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	//look for the constructor and collect argument locations
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		//construct argument types (should parallel argumentIDs
		VariableLocationDescription[] argumentTypes = new VariableLocationDescription[argumentIDs.length];
		boolean foundWith = false;
		int numArgs = 0;
		for(int i = 0; i<argumentIDs.length;i++){
			if(foundWith){
				if(HCISRFileAST.isIdentifier(argumentIDs[i])){
					numArgs++;
					VariableLocationDescription curArg = currentScope.findVariable(argumentIDs[i]);
					argumentTypes[i] = curArg;
				}
			}
			else{
				if(argumentIDs[i].equals("with")){
					foundWith = true;
				}
			}
		}
		//find the constructor to call
		toCall = HCISRFileAST.findConstructor(imports, argumentIDs, typeID, argumentTypes);
		//now, find out where the arguments are
		//note that the created variable is always in slot 0, and the arguments are in order on the stack
		foundWith = false;
		specialVar = new boolean[numArgs];
		arrayIndex = new int[numArgs];
		int curArgNum = 0;
		for(int i = 0; i<argumentIDs.length;i++){
			if(foundWith){
				if(HCISRFileAST.isIdentifier(argumentIDs[i])){
					VariableLocationDescription curArg = currentScope.findVariable(argumentIDs[i]);
					specialVar[curArgNum] = curArg.special;
					arrayIndex[curArgNum] = curArg.location;
					curArgNum++;
				}
			}
			else{
				if(argumentIDs[i].equals("with")){
					foundWith = true;
				}
			}
		}
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException{
		//print constructor name
		System.out.print("Running Constructor ");
		for(String s : argumentIDs){
			System.out.print(s + " ");
		}
		System.out.println();
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
		//now run the constructor and return
		return toCall.run(nsf);
	}
	
	public HCISRConstructorCallAST(String[] methodSignature,String[] typeIdentifier){
		argumentIDs = methodSignature;
		typeID = typeIdentifier;
	}
	
	public HCISRConstructorCallAST(HCISRConstructorCallAST origin,HashMap<String,String[]> bindings){
		typeID = HCISRClassAST.replaceTypeNames(origin.typeID, bindings);
		argumentIDs = HCISRClassAST.replaceConstructorTypeNames(typeID, origin.argumentIDs);
	}
}