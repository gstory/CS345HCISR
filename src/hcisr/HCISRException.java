package hcisr;
import hcisr.ast.*;

public class HCISRException extends Exception{
	HCISRInstance exc;
	
	public HCISRClassAST getType(){
		return exc.type;
	}
	
	public HCISRException(String errorMessage,HCISRInstance scriptedException){
		super(errorMessage);
		exc = scriptedException;
	}
}