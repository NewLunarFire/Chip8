/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
* @author      Tommy Savaria <tommy.savaria@protonmail.ch>
* @version     0.1
*/

public class Chip8 {
	public Registers registers;
	public Memory memory;
	public Stack stack;
	
	public GraphicsSystem graphics;
	public Input input;
	
	private Timer timer;
	public DelayTimer delaytimer;
	public SoundTimer soundtimer;
	public boolean timersFreeze;
	
	private Random random;
	
	public Chip8()
	{
		//Create all objects
		registers = new Registers();
		memory = new Memory();
		stack = new Stack();
		graphics = new GraphicsSystem();
		input = new Input();
		delaytimer = new DelayTimer();
		soundtimer = new SoundTimer();
		random = new Random();
	}
	
	/**
	 * Initializes the system to the default state
	 */
	public void initialize()
	{
		//Initialize all components
		memory.initialize();
		registers.initialize();
		stack.initialize();
		graphics.initialize();
		input.initialize();
		delaytimer.initialize();
		soundtimer.initialize();
		
		//Setup Timer
		timer = new Timer();
		timer.scheduleAtFixedRate(new Chip8TimerTask(), 0, 1000/60);
		timersFreeze = false;
	}
	
	public void freeze()
	{
		//Freezes the timers for the time being
		timersFreeze = true;
	}
	
	public void unfreeze()
	{
		//Unfreeze the timers
		timersFreeze = false;
	}
	/**
	 * Loads a program in memory
	 * @param f File Handle
	 */
	public void loadRom(File f)
	{
		//Load game into memory
		memory.loadFile(f.getAbsolutePath());
	}
	
	/**
	 * Gracefully ends emulation, returning the emulator
	 * to the uninitialized state
	 */
	public void terminate()
	{
		//Create RAM and Graphics Memory
		memory.clear();
		graphics.clear();
		
		//Stop Timer Thread
		timer.cancel();
	}
	
	/**
	 * Emulates one CHIP8 cycle
	 */
	public void emulateCycle()
	{
		//Fetch instruction
		int opcode = memory.fetchShort(registers.pc) & 0xFFFF;
		registers.pc += 2;
		
		//Decode and execute instruction
		executeOpcode(opcode);
	}
	
