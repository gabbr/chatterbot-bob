// $ANTLR : "Bob.g" -> "Bob_Parser.java"$
package it.unibz.lib.bob.check;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.MalformedCachePatternException;

/**
 *
 * @version $Id: Bob_LexerTokenTypes.java 313 2010-12-22 10:41:47Z markus.grandpre $
 */
public interface Bob_LexerTokenTypes
{
  int EOF = 1;

  int NULL_TREE_LOOKAHEAD = 3;

  int OR = 4;

  int AND = 5;

  int NOT = 6;

  int LEFT_PAREN = 7;

  int RIGHT_PAREN = 8;

  int BLANK = 9;

  int STRING_LITERAL = 10;

  int ESCAPE = 11;

  int CONC_ = 12;

  int EMPTYSTRING_ = 13;

}
