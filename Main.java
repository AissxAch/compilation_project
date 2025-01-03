public class Main {
      public static void main(String[] args) throws Exception  {
            Func start = new Func();
            start.startLexicalCompiler();
            Syntaxique syntaxique = new Syntaxique();
            syntaxique.addAllTokens();
            syntaxique.ProgrammeAlgoLang(syntaxique.tokens.get(0));
      }
}
