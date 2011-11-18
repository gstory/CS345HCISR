package hcisr.lib;
import hcisr.*;
import hcisr.ast.*;

//a collection of external methods for booleans
public class HCISRBooleanDefinition{
	public HCISRExternalCodeBlock andMeth;
	public HCISRExternalCodeBlock orMeth;
	public HCISRExternalCodeBlock notMeth;
	
	public HCISRBooleanDefinition(HCISRClassAST booleanClass){
		andMeth = new HCISRBooleanAnd(booleanClass);
		orMeth = new HCISRBooleanOr(booleanClass);
		notMeth = new HCISRBooleanNot(booleanClass);
	}
}

class HCISRBooleanAnd extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRBooleanAnd(HCISRClassAST booleanClass){
		boolClass = booleanClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		//and has two things on the stack frame
		HCISRInstance b1 = sf.getLocation(0);
		HCISRInstance b2 = sf.getLocation(1);
		boolean b1v = ((HCISRInstanceBooleanVars)(b1.getExternalVariables())).value;
		boolean b2v = ((HCISRInstanceBooleanVars)(b2.getExternalVariables())).value;
		boolean brv = b1v && b2v;
		HCISRInstance br = new HCISRInstance(boolClass);
		br.addExternalVariables(new HCISRInstanceBooleanVars(brv));
		return br;
	}
}

class HCISRBooleanOr extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRBooleanOr(HCISRClassAST booleanClass){
		boolClass = booleanClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance b1 = sf.getLocation(0);
		HCISRInstance b2 = sf.getLocation(1);
		boolean b1v = ((HCISRInstanceBooleanVars)(b1.getExternalVariables())).value;
		boolean b2v = ((HCISRInstanceBooleanVars)(b2.getExternalVariables())).value;
		boolean brv = b1v || b2v;
		HCISRInstance br = new HCISRInstance(boolClass);
		br.addExternalVariables(new HCISRInstanceBooleanVars(brv));
		return br;
	}
}

class HCISRBooleanNot extends HCISRExternalCodeBlock{
	HCISRClassAST boolClass;
	public HCISRBooleanNot(HCISRClassAST booleanClass){
		boolClass = booleanClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance b1 = sf.getLocation(0);
		boolean b1v = ((HCISRInstanceBooleanVars)(b1.getExternalVariables())).value;
		boolean brv = !b1v;
		HCISRInstance br = new HCISRInstance(boolClass);
		br.addExternalVariables(new HCISRInstanceBooleanVars(brv));
		return br;
	}
}