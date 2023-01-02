package alexman.dndboard.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import alexman.dndboard.entity.Character;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class CharacterGraphic extends JComponent {

	private final Character character;

	public CharacterGraphic(Character character) {
		this.character = character;
		this.setSize(1, 1);

		MyMouseAdapter mma = new MyMouseAdapter();
		addMouseListener(mma);
		addMouseMotionListener(mma);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Image img = character.getSpriteForCurrentState();
		this.setSize(img.getWidth(null), img.getHeight(null));

		g.drawImage(img, 0, 0, null);
		g.setColor(Color.WHITE);
		g.drawString(character.getDisplayName(), 0, getHeight());
	}

	// override 4 location methods to work with Character's pos

	///* @foff
	@Override
	public Point getLocation(Point rv) {
		Point pos = getLocation();

		Point pointToReturn = rv;
		if (pointToReturn == null)
			pointToReturn = new Point();

		pointToReturn.x = pos.x;
		pointToReturn.y = pos.y;
		return pointToReturn;
	}

	@Override
	public Point getLocation() {
		return character.getPos();
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		character.setPos(new Point(x, y));
	}

	@Override
	public void setLocation(Point p) {
		super.setLocation(p);
		character.setPos(p);
	}
	//@fon */

	private class MyMouseAdapter implements MouseListener, MouseMotionListener {

		private int dx;
		private int dy;

		@Override
		public void mouseDragged(MouseEvent e) {
			int newX = getX() + (e.getX() - dx);
			int newY = getY() + (e.getY() - dy);
			Point newPos = new Point(newX, newY);

			CharacterGraphic.this.setLocation(newPos);
		}

		@Override
		public void mouseMoved(MouseEvent e) {}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			dx = e.getX();
			dy = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
}
