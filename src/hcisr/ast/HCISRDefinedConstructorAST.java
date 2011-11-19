package hcisr.ast;

import hcisr.HCISRException;
import hcisr.HCISRInstance;
import hcisr.HCISRStackFrame;

import java.util.ArrayList;
import java.util.HashMap;

//this class represents a defined constructor
public class HCISRDefinedConstructorAST extends HCISRConstructorAST{
	protected HCISRCodeBlockAST code;

	//look at type restrictions and the return declaration, and create any new types
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		//compile type restrictions
		for(String[] tpnm : typeRes){
			HCISRFileAST.checkForTemplateClass(imports, newClasses, tpnm);
		}
		code.compileTemplates(imports, newClasses);
	}
	
	public HCISRConstructorAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRDefinedConstructorAST(this,bindings);
	}
	
	//anything that this calls, find it
	//this has return type, and a bunch of statements to compile
	public void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> classVars,HCISRClassAST toCreate){
		toConstruct = toCreate;
		//create a copy of the variables, and add in any arguments
		HashMap<String,VariableLocationDescription> methodVars = new HashMap<String,VariableLocationDescription>();
		methodVars.putAll(classVars);
		int curLoc = 0;
		//add in the constructed variable (it's not mentioned elsewhere)
		methodVars.put(createdVariable, new VariableLocationDescription(false,curLoc,createdVariableType));
		curLoc++;
		//and then add in arguments, skipping until you see "with" (get past the type name)
		boolean skipped = false;
		System.out.println("Finding arguments for constructor");
		for(String s : sig){
			System.out.print(s + "/");
		}
		System.out.println();
		for(int i = 0;i<sig.length;i++){
			if(skipped){
				//run through the signature looking for arguments
				if(HCISRFileAST.isIdentifier(sig[i])){
					System.out.print(sig[i] + " ");
					methodVars.put(sig[i], new VariableLocationDescription(false,curLoc,typeRes[i]));
					curLoc += 1;
				}
			}
			else{
				if(sig[i].equals("with")){
					skipped = true;
				}
			}
		}
		System.out.println();
		
		//now, with the new method vars, compile the code block
		stackSize = code.compileReferences(imports, methodVars, curLoc);
	}
	
	public HCISRInstance run(HCISRStackFrame sf) throws HCISRException {
		//make the new instance
		HCISRInstance newInst = new HCISRInstance(toConstruct);
		sf.setLocation(newInst, 0);
		//and run the code
		return code.run(sf, newInst);
	}
	
	public HCISRDefinedConstructorAST(String createdVariable, String[] createdType,String[] methodSignature,String[][] typeRestrictions, HCISRCodeBlockAST commands){
		this.createdVariable = createdVariable;
		sig = methodSignature;
		code = commands;
		createdVariableType = createdType;
		typeRes = typeRestrictions;
		isDefined = true;
	}
	
	public HCISRDefinedConstructorAST(HCISRDefinedConstructorAST origin, HashMap<String,String[]> bindings){
		createdVariable = origin.createdVariable;
		createdVariableType = HCISRClassAST.replaceTypeNames(origin.createdVariableType, bindings);
		typeRes = HCISRClassAST.replaceTypeRestrictions(origin.typeRes, bindings);
		sig = HCISRClassAST.replaceConstructorTypeNames(createdVariableType, origin.sig);
		typeRes = HCISRClassAST.resizeTypeRestrictions(typeRes, origin.sig, sig);
		code = origin.code.copyWithParameters(bindings);
		isDefined = true;
	}
}