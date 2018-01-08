package jflex; 

import java.io.StringReader;
import java.io.IOException;
import java.util.Iterator;
import java.lang.NullPointerException; 

import de.neofonie.textmining.clustering.analysis.token.QuantityTypes;
import de.neofonie.textmining.textanalysis.analysis.tokenizer.standard.StandardTokenizer.TokenizerType;
import de.neofonie.textmining.textanalysis.token.Token;
import de.neofonie.textmining.textanalysis.token.Token;
import de.neofonie.textmining.textanalysis.analysis.tokenizer.Tokenizer;
import de.neofonie.textmining.textanalysis.type.IType;
import de.neofonie.textmining.textanalysis.type.AbstractType;
import de.neofonie.textmining.textanalysis.type.ExpressiveType;
import de.neofonie.textmining.clustering.analysis.token.CurrencyTokenFactory;
import java.lang.UnsupportedOperationException;
import de.neofonie.textmining.lexicon.core.LexiconException; 
import java.util.NoSuchElementException; 

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
    public Tokenizer() throws LexiconException {
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
    public Tokenizer(final String input) throws IOException, LexiconException {
        super();
        if (input == null) {
        	throw new NullPointerException("The tokenizer does not accept 'null' strings"); 
        }
        this.zzReader = new StringReader(input);
        this.nextToken = next_token(); 
        this.text = input;
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
    @Override
    public void reInit(final String input) throws IOException {
    	StringReader stringReader = new StringReader(input);
        this.yyreset(stringReader);
        this.nextToken = next_token();
        this.text = input;
    }
    
    
    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        this.yyclose();
    }
    
    /** {@inheritDoc} */
    @Override
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

%%

/* ex.: 10.01.2010, 1,2345, 1/2 */
{DIGIT}+  ((":" | "-" | "/" | "," | ".") {DIGIT}+)*          	{ Token t = new Token(yytext(), TokenizerType.NUMERIC, yychar, (yylength() + yychar)); return t; } 

"&quot;"														{ Token t = new Token("\"", TokenizerType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }

"&amp;"															{ Token t = new Token("&", TokenizerType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }

"*" | "@" | "&" | "+"  | "§"									{ Token t = new Token(yytext(), TokenizerType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }

/** Ignore HTML entities */
"&" [a-zA-Z0-9#]+ ";"                                         	{ ; }

/** Ignore HTML tags */
"<" ("/")* [a-zA-Z0-9=]+ ">"                                  	{ ; }

/** Ignore HTML tags */
"&lt;" ("/")* ([a-zA-Z0-9] | "=" | "\"")*  "&gt;"          	 	{ ; }

"&lt;" ("/")* "b" + "&gt;"                               		{ ; }

"&lt;" ("/")* [a-zA-Z0-9]+ "&gt;"                               { ; }

/* "I.B.M." */
({LETTER} "." ({LETTER} ".")+) 									{ Token t = new Token(yytext(), TokenizerType.COMPOSITE, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "3 Mrd. Euro, 5 Mrd. Eur., 15 Mrd. €, 15 Tausend Franc, 1.5 Zillionen Dollar, 70 Euro, 5 Tausend JPY" */
{DIGIT}+  (("," | "." | "'") {DIGIT}+)* + {WHITESPACE}* {NUMBERNAMES}* "."* {WHITESPACE}* {UNITOFCURRENCY} "."*
																{ return this.currencyTokenFactory.createToken(yytext(), QuantityTypes.CURRENCY, yychar, (yylength() + yychar)); }

/* EXAMPLE: "EUR 20.000.000" */
{UNITOFCURRENCY} {WHITESPACE}* {DIGIT}+ (("," | ".") {DIGIT}+)* (",-")*
																{ return this.currencyTokenFactory.createToken(yytext(), QuantityTypes.CURRENCY, yychar, (yylength() + yychar)); }
																																
/* e.g.: 'Artikel 1' 'Artikel 12 Absatz 23' */
"Artikel" {WHITESPACE}* {DIGIT}+ ({WHITESPACE}* "Absatz" {WHITESPACE}* {DIGIT}+)* 
							 									{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }
				
/* EUR Lex: Nr. 204/2010 */
"Nr." {WHITESPACE}* {DIGIT}+ "/" {DIGIT}+  						{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }
						 									
/* EUR-Lex: 67/548/EWG */
{DIGIT}+ "/" {DIGIT}+ "/EWG"									{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }
							 									
/* EUR-Lex: ABl. C 247 (Amtsblatt ) */
"ABl." {WHITESPACE}* {LETTER}+ {WHITESPACE}* {DIGIT}+			{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }

/* Verordnung (EG) Nr. 1782/2003 */
("Verordnung" | "VERORDNUNG") " (" {LETTER}{1,2} ") Nr. " {DIGIT}* "/" {DIGIT}* 
							 									{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }
				
/* § 4 Abs. 6 */
"§ " {DIGIT}* (" Abs. " {DIGIT}*){1}							{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }
							 									
/* Veröffentlichung gemäß § 26 Abs. 1 WpHG */
"Veröffentlichung gemäß § " {DIGIT}* (" Abs. " {DIGIT}*){1} ["WpHG"]*  
							 									{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }

/* ISIN DE000CZ31PX3 */
("ISIN"|"WKN") {WHITESPACE}* (":")* {WHITESPACE}* ({MIXED} | {DIGIT}*)  
							 									{ Token t = new Token(yytext(), QuantityTypes.LEGAL_JARGON, yychar, (yylength() + yychar)); return t; }

/* 500 kg */
{DIGIT}+ {WHITESPACE}* {WEIGHTNAME} {WHITESPACE}				{ Token t = new Token(yytext(), QuantityTypes.QUANTITY, yychar, (yylength() + yychar)); return t; }
							 									
// EXAMPLE: "1&1"
{DIGIT}+ {WHITESPACE}* "&" {WHITESPACE}* {DIGIT}+				{ Token t = new Token(yytext(),TokenizerType.COMPOSITE, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "laptop", "walkman" */
{LETTER} "." 												 	{ Token t = new Token(yytext(),TokenizerType.TERM, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "Konrad-Adenauer-Stiftung" */
({LETTER})+ ( "-" ({LETTER})+)+                                 { Token t = new Token(yytext(), TokenizerType.HYPHENATION, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "DVD-1080P7" */
{MIXED} ( "-" {MIXED})+                                       	{ Token t = new Token(yytext(), TokenizerType.COMPOSITE, yychar, (yylength() + yychar));  return t; }

{TERM} "'" {LETTER}{1,2}                                     	{ Token t = new Token(yytext(), TokenizerType.TERM, yychar, (yylength() + yychar)); return t;}

{TERM} "s'"                                                  	{ Token t = new Token(yytext(),TokenizerType.TERM, yychar, (yylength() + yychar)); return t;  }

{TERM}                                                       	{ Token t = new Token(yytext(), TokenizerType.TERM, yychar, (yylength() + yychar)); return t; }

/* EXAMPLE: "W800i" */
{MIXED}+                                       					{ Token t = new Token(yytext(), TokenizerType.COMPOSITE, yychar, (yylength() + yychar)); return t;  }

{BARE_URL}                                                   	{ Token t = new Token(yytext(), TokenizerType.URL, yychar, (yylength() + yychar)); return t; }
	
(("http" | "https" | "ftp") "://")? {BARE_URL} {URL_PATH}?   	{ Token t = new Token(yytext(), TokenizerType.URL, yychar, (yylength() + yychar)); return t; }
	
";"  | "," | "'" | ":" | "-"                  					{ Token t = new Token(yytext(), TokenizerType.PUNCTUATION, yychar, (yylength() + yychar)); return t;  }

"." | "?" | "!" 								                { Token t = new Token(yytext(), TokenizerType.SENTENCEMARKER, yychar, (yylength() + yychar)); return t;  }

{WHITESPACE}+					                           		{ ; }

.																{ Token t = new Token(yytext(), TokenizerType.SPECIALCHAR, yychar, (yylength() + yychar)); return t; }