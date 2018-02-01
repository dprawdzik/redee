package jflex; 

import java.io.StringReader;
import java.io.IOException;
import java.util.Iterator;
import java.lang.NullPointerException;
import java.util.List;
import java.util.ArrayList;

import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;
import com.scout24.redee.extraction.jflex.*;

%%

%class Tokenizer
%public
%unicode
%column
%char
%function next_token
%type Token

%{
    /* ############### */
    /* USER CODE START */
    /* ############### */

	private Token nextToken; 
    private String text;
    
    /**
     * Default constructor.
     */
    public Tokenizer() throws Exception {
        super();
        this.zzReader = new StringReader("");
        this.nextToken =null;
    }

    
    /**
     * Creates a new tokenizer.<br>
     * Try to reuse this tokenizer using the reinit method, since the creation of this class is expensive.
     * 
     * @param input the string to be tokenized
     */
    public Tokenizer(final String input) throws IOException, Exception {
        super();
        if (input == null) {
        	throw new NullPointerException("The tokenizer does not accept 'null' strings"); 
        }
        this.zzReader = new StringReader(input);
        this.nextToken = next_token(); 
        this.text = input;
    }

    public List<Token> toList() {

        List<Token> tokens = new ArrayList<>();
        while(hasNext()) {
            Token next = next();
            tokens.add(next);
        }
        return tokens;
    }

       public List<Token> toList(String tokenType) {

            List<Token> tokens = new ArrayList<>();
            while(hasNext()) {
                Token next = next();
                if(next.getType().equals(tokenType))
                    tokens.add(next);
            }
            return tokens;
        }

    /** @return true if the Tokenizer has more elements. */
    public boolean hasNext() {
        return this.nextToken != null; 
    }
    
    /**@return the next token*/
    public Token next() {
        if (this.nextToken == null) {
        	throw new NoSuchElementException(); 
        }
    	Token token = this.nextToken;
    	try { 
    		this.nextToken = next_token();
    	} catch (IOException e) {
    		throw new RuntimeException("Unable to fetch next token from stream", e); 
   		} 
    	return token; 
    }
    
    public void remove() {
    	throw new UnsupportedOperationException(); 
    }

    
    /** {@inheritDoc} */
    // @Override
    public void reInit(final String input) throws IOException {
    	StringReader stringReader = new StringReader(input);
        this.yyreset(stringReader);
        this.nextToken = next_token();
        this.text = input;
    }
    
    
    /** {@inheritDoc} */
    // @Override
    public void close() throws IOException {
        this.yyclose();
    }
    
    /** {@inheritDoc} */
    // @Override
    public String getTokenizedString() {
        return this.text;
    }

    /* ############# */
    /* USER CODE END */
    /* ############# */

%}

DOMAIN     = "mil" | "info" | "gov" | "edu" | "biz" | "com" | "org" | "net" | 
             "arpa" | {LETTER}{2}
UNITOFCURRENCY = "€" | "Euro" | "euro" | "EUR" | "Eur" | "Dollar" | "USD" | "US Dollar" | "$" | "Pfund" | "GBP" | "Britische Pfund" | "£" |
				 "Franc" | "franc" | "Mark" | "Rubel" | "Franken" |  
             	 "Złoty" | "Zloty" | "Krone" | "Kronen" |
             	 "Japanische Yen" | "Yen" | "JPY"  
             	 
WEIGHTNAME = "Kilogramm" | "KG" | "Kg" | "Kg." | "KG." | "Gramm" | "Gr." | "gr." | "Tonne" | "Tonnen" | "T" | "t"

// Examples are used from this site: http://de.wikipedia.org/wiki/Zahlennamen
NUMBERNAMES = "Hundert" | "Tausend" | "Million" | "Millionen" | "Mio" | "Mill" | "Mrd" | "Mia" | "Md" | "Milliarde" | "Milliarden" | 
"Billion" | "Billionen" | "Bio" | "Bill" | "Billiarde" | "Billiarden" | "Trillion" | "Trillionen" | "Trilliarde" | "Quadrillion" | "Zillion" | "Zillionen"
                      
WHITESPACE = \r\n | [ \r\n\t\f] | [ ]
LETTER     = [\u0041-\u005a\u0061-\u007a\u00c0-\u00d6\u00d8-\u00f6\u00f8-\u00ff\u0100-\u1fff\u3040-\u318f\u3300-\u337f\u3400-\u3d2d\u4e00-\u9fff\uf900-\ufaff]
DIGIT      = [\u0030-\u0039\u0660-\u0669\u06f0-\u06f9\u0966-\u096f\u09e6-\u09ef\u0a66-\u0a6f\u0ae6-\u0aef\u0b66-\u0b6f\u0be7-\u0bef\u0c66-\u0c6f\u0ce6-\u0cef\u0d66-\u0d6f\u0e50-\u0e59\u0ed0-\u0ed9\u1040-\u1049]

TERM       = ({LETTER})+ | {LETTER} ({LETTER} | "'"({LETTER}))*

MIXED       = ({LETTER}|{DIGIT})* {LETTER} ({LETTER}|{DIGIT}|("'"({LETTER})))*
SYMBOL     = ({LETTER}|{DIGIT})({LETTER}|{DIGIT}|"_"|"-")*
BARE_URL   = {SYMBOL}("."{SYMBOL})*"."{DOMAIN}
/* URL_PATH   = ("/" {SYMBOL})+ ("." {SYMBOL})? ("/")? ("?" {SYMBOL} ((";" | ":" | "@" | "&" | "=") {SYMBOL})*)? */
URL_PATH   = (";" | ":" | "@" | "&" | "=" | "?" | "/" | "_" | "%" | "." | {LETTER} | {DIGIT})+

