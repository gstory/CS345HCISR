package hcisr.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

//this class allows a user to describe what a function returns without defining the function
public class HCISRReturnsDeclarationAST{
	protected String[] typeID;
	
	public void compileTemplates(HashMap<String, HCISRFileAST> imports, ArrayList<HCISRClassAST> newClasses) {
		//watch out for Nothing
		if(typeID.length==1){
			if(typeID[0].equals("Nothing")){
				return;
			}
		}
		HCISRFileAST.checkForTemplateClass(imports, newClasses, typeID);
	}
	
	public HCISRReturnsDeclarationAST copyWithParameters(HashMap<String, String[]> bindings) {
		return new HCISRReturnsDeclarationAST(this, bindings);
	}
	
	public HCISRReturnsDeclarationAST(String[] typeDesc){
		typeID = typeDesc;
	}
	
	public HCISRReturnsDeclarationAST(HCISRReturnsDeclarationAST origin, HashMap<String,String[]> bindings){
		typeID = HCISRClassAST.replaceTypeNames(origin.typeID, bindings);
	}

	public String toString(int tabCount){
		String result="";
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result+= "Starting ReturnsDeclarationAST\n";
		tabCount++;
			for(int i=0; i< tabCount;i++)
				result+="\t";
			result+="return type Id of "+ Arrays.toString(typeID)+"\n";
		tabCount--;
		for(int i=0; i < tabCount; i++)
			result +="\t";
		result += "Ending ReturnsDeclarationAST\n";
		return result;
	}
}
