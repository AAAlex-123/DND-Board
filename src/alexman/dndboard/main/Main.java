package alexman.dndboard.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import alexman.dndboard.entity.Area;
import alexman.dndboard.entity.Character;
import alexman.dndboard.entity.FlyweightCharacter;
import alexman.dndboard.entity.FlyweightFactory;
import alexman.dndboard.model.AreaModel;
import alexman.dndboard.model.CharacterModel;
import alexman.dndboard.model.IAreaModel;
import alexman.dndboard.model.ICharacterModel;

public class Main {

	public static void main(String[] args) throws IOException {

		String charactersJSONInputFile = "C:\\Users\\alexm\\projects\\Java\\DND-Board\\characters.json";
		ICharacterModel characterModel;
		try (Reader characterReader = new FileReader(charactersJSONInputFile)) {
			characterModel = new CharacterModel(characterReader);
		}

		Iterable<String> characterNames = characterModel.getCharacterNamesFromCache();

		FlyweightFactory ff = new FlyweightFactory(characterModel);
		FlyweightCharacter fc = ff.getFlyweight("conton");

		Character c = new Character(fc, "cotton");

		// ---

		String charactersJSONOutputFile = "C:\\Users\\alexm\\projects\\Java\\DND-Board\\characters-out.json";
		try (Writer characterWriter = new FileWriter(charactersJSONOutputFile)) {
			characterModel.write(characterWriter);
		}

		// ---

		String areasJSONInputFile = "C:\\Users\\alexm\\projects\\Java\\DND-Board\\areas.json";
		IAreaModel areaModel;
		try (Reader areaReader = new FileReader(areasJSONInputFile)) {
			areaModel = new AreaModel(areaReader);
		}

		Iterable<String> areaNames = areaModel.getAreaNamesFromCache();

		Area church = areaModel.loadAreaFromCache("church", ff);

		// ---

		String areasJSONOutputFile = "C:\\Users\\alexm\\projects\\Java\\DND-Board\\areas-out.json";
		try (Writer areaWriter = new FileWriter(areasJSONOutputFile)) {
			areaModel.write(areaWriter);
		}
	}
}
