import java.io.*;
import java.util.ArrayList;

public class Syntaxique {
    ArrayList<Token> tokens = new ArrayList<>();
    int currentTokenIndex = 0;

    // Constructor: Initialize the syntax analyzer and read tokens from token.al
    Syntaxique() throws Exception {
        readTokensFromFile("token.al");
        ProgrammeAlgoLang();
    }

    // Read tokens from the token.al file
    void readTokensFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove the parentheses and split into type and value
                line = line.substring(1, line.length() - 1); // Remove '(' and ')'
                String[] parts = line.split(", ");
                Token token = new Token(parts[0], parts[1]);
                tokens.add(token);
            }
        }
    }

    // Get the current token
    Token getCurrentToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex);
        }
        return null;
    }

    // Move to the next token
    void nextToken() {
        if (currentTokenIndex < tokens.size()) {
            currentTokenIndex++;
        }
    }

    // Check if the current token matches the expected type and value
    boolean match(String expectedType, String expectedValue) {
        Token currentToken = getCurrentToken();
        if (currentToken != null && currentToken.type.equals(expectedType) && currentToken.value.equals(expectedValue)) {
            nextToken();
            return true;
        }
        return false;
    }

    // Grammar rule: <ProgrammeAlgoLang> ::= programme <NomProgramme> ; <Corps> .
    void ProgrammeAlgoLang() throws Exception {
        if (!match("kw", "programme")) {
            throw new Exception("Syntax Error: Expected 'programme'");
        }
        NomProgramme();
        if (!match("cs", ";")) {
            throw new Exception("Syntax Error: Expected ';' after program name");
        }
        Corps();
        if (!match("cs", ".")) {
            throw new Exception("Syntax Error: Expected '.' at the end of the program");
        }
        System.out.println("Parsing completed successfully!");
    }

    // Grammar rule: <NomProgramme> ::= <Nom>
    void NomProgramme() throws Exception {
        if (!match("id", getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected program name");
        }
    }

    // Grammar rule: <Corps> ::= [<PartieDéfinitionConstante>] [<PartieDéfinitionVariable>] <InstrComp>
    void Corps() throws Exception {
        if (getCurrentToken().value.equals("constante")) {
            PartieDefinitionConstante();
        }
        if (getCurrentToken().value.equals("variable")) {
            PartieDefinitionVariable();
        }
        InstrComp();
    }

    // Grammar rule: <PartieDéfinitionConstante> ::= constante <DéfinitionConstante> {<DéfinitionConstante>}
    void PartieDefinitionConstante() throws Exception {
        if (!match("kw", "constante")) {
            throw new Exception("Syntax Error: Expected 'constante'");
        }
        DefinitionConstante();
        while (getCurrentToken().value.equals("constante")) {
            DefinitionConstante();
        }
    }

    // Grammar rule: <DéfinitionConstante> ::= <NomConstante> => <Constante> ;
    void DefinitionConstante() throws Exception {
        NomConstante();
        if (!match("cs", "=>")) {
            throw new Exception("Syntax Error: Expected '=>'");
        }
        Constante();
        if (!match("cs", ";")) {
            throw new Exception("Syntax Error: Expected ';'");
        }
    }

    // Grammar rule: <NomConstante> ::= <Nom>
    void NomConstante() throws Exception {
        if (!match("id", getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected constant name");
        }
    }

    // Grammar rule: <Constante> ::= <Nombre> / <NomConstante>
    void Constante() throws Exception {
        if (getCurrentToken().type.equals("con")) {
            match("con", getCurrentToken().value);
        } else {
            NomConstante();
        }
    }

    // Grammar rule: <PartieDéfinitionVariable> ::= variable <DéfinitionVariable> {<DéfinitionVariable>}
    void PartieDefinitionVariable() throws Exception {
        if (!match("kw", "variable")) {
            throw new Exception("Syntax Error: Expected 'variable'");
        }
        DefinitionVariable();
        while (getCurrentToken().value.equals("variable")) {
            DefinitionVariable();
        }
    }

    // Grammar rule: <DéfinitionVariable> ::= <GroupeVariable> ;
    void DefinitionVariable() throws Exception {
        GroupeVariable();
        if (!match("cs", ";")) {
            throw new Exception("Syntax Error: Expected ';'");
        }
    }

    // Grammar rule: <GroupeVariable> ::= <NomVariable> [, <NomVariable>] : <NomType>
    void GroupeVariable() throws Exception {
        NomVariable();
        while (match("cs", ",")) {
            NomVariable();
        }
        if (!match("cs", ":")) {
            throw new Exception("Syntax Error: Expected ':'");
        }
        NomType();
    }

    // Grammar rule: <NomVariable> ::= <Nom>
    void NomVariable() throws Exception {
        if (!match("id", getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected variable name");
        }
    }

    // Grammar rule: <NomType> ::= Entier / Caractère / Réel
    void NomType() throws Exception {
        if (!match("kw", "entier") && !match("kw", "caractere") && !match("kw", "reel")) {
            throw new Exception("Syntax Error: Expected type (entier, caractere, or reel)");
        }
    }

    // Grammar rule: <InstrComp> ::= debut <Instruction> { ; <Instruction> } fin
    void InstrComp() throws Exception {
        if (!match("kw", "debut")) {
            throw new Exception("Syntax Error: Expected 'debut'");
        }
        Instruction();
        while (match("cs", ";")) {
            Instruction();
        }
        if (!match("kw", "fin")) {
            throw new Exception("Syntax Error: Expected 'fin'");
        }
    }

    // Grammar rule: <Instruction> ::= <InstructionAffectation> / <InstructionTantque> / <InstrComp> / <Vide>
    void Instruction() throws Exception {
        Token currentToken = getCurrentToken();
        if (currentToken.type.equals("id")) {
            InstructionAffectation();
        } else if (currentToken.value.equals("tantque")) {
            InstructionTantque();
        } else if (currentToken.value.equals("debut")) {
            InstrComp();
        } else {
            // <Vide> case (empty instruction)
        }
    }

    // Grammar rule: <InstructionAffectation> ::= <NomVariable> ::= <Expression>
    void InstructionAffectation() throws Exception {
        NomVariable();
        if (!match("cs", ":=")) {
            throw new Exception("Syntax Error: Expected ':='");
        }
        Expression();
    }

    // Grammar rule: <Expression> ::= <ExpressionSimple> [<OperateurRelationnel> <ExpressionSimple>]
    void Expression() throws Exception {
        ExpressionSimple();
        if (isOperateurRelationnel(getCurrentToken().value)) {
            OperateurRelationnel();
            ExpressionSimple();
        }
    }

    // Grammar rule: <ExpressionSimple> ::= [<OperateurSigne>] <Terme> {<OperateurAddition> <Terme>}
    void ExpressionSimple() throws Exception {
        if (isOperateurSigne(getCurrentToken().value)) {
            OperateurSigne();
        }
        Terme();
        while (isOperateurAddition(getCurrentToken().value)) {
            OperateurAddition();
            Terme();
        }
    }

    // Grammar rule: <Terme> ::= <Facteur> {<OperateurMult> <Facteur>}
    void Terme() throws Exception {
        Facteur();
        while (isOperateurMult(getCurrentToken().value)) {
            OperateurMult();
            Facteur();
        }
    }

    // Grammar rule: <Facteur> ::= <Constante> / <NomVariable> / (<Expression>)
    void Facteur() throws Exception {
        Token currentToken = getCurrentToken();
        if (currentToken.type.equals("con")) {
            match("con", currentToken.value);
        } else if (currentToken.type.equals("id")) {
            match("id", currentToken.value);
        } else if (currentToken.value.equals("(")) {
            match("cs", "(");
            Expression();
            if (!match("cs", ")")) {
                throw new Exception("Syntax Error: Expected ')'");
            }
        } else {
            throw new Exception("Syntax Error: Expected constant, variable, or expression");
        }
    }

    // Grammar rule: <InstructionTantque> ::= tantque <Condition> faire <Instruction>
    void InstructionTantque() throws Exception {
        if (!match("kw", "tantque")) {
            throw new Exception("Syntax Error: Expected 'tantque'");
        }
        Condition();
        if (!match("kw", "faire")) {
            throw new Exception("Syntax Error: Expected 'faire'");
        }
        Instruction();
    }

    // Grammar rule: <Condition> ::= <Expression> <OperateurRelationnel> <Expression>
    void Condition() throws Exception {
        Expression();
        OperateurRelationnel();
        Expression();
    }

    // Helper methods to check operators
    boolean isOperateurRelationnel(String value) {
        return value.equals("<") || value.equals(">") || value.equals("=") || value.equals("<=") || value.equals(">=") || value.equals("<>");
    }

    boolean isOperateurSigne(String value) {
        return value.equals("+") || value.equals("-");
    }

    boolean isOperateurAddition(String value) {
        return value.equals("+") || value.equals("-") || value.equals("ou");
    }

    boolean isOperateurMult(String value) {
        return value.equals("*") || value.equals("div") || value.equals("mod") || value.equals("et");
    }

    // Grammar rule: <OperateurRelationnel> ::= < / > / = / <= / >= / <>
    void OperateurRelationnel() throws Exception {
        if (!isOperateurRelationnel(getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected relational operator");
        }
        match("cs", getCurrentToken().value);
    }

    // Grammar rule: <OperateurSigne> ::= + / -
    void OperateurSigne() throws Exception {
        if (!isOperateurSigne(getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected sign operator (+ or -)");
        }
        match("cs", getCurrentToken().value);
    }

    // Grammar rule: <OperateurAddition> ::= + / - / ou
    void OperateurAddition() throws Exception {
        if (!isOperateurAddition(getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected addition operator (+, -, or ou)");
        }
        match("cs", getCurrentToken().value);
    }

    // Grammar rule: <OperateurMult> ::= * / div / mod / et
    void OperateurMult() throws Exception {
        if (!isOperateurMult(getCurrentToken().value)) {
            throw new Exception("Syntax Error: Expected multiplication operator (*, div, mod, or et)");
        }
        match("cs", getCurrentToken().value);
    }
}