package hcisr;
import hcisr.ast.*;

public class HCISRGotoException extends Exception{
	public int line;
	public HCISRStatementAST target;
	public HCISRGotoException(int lineToGoTo, HCISRStatementAST toGoTo){
		line = lineToGoTo;
		target = toGoTo;
	}
}