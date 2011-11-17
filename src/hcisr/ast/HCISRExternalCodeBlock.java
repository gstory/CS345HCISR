package hcisr.ast;
import hcisr.*;

//a class that allows external code to be added
public abstract class HCISRExternalCodeBlock{
	public abstract HCISRInstance run(HCISRStackFrame sf,HCISRHeapLocation hl);
}