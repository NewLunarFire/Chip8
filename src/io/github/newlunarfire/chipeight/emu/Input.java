/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

/**
 * @author tommy
 *
 */
public class Input {
	public byte key[];
	
	public Input()
	{
		key = new byte[16];
	}
	
	public void initialize()
	{
		for(int i = 0; i < 16; i++)
			key[i] = 0;
	}
	
	public int waitForKeypress()
	{
		int k = -1;
		
		while(k == -1)
		{
			for(int i = 0; i < 16; i++)
			{
				if(key[i] == 1)
					k = i;
			}
		}
		
		return k;
	}
}
