package hcisr;
import hcisr.ast.*;
import java.io.*;

public class HCISRParser{
	public static HCISRFileAST readFile(InputStream in) throws ParseException{
		return HCISR.parseFile(in);
	}
}