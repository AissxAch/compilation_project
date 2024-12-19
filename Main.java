public class Main {
      public static void main(String[] args) throws Exception  {
            Functions start = new Functions();
            start.startLexicalCompiler();
            Syntaxique syntaxique = new Syntaxique();
            syntaxique.addAllTokens();
            syntaxique.ProgrammeAlgoLang(syntaxique.tokens.get(0));
      }
}
