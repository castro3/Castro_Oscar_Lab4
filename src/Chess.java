import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Chess {
    public static void main(String[] args) {
        // Initializing variables.
        final int MAX_CHESS_PIECES = 32; // Total number of pieces available in a chess game from both players.
        File movements = new File("movements.txt"); // Reading the movements.txt file
        Scanner read = null; // Initialize the scanner that will read the data in movements.txt
        try {
            // Make sure the file is available if not throw an exception.
            read = new Scanner(movements);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found.");
        }
        Figure[] pieces = new Figure[MAX_CHESS_PIECES]; // Array that will store the pieces objects created.
        int counter = 0; // A counter to store the new pieces in the array at position counter.
        Scanner input = new Scanner(System.in); // Scanner to obtain input from the user.
        String piece_name; // Name of the piece.
        String figure; // Unique name of piece.
        String color; // Color of the piece.
        char pos_x; // Position x of the piece.
        int pos_y; // Position y of the piece.
        char new_x; // Position x provided by the user.
        int new_y; // Position y provided by the user.

        while(read != null && read.hasNext()) {
            // Read the first line and store the data accordingly
            // Create the piece with the data provided and store that piece in the array of pieces.
            piece_name = read.next().replace(',', ' ').trim(); // Gets next word and
            color = read.next().replace(',', ' ').trim(); // takes out the "," .
            pos_x = read.next().charAt(0); // Gets the next word and returns the first character of that word.
            pos_y = read.nextInt(); // Gets the next available integer.
            // Store the unique name
            figure = piece_name + pos_x + pos_y;
            //System.out.println(figure);

            // Create a object for the piece and store it in array.
            switch (piece_name.toLowerCase()){
                case "pawn":
                    Pawn pawn = new Pawn(figure, color, pos_x, pos_y);
                    pieces[counter] = pawn;
                    break;
                case "knight":
                    Knight knight = new Knight(figure, color, pos_x,pos_y);
                    pieces[counter] = knight;
                    break;
                case "tower":
                    Tower tower = new Tower(figure, color, pos_x, pos_y);
                    pieces[counter] = tower;
                    break;
                case "bishop":
                    Bishop bishop = new Bishop(figure, color, pos_x, pos_y);
                    pieces[counter] = bishop;
                    break;
                case "queen":
                    Queen queen = new Queen(figure, color, pos_x, pos_y);
                    pieces[counter] = queen;
                    break;
                case "king":
                    King king = new King(figure, color, pos_x, pos_y);
                    pieces[counter] = king;
                    break;
                default:
                    System.out.println("Not a valid piece name. ");
                    break;
            }
            counter++;
        }

        // Prompt user for the new location.
        System.out.println("Please enter your move. ");
        System.out.println("Position x: ");
        new_x = input.next().charAt(0);
        System.out.println("Position y: ");
        new_y = input.nextInt();

        // checks that the location is on the board else prints out 'not valid move.'
        if(((Character.toUpperCase(new_x) >= 'A' && Character.toUpperCase(new_x) <= 'H')
                && (new_y >= 1 && new_y <= 8))) {
            for (Figure piece : pieces) {
                if (piece != null) {
                    // traverse the array and call move() on each piece available.
                    if (piece.move(new_x, new_y)) {
                        System.out.println(piece.getType() + ", " + piece.getColor() + " at "
                                + piece.getX() + ", " + piece.getY() + " can move to "
                                + Character.toUpperCase(new_x) + ", " + new_y);
                    }
                } else {
                    break;
                }
            }
        } else {
            System.out.println("Not a valid move.");
        }
    }
}
