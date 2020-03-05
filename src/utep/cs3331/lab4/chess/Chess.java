// Oscar Castro
// CS 3331 - Lab 4

/*
The program will represent a chess game where the user can register or log in.
The user can save the game or resume a previous game.
 */

package utep.cs3331.lab4.chess;
import utep.cs3331.lab4.players.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.input.SAXBuilder;


public class Chess {
    public static void main(String[] args) {

        final int MAX_CHESS_PIECES = 32; // Max pieces available in a chess game.
        Figure[] figures = new Figure[MAX_CHESS_PIECES]; // Array that will store the pieces objects created.
        boolean isRunning = true;
        boolean isMove = true;
        int regOrLog;
        Scanner input = new Scanner(System.in);

        // Creates a new user and saves it in the system.
        // Or logs in the user and gets its saved game.
        while(true) {
            System.out.println("Select an option(1 or 2).");
            System.out.println("1.Register");
            System.out.println("2.Log in");
            regOrLog = input.nextInt();
            if(regOrLog == 1 || regOrLog == 2){
                break;
            }
            System.out.println("Invalid option!");
        }
        User user;
        if(regOrLog == 1) {
            user = registerUser();
        } else {
            user = loginUser();
            System.out.println("User: " + user.getName());
        }

        while(isRunning) {
            // Reads the XML file and parse it to create the objects figure.
            System.out.println("Select an option(1, 2, 3 or 4): ");
            System.out.println("1.New Game");
            System.out.println("2.Resume Game");
            System.out.println("3.Save Game");
            System.out.println("4.Exit");
            int decision = input.nextInt();
            if (decision == 1) {
                readChessConfigFile(figures);
                while(isMove) {
                    userMove(user, figures);
                    System.out.println("Select option (1 or 2).");
                    System.out.println("1.Make a move.");
                    System.out.println("2.Return to menu");
                    decision = input.nextInt();
                    if(decision == 2){
                        isMove = false;
                    }
                }
            } else if (decision == 2){
                getSavedGame(user.getId(), figures);
                while(isMove) {
                    userMove(user, figures);
                    System.out.println("Select option (1 or 2).");
                    System.out.println("1.Make a move.");
                    System.out.println("2.Return to menu");
                    decision = input.nextInt();
                    if(decision == 2){
                        isMove = false;
                    } else if(decision != 1){
                        System.out.println("Pick between 1 or 2");
                    }
                }
            } else if (decision == 3){
                saveGame(user, figures);
            }else if (decision == 4){
                isRunning = false;
            }
        }
    }

    public static void saveGame(User user, Figure[] figures) {
        try {
            //read the XML file
            File saveGameFile = new File("src/utep/cs3331/lab4/files/SaveGame.xml");

            //Create a document builder
            SAXBuilder saxBuilder = new SAXBuilder();

            //Create a DOM tree Obj
            Document configFile = saxBuilder.build(saveGameFile);

            //get the root element
            Element root = configFile.getRootElement();

            // create a new element and add the user data.
            Element gameData = new Element("game");
            gameData.addContent(new Element("id").setText(user.getId()));

            Element pieces = new Element("pieces");

            for (Figure figure: figures){
                Element piece = new Element("piece")
                        .setAttribute("color", figure.getColor())
                        .setAttribute("locationY", String.valueOf(figure.getY()))
                        .setAttribute("locationX", String.valueOf(figure.getX()))
                        .setAttribute("play", figure.getPlay())
                        .setText(figure.getType());
                pieces.addContent(piece);
            }

            gameData.addContent(pieces);
            root.addContent(gameData);
            configFile.setContent(root);

            // write the data into the xml file.
            FileWriter fileWriter = new FileWriter("src/utep/cs3331/lab4/files/SaveGame.xml");
            XMLOutputter outputFile = new XMLOutputter();
            outputFile.setFormat(Format.getPrettyFormat());
            outputFile.output(configFile, fileWriter);
            outputFile.output(configFile, System.out);
            fileWriter.close();

        } catch (JDOMException e) {
            System.out.println("ERROR: JDOM Exception.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: input/output exception.");
            e.printStackTrace();
        }
    }

    public static String randomId(){

        final int MAX_ID_LENGTH = 8;
        String alphaNum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder id = new StringBuilder(MAX_ID_LENGTH);
        for( int i = 0; i < MAX_ID_LENGTH; i++ ) {
            id.append(alphaNum.charAt(rnd.nextInt(alphaNum.length())));
        }

        return id.toString();
    }

