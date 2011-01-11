// $Id$

header {
package it.unibz.lib.macroparser;
}
 

class macroParser_Lexer extends Lexer;
options{
	   // Allow any char but \uFFFF (16 bit -1)
    charVocabulary='\u0000'..'\uFFFE';
}
NON_MACRO	: ( ~( '#' ))+
			;
MACRO		: '#' (NON_MACRO) '#'
	        ; 

class macroParser_Parser extends Parser;
options {
        buildAST=true;
}
tokens {
	STRING_;
}
string 	: c:token (! b:token {#string = #([STRING_,"string"], #c, #b); } )*
		;

token	: (a:MACRO 		{#token = #([MACRO,"macro"], #a);} 
		|  b:NON_MACRO 	{#token = #([NON_MACRO,"non_macro"], #b);}) 
		; 

class macroParser_TreeParser extends TreeParser;
string returns [String result]
{
	result="";
}
		 : 
		 ast:STRING_ { 
		 	AST firstchild = ast.getFirstChild(); 
		 	do {
		 		if (firstchild.getType() == MACRO) {
		 			result += "MACRO";
		 		} else if (firstchild.getType() == NON_MACRO) {
		 			result += firstchild.getFirstChild().toString();
		 		}
		 	} while 
				((firstchild = firstchild.getNextSibling()) != null);
		 }
;
