import java.io.*;

public class States {
    String Token;
    BufferedWriter writer;

    States() throws IOException {
        writer = new BufferedWriter(new FileWriter("token.al"));
    }

    void state0(Character c, BufferedReader reader) throws IOException {
        Token = "";
        Functions.mark(reader);
        Token += c;
        switch (c) {
            case 'p': state1(reader); break; // programme
            case 'c': state14(reader); break; // constante, caractère
            case 'v': state29(reader); break; // variable
            case 'e': state36(reader); break; // entier
            case 't': state41(reader); break; // tantque
            case 'd': state48(reader); break; // debut, div
            case 'f': state53(reader); break; // fin, faire
            case 'm': state55(reader); break; // mod
            case 'o': state66(reader); break; // ou
            case '<': state11(reader); break;
            case '>': state12(reader); break;
            case '=': state13(reader); break;
            case '+': case '-': case '*': state13(reader); break;
            case ';': case ':': case ',': case '(': case ')': case '.': state65(reader); break; // Handle special characters
            default:
                if (Functions.lettre(c)) {
                    // Check if the token is a type keyword (Entier, Caractère, Réel)
                    if (c == 'E' || c == 'C' || c == 'R') {
                        stateTypeKeyword(reader); // Handle type keywords
                    } else {
                        state10(reader); // Handle identifiers
                    }
                } else if (Functions.chiffre(c)) {
                    state63(reader); // Handle numbers
                } else {
                    Functions.error(c); // Handle invalid characters
                }
                break;
        }
    }
  
  void stateTypeKeyword(BufferedReader reader) throws IOException {
      // Read characters until the end of the keyword or an invalid character is found
      while (true) {
          Character c = Functions.ChercherCar(reader);
          if (c == '$' || Character.isWhitespace(c) || Functions.isCs(c)) {
              // End of keyword
              if (Token.equals("Entier") || Token.equals("Caractère") || Token.equals("Réel")) {
                  state64(reader); // Finalize the keyword
              } else {
                  state10(reader); // Fall back to identifier handling
              }
              break;
          } else {
              Token += c; // Append the character to the token
          }
      }
  }

    void state1(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state2(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state2(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'o') {
            state3(reader);
        } else {
            Functions.error(c);
        }
    }

    void state3(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'g') {
            state4(reader);
        } else {
            Functions.error(c);
        }
    }

