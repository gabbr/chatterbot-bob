package it.unibz.lib.bob.check;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 *
 * @version $Id$
 */
public class MapValueSort
{
  /**
   * <p>
   * Logging of this class uses four different log levels:
   * </p>
   * <ul>
   * <li><b>DEBUG</b> to reproduce complete program flow</li>
   * <li><b>INFO</b> to reproduce system activities</li>
   * <li><b>WARN</b> to reproduce system warnings</li>
   * <li><b>ERROR</b> to reproduce system failures</li>
   * <li><b>FATAL</b> to reproduce fatal system failures</li>
   * </ul>
   * <p>
   * The corresponding <tt>log4j.properties</tt> file is located in the
   * <tt>WEB-INF/classes</tt> directory of this web application.
   * </p>
   */
  private static Logger log = Logger.getLogger(MapValueSort.class);

  /* inner class to do scoring of the map */
  public static class ValueComparer implements Comparator<Object>
  {
    private Map<String, String> _data = null;

    public ValueComparer(Map<String, String> data)
    {
      super();
      _data = data;
    }

    public int compare(Object key1, Object key2)
    {
      Comparable<Comparable<?>> value1 = (Comparable) _data.get(key1);
      Comparable<?> value2 = (Comparable<?>) _data.get(key2);
      int c = value1.compareTo(value2);
      if (0 != c)
      {
        return c;
      }
      Integer h1 = key1.hashCode(), h2 = key2.hashCode();
      return h1.compareTo(h2);
    }
  }

  private static void printMap(Map<String, String> data)
  {
    for (Iterator<String> iter = data.keySet().iterator(); iter.hasNext();)
    {
      String key = (String) iter.next();
      log.debug("Value/key:" + data.get(key) + "/" + key);
    }
  }
}
