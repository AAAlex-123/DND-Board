package alexman.dndboard.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import alexman.dndboard.model.ICharacterModel;

/**
 * Factory for Flyweights. It uses a character model to load and then cache the
 * Flyweights.
 *
 * @author Alex Mandelias
 *
 * @see FlyweightCharacter
 */
public class FlyweightFactory {

	private final ICharacterModel characterModel;
	private final Map<String, FlyweightCharacter> cache = new HashMap<>();

	/**
	 * Constructs a Flyweight Factory which reads flyweights from a model.
	 *
	 * @param characterModel the model
	 */
	public FlyweightFactory(ICharacterModel characterModel) {
		this.characterModel = characterModel;
	}

	/**
	 * Returns the Flyweight with the given name. The Flyweight is created the first
	 * time it is requested and then is cached for future calls.
	 *
	 * @param flyweightName the name of the Flyweight to return
	 *
	 * @return the Flyweight with the given name
	 *
	 * @throws IOException if the creation of the Flyweight failed
	 */
	public FlyweightCharacter getFlyweight(String flyweightName) throws IOException {
		if (!cache.containsKey(flyweightName)) {
			cache.put(flyweightName, characterModel.readFlyweightFromCache(flyweightName));
		}

		return cache.get(flyweightName);
	}
}
