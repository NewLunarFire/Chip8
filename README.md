# Chip8
A small Chip8 emulator of my making in Java

## What's working
In short, most of it. In long:

 * Main emulation loop
 * Instructions fetching / decoding / executing is  mostly working (some bugs still remain)
 * The Delay Timer and Sound Timer work fine
 * The Graphics system works fine, with a basic GUI in Swing
 * Input system works fine
 * Loading a file from filesystem
 * Playing / Pausing / Resuming / Stopping Emulation

Thing to do:
 * Fix the last few in-game bugs
  * Ex: Score for Player 2 in Pong does not update
 * Create dialogs to configure speed, graphics, sound and input
 * Add icons for graphics / sound / input, and menu items for play / pause / stop

No platform-specific code or libraries were used, so it should work on any platform. It has only been tested on Ubuntu Linux 16.04 (Xenial Xerus) 64-bit.
