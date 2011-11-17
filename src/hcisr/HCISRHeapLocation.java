package hcisr;

//an interface to represent a heap location
public interface HCISRHeapLocation{
	public HCISRInstance getLocation(int loc);
	public void setLocation(HCISRInstance toAdd, int loc);
}