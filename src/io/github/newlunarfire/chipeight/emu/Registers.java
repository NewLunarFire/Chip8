/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

/**
 * @author tommy
 *
 */
public class Registers {
	public int v[]; //16 V registers
	public int i; //Index
	public int pc; //Program Counter
	
	public Registers()
	{
		v = new int[16];
	}
	
	public void initialize()
	{
		for(int j = 0; j < 16; j++)
			v[j] = 0;
		
		i = 0;
		pc = 0x200;
	}
}