    public static User registerUser(){
        Scanner userInput = new Scanner(System.in);

        // Prompt user for information.
        System.out.println("Please enter your information: ");
        System.out.println("Name: ");
        String name = userInput.next();
        System.out.println("Expertise level (novice, medium, advanced, master): ");
        String expertise = userInput.next();
        System.out.println("Piece color (black / white): ");
        String userColor = userInput.next();

        try {
            //read the XML file
            File userFile = new File("src/utep/cs3331/lab4/files/UserInfo.xml");
             //Create a document builder
            SAXBuilder saxBuilder = new SAXBuilder();

            //Create a DOM tree Obj
            Document configFile = saxBuilder.build(userFile);

            //get the root element
            Element root = configFile.getRootElement();
            List<Element> names = root.getChildren("user");
            
            boolean isTaken = false;
            while(true) {
                for (Element user : names) {
                    if(user.getChild("name").getValue().equalsIgnoreCase(name)) {
                        isTaken = true;
                        break;
                    }
                }
                if(isTaken) {
                    System.out.println("Name is already taken. Please choose another name.");
                    name = userInput.next();
                } else {
                    break;
                }
            }

            // get random id
            String userId = randomId();

            // create a new element and add the user data.
            Element userData = new Element("user");
            userData.addContent(new Element("name").setText(name));
            userData.addContent(new Element("expertise_level").setText(expertise));
            userData.addContent(new Element("user_color").setText(userColor));
            userData.addContent(new Element("id").setText(userId));
            root.addContent(userData);
            configFile.setContent(root);

            // write the data into the xml file.
            FileWriter fileWriter = new FileWriter("src/utep/cs3331/lab4/files/UserInfo.xml");
            XMLOutputter outputFile = new XMLOutputter();
            outputFile.setFormat(Format.getPrettyFormat());
            outputFile.output(configFile, fileWriter);
            outputFile.output(configFile, System.out);
            fileWriter.close();

            // Create a user object to return it.
            return new User(name, userColor, expertise, userId);

        } catch (JDOMException e) {
            System.out.println("ERROR: JDOM Exception.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: input/output exception.");
            e.printStackTrace();
        }
        return new User();
    }

    public static User loginUser(){
        Scanner userInput = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();
        userInfo(users);
        System.out.println("Enter your name: ");
        String userName = userInput.next();
        for (User user: users) {
            System.out.println("Log Users: " + user.getName());
            if (user.getName().equalsIgnoreCase(userName))
                return user;
        }
        return new User();
    }

    public static void getSavedGame(String id, Figure[] figures) {
        try {
            //read the XML file
            File inputFile = new File("src/utep/cs3331/lab4/files/SaveGame.xml");

            //Create a document builder
            SAXBuilder saxBuilder = new SAXBuilder();

            //Create a DOM tree Obj
            Document configFile = saxBuilder.build(inputFile);

            //get the root element
            Element root = configFile.getRootElement();
            List<Element> games = root.getChildren();

            for (Element game: games) {
                if(game.getChild("id").getValue().equals(id)){
                    Element pieces = game.getChild("pieces");
                    List<Element> listPieces = pieces.getChildren();

                    for (int i = 0; i < listPieces.size(); i++) {

                        // Parse the attributes of the figure.
                        Element piece = listPieces.get(i);
                        String color = piece.getAttributeValue("color");
                        char pos_x = piece.getAttributeValue("locationX").charAt(0);
                        int pos_y = Integer.parseInt(piece.getAttributeValue("locationY"));
                        String play = piece.getAttributeValue("play");

                        // Create a object for the piece and store it in array.
                        switch (piece.getValue().toLowerCase()) {
                            case "pawn":
                                Pawn pawn = new Pawn(piece.getValue(), color, pos_x, pos_y, play);
                                figures[i] = pawn;
                                break;
                            case "knight":
                                Knight knight = new Knight(piece.getValue(), color, pos_x, pos_y, play);
                                figures[i] = knight;
                                break;
                            case "rook":
                                Rook rook = new Rook(piece.getValue(), color, pos_x, pos_y, play);
                                figures[i] = rook;
                                break;
                            case "bishop":
                                Bishop bishop = new Bishop(piece.getValue(), color, pos_x, pos_y, play);
                                figures[i] = bishop;
                                break;
                            case "queen":
                                Queen queen = new Queen(piece.getValue(), color, pos_x, pos_y, play);
                                figures[i] = queen;
                                break;
                            case "king":
                                King king = new King(piece.getValue(), color, pos_x, pos_y, play);
                                figures[i] = king;
                                break;
                            default:
                                System.out.println("Not a valid piece name. ");
                                break;
                        }
                    }
                }
            }
        } catch(JDOMException e){
            System.out.println("JDOME exception.");
            e.printStackTrace();
        } catch(IOException e){
            System.out.println("Input/Output exception.");
            e.printStackTrace();
        }
    }

