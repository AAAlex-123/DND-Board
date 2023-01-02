package alexman.dndboard.entity;

import java.awt.Image;
import java.awt.Point;

/**
 * Represents a Character instance on the DND Board.
 * <p>
 * This class holds all the unique data for each Character, such as their
 * display name and current position. Since there can be many instances of the
 * same character (for example many different 'dog' characters on the same
 * board), each Character instance is associated with a Flyweight which stores
 * all the data which is shared between instances of the same character.
 *
 * @author Alex Mandelias
 *
 * @see FlyweightCharacter
 */
public class Character {

	private final FlyweightCharacter flyweight;
	private final String displayName;
	private CharacterState state = CharacterState.IDLE;
	private int hp;
	private Point pos = new Point(0, 0);
	private boolean flipped = false;

	/*
	 * ... and other *shared* dnd stuff ...
	 *
	 * @foff
	 * don't forget to change:
	 *  - constructor
	 *  - getters
	 *  - toString
	 *  - areaModel.read/write
	 * @fon
	 */

	/**
	 * Constructs a new Character instance which belongs to a given character type,
	 * a Flyweight.
	 *
	 * @param flyweightCharacter the Flyweight that this Character belongs to
	 * @param displayName the display name for this Character
	 */
	public Character(FlyweightCharacter flyweightCharacter, String displayName) {
		this.flyweight = flyweightCharacter;
		this.displayName = displayName;
		this.hp = flyweight.getMaxHp();
	}

	/**
	 * Returns this FlyweightCharacter's name.
	 *
	 * @return the name
	 */
	public String getCharacterName() {
		return flyweight.getName();
	}

	/**
	 * Returns the Image for the Character's current state.
	 *
	 * @return an Image for the sprite that corresponds to the state
	 */
	public Image getSpriteForCurrentState() {
		return flyweight.getSprite(state);
	}

	/**
	 * Returns this Character's name.
	 *
	 * @return the name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public CharacterState getState() {
		return state;
	}

	public void setState(CharacterState newState) {
		if ((state == CharacterState.DEAD) && (newState == CharacterState.DOWN)) {
			throw new IllegalArgumentException("Can't go from DEAD to DOWN");
		}

		state = newState;
	}

	/**
	 * Returns this Character's maxHp.
	 *
	 * @return the maxHp
	 */
	public int getMaxHp() {
		return flyweight.getMaxHp();
	}

	/**
	 * Returns this Character's hp.
	 *
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Increases the character's hp by a given amount. The character's hp is capped
	 * at its max hp.
	 *
	 * @param healHp the amount to heal
	 *
	 * @throws IllegalArgumentException if the healHp is negative
	 */
	public void heal(int healHp) {
		if (healHp < 0) {
			throw new IllegalArgumentException("heal hp cannot be negative");
		}

		int newHp = getHp() + healHp;
		setHp(Math.max(getMaxHp(), newHp));
	}

	/**
	 * Decreases the character's hp by a given amount. The character's hp is capped
	 * at 0.
	 *
	 * @param damageHp the amount to damage
	 *
	 * @throws IllegalArgumentException if the healHp is negative
	 */
	public void damage(int damageHp) {
		if (damageHp < 0) {
			throw new IllegalArgumentException("damage hp cannot be negative");
		}

		int newHp = getHp() - damageHp;
		setHp(Math.min(0, newHp));
	}

	/**
	 * Sets the hp to hp.
	 *
	 * @param hp the new value
	 */
	public void setHp(int hp) {
		if (hp < 0) {
			throw new IllegalArgumentException("new hp cannot be negative");
		}

		if (hp > getMaxHp()) {
			throw new IllegalArgumentException("new hp cannot exceed max hp");
		}

		this.hp = hp;
	}

	/**
	 * Returns this Character's pos.
	 *
	 * @return the pos
	 */
	public Point getPos() {
		return pos;
	}

	/**
	 * Sets the pos to pos.
	 *
	 * @param pos the new value
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}

	/**
	 * Returns this Character's flipped.
	 *
	 * @return the flipped
	 */
	public boolean getFlipped() {
		return flipped;
	}

	/**
	 * Sets the flipped to flipped.
	 *
	 * @param flipped the new value
	 */
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	/** Sets the flipped to its opposite. */
	public void flip() {
		this.flipped = !flipped;
	}

	@Override
	public String toString() {
		return String.format("Character [flyweight=%s, displayName=%s, hp=%s, pos=%s, flipped=%s]",
		        flyweight, displayName, hp, pos, flipped);
	}
}
