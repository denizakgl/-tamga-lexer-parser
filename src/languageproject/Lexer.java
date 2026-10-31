package languageproject;

public class Lexer {
    // Character classes
    public static final int LETTER = 0;
    public static final int DIGIT = 1;
    public static final int UNKNOWN = 99;

    // Token code definitions
    public static final int EOF = -1;
    public static final int INT_LIT = 10;
    public static final int IDENT = 11;

    // Token codes for the Turkish keywords used in TAMGA
    public static final int NUMBER_KW = 12;
    public static final int IF_KW = 13;
    public static final int ELSE_KW = 14;
    public static final int WHILE_KW = 15;

    // Operators and delimiters
    public static final int ASSIGN_OP = 20;
    public static final int ADD_OP = 21;
    public static final int SUB_OP = 22;
    public static final int MULT_OP = 23;
    public static final int DIV_OP = 24;
    public static final int LESS_OP = 25;
    public static final int GREATER_OP = 26;
    public static final int EQUAL_OP = 27;
    public static final int LEFT_PAREN = 28;
    public static final int RIGHT_PAREN = 29;
    public static final int LEFT_BRACE = 30;
    public static final int RIGHT_BRACE = 31;
    public static final int SEMICOLON = 32;

    private int charClass;
    private String lexeme = "";
    private char nextChar;
    private int nextToken;

    private String input;
    private int index = 0;
    private int line = 1;

    public Lexer(String input) {
        this.input = input;
        getChar();
    }

    /**
     * Reads the next character from the input and updates the character class
     * and line counter accordingly.
     */
    private void getChar() {
        if (index < input.length()) {
            nextChar = input.charAt(index);
            index++;

            if (nextChar == '\n') {
                line++;
            }

            if (Character.isLetter(nextChar) || nextChar == '_') {
                charClass = LETTER;
            } else if (Character.isDigit(nextChar)) {
                charClass = DIGIT;
            } else {
                charClass = UNKNOWN;
            }
        } else {
            charClass = EOF;
            nextChar = '\0';
        }
    }

    private void addChar() {
        lexeme += nextChar;
    }

    /**
     * Skip over any whitespace characters until a non-blank character or EOF is reached.
     */
    private void getNonBlank() {
        while (Character.isWhitespace(nextChar) && charClass != EOF) {
            getChar();
        }
    }

    /**
     * Determine the token type of a single-character operator or delimiter.
     */
    private int lookup(char ch) {
        switch (ch) {
            case '(': addChar(); nextToken = LEFT_PAREN; break;
            case ')': addChar(); nextToken = RIGHT_PAREN; break;
            case '{': addChar(); nextToken = LEFT_BRACE; break;
            case '}': addChar(); nextToken = RIGHT_BRACE; break;
            case ';': addChar(); nextToken = SEMICOLON; break;
            case '+': addChar(); nextToken = ADD_OP; break;
            case '-': addChar(); nextToken = SUB_OP; break;
            case '*': addChar(); nextToken = MULT_OP; break;
            case '/': addChar(); nextToken = DIV_OP; break;
            case '<': addChar(); nextToken = LESS_OP; break;
            case '>': addChar(); nextToken = GREATER_OP; break;
            case '=':
                addChar();
                if (index < input.length() && input.charAt(index) == '=') {
                    getChar();
                    addChar();
                    nextToken = EQUAL_OP;
                } else {
                    nextToken = ASSIGN_OP;
                }
                break;
            default:
                addChar();
                nextToken = UNKNOWN;
                System.out.println("LEXICAL ERROR (Line " + line + "): Undefined character detected -> " + nextChar);
                System.exit(1);
                break;
        }
        return nextToken;
    }

    /**
     * Lexical analyzer that returns the next token in the input.
     */
    public int lex() {
        lexeme = "";
        getNonBlank();

        switch (charClass) {
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) {
                    addChar();
                    getChar();
                }

                switch (lexeme) {
                    case "sayı":    nextToken = NUMBER_KW; break;
                    case "eğer":    nextToken = IF_KW; break;
                    case "değilse": nextToken = ELSE_KW; break;
                    case "iken":    nextToken = WHILE_KW; break;
                    default:        nextToken = IDENT; break;
                }
                break;

            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                if (charClass == LETTER) {
                    addChar();
                    System.out.println("LEXICAL ERROR (Line " + line + "): Variable name cannot start with a digit -> " + lexeme);
                    System.exit(1);
                }
                nextToken = INT_LIT;
                break;

            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;

            case EOF:
                nextToken = EOF;
                lexeme = "EOF";
                break;
        }

        return nextToken;
    }

    public int getNextToken() { return nextToken; }
    public String getLexeme() { return lexeme; }
    public int getLine() { return line; }
}
