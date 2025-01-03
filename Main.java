public class Main {
      public static void main(String[] args) throws Exception {
          Functions start = new Functions();
          start.startLexicalCompiler();
          SyntaxAnalyzer S = new SyntaxAnalyzer("token.al");
          S.parse();
      }
  }