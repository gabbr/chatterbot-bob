package it.unibz.lib.bob.check;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <p>
 *
 * </p>
 *
 * @author manuel.kirschner@gmail.com
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

  /**
   * <p>
   * inner class to do scoring of the map
   * </p>
   */
  public static class ValueComparer implements Comparator<Object>
  {

    /**
     * <p>
     * 
     * </p>
     */
    private Map<String, String> _data = null;

    /**
     * <p>
     * 
     * </p>
     * 
     * @param data 
     */
    public ValueComparer(Map<String, String> data)
    {
      super();
      _data = data;
    }

    @Override
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
}
