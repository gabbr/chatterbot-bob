// $Id: Bob.g 313 2010-12-22 10:41:47Z markus.grandpre $

header {
	package it.unibz.lib.bob.check;
	import org.apache.oro.text.perl.Perl5Util;
	import org.apache.oro.text.MalformedCachePatternException;
}

class Bob_Lexer extends Lexer;

options {
	k=3; 								// 3 characters of lookahead
   	charVocabulary='\u0000'..'\uFFFE'; 	// Allow any char but \uFFFF (16 bit -1)
}    

// Boolean operations; 
// Operations precedence is encoded in the rules
OR : "||";
AND: "&&";
NOT: "!";

// Grouping with parentheses
LEFT_PAREN: '(';
RIGHT_PAREN: ')'; 

BLANK: (
	options {greedy = true;}
	:   '\r' '\n' {newline();}
    |   '\n'      {newline();}
    |   ' '
    |   '\t'
    );

STRING_LITERAL         
	:	( '"' ( ESCAPE | ~('"'|'\\') )* '"' );

protected
ESCAPE
    :    '\\'
         ( 'n' { $setText("\\n"); }
         | 'r' { $setText("\\r"); }
         | 't' { $setText("\\t"); }
         | 'b' { $setText("\\b"); }
         | '"' { $setText("\""); }
         | '\\'   
         | '?'  
         | '!' 
         | '.'  
         | '+' 
         | '*' 
         | '[' 
         | ']' 
         | '(' 
         | ')'  
         | '^' 
         | '$' 
         | '/' 
         | "'"
         )
    ;

class Bob_Parser extends Parser;

options {
        buildAST=true;
		ASTLabelType = "it.unibz.lib.bob.check.MyAST";
		defaultErrorHandler=false;
}

tokens {
	CONC_;	
	EMPTYSTRING_;
}

bExpression 	: bExpression2 EOF!;
bExpression2 	: bTerm  ( OR^ bTerm )*;	    
bTerm 			:  notFactor  ( AND^  notFactor  )*;
notFactor 		: (BLANK!)* (NOT^ (BLANK!)*)?  bFactor (BLANK!)*; // allow blanks in boolean part
bFactor 		: parExpression | quotedRe;
parExpression 	: LEFT_PAREN! bExpression2 RIGHT_PAREN!;
quotedRe 		: x:STRING_LITERAL;

class Bob_TreeParser extends TreeParser;
options {
        // need own type for line numbers  
		ASTLabelType = "it.unibz.lib.bob.check.MyAST";
}


bExpression [String query] returns [boolean result] throws ANTLRException
{
	result=false;
	boolean a = false;
	boolean b = false;
	Perl5Util regex = new Perl5Util();	
	}
			 : 					
 						  #(OR a=bExpression[query] b=bExpression[query]) { result = a || b; }
						| #(AND a=bExpression[query] b=bExpression[query]) { result = a && b; }
						| #(NOT a=bExpression[query]) { result = !a; } 					
						| s:STRING_LITERAL { //try {
												result = regex.match(s + "i", query); 
												//}	
												//catch (MalformedCachePatternException me){
												//    System.err.println("Caught regex error"); //on line " + s.getLine());
												//    throw new ANTLRException();
											
											//}
												//catch (Exception me){
												//    System.err.println("Caught strange error"); //on line " + s.getLine());
												//    throw new ANTLRException();
												//}
												    
												
											}
						;		

	exception // for rule
    catch [ANTLRException ex] {   
       //reportError("Oops: " + ex.toString());
       
       //needed so that giedre's code can detect the error
       throw ex;
    }	
