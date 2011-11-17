package hcisr;

public interface HCISRRunnable{
	public HCISRInstance run(HCISRStackFrame sf) throws HCISRException;
	public int getStackSize();
}