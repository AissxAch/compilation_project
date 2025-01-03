public class Main {
      public static void main(String[] args) throws Exception  {
            Func start = new Func();
            start.startLexicalCompiler();
            try {
                  new Syntaxique();
            } catch (Exception e) {
                  System.out.println(e.getMessage());
            }
            
      }
}
