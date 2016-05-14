/**
 * 
 */
package io.github.newlunarfire.chipeight;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;

import io.github.newlunarfire.chipeight.emu.Chip8;
import io.github.newlunarfire.chipeight.gui.Window;

/**
 * @author tommy
 *
 */
public class Main {
	public static void main(String[] args) {
		Chip8EmulatorRunner runner = new Chip8EmulatorRunner();
		Window window = new Window();
		MainController controller = new MainController(runner, window);
		
		Timer t = new Timer();
		
		//Setup Window
		window.setEmulatorViewport(runner.getEmulatorInstance());
		window.addActionListener(controller);
		window.initUI();
		window.setVisible(true);
		
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				window.repaint();
			}
		}, 0, 1000/60);
	}

}
