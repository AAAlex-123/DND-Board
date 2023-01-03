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
import alexman.dndboard.gui.grid.Grid;
import alexman.dndboard.model.IAreaModel;
import alexman.dndboard.model.ICharacterModel;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class Application extends JFrame {

	private final JPanel panel;

	private final ICharacterModel characterModel;
	private final IAreaModel areaModel;
	private Area currentArea;

	private final Map<String, CharacterGraphic> characterGraphicMap = new HashMap<>();

	private final Grid grid;
	private int gridX, gridY;

	public Application(ICharacterModel characterModel, IAreaModel areaModel, Grid grid,
	        int gridX, int gridY) {
		super();
		setLayout(null);
		this.characterModel = characterModel;
		this.areaModel = areaModel;
		this.grid = grid;
		setGridX(gridX);
		setGridY(gridY);

		panel = new JPanel();
		this.setContentPane(panel);
		JMenuBar menubar = new ApplicationMenu(this);
		this.setJMenuBar(menubar);

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
			flyweightCharacter = characterModel.readFlyweightFromCache("dog");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Character character1 = new Character(flyweightCharacter, "aaa", new Point(50, 50));
		Character character2 = new Character(flyweightCharacter, "bbb", new Point(150, 150));
		addCharacter(character1);
		addCharacter(character2);
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
