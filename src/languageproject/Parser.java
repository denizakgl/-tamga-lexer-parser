package languageproject;

import java.util.HashSet;

/*
 * @author Eren
 */

public class Parser {
    private Lexer lexer;
    private int nextToken;

    private HashSet<String> symbolTable = new HashSet<>(); // Symbol table for declared identifiers in TAMGA. This table stores variable names declared with the "sayı" keyword.


    /**
     * Construct the parser with a provided Lexer instance. The first token is
     * read immediately to initialize the parser state.
     */
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        advance(); // Read the first token to start the parser, as in the textbook
    }

    /**
     * Helper function that wraps the lex() call and updates the nextToken
     * variable. This mirrors the logic in the textbook example.
     */
    private void advance() {
        nextToken = lexer.lex();
    }

    /**
     * Checks whether the current token matches the expected token. If it does,
     * advance to the next token; otherwise report a syntax error with a
     * meaningful message.
     */
    private void match(int expectedToken, String errorMessage) {
        if (nextToken == expectedToken) {
            advance();
        } else {
            syntaxError(errorMessage + " (Expected Token Code: " + expectedToken + ", Received: " + nextToken + ")");
        }
    }

    /**
     * Report a syntax error with the current line number and message, then exit.
     */
    private void syntaxError(String message) {
        System.out.println("SYNTAX ERROR (Line " + lexer.getLine() + "): " + message);
        System.exit(1);
    }

    // GRAMMAR RULE FUNCTIONS

    public void program() {
        System.out.println("--- Parsing Process Started ---");
        statementList();

        // If all statements have finished and we've reached the end of file, it is successful
        if (nextToken == Lexer.EOF) {
            System.out.println("--- SUCCESS: Code is syntactically completely valid! ---");
        } else {
            syntaxError("Program finished successfully but there are leftover characters at the end of the file.");
        }
    }

    private void statementList() {
        statement();
        // Continue reading statements until end of file, end of block (}) or an else block is encountered
        while (nextToken != Lexer.EOF && nextToken != Lexer.RIGHT_BRACE && nextToken != Lexer.ELSE_KW) {
            statement();
        }
    }

    private void statement() {
        if (nextToken == Lexer.NUMBER_KW) {
            declaration();
        } else if (nextToken == Lexer.IDENT) {
            assignment();
        } else if (nextToken == Lexer.IF_KW) {
            conditional();
        } else if (nextToken == Lexer.WHILE_KW) {
            loop();
        } else {
            syntaxError("Invalid statement start! A line can only begin with 'sayı', a variable name, 'eğer' or 'iken'.");
        }
    }

    private void declaration() {
        System.out.println("-> Parsing declaration statement: " + lexer.getLexeme());

        match(Lexer.NUMBER_KW, "Variable declaration must start with the keyword 'sayı'.");

        if (nextToken != Lexer.IDENT) {
            syntaxError("After 'sayı' a valid variable name must follow.");
        }

        String identifierName = lexer.getLexeme();

        if (symbolTable.contains(identifierName)) {
            syntaxError("Variable '" + identifierName + "' is already declared.");
        }

        match(Lexer.IDENT, "After 'sayı' a valid variable name must follow.");

        symbolTable.add(identifierName);

        match(Lexer.SEMICOLON, "A semicolon ';' must be placed at the end of the declaration line.");
    }

    private void assignment() {
        System.out.println("-> Parsing assignment statement: " + lexer.getLexeme());

        if (nextToken != Lexer.IDENT) {
            syntaxError("An assignment must start with a variable name.");
        }

        String identifierName = lexer.getLexeme();

        if (!symbolTable.contains(identifierName)) {
            syntaxError("Variable '" + identifierName + "' is used before declaration.");
        }

        match(Lexer.IDENT, "An assignment must start with a variable name.");
        match(Lexer.ASSIGN_OP, "An '=' operator must follow the variable name.");
        expr();
        match(Lexer.SEMICOLON, "A semicolon ';' must be placed at the end of the assignment line.");
    }

    private void conditional() {
        System.out.println("-> Parsing conditional structure (if)...");
        match(Lexer.IF_KW, "Failed to start 'eğer' block.");
        match(Lexer.LEFT_PAREN, "An '(' must follow the keyword 'eğer'.");
        condition();
        match(Lexer.RIGHT_PAREN, "The condition expression must be closed with a ')'.");

        match(Lexer.LEFT_BRACE, "The conditional block must start with '{'.");
        statementList();
        match(Lexer.RIGHT_BRACE, "The conditional block must end with '}'.");

        // Optional 'değilse' (else) part
        if (nextToken == Lexer.ELSE_KW) {
            System.out.println("-> Parsing alternative conditional (else) structure...");
            match(Lexer.ELSE_KW, "Error in 'değilse' block.");
            match(Lexer.LEFT_BRACE, "The 'değilse' block must start with '{'.");
            statementList();
            match(Lexer.RIGHT_BRACE, "The 'değilse' block must end with '}'.");
        }
    }

    private void loop() {
        System.out.println("-> Parsing loop structure (while)...");
        match(Lexer.WHILE_KW, "Failed to start 'iken' loop.");
        match(Lexer.LEFT_PAREN, "An '(' must follow the keyword 'iken'.");
        condition();
        match(Lexer.RIGHT_PAREN, "The loop condition must be closed with a ')'.");

        match(Lexer.LEFT_BRACE, "The loop block must start with '{'.");
        statementList();
        match(Lexer.RIGHT_BRACE, "The loop block must end with '}'.");
    }

    private void condition() {
        expr();
        if (nextToken == Lexer.LESS_OP || nextToken == Lexer.GREATER_OP || nextToken == Lexer.EQUAL_OP) {
            advance();
            expr();
        } else {
            syntaxError("No valid comparison operator (<, >, ==) found in the condition expression.");
        }
    }

    // MATHEMATICAL EXPRESSIONS
    private void expr() {
        term();
        while (nextToken == Lexer.ADD_OP || nextToken == Lexer.SUB_OP) {
            advance();
            term();
        }
    }

    private void term() {
        factor();
        while (nextToken == Lexer.MULT_OP || nextToken == Lexer.DIV_OP) {
            advance();
            factor();
        }
    }

    private void factor() {
        if (nextToken == Lexer.IDENT) {
            String identifierName = lexer.getLexeme();

            if (!symbolTable.contains(identifierName)) {
                syntaxError("Variable '" + identifierName + "' is used before declaration.");
            }

            match(Lexer.IDENT, "Identifier error.");

        } else if (nextToken == Lexer.INT_LIT) {
            match(Lexer.INT_LIT, "Integer literal error.");

        } else if (nextToken == Lexer.LEFT_PAREN) {
            match(Lexer.LEFT_PAREN, "Parenthesis opening error.");
            expr();
            match(Lexer.RIGHT_PAREN, "Missing ')' at the end of the arithmetic expression.");

        } else {
            syntaxError("Invalid character or missing component detected in arithmetic expression.");
        }
    }
}
