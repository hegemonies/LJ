package LJ;

public class Token {
    private String type;
    private String value;
    private Location location;

    Token(String type, String value, Location location) {
        this.type = type;
        this.value = value;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Location getLocation() {
        return location;
    }

    public String toString() {
        return type.toString() + "\t" + value + "\t" + location.toString();
    }
}
