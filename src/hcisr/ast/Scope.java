package hcisr.ast;
import java.util.*;

//a class used to find the location of items
public class Scope{
	Scope superScope;
	ArrayList<Scope> subScopes;
	HashMap<String,VariableLocationDescription> variableLocations;
	HashMap<String,GotoTargetLocationDescription> labelLocations;
	int numStackVars = 0;
	
	public int getNumberOfStackVariables(){
		if(superScope!=null){
			return superScope.getNumberOfStackVariables();
		}
		return numStackVars;
	}
	
	public void addLabel(String label, int loc, HCISRLabelCallAST statementID){
		labelLocations.put(label, new GotoTargetLocationDescription(loc, statementID));
	}
	
	//add a new stack variable to this scope
	public void addStackVariable(String name,String[] type){
		VariableLocationDescription newVarLoc = new VariableLocationDescription(false,getNumberOfStackVariables(),type);
		variableLocations.put(name, newVarLoc);
		addStackVariable();
	}
	
	public void addStackVariable(){
		if(superScope != null){
			superScope.addStackVariable();
		}
		else{
			numStackVars += 1;
		}
	}
	
	//return the location of a label
	//note that it is not absolute, you have to check where you're jumping to
	public GotoTargetLocationDescription findLabel(String name){
		if(labelLocations.containsKey(name)){
			return labelLocations.get(name);
		}
		return superScope.findLabel(name);
	}
	
	//find the location of a variable, in either this or a parent scope
	public VariableLocationDescription findVariable(String name){
		if(variableLocations.containsKey(name)){
			return variableLocations.get(name);
		}
		else{
			return superScope.findVariable(name);
		}
	}
	
	//used for making subscopes
	public Scope(Scope supers){
		superScope = supers;
		superScope.subScopes.add(this);
		subScopes = new ArrayList<Scope>();
		variableLocations = new HashMap<String,VariableLocationDescription>();
		labelLocations = new HashMap<String,GotoTargetLocationDescription>();
	}
	
	//used for top level scopes
	public Scope(HashMap<String,VariableLocationDescription> classVarsAndArguments,int numberOfParameters){
		superScope = null;
		subScopes = new ArrayList<Scope>();
		variableLocations = new HashMap<String,VariableLocationDescription>();
		variableLocations.putAll(classVarsAndArguments);
		labelLocations = new HashMap<String,GotoTargetLocationDescription>();
		numStackVars = numberOfParameters;
	}
	
	//since the class has a two way association, need a way to remove
	public void kill(){
		superScope = null;
		for(Scope s : subScopes){
			s.kill();
		}
		subScopes = null;
		variableLocations = null;
	}
}