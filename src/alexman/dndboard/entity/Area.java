package alexman.dndboard.entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * Represents an Area of the DND Board, for example a School or a Church.
 * Characters are placed inside Areas.
 *
 * @author Alex Mandelias
 */
public class Area {

	private final String name;
	private final Image backgroundImage;
	private final String backgroundImagePath;
	private final Set<Character> characters = new HashSet<>();

	/**
	 * Constructs a new Area with a name and a sprite for its background.
	 *
	 * @param name the name of the new Area
	 * @param backgroundImageSprite the sprite
	 *
	 * @throws IOException if the Image associated with the sprite cannot be loaded
	 */
	public Area(String name, String backgroundImageSprite) throws IOException {
		this.name = name;
		this.backgroundImage = ImageIO.read(new File(backgroundImageSprite));
		this.backgroundImagePath = backgroundImageSprite;
	}

	/**
	 * Moves a Character from one Area to another. The Character must be present in
	 * the first area and absent from the second.
	 *
	 * @param from the Area to move from
	 * @param to the Area to move to
	 * @param character the Character to move
	 *
	 * @throws IllegalArgumentException if either the Character is not in the first
	 *         Area or it is in the second Area
	 */
	public static void move(Area from, Area to, Character character) {
		from.removeCharacter(character);
		to.addCharacter(character);
	}

	/**
	 * Returns this Area's name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns this Area's backgroundImage.
	 *
	 * @return the backgroundImage
	 */
	public Image getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * Returns this Area's backgroundImagePath.
	 *
	 * @return the backgroundImagePath
	 */
	public String getBackgroundImagePath() {
		return backgroundImagePath;
	}

	/**
	 * Returns a copy of this Area's characters.
	 *
	 * @return the characters
	 */
	public Set<Character> getCharacters() {
		return new HashSet<>(characters);
	}

	/**
	 * Adds a Character to this Area.
	 *
	 * @param character the Character to add
	 *
	 * @throws IllegalArgumentException if the Character is already in this Area
	 */
	public void addCharacter(Character character) {
		if (characters.contains(character)) {
			throw new IllegalArgumentException("Area already contains the given Character");
		}

		characters.add(character);
	}

	/**
	 * Removes a Character from this Area.
	 *
	 * @param character the Character to remove
	 *
	 * @throws IllegalArgumentException if the Character is not in this Area
	 */
	public void removeCharacter(Character character) {
		if (!characters.contains(character)) {
			throw new IllegalArgumentException("Area does not contain the given Character");
		}

		characters.remove(character);
	}

	@Override
	public String toString() {
		return String.format(
		        "Area [name=%s, backgroundImage=%s, backgroundImagePath=%s, characters=%s]", name,
		        backgroundImage, backgroundImagePath, characters);
	}
}
