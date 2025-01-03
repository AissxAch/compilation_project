class Token {
    String type;
    String value;

    Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + type + ", " + value + ")";
    }
<<<<<<< HEAD
    public String getNom(){
        return value;
    }  
}
=======
}
>>>>>>> 7cb13346282da23d5193b8bda3076fdc9682d71b
