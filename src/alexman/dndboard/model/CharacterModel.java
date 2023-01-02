package alexman.dndboard.model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.EnumMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import alexman.dndboard.entity.CharacterState;
import alexman.dndboard.entity.FlyweightCharacter;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class CharacterModel implements ICharacterModel {

	private JSONObject allFlyweightCharactersObject;

	/**
	 * TODO
	 *
	 * @param reader
	 *
	 * @throws IOException
	 */
	public CharacterModel(Reader reader) throws IOException {
		read(reader);
	}

	@Override
	public void read(Reader reader) throws IOException {
		allFlyweightCharactersObject = new JSONObject(new JSONTokener(reader));
	}

	@Override
	public void write(Writer writer) throws JSONException, IOException {
		writer.write(allFlyweightCharactersObject.toString(4));
	}

	@Override
	public Iterable<String> getCharacterNamesFromCache() {
		return allFlyweightCharactersObject.keySet();
	}

	@Override
	public FlyweightCharacter readFlyweightFromCache(String flyweightName) throws IOException {

		JSONObject flyweightObject = allFlyweightCharactersObject.getJSONObject(flyweightName);

		EnumMap<CharacterState, String> flyweightSpriteMap = new EnumMap<>(CharacterState.class);

		JSONArray spriteArray = flyweightObject.getJSONArray("sprites");
		for (int i = 0; i < spriteArray.length(); i++) {

			JSONObject currSprite = spriteArray.getJSONObject(i);
			CharacterState state = currSprite.getEnum(CharacterState.class, "state");
			String sprite = currSprite.getString("sprite");

			if (flyweightSpriteMap.containsKey(state)) {
				throw new IllegalStateException("Duplicate sprite found for character "
				        + flyweightName + " and state " + state);
			}

			flyweightSpriteMap.put(state, sprite);
		}

		int flyweightMaxHp = flyweightObject.getInt("maxHp");

		return new FlyweightCharacter(flyweightName, flyweightSpriteMap, flyweightMaxHp);
	}

	@Override
	public void writeFlyweightCharacterToCache(FlyweightCharacter flyweightCharacter) {

		JSONObject characterObject = new JSONObject();

		characterObject.put("maxHp", flyweightCharacter.getMaxHp());

		JSONArray characterSpriteArray = new JSONArray();
		for (Entry<CharacterState, String> e : flyweightCharacter.getSpritePathMap().entrySet()) {
			JSONObject spriteObject = new JSONObject();

			spriteObject.put("state", e.getKey());
			spriteObject.put("sprite", e.getValue());

			characterSpriteArray.put(spriteObject);
		}

		characterObject.put("sprites", characterSpriteArray);

		allFlyweightCharactersObject.put(flyweightCharacter.getName(), characterObject);
	}
}
