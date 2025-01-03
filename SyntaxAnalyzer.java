import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;

    public SyntaxAnalyzer(String filename) throws IOException {
        tokens = new ArrayList<>();
        loadTokens(filename);
        currentTokenIndex = 0;
        currentToken = tokens.get(currentTokenIndex);
    }

    private void loadTokens(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            String id = parts[0].substring(1);
            String value = parts[1].substring(0, parts[1].length() - 1);
            tokens.add(new Token(id, value));
        }
        reader.close();
    }

    private void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else {
            currentToken = null;
        }
    }

    private void match(String expectedType) throws SyntaxError {
        System.out.println("Matching: Expected " + expectedType + ", Found " + currentToken.id + " " + currentToken.value);
        if (currentToken != null && currentToken.getType().equals(expectedType)) {
            advance();
        } else {
            throw new SyntaxError("Expected " + expectedType + " but found " + (currentToken != null ? currentToken.getType() + " " + currentToken.value : "end of input"));
        }
    }

    public void parse() throws SyntaxError {
        parseProgram();
        if (currentToken != null) {
            throw new SyntaxError("Unexpected token at the end: " + currentToken);
        }
        System.out.println("Syntax analysis completed successfully.");
    }

    private void parseProgram() throws SyntaxError {
        match("kw"); // programme
        match("id"); // <NomProgramme>
        match("cs"); // ;
        parseCorps();
        if (currentToken != null && currentToken.getType().equals("cs") && currentToken.value.equals(";")) {
            match("cs"); // ;
            if (currentToken != null && currentToken.getType().equals("kw") && currentToken.value.equals("fin")) {
                match("kw"); // fin
                
                }
            match("cs"); // .
        }else{
            match("cs"); // .
        }
    }

    private void parseCorps() throws SyntaxError {
        if (currentToken != null && currentToken.getType().equals("kw") && currentToken.value.equals("constante")) {
            parsePartieDefinitionConstante();
        }
        if (currentToken != null && currentToken.getType().equals("kw") && currentToken.value.equals("variable")) {
            parsePartieDefinitionVariable();
        }
        parseInstrComp();
    }

    private void parsePartieDefinitionConstante() throws SyntaxError {
        match("kw"); // constante
        parseDefinitionConstante();
        while (currentToken != null && currentToken.getType().equals("id")) {
            parseDefinitionConstante();
        }
    }

    private void parseDefinitionConstante() throws SyntaxError {
        match("id"); // <NomConstante>
        match("cs"); // =>
        parseConstante();
        match("cs"); // ;
    }

    private void parseConstante() throws SyntaxError {
        if (currentToken != null && currentToken.getType().equals("con")) {
            match("con"); // <Nombre>
        } else if (currentToken != null && currentToken.getType().equals("id")) {
            match("id"); // <NomConstante>
        } else {
            throw new SyntaxError("Expected constant or identifier but found " + (currentToken != null ? currentToken.getType() + " " + currentToken.value : "end of input"));
        }
    }

    private void parsePartieDefinitionVariable() throws SyntaxError {
        match("kw"); // variable
        parseDefinitionVariable();
        while (currentToken != null && currentToken.getType().equals("id")) {
            parseDefinitionVariable();
        }
    }

    private void parseDefinitionVariable() throws SyntaxError {
        parseGroupeVariable();
        match("cs"); // ;
    }

    private void parseGroupeVariable() throws SyntaxError {
        match("id"); // <NomVariable>
        while (currentToken != null && currentToken.getType().equals("cs") && currentToken.value.equals(",")) {
            match("cs"); // ,
            match("id"); // <NomVariable>
        }
        match("cs"); // :
        parseNomType();
    }

    private void parseNomType() throws SyntaxError {
        if (currentToken != null && currentToken.getType().equals("kw") && 
            (currentToken.value.equals("Entier") || currentToken.value.equals("Caractère") || currentToken.value.equals("Réel"))) {
            match("kw"); // <NomType>
        } else {
            throw new SyntaxError("Expected type keyword but found " + (currentToken != null ? currentToken.getType() + " " + currentToken.value : "end of input"));
        }
    }

    private void parseInstrComp() throws SyntaxError {
        match("kw"); // debut
        parseInstruction();
        while (currentToken != null && currentToken.getType().equals("cs") && currentToken.value.equals(";")) {
            match("cs"); // ;
            parseInstruction();
        }
        match("kw"); // fin
        
    }

    private void parseInstruction() throws SyntaxError {
        if (currentToken != null && currentToken.getType().equals("id")) {
            parseInstructionAffectation();
        } else if (currentToken != null && currentToken.getType().equals("kw") && currentToken.value.equals("tantque")) {
            parseInstructionTantque();
        } else if (currentToken != null && currentToken.getType().equals("kw") && currentToken.value.equals("debut")) {
            parseInstrComp();
        } else {
            // Empty instruction (Vide)
        }
    }

    private void parseInstructionAffectation() throws SyntaxError {
        match("id"); // <NomVariable>
        match("cs"); // :=
        parseExpression();
    }

    private void parseExpression() throws SyntaxError {
        parseExpressionSimple();
        if (currentToken != null && currentToken.getType().equals("cs") && 
            (currentToken.value.equals("<") || currentToken.value.equals(">") || currentToken.value.equals("=") || 
             currentToken.value.equals("<=") || currentToken.value.equals(">=") || currentToken.value.equals("<>"))) {
            match("cs"); // <OperateurRelationnel>
            parseExpressionSimple();
        }
    }

    private void parseExpressionSimple() throws SyntaxError {
        if (currentToken != null && currentToken.getType().equals("cs") && 
            (currentToken.value.equals("+") || currentToken.value.equals("-"))) {
            match("cs"); // <OperateurSigne>
        }
        parseTerme();
        while (currentToken != null && currentToken.getType().equals("cs") && 
               (currentToken.value.equals("+") || currentToken.value.equals("-") || currentToken.value.equals("ou"))) {
            match("cs"); // <OperateurAddition>
            parseTerme();
        }
    }

    private void parseTerme() throws SyntaxError {
        parseFacteur();
        while (currentToken != null && currentToken.getType().equals("cs") && 
               (currentToken.value.equals("*") || currentToken.value.equals("div") || currentToken.value.equals("mod") || currentToken.value.equals("et"))) {
            match("cs"); // <OperateurMult>
            parseFacteur();
        }
    }

    private void parseFacteur() throws SyntaxError {
        if (currentToken != null && currentToken.getType().equals("con")) {
            match("con"); // <Constante>
        } else if (currentToken != null && currentToken.getType().equals("id")) {
            match("id"); // <NomVariable>
        } else if (currentToken != null && currentToken.getType().equals("cs") && currentToken.value.equals("(")) {
            match("cs"); // (
            parseExpression();
            match("cs"); // )
        } else if (currentToken != null && currentToken.getType().equals("kw")) {
            
        }
        
        else {
            throw new SyntaxError("Expected constant, variable, or expression but found " + (currentToken != null ? currentToken.getType() + " " + currentToken.value : "end of input"));
        }
    }

    private void parseInstructionTantque() throws SyntaxError {
        match("kw"); // tantque
        parseCondition(); // Parse the condition
        match("kw"); // faire
        parseInstruction(); // Parse the instruction
        if (currentToken != null && currentToken.getType().equals("kw") && currentToken.value.equals("fin")) {
            match("kw"); // fin
            match("cs"); // ;
            
        }
    }

    private void parseCondition() throws SyntaxError {
        parseExpression(); // Parse the first expression

        // Debugging: Print the current token
        System.out.println("Current Token in parseCondition: " + (currentToken != null ? currentToken.getType() + " " + currentToken.value : "null"));

        // Match the relational operator
        if (currentToken != null && currentToken.getType().equals("cs") &&
            (currentToken.value.equals("<") || currentToken.value.equals(">") ||
             currentToken.value.equals("=") || currentToken.value.equals("<=") ||
             currentToken.value.equals(">=") || currentToken.value.equals("<>"))) {
            match("cs"); // Match the relational operator
        } else if (currentToken != null && currentToken.getType().equals("kw")) {
            
        }
        else {
            throw new SyntaxError("Expected relational operator but found " + (currentToken != null ? currentToken.getType() + " " + currentToken.value : "end of input"));
        }

        parseExpression(); // Parse the second expression
    }
}