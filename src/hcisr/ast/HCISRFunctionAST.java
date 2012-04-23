package hcisr.ast;
import hcisr.*;
import java.util.*;

public abstract class HCISRFunctionAST implements HCISRRunnable{
	public boolean isDefined;
	protected String[] sig;
	protected String[][] typeRes;
	protected int stackSize;
	public int getStackSize(){
		return stackSize;
	}
	
//GS TEST
	public void print(){
		for( String s : sig )  System.out.print(" " + s);
		System.out.println();
	}

	//global data location
	protected HCISRFunctionFileAST globalDataLoc;
	
	public abstract void compileTemplates(HashMap<String,HCISRFileAST> imports,ArrayList<HCISRClassAST> newClasses);
	public abstract void compileReferences(HashMap<String,HCISRFileAST> imports,HashMap<String,VariableLocationDescription> globalDataDescription, HCISRFunctionFileAST globalData);
<<<<<<< HEAD
	public abstract String toString(int tabCount);
=======
>>>>>>> 56109887d363a710257c4b5983f7112781c2c525
}
