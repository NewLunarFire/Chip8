package io.github.newlunarfire.chipeight.gui;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Chip8Viewport extends JPanel {
	public BufferedImage image;
	byte[] pixels;
	byte[][] c8buffer;
	
	public final int VIEWPORT_WIDTH = 512;
	public final int VIEWPORT_HEIGHT = 256;
	public final Dimension VIEWPORT_SIZE = new Dimension(512, 256);
	
	public Chip8Viewport(byte[][] chip8)
	{
		image = new BufferedImage(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
		pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		c8buffer = chip8;
		
		this.setMinimumSize(VIEWPORT_SIZE);
		this.setMaximumSize(VIEWPORT_SIZE);
		this.setPreferredSize(VIEWPORT_SIZE);
		
		setSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
	}
	
	public void draw()
	{
		for(int y = 0; y < (32*8); y++)
		{
			for(int x = 0; x < 64; x++)
			{
				if(c8buffer[x][y>>3] == 1)
					pixels[(y*64)+x] = (byte)0xFF;
				else
					pixels[(y*64)+x] = (byte)0x00;
			}
		}
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw();
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}