// $ANTLR : "Bob.g" -> "Bob_Parser.java"$
package it.unibz.lib.bob.check;

/**
 * <p>
 *
 * </p>
 *
 * @author manuel.kirschner@gmail.com
 * @version $Id$
 */
public interface Bob_LexerTokenTypes
{
  // improve: public/protected final int

  /**
   * <p>
   *
   * </p>
   */
  int EOF = 1;

  /**
   * <p>
   *
   * </p>
   */
  int NULL_TREE_LOOKAHEAD = 3;

  /**
   * <p>
   *
   * </p>
   */
  int OR = 4;

  /**
   * <p>
   *
   * </p>
   */
  int AND = 5;

  /**
   * <p>
   *
   * </p>
   */
  int NOT = 6;

  /**
   * <p>
   *
   * </p>
   */
  int LEFT_PAREN = 7;

  /**
   * <p>
   *
   * </p>
   */
  int RIGHT_PAREN = 8;

  /**
   * <p>
   *
   * </p>
   */
  int BLANK = 9;

  /**
   * <p>
   *
   * </p>
   */
  int STRING_LITERAL = 10;

  /**
   * <p>
   *
   * </p>
   */
  int ESCAPE = 11;

  /**
   * <p>
   *
   * </p>
   */
  int CONC_ = 12;

  /**
   * <p>
   *
   * </p>
   */
  int EMPTYSTRING_ = 13;
}
