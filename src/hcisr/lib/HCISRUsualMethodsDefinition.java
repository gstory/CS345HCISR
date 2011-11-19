package hcisr.lib;
import hcisr.*;
import hcisr.ast.*;

//a collection of external methods for booleans
public class HCISRUsualMethodsDefinition{
	public HCISRExternalCodeBlock identityEqualsMeth;
	public HCISRUsualMethodsDefinition(HCISRClassAST boolClass){
		identityEqualsMeth = new HCISRUsualMethodsIEquals(boolClass);
	}
}

class HCISRUsualMethodsIEquals extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRUsualMethodsIEquals(HCISRClassAST booleanEquals){
		boolClass = booleanEquals;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		boolean areSame = i1 == i2;
		System.out.println(areSame);
		HCISRInstance ir = new HCISRInstance(boolClass);
		ir.addExternalVariables(new HCISRInstanceBooleanVars(areSame));
		return ir;
	}
}