	/**
	 * Decode and Excute an opcode
	 * 
	 * This functions decodes the opcode and calls
	 * the appropriate function
	 * 
	 * @param oc Opcode to decode and execute
	 */
	public void executeOpcode(int oc)
	{
		switch((oc >> 12) & 0xF)
		{
		case 0:
			if(oc == 0x00E0)
				this.cls();
			else if(oc == 0x00EE)
				this.ret();
			else
				this.sys(oc & 0x0FFF);
			break;
		case 1:
			this.jmp_imm(oc & 0x0FFF);
			break;
		case 2:
			this.call(oc & 0x0FFF);
			break;
		case 3:
			this.se_imm((oc >> 8) & 0xF, oc & 0xFF);
			break;
		case 4:
			this.sne_imm((oc >> 8) & 0xF, oc & 0xFF);
			break;
		case 5:
			this.se_r((oc >> 8) & 0xF, (oc >> 4) & 0xF);
			break;
		case 6:
			this.ld_imm((oc >> 8) & 0xF, oc & 0xFF);
			break;
		case 7:
			this.add_imm((oc >> 8) & 0xF, oc & 0xFF);
			break;
		case 8:
			switch(oc & 0xF)
			{
			case 0:
				this.ld_r((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 1:
				this.or((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 2:
				this.and((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 3:
				this.xor((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 4:
				this.add_r((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 5:
				this.sub((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 6:
				this.shr((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 7:
				this.subn((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			case 14:
				this.shl((oc >> 8) & 0xF, (oc >> 4) & 0xF);
				break;
			}
			break;
		case 9:
			if((oc & 0xF) == 0)
				this.sne_r((oc >> 8) & 0xF, (oc >> 4) & 0xF);
			break;
		case 10:
			this.ld_idx(oc & 0xFFF);
			break;
		case 11:
			this.jmp_v0(oc & 0xFFF);
			break;
		case 12:
			this.rnd((oc >> 8) & 0xF, oc & 0xFF);
			break;
		case 13:
			this.drw((oc >> 8) & 0xF, (oc >> 4) & 0xF, oc & 0xF);
			break;
		case 14:
			if((oc & 0xFF) == 0x9E)
				this.skp((oc >> 8) & 0xF);
			else if((oc & 0xFF) == 0xA1)
				this.sknp((oc >> 8) & 0xF);
			break;
		case 15:
			switch(oc & 0xFF)
			{
			case 0x07:
				this.ld_r_dt((oc >> 8) & 0xF);
				break;
			case 0x0A:
				this.ld_k((oc >> 8) & 0xF);
				break;
			case 0x15:
				this.ld_dt((oc >> 8) & 0xF);
				break;
			case 0x18:
				this.ld_st((oc >> 8) & 0xF);
				break;
			case 0x1E:
				this.add_idx((oc >> 8) & 0xF);
				break;
			case 0x29:
				this.ld_font((oc >> 8) & 0xF);
				break;
			case 0x33:
				this.ld_bcd((oc >> 8) & 0xF);
				break;
			case 0x55:
				this.ld_idx_r((oc >> 8) & 0xF);
				break;
			case 0x65:
				this.ld_r_idx((oc >> 8) & 0xF);
				break;
			}
			break;
		}
	}
	
	/**
	 * Add register value to index register
	 * @param rs Source register
	 */
	public void add_idx(int rs)
	{
		registers.i = (registers.i + registers.v[rs]) & 0xFFFF;
	}
	
	/**
	 * Add immediate value to register Vx
	 * @param reg Destination register
	 * @param value Value to add
	 */
	public void add_imm(int reg, int value)
	{
		registers.v[reg] = (registers.v[reg] +  value) & 0xFF;
	}
	
	/**
	 * Add two registers, sets the carry flag if result > 255
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void add_r(int rd, int rs)
	{
		registers.v[rd] = registers.v[rd] + registers.v[rs];
		
		if(registers.v[rd] > 255)
			registers.v[15] = 1;
		else
			registers.v[15] = 0;
		
		registers.v[rd] = registers.v[rd] & 0xFF;
	}
	
	/**
	 * Perform bitwise AND between 2 registers
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void and(int rd, int rs)
	{
		registers.v[rd] = (registers.v[rd] & registers.v[rs]) & 0xFF;
	}
	
	/**
	 * Call subroutine
	 * @param adr Address of subroutine to jump to
	 */
	public void call(int adr)
	{
		stack.push(registers.pc);
		registers.pc = adr;
	}
	
	/**
	 * Clears the display
	 */
	public void cls()
	{
		graphics.clear();
	}
	
	/**
	 * Draws sprite on screen
	 * @param rx Starting x coordinate
	 * @param ry Starting y coordinate
	 * @param rows Number of rows
	 */
	public void drw(int rx, int ry, int rows)
	{
		int x = registers.v[rx];
		int y = registers.v[ry];
		
		for(int i = 0; i < rows; i++, y++)
		{
			if(graphics.drawLine(x, y, memory.fetchByte(registers.i+i)))
				registers.v[15] = 1;
		}
	}
	
	/**
	 * Jump to immediate address
	 * @param adr Address to jump to
	 */
	public void jmp_imm(int adr)
	{
		registers.pc = adr & 0xFFFF;
	}
	
	/**
	 * Jump to address v0 + immediate
	 * @param adr Immediate address to add to v0
	 */
	public void jmp_v0(int adr)
	{
		registers.pc = (registers.v[0] + adr) & 0xFFFF;
	}
	
	/**
	 * Load delay timer with value in register
	 * @param rs Source register
	 */
	public void ld_dt(int rs)
	{
		delaytimer.setValue(registers.v[rs]);
	}
	
	/**
	 * Set index point to location of digit in register 
	 * @param rs Source register containing the digit
	 */
	public void ld_font(int rs)
	{
		registers.i = 0x50 + (registers.v[rs] * 5);
	}
	
	/**
	 * Store BCD representation of register
	 * @param rs Source register
	 */
	public void ld_bcd(int rs)
	{
		memory.rom[registers.i] = (byte) (registers.v[rs] / 100);
		memory.rom[registers.i + 1] = (byte) ((registers.v[rs] / 10) % 10);
		memory.rom[registers.i + 2] = (byte) (registers.v[rs] % 10);
	}
	
	/**
	 * Load immediate value into register
	 * @param reg Destination register
	 * @param value Value to load
	 */
	public void ld_imm(int reg, int value)
	{
		registers.v[reg] = value & 0xFF;
	}
	
	/**
	 * Load address into index register
	 * @param adr Address to load
	 */
	public void ld_idx(int adr)
	{
		registers.i = adr & 0xFFFF;
	}
	
	/**
	 * Store registers in memory pointed by index register
	 * @param count Number of registers to store
	 */
	public void ld_idx_r(int count)
	{
		for(int i = 0; i < count; i++)
			memory.rom[registers.i + i] = (byte) registers.v[i];
	}
	
	/**
	 * Load registers from memory pointed by index register
	 * @param count Number of registers to load
	 */
	public void ld_r_idx(int count)
	{
		for(int i = 0; i < count; i++)
			registers.v[i] = memory.rom[registers.i + i];
	}
	
	/**
	 * Wait for key press and save in register
	 * @param rd Destination register
	 */
	public void ld_k(int rd)
	{
		registers.v[rd] = input.waitForKeypress();
	}
	
	/**
	 * Load register from another register
	 * @param rd Destination register
	 * @param rs Source Register
	 */
	public void ld_r(int rd, int rs)
	{
		registers.v[rd] = registers.v[rs] & 0xFF;
	}
	
	/**
	 * Load value from Delay Timer into register
	 * @param rd Destination register
	 */
	public void ld_r_dt(int rd)
	{
		registers.v[rd] = delaytimer.getValue() & 0xFF;
	}
	
	/**
	 * Load Sound Timer with value from register
	 * @param rs Source register
	 */
	public void ld_st(int rs)
	{
		soundtimer.setCounter(registers.v[rs]);
	}
	
	/**
	 * Perform bitwise OR between 2 registers
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void or(int rd, int rs)
	{
		registers.v[rd] = (registers.v[rd] | registers.v[rs]) & 0xFF;
	}
	
	/**
	 * Return from subroutine
	 */
	public void ret()
	{
		//Return from subroutine
		registers.pc = stack.pop();
	}
	
	/**
	 * Generate and store random number into register
	 * @param reg Destination Register
	 * @param mask Value to AND with the random number
	 */
	public void rnd(int reg, int mask)
	{
		registers.v[reg] = (random.nextInt() & mask) & 0xFF;
	}
	
	/**
	 * Skip instruction if equal to immediate
	 * @param reg Register Vx to compare
	 * @param val Value to compare
	 */
	public void se_imm(int reg, int val)
	{
		if(registers.v[reg] == val)
			registers.pc += 2;
	}
	
	/**
	 * Skip instruction if registers are equal
	 * @param rx First register to compare
	 * @param vy Second register to compare
	 */
	public void se_r(int rx, int ry)
	{
		if(registers.v[rx] == registers.v[ry])
			registers.pc += 2;
	}
	
	/**
	 * Shift register left, set carry if MSB = 1
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void shl(int rd, int rs)
	{
		if((registers.v[rd] & 0x80) == 0x80)
			registers.v[15] = 1;
		else
			registers.v[15] = 0;
		
		registers.v[rd] = (registers.v[rd] << 1) & 0xFF;
	}
	
	/**
	 * Shift register right, set carry if LSB = 1
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void shr(int rd, int rs)
	{
		if((registers.v[rd] & 0x1) == 0x1)
			registers.v[15] = 1;
		else
			registers.v[15] = 0;
		
		registers.v[rd] = (registers.v[rd] >> 1) & 0xFF;
	}
	
	/**
	 * Skip if key value in Vx is pressed
	 * @param rx Register holding the key value
	 */
	public void skp(int rx)
	{
		if(input.key[registers.v[rx]] == 1)
			registers.pc += 2;
	}
	
	/**
	 * Skip if key value in Vx is not pressed
	 * @param rx Register holding the key value
	 */
	public void sknp(int rx)
	{
		if(input.key[registers.v[rx]] != 1)
			registers.pc += 2;
	}
	
	/**
	 * Skip instruction if not equal to immediate
	 * @param reg Register Vx to compare
	 * @param val Value to compare
	 */
	public void sne_imm(int reg, int val)
	{
		if(registers.v[reg] != val)
			registers.pc += 2;
	}
	
	/**
	 * Skip instruction if registers not equal
	 * @param rx First register to compare
	 * @param ry Second register to compare
	 */
	public void sne_r(int rx, int ry)
	{
		if(registers.v[rx] != registers.v[ry])
			registers.pc += 2;
	}
	
	/**
	 * Substract source from destination, sets the carry flag if result < 0
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void sub(int rd, int rs)
	{
		if(registers.v[rd] > registers.v[rs])
			registers.v[15] = 1;
		else
			registers.v[15] = 0;
		
		registers.v[rd] = (registers.v[rd] - registers.v[rs]) & 0xFF;
	}
	
	/**
	 * Substract destination from source, sets the carry flag if result < 0
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void subn(int rd, int rs)
	{
		if(registers.v[rs] > registers.v[rd])
			registers.v[15] = 1;
		else
			registers.v[15] = 0;
		
		registers.v[rd] = (registers.v[rs] - registers.v[rd]) & 0xFF;
	}
	
	/**
	 * Jump to machine code routine
	 * @param adr Address to jump to
	 */
	public void sys(int adr)
	{
		//It is reccomended not to implement this
		//function on CHIP8 emulators
	}
	
	/**
	 * Perform bitwise XOR between 2 registers
	 * @param rd Destination register
	 * @param rs Source register
	 */
	public void xor(int rd, int rs)
	{
		registers.v[rd] = (registers.v[rd] ^ registers.v[rs]) & 0xFF;
	}
	
	/**
	 * Chip8TimerTask
	 * @author Tommy Savaria
	 *
	 * Timer Task that updates the delay and the sound timer at the same time
	 */
	public class Chip8TimerTask extends TimerTask
	{
		@Override
		public void run() {
			if(!timersFreeze)
			{
				soundtimer.update();
				delaytimer.update();
			}
		}
		
	}
}
