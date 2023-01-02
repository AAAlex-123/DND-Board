package alexman.dndboard.model;

import java.awt.Point;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import alexman.dndboard.entity.Area;
import alexman.dndboard.entity.Character;
import alexman.dndboard.entity.FlyweightCharacter;
import alexman.dndboard.entity.FlyweightFactory;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class AreaModel implements IAreaModel {

	private JSONObject allAreasObject;

	/**
	 * TODO
	 *
	 * @param reader
	 *
	 * @throws IOException
	 */
	public AreaModel(Reader reader) throws IOException {
		read(reader);
	}

	@Override
	public void read(Reader reader) throws IOException {
		allAreasObject = new JSONObject(new JSONTokener(reader));
	}

	@Override
	public void write(Writer writer) throws JSONException, IOException {
		writer.write(allAreasObject.toString(4));
	}

	@Override
	public Iterable<String> getAreaNamesFromCache() {
		return allAreasObject.keySet();
	}

	@Override
	public Area loadAreaFromCache(String areaName, FlyweightFactory flyweightFactory) throws IOException {

		JSONObject areaObject = allAreasObject.getJSONObject(areaName);
		String areaBackgroundImageSprite = areaObject.getString("sprite");
		Area loadedArea = new Area(areaName, areaBackgroundImageSprite);

		JSONArray characterArray = areaObject.getJSONArray("characters");
		for (int i = 0; i < characterArray.length(); i++) {

			JSONObject characterObject = characterArray.getJSONObject(i);

			String flyweightCharacterId = characterObject.getString("character");
			FlyweightCharacter flyweightCharacter = flyweightFactory
			        .getFlyweight(flyweightCharacterId);

			String characterDisplayName = characterObject.getString("display_name");
			Character character = new Character(flyweightCharacter, characterDisplayName);

			character.setHp(characterObject.getInt("hp"));

			Point pos = new Point(characterObject.getInt("posX"), characterObject.getInt("posY"));
			character.setPos(pos);

			character.setFlipped(characterObject.getBoolean("flipped"));
			// ... set other dnd stuff ...

			loadedArea.addCharacter(character);
		}

		return loadedArea;
	}

	@Override
	public void saveAreaToCache(Area area) {

		JSONObject areaObject = new JSONObject();

		areaObject.put("sprite", area.getBackgroundImagePath());

		JSONArray areaCharacterArray = new JSONArray();
		for (Character c : area.getCharacters()) {
			JSONObject characterObject = new JSONObject();

			characterObject.put("character", c.getCharacterName());
			characterObject.put("display_name", c.getDisplayName());
			characterObject.put("hp", c.getHp());
			characterObject.put("posX", c.getPos().getX());
			characterObject.put("posY", c.getPos().getY());
			characterObject.put("flipped", c.getFlipped());

			areaCharacterArray.put(characterObject);
		}

		areaObject.put("characters", areaCharacterArray);

		allAreasObject.put(area.getName(), areaObject);
	}
}
