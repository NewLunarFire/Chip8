/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

/**
 * @author tommy
 *
 */
public class DelayTimer {
	private int value;
	
	public void initialize()
	{
		value = 0;
	}
	
	public int getValue()
	{
		return value & 0xFF;
	}
	
	public void setValue(int v)
	{
		value = v & 0xFF;
	}
	
	public void update()
	{
		if(value != 0)
			value--;
	}
}
