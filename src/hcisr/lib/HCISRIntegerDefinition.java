package hcisr.lib;
import hcisr.*;
import hcisr.ast.*;

//a collection of external methods for booleans
public class HCISRIntegerDefinition{
	public HCISRExternalCodeBlock addMeth;
	public HCISRExternalCodeBlock ltMeth;
	public HCISRExternalCodeBlock asStrMeth;
	public HCISRExternalCodeBlock eqlMeth;
	
	public HCISRIntegerDefinition(HCISRClassAST integerClass,HCISRClassAST booleanClass,HCISRClassAST stringClass){
		addMeth = new HCISRIntegerAdd(integerClass);
		ltMeth = new HCISRIntegerLessThan(booleanClass);
		asStrMeth = new HCISRIntegerAsString(stringClass);
		eqlMeth = new HCISRIntegerEquals(booleanClass);
	}
}

class HCISRIntegerAsString extends HCISRExternalCodeBlock{
	HCISRClassAST strClass;
	public HCISRIntegerAsString(HCISRClassAST stringClass){
		strClass = stringClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance i1 = sf.getLocation(0);
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		HCISRInstance sr = new HCISRInstance(strClass);
		sr.addExternalVariables(new HCISRInstanceStringVars(Integer.toString(i1v)));
		return sr;
	}
}

class HCISRIntegerAdd extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerAdd(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		int irv = i1v + i2v;
		HCISRInstance ir = new HCISRInstance(i1.getHCISRClass());
		ir.addExternalVariables(new HCISRInstanceIntegerVars(irv));
		return ir;
	}
}

class HCISRIntegerEquals extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRIntegerEquals(HCISRClassAST booleanClass){
		boolClass = booleanClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		boolean brv = i1v == i2v;
		HCISRInstance br = new HCISRInstance(boolClass);
		br.addExternalVariables(new HCISRInstanceBooleanVars(brv));
		return br;
	}
}

class HCISRIntegerLessThan extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRIntegerLessThan(HCISRClassAST booleanClass){
		boolClass = booleanClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		boolean brv = i1v < i2v;
		HCISRInstance br = new HCISRInstance(boolClass);
		br.addExternalVariables(new HCISRInstanceBooleanVars(brv));
		return br;
	}// something
}