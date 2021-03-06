package hcisr.lib;
import hcisr.*;
import hcisr.ast.*;

//a collection of external methods for booleans
public class HCISRIntegerDefinition{
	public HCISRExternalCodeBlock addMeth;
	public HCISRExternalCodeBlock subMeth;
	public HCISRExternalCodeBlock multMeth;
	public HCISRExternalCodeBlock divMeth;
	public HCISRExternalCodeBlock modMeth;
	public HCISRExternalCodeBlock ltMeth;
	public HCISRExternalCodeBlock gtMeth;
	public HCISRExternalCodeBlock asStrMeth;
	public HCISRExternalCodeBlock eqlMeth;
	public HCISRExternalCodeBlock setMeth;
	public HCISRExternalCodeBlock identMeth;
	
	public HCISRIntegerDefinition(HCISRClassAST integerClass,HCISRClassAST booleanClass,HCISRClassAST stringClass){
		addMeth = new HCISRIntegerAdd(integerClass);
		subMeth = new HCISRIntegerSub(integerClass);
		multMeth = new HCISRIntegerMult(integerClass);
		divMeth = new HCISRIntegerDiv(integerClass);
		modMeth = new HCISRIntegerMod(integerClass);
		ltMeth = new HCISRIntegerLessThan(booleanClass);
		gtMeth = new HCISRIntegerGreaterThan(booleanClass);
		asStrMeth = new HCISRIntegerAsString(stringClass);
		eqlMeth = new HCISRIntegerEquals(booleanClass);
		setMeth = new HCISRIntegerSet(integerClass);
		identMeth = new HCISRIntegerIdent(integerClass);
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

class HCISRIntegerSub extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerSub(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		int irv = i1v - i2v;
		HCISRInstance ir = new HCISRInstance(i1.getHCISRClass());
		ir.addExternalVariables(new HCISRInstanceIntegerVars(irv));
		return ir;
	}
}

class HCISRIntegerMult extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerMult(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		int irv = i1v * i2v;
		HCISRInstance ir = new HCISRInstance(i1.getHCISRClass());
		ir.addExternalVariables(new HCISRInstanceIntegerVars(irv));
		return ir;
	}
}

class HCISRIntegerDiv extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerDiv(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		int irv = i1v / i2v;
		HCISRInstance ir = new HCISRInstance(i1.getHCISRClass());
		ir.addExternalVariables(new HCISRInstanceIntegerVars(irv));
		return ir;
	}
}

class HCISRIntegerMod extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerMod(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		int irv = i1v % i2v;
		HCISRInstance ir = new HCISRInstance(i1.getHCISRClass());
		ir.addExternalVariables(new HCISRInstanceIntegerVars(irv));
		return ir;
	}
}

class HCISRIntegerSet extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerSet(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
//System.out.println("___"+i2v+"___");
		int irv = i2v;
		HCISRInstance ir = new HCISRInstance(i1.getHCISRClass());
		ir.addExternalVariables(new HCISRInstanceIntegerVars(irv));
		return ir;
	}
}

class HCISRIntegerIdent extends HCISRExternalCodeBlock{
	HCISRClassAST intClass;
	public HCISRIntegerIdent(HCISRClassAST integerClass){
		intClass = integerClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
//		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
//		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		int irv = i1v;
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

class HCISRIntegerGreaterThan extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRIntegerGreaterThan(HCISRClassAST booleanClass){
		boolClass = booleanClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		//get the values
		int i1v = ((HCISRInstanceIntegerVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		boolean brv = i1v > i2v;
		HCISRInstance br = new HCISRInstance(boolClass);
		br.addExternalVariables(new HCISRInstanceBooleanVars(brv));
		return br;
	}// something
}

