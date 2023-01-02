package alexman.dndboard.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import alexman.dndboard.entity.Character;
import alexman.dndboard.entity.FlyweightCharacter;
import alexman.dndboard.entity.FlyweightFactory;
import alexman.dndboard.gui.grid.Grid;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class Application extends JFrame {

	private final JPanel panel;

	private final FlyweightFactory flyweightFactory;
	private final Grid grid;

	private int gridX, gridY;

	/**
	 * TODO
	 *
	 * @param grid
	 * @param gridX
	 * @param gridY
	 */
	public Application(FlyweightFactory flyweightFactory, Grid grid, int gridX, int gridY) {
		super();
		setLayout(null);
		this.flyweightFactory = flyweightFactory;
		this.grid = grid;
		setGridX(gridX);
		setGridY(gridY);

		panel = new JPanel();
		this.setContentPane(panel);

		panel.setLayout(null);
		panel.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e) {
				e.getChild().addComponentListener(new ComponentAdapter() {
					@Override
					public void componentMoved(ComponentEvent e1) {
						Application.this.recalculateZOrderForAllComponents();
					}
				});
			}
		});

		stupidSetup();
	}

	private void stupidSetup() {
		FlyweightCharacter flyweightCharacter;
		try {
			flyweightCharacter = flyweightFactory.getFlyweight("dog");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Character character1 = new Character(flyweightCharacter, "aaa");
		Character character2 = new Character(flyweightCharacter, "bbb");
		character1.setPos(new Point(50, 50));
		character2.setPos(new Point(150, 150));
		panel.add(new CharacterGraphic(character1));
		panel.add(new CharacterGraphic(character2));
	}

	/**
	 * Sets the gridX to gridX.
	 *
	 * @param gridX the new value
	 */
	public final void setGridX(int gridX) {
		this.gridX = gridX;
	}

	/**
	 * Sets the gridY to gridY.
	 *
	 * @param gridY the new value
	 */
	public final void setGridY(int gridY) {
		this.gridY = gridY;
	}

	enum Actions {

	}

	private void recalculateZOrderForAllComponents() {
		Component[] comps = panel.getComponents();
		Arrays.sort(comps, (c1, c2) -> c1.getY() - c2.getY());
		for (int i = 0; i < comps.length; i++) {
			panel.setComponentZOrder(comps[i], i);
		}
	}
}