MONTH     = ("Januar" | "Februar" | "Mai" | "Juni" | "Juli" | "August" | "Oktober" | "September" | "November" | "Dezember"
        "Jan." | "Feb." | "Aug." | "Okt." | "Sept." | "Nov." | "Dez.")

%%

"Balkon" | "Loggia"	    { Token t = new Token(yytext(), TokenType.BALCONY, yychar, (yylength() + yychar)); return t; }
[Oo]"hne" | "KEINEN" | "KEIN" | [N|n]"ahezu" | [G|g]"roßteil" | [G|g]"roßenteils" | [K|k]"einen" | [K|k]"ein" | [k|K]"einer" | [N|n]"icht" | "k e i n"	| "k e i n e n"    { Token t = new Token(yytext(), TokenType.NEGATION, yychar, (yylength() + yychar)); return t; }


/* "I.B.M." */
{DIGIT}{2}"."{DIGIT}{2}".20"{DIGIT}{2}" um "{DIGIT}{2}":"{DIGIT}{2}" Uhr"	{ Token t = new Token(yytext(), TokenType.APPOINTMENT, yychar, (yylength() + yychar)); return t; }

/* 02.02.1995 */
{DIGIT}{2}"."{DIGIT}{2}".19"{DIGIT}{2}                 	{ Token t = new Token(yytext().trim(), TokenType.DATE, yychar, (yylength() + yychar)); return t; }

/*  Oktober 2014 */
{MONTH}" "{DIGIT}{4}                                    { Token t = new Token(yytext().trim(), TokenType.DATE, yychar, (yylength() + yychar)); return t; }

/* 02.02.2014 */
{DIGIT}{2}"."{DIGIT}{2}".20"{DIGIT}{2}            	    { Token t = new Token(yytext().trim(), TokenType.DATE, yychar, (yylength() + yychar)); return t; }

/* ex.: 10.01.2010, 1,2345, 1/2 */
/* {DIGIT}+  ((":" | "-" | "/" | "," | ".") {DIGIT}+)*  	{ Token t = new Token(yytext(), TokenType.NUMERIC, yychar, (yylength() + yychar)); return t; } */

"&quot;"												{ Token t = new Token("\"", TokenType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }

"&amp;"													{ Token t = new Token("&", TokenType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }

"*" | "@" | "&" | "+"  | "§"							{ Token t = new Token(yytext(), TokenType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }

/** Ignore HTML entities */
"&" [a-zA-Z0-9#]+ ";"                                  	{ ; }

/** Ignore HTML tags */
"<" ("/")* [a-zA-Z0-9=]+ ">"                                  	{ ; }

/** Ignore HTML tags */
"&lt;" ("/")* ([a-zA-Z0-9] | "=" | "\"")*  "&gt;"          	 	{ ; }

"&lt;" ("/")* "b" + "&gt;"                               		{ ; }

"&lt;" ("/")* [a-zA-Z0-9]+ "&gt;"                               { ; }

/* "I.B.M." */
({LETTER} "." ({LETTER} ".")+) 									{ Token t = new Token(yytext(), TokenType.COMPOSITE, yychar, (yylength() + yychar)); return t; }


// EXAMPLE: "1&1"
{DIGIT}+ {WHITESPACE}* "&" {WHITESPACE}* {DIGIT}+				{ Token t = new Token(yytext(),TokenType.COMPOSITE, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "laptop", "walkman" */
{LETTER} "." 												 	{ Token t = new Token(yytext(),TokenType.TERM, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "Konrad-Adenauer-Stiftung" */
({LETTER})+ ( "-" ({LETTER})+)+                                 { Token t = new Token(yytext(), TokenType.HYPHENATION, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "DVD-1080P7" */
{MIXED} ( "-" {MIXED})+                                       	{ Token t = new Token(yytext(), TokenType.COMPOSITE, yychar, (yylength() + yychar));  return t; }

{TERM} "'" {LETTER}{1,2}                                     	{ Token t = new Token(yytext(), TokenType.TERM, yychar, (yylength() + yychar)); return t;}

{TERM} "s'"                                                  	{ Token t = new Token(yytext(),TokenType.TERM, yychar, (yylength() + yychar)); return t;  }

{TERM}                                                       	{ Token t = new Token(yytext(), TokenType.TERM, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "W800i" */
{MIXED}+                                       					{ Token t = new Token(yytext(), TokenType.COMPOSITE, yychar, (yylength() + yychar)); return t;  }

{BARE_URL}                                                   	{ Token t = new Token(yytext(), TokenType.URL, yychar, (yylength() + yychar)); return t; }
	
(("http" | "https" | "ftp") "://")? {BARE_URL} {URL_PATH}?   	{ Token t = new Token(yytext(), TokenType.URL, yychar, (yylength() + yychar)); return t; }
	
";"  | "," | "'" | ":" | "-"                  					{ Token t = new Token(yytext(), TokenType.PUNCTUATION, yychar, (yylength() + yychar)); return t;  }

"." | "?" | "!" 								                { Token t = new Token(yytext(), TokenType.SENTENCEMARKER, yychar, (yylength() + yychar)); return t;  }

{WHITESPACE}+					                           		{ ; }

.																{ Token t = new Token(yytext(), TokenType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }