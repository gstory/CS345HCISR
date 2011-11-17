package hcisr.ast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import hcisr.*;

//this class represents a define call in HCISR
public class HCISRDefineCallAST extends HCISRStatementAST{
	protected String[] typeName;
	protected String varName;
	protected String initVal;
	protected int initType;
	
	//what is the initial value
	protected HCISRInstance initialVal;
	//and where should it be put (will be on stack)
	protected int arrayIndex;
	
	//this may introduce a new type
	public void compileTemplates(HashMap<String, HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses) {
		HCISRFileAST.checkForTemplateClass(imports, newClasses, typeName);
	}
	
	//and may need some type replacement
	public HCISRStatementAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRDefineCallAST(this,bindings);
	}
	
	//there are no goto statements here, do nothing
	public void compileLabelReferences(Scope currentScope, Iterator<Scope> subScopes) {
		
	}
	
	//add the variable to the scope (and figure out the initial value)
	public void compileReferences(HashMap<String,HCISRFileAST> imports,Scope currentScope, int line) {
		currentScope.addStackVariable(varName,typeName);
		arrayIndex = currentScope.findVariable(varName).location;
	}
	
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl) throws HCISRException{
		sf.setLocation(HCISRVariableAST.getInitialValue(initType, initVal), arrayIndex);
		return null;
	}
	
	public HCISRDefineCallAST(String[] typeIdentifier, String variableName, String initialValue, int initialType){
		typeName = typeIdentifier;
		varName = variableName;
		initVal = initialValue;
		initType = initialType;
	}
	
	public HCISRDefineCallAST(HCISRDefineCallAST origin,HashMap<String,String[]> bindings){
		typeName = HCISRClassAST.replaceTypeNames(origin.typeName, bindings);
		varName = origin.varName;
		initVal = origin.initVal;
		initType = origin.initType;
	}
}