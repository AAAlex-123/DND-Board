package alexman.dndboard.model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.json.JSONException;

import alexman.dndboard.entity.Area;

/**
 * TODO
 *
 *
 * @author Alex Mandelias
 */
public interface IAreaModel {

	/**
	 * TODO
	 *
	 * @param reader
	 *
	 * @throws IOException
	 */
	void read(Reader reader) throws IOException;

	/**
	 * TODO
	 *
	 * @param writer
	 *
	 * @throws JSONException
	 * @throws IOException
	 */
	void write(Writer writer) throws JSONException, IOException;

	/**
	 * TODO
	 *
	 * @return
	 */
	Iterable<String> getAreaNamesFromCache();

	/**
	 * TODO
	 *
	 * @param areaName
	 * @param flyweightFactory
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	Area loadAreaFromCache(String areaName, ICharacterModel characterModel) throws IOException;

	/**
	 * TODO
	 *
	 * @param area
	 */
	void saveAreaToCache(Area area);

}
