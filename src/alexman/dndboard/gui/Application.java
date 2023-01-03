package alexman.dndboard.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import alexman.dndboard.entity.Area;
import alexman.dndboard.entity.Character;
import alexman.dndboard.entity.FlyweightCharacter;
import alexman.dndboard.gui.grid.Grid;
import alexman.dndboard.model.AreaModel;
import alexman.dndboard.model.CharacterModel;
import alexman.dndboard.model.IAreaModel;
import alexman.dndboard.model.ICharacterModel;
import requirement.requirements.ListRequirement;
import requirement.requirements.StringRequirement;
import requirement.requirements.StringType;
import requirement.util.HasRequirements;
import requirement.util.Requirements;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class Application extends JFrame {

	private final JPanel mainContentPanel;
	private final JPanel optionsPanel;

	private final CharacterManager characterManager = new CharacterManager();
	private ICharacterModel characterModel;
	private IAreaModel areaModel;
	private Path currentGameRoot;
	private Area currentArea;

	private final Grid grid;
	private int gridX, gridY;

	public Application(Grid grid, int gridX, int gridY) {
		super();
		setLayout(null);
		this.grid = grid;
		setGridX(gridX);
		setGridY(gridY);

		JMenuBar menubar = new ApplicationMenu(this);
		this.setJMenuBar(menubar);

		mainContentPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Area currentArea = getCurrentArea();

				if (currentArea != null) {
					g.drawImage(currentArea.getBackgroundImage(), 0, 0, null);
				}
			}
		};

		JPanel characterPanel = characterManager.getCharacterPanel();
		optionsPanel = new JPanel();
		this.setContentPane(mainContentPanel);
		mainContentPanel.add(characterPanel, BorderLayout.CENTER);
		mainContentPanel.add(optionsPanel, BorderLayout.NORTH);

		characterPanel.setOpaque(false); // don't hide background from `mainContentPanel`
		characterPanel.setLayout(null);
		characterPanel.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent e) {
				Application.this.recalculateZOrderForAllComponents();
				e.getChild().addComponentListener(new ComponentAdapter() {
					@Override
					public void componentMoved(ComponentEvent e1) {
						Application.this.recalculateZOrderForAllComponents();
					}
				});
			}
		});
	}

	private Area getCurrentArea() {
		return currentArea;
	}

	private final void setGridX(int gridX) {
		this.gridX = gridX;
	}

	private final void setGridY(int gridY) {
		this.gridY = gridY;
	}

	// delegates to character manager
	private List<String> getCharacterNames() {
		return characterManager.getCharacterNames();
	}

	private void addCharacterToCurrentArea(Character character) {
		getCurrentArea().addCharacter(character);
		addCharacterToScreen(character);
		areaModel.saveAreaToCache(currentArea);
	}

	private void addCharacterToScreen(Character character) {
		characterManager.addCharacter(character);
	}

	private void removeCharacterFromCurrentArea(String name) {
		getCurrentArea().removeCharacter(characterManager.getCharacter(name));
		removeCharacterFromScreen(name);
		areaModel.saveAreaToCache(currentArea);
	}

	private void removeCharacterFromScreen(String name) {
		characterManager.removeCharacter(name);
	}

	private void removeAllCharactersFromScreen() {
		characterManager.removeAllCharacters();
	}

	private void moveCharacterToArea(Character character, Area area) {
		Area.move(getCurrentArea(), area, character);
		removeCharacterFromScreen(character.getDisplayName());
		areaModel.saveAreaToCache(area);
	}

	private void recalculateZOrderForAllComponents() {
		characterManager.recalculateZOrderForAllComponents();
	}

	private List<CharacterGraphic> getCharacterGraphics() {
		return characterManager.getCharacterGraphics();
	}

	enum Actions implements HasRequirements {

		OPEN {
			@Override
			protected void executeAction() throws Exception {

				reqs.fulfillWithDialog(context, "Choose a Game to open");
				if (!reqs.fulfilled()) {
					return;
				}

				String gameDirectory = reqs.getValue("Game", String.class);
				Path gameRoot = Path.of(System.getProperty("user.dir"), File.separator, "DND-Games",
				        gameDirectory);
				context.currentGameRoot = gameRoot;
				Path areaPath = Path.of(gameRoot.toString(), "areas.json");
				Path characterPath = Path.of(gameRoot.toString(), "characters.json");

				try (Reader reader = new FileReader(areaPath.toFile())) {
					context.areaModel = new AreaModel(reader);
				}

				try (Reader reader = new FileReader(characterPath.toFile())) {
					context.characterModel = new CharacterModel(reader);
				}

				context.characterManager.removeAllCharacters();
			}

			@Override
			public void constructRequirements() {
				reqs.add("Game", new LinkedList<String>());
			}

			@Override
			public void adjustRequirements() {
				((ListRequirement<String>) reqs.get("Game"))
				        .setOptions(Arrays
				        		.asList(
									Path.of(
										System.getProperty("user.dir"),
										File.separator,
										"DND-Games"
									)
									.toFile()
									.listFiles(File::isDirectory)
				        		)
				                .stream()
				                .map(File::getName)
				                .toList());
			}
		},

		SAVE {
			@Override
			protected void executeAction() throws Exception {
				context.areaModel.saveAreaToCache(context.currentArea);
				Path gameRoot = context.currentGameRoot;

				Path areaPath = Path.of(gameRoot.toString(), "areas.json");
				Path characterPath = Path.of(gameRoot.toString(), "characters.json");

				try (Writer writer = new FileWriter(areaPath.toFile())) {
					context.areaModel.write(writer);
				}

				try (Writer writer = new FileWriter(characterPath.toFile())) {
					context.characterModel.write(writer);
				}
			}
		},

		SAVE_AS {
			@Override
			protected void executeAction() throws Exception {
				reqs.fulfillWithDialog(context, "Choose a Name for your Game");
				if (!reqs.fulfilled()) {
					return;
				}

				String gameName = reqs.getValue("Game Name", String.class);
				Path gameRoot = Path.of(System.getProperty("user.dir"), File.separator, "DND-Games",
				        gameName);

				if (new File(gameRoot.toString()).isDirectory()) {
					throw new IllegalArgumentException(
					        "Game with name " + gameName + " already exists");
				}

				new File(gameRoot.toString()).mkdir();

				Path areaPath = Path.of(gameRoot.toString(), "areas.json");
				Path characterPath = Path.of(gameRoot.toString(), "characters.json");

				try (Writer writer = new FileWriter(areaPath.toFile())) {
					context.areaModel.write(writer);
				}

				try (Writer writer = new FileWriter(characterPath.toFile())) {
					context.characterModel.write(writer);
				}
			}

			@Override
			public void constructRequirements() {
				reqs.add("Game Name", StringType.NON_EMPTY);
			}
		},

		SWITCH_TO_AREA {
			@Override
			protected void executeAction() throws Exception {
				// first load
				if (context.currentArea != null)
					context.areaModel.saveAreaToCache(context.currentArea);

				reqs.fulfillWithDialog(context, "Choose an Area");
				if (!reqs.fulfilled()) {
					return;
				}

				try {
					context.currentArea = context.areaModel.loadAreaFromCache(
					        reqs.getValue("Area", String.class),
					        context.characterModel);
				} catch (IOException e) {
					e.printStackTrace();
				}

				context.removeAllCharactersFromScreen();
				context.getCurrentArea().getCharacters().forEach(context::addCharacterToScreen);
			}

			@Override
			public void constructRequirements() {
				reqs.add(new ListRequirement<String>("Area"));
			}

			@Override
			public void adjustRequirements() {
				List<String> areas = new LinkedList<>();
				context.areaModel.getAreaNamesFromCache().forEach(areas::add);
				((ListRequirement<String>) reqs.get("Area")).setOptions(areas);
			}
		},

		ADD_CHARACTER {
			@Override
			protected void executeAction() throws Exception {

				reqs.fulfillWithDialog(context, "Create a Character");
				if (!reqs.fulfilled()) {
					return;
				}

				String flyweightName = reqs.getValue("Character", String.class);
				FlyweightCharacter fc = context.characterModel
				        .readFlyweightFromCache(flyweightName);

				String name = reqs.getValue("Name", String.class);
				int posX = reqs.getValue("Position X", x -> Integer.parseInt((String) x));
				int posY = reqs.getValue("Position Y", y -> Integer.parseInt((String) y));
				Point pos = new Point(posX, posY);

				Character character = new Character(fc, name, pos);

				context.addCharacterToCurrentArea(character);
			}

			@Override
			public void constructRequirements() {
				reqs.add(new ListRequirement<String>("Character"));
				reqs.add(new StringRequirement("Name"));
				reqs.add(new StringRequirement("Position X", StringType.NON_NEG_INTEGER));
				reqs.add(new StringRequirement("Position Y", StringType.NON_NEG_INTEGER));
			}

			@Override
			public void adjustRequirements() {
				List<String> characters = new LinkedList<>();
				context.characterModel.getCharacterNamesFromCache().forEach(characters::add);
				((ListRequirement<String>) reqs.get("Character")).setOptions(characters);
			}
		},

		REMOVE_CHARACTER {
			@Override
			protected void executeAction() throws Exception {

				reqs.fulfillWithDialog(context, "Remove a Character");
				if (!reqs.fulfilled()) {
					return;
				}

				String name = reqs.getValue("Name", String.class);
				context.removeCharacterFromCurrentArea(name);
			}

			@Override
			public void constructRequirements() {
				reqs.add(new ListRequirement<String>("Name"));
			}

			@Override
			public void adjustRequirements() {
				((ListRequirement<String>) reqs.get("Name"))
				        .setOptions(context.getCharacterNames());
			}
		},

		MOVE_CHARACTER_TO_AREA {
			@Override
			protected void executeAction() throws Exception {
				reqs.fulfillWithDialog(context, "Choose a Character to move");
				if (!reqs.fulfilled()) {
					return;
				}

				String characterName = reqs.getValue("Character", String.class);
				Character character = context.characterManager.getCharacter(characterName);

				String areaName = reqs.getValue("To Area", String.class);
				Area areaTo = context.areaModel.loadAreaFromCache(areaName, context.characterModel);

				context.moveCharacterToArea(character, areaTo);
			}

			@Override
			public void constructRequirements() {
				reqs.add(new ListRequirement<String>("To Area"));
				reqs.add(new ListRequirement<Character>("Character"));
			}

			@Override
			public void adjustRequirements() {
				List<String> toAreas = new LinkedList<>();
				context.areaModel.getAreaNamesFromCache().forEach(toAreas::add);
				toAreas.remove(context.getCurrentArea().getName());
				((ListRequirement<String>) reqs.get("To Area")).setOptions(toAreas);

				((ListRequirement<String>) reqs.get("Character"))
				        .setOptions(context.getCharacterNames());
			}
		},

		;

		Actions() {
			constructRequirements();
		}

		protected Requirements reqs = new Requirements();

		protected Application context;

		protected final void execute() {
			if (context == null)
				throw new NullPointerException("No conext set for Action");

			try {
				executeAction();
			} catch (final Exception e) {
				e.printStackTrace();
			}

			reqs.clear();
			context = null;
		}

		protected abstract void executeAction() throws Exception;

		@Override
		public void constructRequirements() {}

		@Override
		public void adjustRequirements() {}

		protected Actions context(Application context) {
			this.context = context;
			return this;
		}
	}
}
