package alexman.dndboard.model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.json.JSONException;

import alexman.dndboard.entity.FlyweightCharacter;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public interface ICharacterModel {

	/**
	 * TODO
	 *
	 * @param reader
	 * @throws IOException
	 */
	void read(Reader reader) throws IOException;


	/**
	 * TODO
	 *
	 * @param writer
	 * @throws JSONException
	 * @throws IOException
	 */
	void write(Writer writer) throws JSONException, IOException;

	/**
	 * TODO
	 *
	 * @return
	 */
	Iterable<String> getCharacterNamesFromCache();

	/**
	 * TODO
	 *
	 * @param flyweightName
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	FlyweightCharacter readFlyweightFromCache(String flyweightName) throws IOException;

	/**
	 * TODO
	 *
	 * @param flyweightCharacter
	 */
	void writeFlyweightCharacterToCache(FlyweightCharacter flyweightCharacter);
}
