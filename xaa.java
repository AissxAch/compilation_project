import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class xaa {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;
    private BufferedReader treader;

    public SyntaxAnalyzer(String tokenFilePath) throws IOException {
        tokens = new ArrayList<>();
        loadTokens(tokenFilePath);
        currentTokenIndex = 0;
        currentToken = tokens.get(currentTokenIndex);
    }

    private void loadTokens(String tokenFilePath) throws IOException {
        String line;
        treader = new BufferedReader(new FileReader(tokenFilePath));
        while ((line = treader.readLine()) != null) {
            String[] parts = line.split(",");
            parts[0] = parts[0].substring(1);
            parts[1] = parts[1].substring(0, parts[1].length() - 1);
            Token token = new Token(parts[0], parts[1]);
            tokens.add(token);
        }
    }

    private void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else {
            currentToken = null; // End of tokens
        }
    }

    private void match(String expectedTokenType) throws SyntaxError {
        System.out.println("Matching: Expected " + expectedTokenType + ", Found " + currentToken);
        if (currentToken != null && currentToken.id.equals(expectedTokenType)) {
            advance();
        } else {
            throw new SyntaxError("Expected " + expectedTokenType + " but found " + (currentToken != null ? currentToken.id : "EOF"));
        }
    }

    public void parse() throws SyntaxError {
        parseProgram();
        if (currentToken != null) {
            throw new SyntaxError("Unexpected token at the end: " + currentToken);
        }
        System.out.println("Parsing completed successfully.");
    }

    private void parseProgram() throws SyntaxError {
        match("kw"); // Match 'programme'
        match("id"); // Match program name
        match("cs"); // Match ';'
        parseCorps(); // Parse <Corps>
        match("cs"); // Match '.'
    }

    private void parseCorps() throws SyntaxError {
        if (currentToken != null && currentToken.value.equals("constante")) {
            parsePartieDefinitionConstante(); // Parse constant definitions
        }
        if (currentToken != null && currentToken.value.equals("variable")) {
            parsePartieDefinitionVariable(); // Parse variable definitions
        }
        parseInstrComp(); // Parse compound instructions
    }

    private void parsePartieDefinitionConstante() throws SyntaxError {
        match("kw"); // Match 'constante'
        parseDefinitionConstante(); // Parse the first constant definition
        while (currentToken != null && currentToken.id.equals("id")) { // Loop for additional constant definitions
            parseDefinitionConstante();
        }
    }

    private void parseDefinitionConstante() throws SyntaxError {
        match("id"); // Match <NomConstante>
        match("cs"); // Match '='
        parseConstante(); // Parse <Constante>
        match("cs"); // Match ';'
    }

    private void parseConstante() throws SyntaxError {
        if (currentToken != null && currentToken.id.equals("con")) {
            match("con"); // Match constant value
        } else if (currentToken != null && currentToken.id.equals("id")) {
            match("id"); // Match constant name
        } else {
            throw new SyntaxError("Expected constant value or name but found " + (currentToken != null ? currentToken.id : "EOF"));
        }
    }

    private void parsePartieDefinitionVariable() throws SyntaxError {
        match("kw"); // Match 'variable'
        parseDefinitionVariable();
        while (currentToken != null && currentToken.value.equals("variable")) {
            parseDefinitionVariable();
        }
    }

    private void parseDefinitionVariable() throws SyntaxError {
        parseGroupeVariable();
        match("cs"); // Match ';'
    }

    private void parseGroupeVariable() throws SyntaxError {
        parseNomVariable();
        while (currentToken != null && currentToken.value.equals(",")) {
            match("cs"); // Match ','
            parseNomVariable();
        }
        match("cs"); // Match ':'
        parseNomType();
    }

    private void parseNomVariable() throws SyntaxError {
        match("id"); // Match variable name
    }

    private void parseNomType() throws SyntaxError {
        if (currentToken != null && (currentToken.value.equals("Entier") || currentToken.value.equals("Caractère") || currentToken.value.equals("Réel"))) {
            match("kw"); // Match type
        } else {
            throw new SyntaxError("Expected type but found " + (currentToken != null ? currentToken.value : "EOF"));
        }
    }

    private void parseInstrComp() throws SyntaxError {
        match("kw"); // Match 'debut'
        parseInstruction();
        while (currentToken != null && currentToken.value.equals(";")) {
            match("cs"); // Match ';'
            parseInstruction();
        }
        match("kw"); // Match 'fin'
    }

    private void parseInstruction() throws SyntaxError {
        if (currentToken != null && currentToken.id.equals("id")) {
            parseInstructionAffectation();
        } else if (currentToken != null && currentToken.value.equals("tantque")) {
            parseInstructionTantque();
        } else if (currentToken != null && currentToken.value.equals("debut")) {
            parseInstrComp();
        } else if (currentToken != null && currentToken.id.equals("cs")) {
            throw new SyntaxError("Unexpected symbol: " + currentToken.value);
        } else {
            throw new SyntaxError("Expected instruction but found " + (currentToken != null ? currentToken.id : "EOF"));
        }
    }

    private void parseInstructionAffectation() throws SyntaxError {
        parseNomVariable();
        match("cs"); // Match ':='
        parseExpression();
    }

    private void parseExpression() throws SyntaxError {
        parseExpressionSimple();
        if (currentToken != null && (currentToken.value.equals("<") || currentToken.value.equals(">") || currentToken.value.equals("=") || currentToken.value.equals("<=") || currentToken.value.equals(">=") || currentToken.value.equals("<>"))) {
            parseOperateurRelationnel();
            parseExpressionSimple();
        }
    }

    private void parseExpressionSimple() throws SyntaxError {
        if (currentToken != null && (currentToken.value.equals("+") || currentToken.value.equals("-"))) {
            parseOperateurSigne();
        }
        parseTerme();
        while (currentToken != null && (currentToken.value.equals("+") || currentToken.value.equals("-") || currentToken.value.equals("ou"))) {
            parseOperateurAddition();
            parseTerme();
        }
    }

    private void parseTerme() throws SyntaxError {
        parseFacteur();
        while (currentToken != null && (currentToken.value.equals("*") || currentToken.value.equals("div") || currentToken.value.equals("mod") || currentToken.value.equals("et"))) {
            parseOperateurMult();
            parseFacteur();
        }
    }

    private void parseFacteur() throws SyntaxError {
        if (currentToken != null && currentToken.id.equals("con")) {
            match("con"); // Match constant
        } else if (currentToken != null && currentToken.id.equals("id")) {
            match("id"); // Match variable
        } else if (currentToken != null && currentToken.value.equals("(")) {
            match("cs"); // Match '('
            parseExpression();
            match("cs"); // Match ')'
        } else {
            throw new SyntaxError("Expected constant, variable, or expression but found " + (currentToken != null ? currentToken.id : "EOF"));
        }
    }

    private void parseOperateurRelationnel() throws SyntaxError {
        if (currentToken != null && (currentToken.value.equals("<") || currentToken.value.equals(">") || currentToken.value.equals("=") || currentToken.value.equals("<=") || currentToken.value.equals(">=") || currentToken.value.equals("<>"))) {
            match("cs"); // Match relational operator
        } else {
            throw new SyntaxError("Expected relational operator but found " + (currentToken != null ? currentToken.value : "EOF"));
        }
    }

    private void parseOperateurSigne() throws SyntaxError {
        if (currentToken != null && (currentToken.value.equals("+") || currentToken.value.equals("-"))) {
            match("cs"); // Match sign operator
        } else {
            throw new SyntaxError("Expected sign operator but found " + (currentToken != null ? currentToken.value : "EOF"));
        }
    }

    private void parseOperateurAddition() throws SyntaxError {
        if (currentToken != null && (currentToken.value.equals("+") || currentToken.value.equals("-") || currentToken.value.equals("ou"))) {
            match("cs"); // Match addition operator
        } else {
            throw new SyntaxError("Expected addition operator but found " + (currentToken != null ? currentToken.value : "EOF"));
        }
    }

    private void parseOperateurMult() throws SyntaxError {
        if (currentToken != null && (currentToken.value.equals("*") || currentToken.value.equals("div") || currentToken.value.equals("mod") || currentToken.value.equals("et"))) {
            match("cs"); // Match multiplication operator
        } else {
            throw new SyntaxError("Expected multiplication operator but found " + (currentToken != null ? currentToken.value : "EOF"));
        }
    }

    private void parseInstructionTantque() throws SyntaxError {
        match("kw"); // Match 'tantque'
        parseCondition();
        match("kw"); // Match 'faire'
        parseInstruction();
    }

    private void parseCondition() throws SyntaxError {
        parseExpression();
        parseOperateurRelationnel();
        parseExpression();
    }
}

class SyntaxError extends Exception {
    public SyntaxError(String message) {
        super(message);
    }
}