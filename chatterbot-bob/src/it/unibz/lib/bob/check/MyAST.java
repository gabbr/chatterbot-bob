/**
 * Extension of basic AST type to include line number info 
 */

package it.unibz.lib.bob.check;
import antlr.CommonAST;
import antlr.Token;

@SuppressWarnings("serial")
public class MyAST extends CommonAST {

	private Token token;

	public void initialize(Token tok) {
		token = tok;
		super.initialize(tok);
	}

	public int getLine() {
		if (token != null)
			return token.getLine();		
		else
			return -1;
	}
	
	public String getText() {
		if (token != null)
			return token.getText();		
		else
			return null;
	}

	public int getColumn() {
		if (token != null)
			return token.getColumn();
		else
			return -1;
	}

}
