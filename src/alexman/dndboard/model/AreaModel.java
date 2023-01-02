package alexman.dndboard.model;

import java.awt.Point;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import alexman.dndboard.entity.Area;
import alexman.dndboard.entity.Character;
import alexman.dndboard.entity.FlyweightCharacter;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class AreaModel implements IAreaModel {

	private JSONObject allAreasObject;

	private final Map<String, Area> cache = new HashMap<>();
	private final Map<String, Boolean> dirty = new HashMap<>();

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
	public Area loadAreaFromCache(String areaName, ICharacterModel characterModel)
	        throws IOException {

		if (cache.containsKey(areaName) && !dirty.get(areaName)) {
			return cache.get(areaName);
		}

		JSONObject areaObject = allAreasObject.getJSONObject(areaName);
		String areaBackgroundImageSprite = areaObject.getString("sprite");
		Area loadedArea = new Area(areaName, areaBackgroundImageSprite);

		JSONArray characterArray = areaObject.getJSONArray("characters");
		for (int i = 0; i < characterArray.length(); i++) {

			JSONObject characterObject = characterArray.getJSONObject(i);

			String flyweightCharacterId = characterObject.getString("character");
			FlyweightCharacter flyweightCharacter = characterModel
			        .readFlyweightFromCache(flyweightCharacterId);

			String characterDisplayName = characterObject.getString("display_name");
			Point pos = new Point(characterObject.getInt("posX"), characterObject.getInt("posY"));
			Character character = new Character(flyweightCharacter, characterDisplayName, pos);

			character.setHp(characterObject.getInt("hp"));

			character.setFlipped(characterObject.getBoolean("flipped"));
			// ... set other dnd stuff ...

			loadedArea.addCharacter(character);
		}

		cache.put(areaName, loadedArea);
		dirty.put(areaName, false);

		return loadedArea;
	}

	@Override
	public void saveAreaToCache(Area area) {

		if (!cache.containsKey(area.getName())) {
			throw new IllegalArgumentException(
			        "Can't save Area " + area.getName() + " which does not exist");
		}

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

		dirty.put(area.getName(), true);
	}
}
