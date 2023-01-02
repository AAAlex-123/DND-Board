package alexman.dndboard.entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

/**
 * Represents a 'class' of Characters, for example all 'dog' Characters.
 * <p>
 * This class hold all the shared data for the Characters, namely their names,
 * sprites, and maxHp, which does not change among all 'dog' Characters.
 *
 * @author Alex Mandelias
 *
 * @see Character
 */
public class FlyweightCharacter {

	private final String name;
	private final EnumMap<CharacterState, Image> spriteMap;
	private final EnumMap<CharacterState, String> spritePathMap;
	private final int maxHp;

	/*
	 * ... and other *shared* dnd data ...
	 *
	 * @foff
	 * don't forget to change:
	 *  - constructor
	 *  - getters
	 *  - toString
	 *  - characterModel.read/write
	 * @fon
	 */

	/**
	 * Constructs a new FlyweightCharacter, which contains data that is shared among
	 * many Character instances.
	 *
	 * @param name the name of the new Flyweight
	 * @param spriteMap a map with the sprites of this Flyweight for each Character
	 *        State
	 * @param maxHp the max hp for this Flyweight
	 *
	 * @throws IOException if any of the Images for the sprites cannot be loaded
	 */
	public FlyweightCharacter(String name, Map<CharacterState, String> spriteMap, int maxHp)
	        throws IOException {
		this.name = name;
		this.spriteMap = new EnumMap<>(CharacterState.class);
		this.spritePathMap = new EnumMap<>(spriteMap);
		this.maxHp = maxHp;

		for (Entry<CharacterState, String> e : spriteMap.entrySet()) {
			this.spriteMap.put(e.getKey(), ImageIO.read(new File(e.getValue())));
		}
	}

	/**
	 * Returns this FlyweightCharacter's name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns this FlyweightCharacter's sprite that corresponds to a state.
	 *
	 * @param state the state
	 *
	 * @return an Image for the sprite that corresponds to the state
	 */
	public Image getSprite(CharacterState state) {
		return spriteMap.get(state);
	}

	/**
	 * Returns a copy of this FlyweightCharacter's spritePathMap.
	 *
	 * @return the spritePathMap
	 */
	public Map<CharacterState, String> getSpritePathMap() {
		return new EnumMap<>(spritePathMap);
	}

	/**
	 * Returns this FlyweightCharacter's maxHp.
	 *
	 * @return the maxHp
	 */
	public int getMaxHp() {
		return maxHp;
	}

	@Override
	public String toString() {
		return String.format("FlyweightCharacter [name=%s, spritePathMap=%s, maxHp=%s]", name,
		        spritePathMap, maxHp);
	}
}