    public static void readChessConfigFile(Figure[] figures){
        try {
            //read the XML file
            File inputFile = new File("src/utep/cs3331/lab4/files/ChessConfig.xml");

            //Create a document builder
            SAXBuilder saxBuilder = new SAXBuilder();

            //Create a DOM tree Obj
            Document configFile = saxBuilder.build(inputFile);

            //get the root element
            Element root = configFile.getRootElement();
            Element pieces = root.getChildren().get(0);
            List<Element> listPieces = pieces.getChildren();

            for (int i = 0; i < listPieces.size(); i++) {

                // Parse the attributes of the figure.
                Element piece = listPieces.get(i);
                String color = piece.getAttributeValue("color");
                char pos_x = piece.getAttributeValue("locationX").charAt(0);
                int pos_y = Integer.parseInt(piece.getAttributeValue("locationY"));
                String play = piece.getAttributeValue("play");

                // Create a object for the piece and store it in array.
                switch (piece.getValue().toLowerCase()) {
                    case "pawn":
                        Pawn pawn = new Pawn(piece.getValue(), color, pos_x, pos_y, play);
                        figures[i] = pawn;
                        break;
                    case "knight":
                        Knight knight = new Knight(piece.getValue(), color, pos_x, pos_y, play);
                        figures[i] = knight;
                        break;
                    case "rook":
                        Rook rook = new Rook(piece.getValue(), color, pos_x, pos_y, play);
                        figures[i] = rook;
                        break;
                    case "bishop":
                        Bishop bishop = new Bishop(piece.getValue(), color, pos_x, pos_y, play);
                        figures[i] = bishop;
                        break;
                    case "queen":
                        Queen queen = new Queen(piece.getValue(), color, pos_x, pos_y, play);
                        figures[i] = queen;
                        break;
                    case "king":
                        King king = new King(piece.getValue(), color, pos_x, pos_y, play);
                        figures[i] = king;
                        break;
                    default:
                        System.out.println("Not a valid piece name. ");
                        break;
                }
            }

        } catch(JDOMException e){
            System.out.println("JDOME exception.");
            e.printStackTrace();
        } catch(IOException e){
            System.out.println("Input/Output exception.");
            e.printStackTrace();
        }

    }

    public static void userMove(User user, Figure[] figures){
        Scanner input = new Scanner(System.in);
        char new_x; // user's new position x
        int new_y; // user's new position y
        String piece_name; // user's piece to move.
        char pos_x; // Selected piece position x.
        int pos_y; // Selected piece position y.

        while (true) {
            // Prompt user to select the piece they want to move
            // and the piece's X and Y position
            System.out.println("Select a piece to move(bishop, king, knight, pawn, queen, rook).");
            piece_name = input.next();
            System.out.println("Position x (a - h): ");
            pos_x = input.next().charAt(0);
            System.out.println("Position y (1 - 8): ");
            pos_y = input.nextInt();

            // Prompt user for the new location.
            System.out.println("Enter the new location(x, y).");
            System.out.println("Position x (a - h): ");
            new_x = input.next().charAt(0);
            System.out.println("Position y (1 - 8): ");
            new_y = input.nextInt();

            if (((Character.toLowerCase(new_x) >= 'a' && Character.toLowerCase(new_x) <= 'h')
                    && (new_y >= 1 && new_y <= 8)) &&
                    ((Character.toLowerCase(pos_x) >= 'a' && Character.toLowerCase(pos_x) <= 'h')
                    && (pos_y >= 1 && pos_y <= 8))){
                break;
            }
            System.out.println("Invalid location! Please try again.");
        }

        // Traverse array of figures to check which figures can move to an empty location.
        for (Figure figure : figures) {
            System.out.println("Piece: " + figure.getType() + "color: " + figure.getColor());
            if(figure.getType().equalsIgnoreCase(piece_name) && figure.getX()== pos_x && figure.getY()== pos_y
                && figure.move(new_x, new_y) && user.getColor().equalsIgnoreCase(figure.getColor())){
                System.out.println("We are here!");
                for (Figure movFig: figures) {
                    System.out.println("movFig Piece: " + movFig.getType() + " position " + figure.getX() + ", " + figure.getY());
                    if(figure.getX() == movFig.getX() && figure.getY() == movFig.getY()){
                        System.out.println("Piece: " + figure.getType()
                                            + "\nMoves to " + new_x + ", " + new_y);
                        break;
                    }
                }
                figure.setX(new_x);
                figure.setY(new_y);
                break;
            }
        }

        for (Figure figure: figures){
            System.out.println("Piece: " + figure.getType());
            System.out.println("Location X: " + figure.getX());
            System.out.println("Location Y: " + figure.getY());
            System.out.println("Color: " + figure.getColor());

        }
    }

    public static void userInfo(ArrayList<User> usersList){

        try {
            //read the XML file
            File inputFile = new File("src/utep/cs3331/lab4/files/UserInfo.xml");

            //Create a document builder
            SAXBuilder saxBuilder = new SAXBuilder();

            //Create a DOM tree Obj
            Document userFile = saxBuilder.build(inputFile);

            Element root = userFile.getRootElement();

            //read users profile
            List<Element> Users = root.getChildren("user");

            for (Element user : Users) {
                // Create a new user and add it to the arrayList of Users.
                User newUser = new User(user.getChild("name").getValue(),
                        user.getChild("user_color").getValue(),
                        user.getChild("expertise_level").getValue(),
                        user.getChild("id").getValue());
                usersList.add(newUser);
            }

        } catch(JDOMException e){
            System.out.println("JDOME exception.");
            e.printStackTrace();
        } catch(IOException e){
            System.out.println("Input/Output exception.");
            e.printStackTrace();
        }
    }

}