    void state4(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state5(reader);
        } else {
            Functions.error(c);
        }
    }

    void state5(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state6(reader);
        } else {
            Functions.error(c);
        }
    }

    void state6(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'm') {
            state7(reader);
        } else {
            Functions.error(c);
        }
    }

    void state7(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'm') {
            state8(reader);
        } else {
            Functions.error(c);
        }
    }

    void state8(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        if (c == 'e') {
            Token += c;
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state9(BufferedReader reader) throws IOException {
        reader.mark(1024);
        Character c = Functions.ChercherCar(reader);
        if (Functions.lettre(c)) {
            Token += c;
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state10(BufferedReader reader) throws IOException {
        reader.mark(1024);
        Character c = Functions.ChercherCar(reader);
        if (c == '$' || Character.isWhitespace(c) || Functions.isCs(c)) {
            if (Functions.isCs(c)) reader.reset();
            System.out.println("(id, " + Token + ")");
            writer.write("(id, " + Token + ")\n");
        } else if (Functions.chiffre(c)) {
            Token += c;
            state10(reader);
        } else if (Functions.lettre(c)) {
            Token += c;
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state11(BufferedReader reader) throws IOException {
        reader.mark(2);
        Character c = Functions.ChercherCar(reader);
        if (Token.equals("<") && (c == '=' || c == '>')) {
            Token += c;
            state11(reader);
        } else if (!Functions.isCs(c) || c == ';') {
            reader.reset();
            System.out.println("(cs, " + Token + ")");
            writer.write("(cs, " + Token + ")\n");
        } else {
            Functions.error(c);
        }
    }

    void state12(BufferedReader reader) throws IOException {
        reader.mark(2);
        Character c = Functions.ChercherCar(reader);
        if (Token.equals(">") && c == '=') {
            Token += c;
            state12(reader);
        } else if (!Functions.isCs(c)) {
            reader.reset();
            System.out.println("(cs, " + Token + ")");
            writer.write("(cs, " + Token + ")\n");
        } else {
            Functions.error(c);
        }
    }

    void state13(BufferedReader reader) throws IOException {
        reader.mark(2);
        Character c = Functions.ChercherCar(reader);
        if (!Functions.isCs(c)) {
            reader.reset();
            System.out.println("(cs, " + Token + ")");
            writer.write("(cs, " + Token + ")\n");
        } else {
            Functions.error(c);
        }
    }

    void state14(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state15(reader);
        } else if (c == 'o') {
            state22(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state15(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state16(reader);
        } else {
            Functions.error(c);
        }
    }

    void state16(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state17(reader);
        } else {
            Functions.error(c);
        }
    }

    void state17(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'c') {
            state18(reader);
        } else {
            Functions.error(c);
        }
    }

    void state18(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state19(reader);
        } else {
            Functions.error(c);
        }
    }

    void state19(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'e') {
            state20(reader);
        } else {
            Functions.error(c);
        }
    }

    void state20(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state21(reader);
        } else {
            Functions.error(c);
        }
    }

    void state21(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        if (c == 'e') {
            Token += c;
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state22(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'n') {
            state23(reader);
        } else {
            Functions.error(c);
        }
    }

    void state23(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 's') {
            state24(reader);
        } else {
            Functions.error(c);
        }
    }

    void state24(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state25(reader);
        } else {
            Functions.error(c);
        }
    }

    void state25(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state26(reader);
        } else {
            Functions.error(c);
        }
    }

    void state26(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'n') {
            state27(reader);
        } else {
            Functions.error(c);
        }
    }

    void state27(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state28(reader);
        } else {
            Functions.error(c);
        }
    }

    void state28(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        if (c == 'e') {
            Token += c;
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state29(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state30(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state30(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state31(reader);
        } else {
            Functions.error(c);
        }
    }

    void state31(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'i') {
            state32(reader);
        } else {
            Functions.error(c);
        }
    }

    void state32(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state33(reader);
        } else {
            Functions.error(c);
        }
    }

    void state33(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'b') {
            state34(reader);
        } else {
            Functions.error(c);
        }
    }

    void state34(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'l') {
            state35(reader);
        } else {
            Functions.error(c);
        }
    }

    void state35(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        if (c == 'e') {
            Token += c;
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state36(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state64(reader);
        } else if (c == 'n') {
            state37(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state37(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state38(reader);
        } else {
            Functions.error(c);
        }
    }

    void state38(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'i') {
            state39(reader);
        } else {
            Functions.error(c);
        }
    }

    void state39(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'e') {
            state40(reader);
        } else {
            Functions.error(c);
        }
    }

    void state40(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        if (c == 'r') {
            Token += c;
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state41(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state42(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state42(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'n') {
            state43(reader);
        } else if (c == 'e') {
            state44(reader);
        } else {
            Functions.error(c);
        }
    }

    void state43(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state45(reader);
        } else {
            Functions.error(c);
        }
    }

    void state44(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'l') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state45(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'q') {
            state46(reader);
        } else {
            Functions.error(c);
        }
    }

    void state46(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'u') {
            state47(reader);
        } else {
            Functions.error(c);
        }
    }

    void state47(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        if (c == 'e') {
            Token += c;
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state48(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'i') {
            state49(reader);
        } else if (c == 'e') {
            state50(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state49(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'v') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state50(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'b') {
            state51(reader);
        } else {
            Functions.error(c);
        }
    }

    void state51(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'u') {
            state52(reader);
        } else {
            Functions.error(c);
        }
    }

    void state52(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 't') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state53(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'i') {
            state54(reader);
        } else if (c == 'a') {
            state69(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state54(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'n') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state55(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'o') {
            state56(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state56(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'd') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state57(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'u') {
            state58(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else {
            Functions.error(c);
        }
    }

    void state58(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 's') {
            state59(reader);
        } else {
            Functions.error(c);
        }
    }

    void state59(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'q') {
            state60(reader);
        } else {
            Functions.error(c);
        }
    }

    void state60(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'u') {
            state61(reader);
        } else {
            Functions.error(c);
        }
    }

    void state61(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == '\'') {
            state62(reader);
        } else {
            Functions.error(c);
        }
    }

    void state62(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'a') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state63(BufferedReader reader) throws IOException {
        reader.mark(10);
        Character c = Functions.ChercherCar(reader);
        if (c == '$' || Character.isWhitespace(c) || Functions.isCs(c)) {
            if (Functions.isCs(c)) reader.reset();
            System.out.println("(con, " + Token + ")");
            writer.write("(con, " + Token + ")\n");
        } else if (Functions.chiffre(c)) {
            Token += c;
            state63(reader);
        } else if (c == '.') {
            // Reject floating-point numbers
            reader.reset();
            Functions.error(c);
        } else {
            reader.reset();
            Functions.error(c);
        }
    }

    void state64(BufferedReader reader) throws IOException {
      reader.mark(2);
      Character c = Functions.ChercherCar(reader);
      if (Functions.isChar(c)) {
          reader.reset();
          Functions.errorz(reader, Token);
      } else {
          reader.reset();
          System.out.println("(kw, " + Token + ")");
          writer.write("(kw, " + Token + ")\n");
      }
  }

  void state65(BufferedReader reader) throws IOException {
    if (Token.equals(":")) {
        Character c = Functions.ChercherCar(reader);
        if (c == '=') {
            Token += c; // Combine `:` and `=`
            System.out.println("(cs, " + Token + ")");
            writer.write("(cs, " + Token + ")\n");
        } else {
            reader.reset();
            System.out.println("(cs, " + Token + ")");
            writer.write("(cs, " + Token + ")\n");
        }
    } else {
        System.out.println("(cs, " + Token + ")");
        writer.write("(cs, " + Token + ")\n");
    }
}


    void state66(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'u') {
            state67(reader);
        } else if (Functions.chiffre(c)) {
            state9(reader);
        } else if (Functions.lettre(c)) {
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state67(BufferedReader reader) throws IOException {
        reader.mark(2);
        Character c = Functions.ChercherCar(reader);
        if (Functions.isChar(c)) {
            reader.reset();
            Functions.errorz(reader, Token);
        } else {
            reader.reset();
            System.out.println("(kw, " + Token + ")");
            writer.write("(kw, " + Token + ")\n");
        }
    }

    void state68(BufferedReader reader) throws IOException {
        reader.mark(1024);
        Character c = Functions.ChercherCar(reader);
        if (Functions.chiffre(c)) {
            Token += c;
            state10(reader);
        } else {
            Functions.error(c);
        }
    }

    void state69(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'i') {
            state70(reader);
        } else {
            Functions.error(c);
        }
    }

    void state70(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state71(reader);
        } else {
            Functions.error(c);
        }
    }

    void state71(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'e') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void state72(BufferedReader reader) throws IOException {
        Character c = Functions.ChercherCar(reader);
        Token += c;
        if (c == 'r') {
            state64(reader);
        } else {
            Functions.error(c);
        }
    }

    void close() throws IOException {
        writer.close();
    }
}