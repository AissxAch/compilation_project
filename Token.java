public class Token {
    String id;
    String value;

    Token(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String toString() {
        return id + ":" + value;
    }
}