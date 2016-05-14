package io.github.newlunarfire.chipeight;

import java.util.Timer;
import java.util.TimerTask;

import io.github.newlunarfire.chipeight.emu.Chip8;

public class Chip8EmulatorRunner extends TimerTask {
	private Chip8 emulator;
	private Timer t;
	
	private boolean started = false;
	private boolean paused = false;
	
	Chip8EmulatorRunner()
	{
		emulator = new Chip8();
		t = new Timer();
		t.scheduleAtFixedRate(this, 0, 1);
	}
	
	public void start()
	{
		emulator.initialize();
		emulator.unfreeze();
		started = true;
	}
	
	public void pause()
	{
		emulator.freeze();
		paused = true;
	}
	
	public void resume()
	{
		emulator.unfreeze();
		paused = false;
	}
	
	public void stop()
	{
		if(started)
		{
			emulator.terminate();
			started = false;
			paused = false;
		}
	}

	public boolean isStarted()
	{
		return started;
	}
	
	public boolean isRunning()
	{
		return started && !paused;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public Chip8 getEmulatorInstance()
	{
		return emulator;
	}
	
	@Override
	public void run() {
		if(!paused && started)
			emulator.emulateCycle();
	}
}
