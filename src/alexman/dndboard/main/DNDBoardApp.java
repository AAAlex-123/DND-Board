package alexman.dndboard.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.WindowConstants;

import alexman.dndboard.gui.Application;
import alexman.dndboard.model.AreaModel;
import alexman.dndboard.model.CharacterModel;
import alexman.dndboard.model.IAreaModel;
import alexman.dndboard.model.ICharacterModel;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class DNDBoardApp {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String charactersJSONInputFile = "C:\\Users\\alexm\\projects\\Java\\DND-Board\\characters.json";
		ICharacterModel characterModel;
		try (Reader characterReader = new FileReader(charactersJSONInputFile)) {
			characterModel = new CharacterModel(characterReader);
		}

		String areasJSONInputFile = "C:\\Users\\alexm\\projects\\Java\\DND-Board\\areas.json";
		IAreaModel areaModel;
		try (Reader areaReader = new FileReader(areasJSONInputFile)) {
			areaModel = new AreaModel(areaReader);
		}

		// FlyweightFactory ff = new FlyweightFactory(characterModel);

		Application aw = new Application(characterModel, areaModel, null, 0, 0);
		aw.setAutoRequestFocus(true);
		aw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		aw.setIconImage(null/* TODO */);
		aw.setSize(800, 600);
		aw.setTitle("DND Board");
		aw.setVisible(true);
	}
}
