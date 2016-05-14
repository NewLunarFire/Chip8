/**
 * 
 */
package io.github.newlunarfire.chipeight;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import io.github.newlunarfire.chipeight.emu.Chip8;
import io.github.newlunarfire.chipeight.gui.Window;

/**
 * @author tommy
 *
 */
public class MainController implements ActionListener, KeyEventDispatcher {
	Chip8EmulatorRunner runner;
	Chip8 emulator;
	
	Window window;
	
	public MainController(Chip8EmulatorRunner r, Window w)
	{
		runner = r;
		emulator = r.getEmulatorInstance();
		window = w;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Play")
		{
			//Start or resume emulation
			if(runner.isPaused())
				runner.resume();
			else
				runner.start();
			
			window.setEmulatorState("Running");
		}
		else if(e.getActionCommand() == "Pause")
		{
			//Pause emulation
			runner.pause();
			window.setEmulatorState("Paused");
		}
		else if(e.getActionCommand() == "Stop")
		{
			//Stop emulation
			runner.stop();
			window.setEmulatorState("Stopped");
		}
		else if(e.getActionCommand() == "Open")
		{
			//Open a file
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
			{
				File file = fileChooser.getSelectedFile();
				
				runner.stop();
				runner.getEmulatorInstance().loadRom(file);
				runner.start();
				
				window.setWindowGameName(file.getName());
				window.setEmulatorState("Running");
			}
		}
		else if(e.getActionCommand() == "Quit")
		{
			//Quit the emulator
			System.exit(0);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		int key;
		
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_1:
			key = 1;
			break;
		case KeyEvent.VK_2:
			key = 2;
			break;
		case KeyEvent.VK_3:
			key = 3;
			break;
		case KeyEvent.VK_4:
			key = 0xC;
			break;
		case KeyEvent.VK_Q:
			key = 4;
			break;
		case KeyEvent.VK_W:
			key = 5;
			break;
		case KeyEvent.VK_E:
			key = 6;
			break;
		case KeyEvent.VK_R:
			key = 0xD;
			break;
		case KeyEvent.VK_A:
			key = 7;
			break;
		case KeyEvent.VK_S:
			key = 8;
			break;
		case KeyEvent.VK_D:
			key = 9;
			break;
		case KeyEvent.VK_F:
			key = 0xE;
			break;
		case KeyEvent.VK_Z:
			key = 0xA;
			break;
		case KeyEvent.VK_X:
			key = 0;
			break;
		case KeyEvent.VK_C:
			key = 0xB;
			break;
		case KeyEvent.VK_V:
			key = 0xF;
			break;
		default:
			key = -1;
			break;
		}
		
		if(key == -1)
			return false;
		
		if(e.getID() == KeyEvent.KEY_PRESSED)
			emulator.input.key[key] = 1;
		else if(e.getID() == KeyEvent.KEY_RELEASED)
			emulator.input.key[key] = 0;
		else
			return false;
		
		return true;
	}

}
