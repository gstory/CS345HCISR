package hcisr.lib;
import hcisr.*;
import hcisr.ast.*;

//a collection of external methods for booleans
public class HCISRStringDefinition{
	public HCISRExternalCodeBlock charAtMeth;
	public HCISRExternalCodeBlock concatenatesMeth;
	
	public HCISRStringDefinition(HCISRClassAST integerClass,HCISRClassAST booleanClass,HCISRClassAST stringClass){
		charAtMeth = new HCISRStringCharAt(stringClass);
		concatenatesMeth = new HCISRStringConcatenates(stringClass);
	}
}


class HCISRStringCharAt extends HCISRExternalCodeBlock{
	HCISRClassAST stringClass;
	public HCISRStringCharAt(HCISRClassAST strClass){
		stringClass=strClass;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		
		String i1v = ((HCISRInstanceStringVars)(i1.getExternalVariables())).value;
		int i2v = ((HCISRInstanceIntegerVars)(i2.getExternalVariables())).value;
		String result = i1v.charAt(i2v)+"";
		HCISRInstance sr = new HCISRInstance(stringClass);
		sr.addExternalVariables(new HCISRInstanceStringVars(result));
		return sr;
	}
}

class HCISRStringConcatenates extends HCISRExternalCodeBlock{
	HCISRClassAST stringClass;
	public HCISRStringConcatenates(HCISRClassAST string1Class){
		stringClass=string1Class;
	}
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance i1 = sf.getLocation(0);
		HCISRInstance i2 = sf.getLocation(1);
		
		String i1v = ((HCISRInstanceStringVars)(i1.getExternalVariables())).value;
		String i2v = ((HCISRInstanceStringVars)(i2.getExternalVariables())).value;
		String result = "" + i1v + i2v;
		HCISRInstance sr = new HCISRInstance(stringClass);
		sr.addExternalVariables(new HCISRInstanceStringVars(result));
		return sr;
	}
}
