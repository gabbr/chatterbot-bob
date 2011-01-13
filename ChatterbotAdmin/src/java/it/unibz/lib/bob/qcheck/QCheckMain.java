package it.unibz.lib.bob.qcheck;

import it.unibz.lib.bob.check.MyAST;
import it.unibz.lib.bob.check.Bob_Lexer;
import it.unibz.lib.bob.check.Bob_Parser;
import it.unibz.lib.bob.check.Bob_TreeParser;

import java.io.Reader;
import java.io.StringReader;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.debug.misc.ASTFrame;

public class QCheckMain {

	public static void main(String[] argv) {
		Reader reader;
		String regex = "\"_a\\'b_\"";
		
		regex = regex.replaceAll("_", "\\\\b");
		//regex = regex.replaceAll("_", "\\\\b");
		
		// unmatched parenthesis 1
//		String regex_error1 = "((\"  )  ^ *$\")||((\"^(#POSITIVE#|#WEITER#)#SATZENDE#\")))";
		// unmatched parenthesis 2
//		String regex_error2 = "((\"^ *$\")  )  ||((\"^(#POSITIVE#|#WEITER#)#SATZENDE#\")))";

		String stringToMatch = "a";
		String result = null;

		Bob_Lexer lexer;
		Bob_Parser parser;
		Bob_TreeParser treeparser;
		try {
			reader = new StringReader(regex);
			lexer = new Bob_Lexer(reader);
			parser = new Bob_Parser(lexer);
			parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
			treeparser = new Bob_TreeParser();
			treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");

			parser.bExpression();
			MyAST tree = (MyAST) parser.getAST();
			// System.out.println(tree.toStringTree());
			/*
			 * try { BufferedWriter out = new BufferedWriter(new
			 * OutputStreamWriter( new FileOutputStream("ast.txt"), "UTF8"));
			 * out.write(tree.toStringTree()); out.close(); } catch
			 * (UnsupportedEncodingException ue) { System.err.println("Not
			 * supported : "); } catch (IOException e) {
			 * System.err.println(e.getMessage()); } ASTFrame frame = new
			 * ASTFrame("The tree", tree); frame.setVisible(true);
			 */

			ASTFrame frame = new ASTFrame("The tree", tree);
			frame.setVisible(true);

			result = "(" + treeparser.bExpression(tree, stringToMatch) + ")";
			System.out.println(result);

		} catch (RecognitionException e) {
			System.err.println("TreeParser threw RecognitionException. "
					+ e.toString());
		} catch (TokenStreamException e) {
			System.err.println("TreeParser threw TokenStreamException. "
					+ e.toString());
		} catch (ANTLRException e) {
			System.err.println("TreeParser threw ANTLRException. "
					+ e.toString());
		} catch (Exception e) {
			System.err.println("TreeParser threw Exception. " + e.toString());
		}
	}
}