import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Func {
      BufferedReader reader;
      States states;
      static List<Character> Cs;

      Func() throws Exception {
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
      }

      void startLexicalCompiler() throws IOException {
            int c;
            while((c = reader.read()) != -1) {
                  if(!Character.isWhitespace(c))
                  states.state0((char)c, reader);
            }
            states.close();
      }

      static boolean lettre(Character c) {
           for(Character ch = 'a'; ch <= 'z'; ch++) {
                  if(c == ch) {
                        return true;
                  }
           }
           for(Character ch = 'A'; ch <= 'Z'; ch++) {
                  if(c == ch) {
                        return true;
                  }
           }
           return false;
      }

      static boolean chiffre(Character c) {
            for(Character ch = '0'; ch <= '9'; ch++) {
                  if(c == ch) {
                        return true;
                  }
            }
            return false;
      }
      
      static boolean isCs(Character c) {
            return (Cs.contains(c)) ? true :  false;
      }

      static boolean isChar(Character c) {
            if(isCs(c) || Character.isWhitespace(c) || c == '$') {
                  return false;
            }else {
                  return true;
            }
      }
      
      static Character ChercherCar(BufferedReader reader) throws IOException {
            int character;
            if((character = reader.read()) != -1)
            return (char)character;
            else return '$';
      }

      static void mark(BufferedReader reader) throws IOException {
            reader.mark(100);
      }

      static void reset(BufferedReader reader) throws IOException {
            reader.reset();
      }
      
      static void error(Character c){
            System.out.println("Undifined character : " + c);
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
            System.out.println("Undefined : "+Token);
            
      }
}         
