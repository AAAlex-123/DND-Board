package alexman.dndboard.gui.grid;

import java.awt.Point;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public interface Grid {

	/**
	 * TODO
	 *
	 * @param pos
	 *
	 * @return
	 */
	Point move(Point pos);

	void setWidth(int width);

	void setHeight(int height);

}
