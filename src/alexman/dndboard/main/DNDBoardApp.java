package alexman.dndboard.main;

import javax.swing.WindowConstants;

import alexman.dndboard.gui.Application;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class DNDBoardApp {

	public static void main(String[] args) {
		Application aw = new Application(null, 0, 0);
		aw.setAutoRequestFocus(true);
		aw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		aw.setIconImage(null/* TODO */);
		aw.setSize(800, 600);
		aw.setTitle("DND Board");
		aw.setVisible(true);
	}
}
