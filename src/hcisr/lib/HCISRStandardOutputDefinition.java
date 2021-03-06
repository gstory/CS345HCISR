package hcisr.lib;
import hcisr.*;
import hcisr.ast.*;

//a collection of external methods for booleans
public class HCISRStandardOutputDefinition{
	public HCISRExternalCodeBlock printMeth;
	public HCISRExternalCodeBlock printlnMeth;
	public HCISRStandardOutputDefinition(){
		printMeth = new HCISRStandardOutputPrint();
		printlnMeth = new HCISRStandardOutputPrintln();
	}
}

class HCISRStandardOutputPrint extends HCISRExternalCodeBlock{
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		HCISRInstance i1 = sf.getLocation(0);
		
		//String toP = ((HCISRInstanceStringVars)(i1.getExternalVariables())).value;
		//System.out.print(toP);
		//System.out.print(i1.getExternalVariables());
		System.out.print(i1.getExternalVariables());

		/*System.out.print("   // ");
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		System.out.print(stackTraceElements[1].getClassName() + " | " + stackTraceElements[1].getFileName() + " | " + stackTraceElements[1].getMethodName() );
		System.out.println();*/
 
		return null;
	}
}

class HCISRStandardOutputPrintln extends HCISRExternalCodeBlock{
	public HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl){
		System.out.println();
		return null;
	}
}
