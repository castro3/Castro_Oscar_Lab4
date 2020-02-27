public class Figure {
    protected String type;
    protected String color;
    protected char x;
    protected int y;

    public Figure(String type, String color, char x, int y) {
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public boolean move(char new_x, int new_y){
        // Checks if the position is inside the board.
        new_x = Character.toUpperCase(new_x);
        x = Character.toUpperCase(x);
        return ((new_x >= 'A' && new_x <= 'H') && (new_y >= 1 && new_y <= 8)) && (new_x != x || new_y != y);
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public char getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
