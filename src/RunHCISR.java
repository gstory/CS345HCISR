import hcisr.*;
import hcisr.ast.*;
import java.io.*;

public class RunHCISR{
	public static void main(String[] args) throws ParseException,IOException,HCISRException{
		RunHCISR toGetClass = new RunHCISR();
		Class ctu = toGetClass.getClass();
		String loc = args[0];
		HCISRInterpreter interpreter = new HCISRInterpreter(ctu);
		HCISRFileAST toRun = interpreter.loadFile(loc);
		interpreter.runProgram(toRun);
	}
}