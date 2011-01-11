package it.unibz.lib.bob.check;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @version $Id$
 */
public class MapValueSort {

	/** inner class to do scoring of the map **/
	public static class ValueComparer implements Comparator<Object> {
		private Map<String, String> _data = null;

		public ValueComparer(Map<String, String> data) {
			super();
			_data = data;
		}

		public int compare(Object key1, Object key2) {
			Comparable<Comparable<?>> value1 = (Comparable) _data.get(key1);
			Comparable<?> value2 = (Comparable<?>) _data.get(key2);
			int c = value1.compareTo(value2);
			if (0 != c)
				return c;
			Integer h1 = key1.hashCode(), h2 = key2.hashCode();
			return h1.compareTo(h2);
		}
	}

	public static void main(String[] args) {

		Map<String, String> unsortedData = new HashMap<String, String>();
		unsortedData.put("2", "DEF");
		unsortedData.put("1", "ABC");
		unsortedData.put("4", "ZXY");
		unsortedData.put("3", "BCD");

		SortedMap<String, String> sortedData = new TreeMap<String, String>(new MapValueSort.ValueComparer(
				unsortedData));

		printMap(unsortedData);

		sortedData.putAll(unsortedData);
		System.out.println();
		printMap(sortedData);
	}

	private static void printMap(Map<String, String> data) {
		for (Iterator<String> iter = data.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			System.out.println("Value/key:" + data.get(key) + "/" + key);
		}
	}

}