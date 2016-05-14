/**
 * 
 */
package io.github.newlunarfire.chipeight.gui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import io.github.newlunarfire.chipeight.emu.Chip8;

/**
 * @author tommy
 *
 */
public class Window extends JFrame {
	public Chip8Viewport viewport;
	public byte[] keymap;
	
	private JButton playButton;
	private JButton pauseButton;
	private JButton stopButton;
	private JToolBar toolBar;
	
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenu fileMenu;
	private JMenuItem openItem;
	private JMenuItem quitItem;
	private JMenuItem graphicsItem;
	private JMenuItem soundItem;
	private JMenuItem inputItem;
	
	public Window() {
		menuBar = new JMenuBar();
		optionsMenu = new JMenu("Options");
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		quitItem = new JMenuItem("Quit");
		graphicsItem = new JMenuItem("Graphics");
		soundItem = new JMenuItem("Sound");
		inputItem = new JMenuItem("Input");
		
		toolBar = new JToolBar();
		playButton = new JButton();
		pauseButton = new JButton();
		stopButton = new JButton();
	}
	
	public void addActionListener(ActionListener a)
	{
		playButton.addActionListener(a);
		pauseButton.addActionListener(a);
		stopButton.addActionListener(a);
		
		openItem.addActionListener(a);
		quitItem.addActionListener(a);
		graphicsItem.addActionListener(a);
		soundItem.addActionListener(a);
		inputItem.addActionListener(a);
	}
	
	public void setEmulatorViewport(Chip8 emulator)
	{
		viewport = new Chip8Viewport(emulator.graphics.memory);
	}
	
	public void initUI()
	{	
		setTitle("CHIP8 Emulator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		try
		{
			setIconImage(ImageIO.read(new File("res/icon.png")));
		} catch(IOException e){};
		
		playButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		
		setupMenuBar();
		addComponentsToPane(this.getContentPane());
		pack();
		
		setActionCommands();
	}
	
	public void setWindowGameName(String name)
	{
		if(name == null || null == "")
			setTitle("CHIP8 Emulator");
		else
			setTitle("CHIP8 Emulator [" + name + "]");
	}
	
	public void setEmulatorState(String state)
	{
		if(state == "Running")
		{
			playButton.setEnabled(false);
			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);
		}
		else if(state == "Paused")
		{
			playButton.setEnabled(true);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(true);
		}
		else if(state == "Stopped")
		{
			playButton.setEnabled(true);
			pauseButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
	}
	
	private void addComponentsToPane(Container pane)
	{
		setButtonImage(playButton, "res/play-button.png");
		setButtonImage(pauseButton, "res/pause-button.png");
		setButtonImage(stopButton, "res/stop-button.png");
		
		toolBar.add(playButton);
		toolBar.add(pauseButton);
		toolBar.add(stopButton);
		
		pane.add(toolBar, BorderLayout.PAGE_START);
		pane.add(viewport, BorderLayout.CENTER);
		pane.setPreferredSize(new Dimension(viewport.VIEWPORT_WIDTH, viewport.VIEWPORT_HEIGHT + 48));
	}
	
	private void setupMenuBar()
	{
		fileMenu.add(openItem);
		fileMenu.add(quitItem);
		
		optionsMenu.add(graphicsItem);
		optionsMenu.add(soundItem);
		optionsMenu.add(inputItem);
		
		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);
		setJMenuBar(menuBar);
	}
	
	private void setActionCommands()
	{
		playButton.setActionCommand("Play");
		pauseButton.setActionCommand("Pause");
		stopButton.setActionCommand("Stop");
		openItem.setActionCommand("Open");
		quitItem.setActionCommand("Quit");
		graphicsItem.setActionCommand("Dialog/GraphicsOptions");
		soundItem.setActionCommand("Dialog/SoundOptions");
		inputItem.setActionCommand("Dialog/InputOptions");
	}
	
	private void setButtonImage(JButton button, String image)
	{
			try {
				button.setIcon(new ImageIcon(ImageIO.read(new File(image))));
			} catch (IOException e) { e.printStackTrace(); }
	}
}
