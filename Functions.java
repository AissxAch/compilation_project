import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Functions {
    BufferedReader reader;
    States states;
    static List<Character> Cs;
    static List<String> errors = new ArrayList<>(); // Store errors

    Functions() throws Exception {
        reader = new BufferedReader(new FileReader("test.txt"));
        states = new States();
        Cs = new ArrayList<>();
        Cs.add('<');
        Cs.add('>');
        Cs.add('=');
        Cs.add('+');
        Cs.add('-');
        Cs.add('*');
        Cs.add(';');
        Cs.add(',');
        Cs.add(':');
        Cs.add('.');
        Cs.add('/');
    }

    void startLexicalCompiler() throws IOException {
        int c;
        while ((c = reader.read()) != -1) {
            if (!Character.isWhitespace(c))
                states.state0((char) c, reader);
        }
        states.close();
        // Print errors
        System.out.println("Errors:");
        for (String error : errors) {
            System.out.println(error);
        }
    }

    static boolean lettre(Character c) {
        return Character.isLetter(c); // Simplified
    }
    
    static boolean chiffre(Character c) {
        return Character.isDigit(c); // Simplified
    }
    
    static boolean isCs(Character c) {
        return Cs.contains(c);
    }

    static boolean isChar(Character c) {
        return !(isCs(c) || Character.isWhitespace(c) || c == '$');
    }

    static Character ChercherCar(BufferedReader reader) throws IOException {
        int character;
        if ((character = reader.read()) != -1)
            return (char) character;
        else return '$';
    }

    static void mark(BufferedReader reader) throws IOException {
        reader.mark(100);
    }

    static void reset(BufferedReader reader) throws IOException {
        reader.reset();
    }

    static void error(Character c) {
        String errorMsg = "Undefined character: " + c;
        System.out.println(errorMsg);
        errors.add(errorMsg); // Store error
    }

    static void errorz(BufferedReader reader, String Token) throws IOException {
        reader.mark(100);
        Character c = ChercherCar(reader);
        while (!Character.isWhitespace(c) && c != '$' && !isCs(c)) {
            Token += c;
            reader.mark(100);
            c = ChercherCar(reader);
        }
        reader.reset();
        String errorMsg = "Undefined token: " + Token;
        System.out.println(errorMsg);
        errors.add(errorMsg); // Store error
    }
}