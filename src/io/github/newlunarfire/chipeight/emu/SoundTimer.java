/**
 * 
 */
package io.github.newlunarfire.chipeight.emu;

import javax.sound.midi.*;

/**
 * @author tommy
 *
 */
public class SoundTimer {
	private int counter;
	
	private Synthesizer synth;
	private MidiChannel channel;
	private final int note = 60;
	private final int velocity = 255;
	
	public SoundTimer()
	{
		//Setup Midi Synthesizer
		try
		{
			synth = MidiSystem.getSynthesizer();
	        synth.open();

	        channel = synth.getChannels()[0];
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	
	public void finalize()
	{
		if(synth != null)
			synth.close();
	}
	
	public void initialize()
	{
		//Reset Counter
		counter = 0;		
	}
	
	public void setCounter(int value)
	{
		counter = value & 0xFF;
	}
	
	public void update()
	{
		//End of timer
		if(counter == 1)
		{
			//Emit a beep
			if(channel != null)
				channel.noteOn(note, velocity);
		}
		else
		{
			if(channel != null)
				channel.noteOff(note);
		}
		
		//Decrement timer
		if(counter != 0)
			counter--;
	}
}
