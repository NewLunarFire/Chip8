/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

/**
 * @author tommy
 *
 */
public class Stack {
	public int stack[];
	public int pointer;
	
	public Stack()
	{
		//The CHIP8 has a 16-level stack
		stack = new int[16];
	}
	
	public void initialize()
	{
		//Simply initialize the stack pointer to 0
		pointer = 0;
	}
	
	//Pushes an value on the stack
	public void push(int value)
	{
		stack[pointer] = value;
		pointer++;
	}
	
	//Takes a value from the top of the stack
	public int pop()
	{
		pointer--;
		return stack[pointer];
	}
	
	
}
