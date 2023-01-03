package alexman.dndboard.gui;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import alexman.dndboard.entity.Character;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public class CharacterManager {

	private final Map<String, CharacterGraphic> characterGraphicMap = new HashMap<>();
	private final JPanel characterPanel = new JPanel();

	public List<String> getCharacterNames() {
		return new LinkedList<>(characterGraphicMap.keySet());
	}

	public List<CharacterGraphic> getCharacterGraphics() {
		return new LinkedList<>(characterGraphicMap.values());
	}

	public void addCharacter(Character character) {
		// TODO: check
		CharacterGraphic characterGraphic = new CharacterGraphic(character);
		characterGraphicMap.put(character.getDisplayName(), characterGraphic);
		characterPanel.add(characterGraphic);
		characterPanel.repaint();
	}

	public void removeCharacter(String name) {
		// TODO: check
		CharacterGraphic characterGraphicToRemove = characterGraphicMap.remove(name);
		characterPanel.remove(characterGraphicToRemove);
		characterPanel.repaint();
	}

	public void removeAllCharacters() {
		characterGraphicMap.clear();
		characterPanel.removeAll();
	}

	public void recalculateZOrderForAllComponents() {
		Component[] comps = characterPanel.getComponents();
		Arrays.sort(comps, (c1, c2) -> (c2.getY() + c2.getHeight()) - (c1.getY() + c1.getHeight()));
		for (int i = 0; i < comps.length; i++) {
			characterPanel.setComponentZOrder(comps[i], i);
		}
	}

	/**
	 * Returns this CharacterManager's characterPanel.
	 *
	 * @return the characterPanel
	 */
	public JPanel getCharacterPanel() {
		return characterPanel;
	}

}
