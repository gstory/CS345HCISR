package hcisr.ast;

public class VariableLocationDescription{
	public boolean special;
	public int location;
	public String[] typeNm;
	
	public VariableLocationDescription(boolean spec, int loc, String[] typeName){
		special = spec;
		location = loc;
		typeNm = typeName;
	}
}