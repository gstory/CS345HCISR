README

=========><>

HCISR: Holy crap it shoots rockets!

=========><>

Authors:
Benjamin Crysup
Benjamin Harris

=========><>

*** Changed! - call "javac RunHCISR.java" instead of -call "javac HCISR.java" ***

To Run:
 - go to HCISR/SRC/
 - call javac RunHCISR.java
	note that, if you go to HCISR/BIN/, you may skip compilation
 - call java RunHCISR ProgramName.hcisr
	where ProgramName.hcisr is a text file containing a HCISR program
	some premade examples are
		HelloWorld.hcisr
		HCISRMergeSort.hcisr
		CheckGoTo.hcisr
 - note that not all .hcisr files are runnable. Those containing functions or classes cannot be run.

=========><>

Some notes on file structure

everything in hcisr is used to run the code (the only important thing above hcisr is the runnable, RunHCISR)
everything in hcisr/ast is used to construct the ast (or to compile the references in the ast)
everything in hcisr/lib is library code, either implemented entirely in hcisr code or supplemented with java plugins (for instance, HCISRBoolean.hcisr is supplemented with HCISRBooleanDefinition.java) 
the most important files to look at may be
	hcisr/Parser.jj
	hcisr/Interpreter.java
	hcisr/ast/HCISRFileAST.java

=========><>



