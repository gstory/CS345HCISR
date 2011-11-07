package hcisr;
import hcisr.ast.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class HCISRInterpreter{
	HashMap<String,HCISRFileAST> loadedFiles = new HashMap<String,HCISRFileAST>();
	
	public HCISRFileAST loadFile(Class ctu,String resourceName) throws IOException{
		if(loadedFiles.containsKey(resourceName)){
			return loadedFiles.get(resourceName);
		}
		
		//a list of files to load
		ArrayList<String> toLoad = new ArrayList<String>();
		
		//first load in the main file (that will be returned)
		URL floc = ctu.getResource(resourceName);
		if(floc == null){
			throw new FileNotFoundException();
		}
		InputStream fin = floc.openStream();
		HCISRFileAST toRet = HCISRParser.readFile(fin);
		loadedFiles.put(resourceName, toRet);
		//now load in linked files
		for(String toImp : toRet.getImports()){
			toLoad.add(toImp);
		}
		while(toLoad.size()>0){
			String toImp = toLoad.remove(toLoad.size()-1);
			//if it's already loaded, do not load the new file
			if(loadedFiles.containsKey(toImp)){}
			else{
				//otherwise, load
				floc = ctu.getResource(toImp);
				if(floc == null){
					throw new FileNotFoundException();
				}
				fin = floc.openStream();
				HCISRFileAST toAdd = HCISRParser.readFile(fin);
				loadedFiles.put(toImp, toAdd);
				for(String s:toAdd.getImports()){
					toLoad.add(s);
				}
			}
		}
		
		//now run through anything that's not a template and look for parameterized types
		ArrayList<HCISRClassAST> toFurtParam = new ArrayList<HCISRClassAST>();
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileTemplates(loadedFiles, toFurtParam);
		}
		while(toFurtParam.size()>0){
			HCISRClassAST c = toFurtParam.get(toFurtParam.size()-1);
			c.compileTemplates(loadedFiles, toFurtParam);
		}
		
		//now resolve the class hierarchy
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileHierarchy(loadedFiles);
		}
		
		//now resolve references
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileReferences(loadedFiles);
		}
		
		//now check types
		for(HCISRFileAST f:loadedFiles.values()){
			f.compileTypeChecking(loadedFiles);
		}
		
		return toRet;
	}
}