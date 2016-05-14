/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

/**
 * @author Tommy Savaria
 *
 */
public class GraphicsSystem {
	public byte memory[][];
	
	public GraphicsSystem()
	{
		memory = new byte[64][32];
	}
	
	public void initialize()
	{
		//Clear display
		this.clear();
	}
	
	public void clear()
	{
		//Write all pixels to 0
		for(int i = 0; i < 64; i++)
		{
			for(int j = 0; j < 32; j++)
				memory[i][j] = 0;
		}
	}
	
	public boolean drawLine(int x, int y, int val)
	{
		boolean collision = false;
		
		//if(y >= 32)
			//y %= 32;
		
		for(int i = 7; i >= 0; i--, x++)
		{
			//if(x >= 64)
				//x %= 64;
			
			//Determine pixel status
			int p = (val >> i) & 1;
			
			//Xor value
			//Detect collision
			if((x < 64) && (y < 32))
			{
				if((memory[x][y] == 1) && (p == 1))
				{
					memory[x][y] = 0;
					collision = true;
				}	
				else if(((memory[x][y] == 1) && (p == 0)) || ((memory[x][y] == 0) && (p == 1)))
				{
					memory[x][y] = 1;
				}
				else
				{
					memory[x][y] = 0;
				}
			}
		}
		
		return collision;
	}
}
