import java.io.*;
import java.util.ArrayList;
public class Syntaxique {
      BufferedReader reader,treader;
      States states;
      ArrayList<Token> tokens = new ArrayList<>();
      
Syntaxique() throws Exception {
      treader = new BufferedReader(new FileReader("token.al"));
      // addAllTokens();
      // ProgrammeAlgoLang(tokens.get(0));
}

      void ProgrammeAlgoLang(Token token) throws IOException {
            printTokens();
            
      }

      void addAllTokens() throws IOException {
            String line;
            while ((line = treader.readLine()) != null) {   
                  String[] parts = line.split(",");
                  parts[0] = parts[0].substring(1);
                  parts[1] = parts[1].substring(0,parts[1].length()-1);
                  Token token = new Token(parts[0], parts[1]);
                  tokens.add(token);
            }
      }

      void printTokens() {
            for(Token token : tokens) {
                  System.out.println(token);
            }
      }
}
