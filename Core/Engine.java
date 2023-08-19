package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private TETile[][] worldKey;
    private HouseWorld builder;
    private int mouseX, mouseY;
    private static final int MOUSEXBOUND = 79;
    private static final int MOUSEYBOUND = 29;
    private static final int PAUSERATE = 200;
    private static final int TILESIZE = 16;

    private String avatarName;
    private String seed;
    private String moves;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        moves = "";
        String response = "";
        displayTitleScreen();

        while (!goodResp(response)) {
            response = "";
            if (StdDraw.hasNextKeyTyped()) {
                response = response + Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
        if (response.equals("N")) {
            nameAvatar();
            response = response + getSeed();
            seed = response;
            worldKey = interactWithInputString(response);
            renderWorld(worldKey);
            response = "";
        } else if (response.equals("L")) {
            seed = handleLoad();
            worldKey = interactWithInputString(seed);
            renderWorld(worldKey);
            response = "";
        } else if (response.equals("R")){
            seed = handleLoad();
            worldKey = interactWithInputString('r' + seed);
            response = "";
        } else if (response.equals("Q")) {
                handleExit();
            }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                response = response + Character.toUpperCase(StdDraw.nextKeyTyped());
                if (!isMove(response) && response.charAt(0) != ':') {
                    response = "";
                }
            }
            if (isMove(response)) {
                moves += response;
                handleMove(response, worldKey);
                response = "";
            } else if (response.length() > 1 && response.charAt(0) == ':') {
                if (response.charAt(1) != 'Q') {
                    response = "";
                } else {
                    handleSave();
                    handleExit();
                }
            }
        }
    }

    private boolean goodResp(String response) {
        if (response.equals("L")) {
            return true;
        } else if (response.equals("N")) {
            return true;
        } else if (response.equals("R")) {
            return true;
        } else {
            return response.equals("Q");
        }
    }

    private void nameAvatar(){
        String response = "";
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 2, "Would you like to name your Avatar?");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 3, "(Y/N)");

        while (!nameAvatarHelper(response)){
            response = "";
            if (StdDraw.hasNextKeyTyped()){
                response = response + Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }

        if (response.equals("Y")){
            chooseName();
        } else {
            avatarName = ".";
        }
    }

    private boolean nameAvatarHelper(String response){
        if (response.equals("Y")){
            return true;
        } else if (response.equals("N")){
            return true;
        } else {
            return false;
        }
    }

    private void chooseName(){
        avatarName = "";
        String response = "";
        Character c;
        displayChooseNameScreen();

        while(!response.equals(":S")){
            if (StdDraw.hasNextKeyTyped()){
                c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c == ':'){
                    response = response + c;
                } else {
                    if (!response.equals("") && response.charAt(0) == ':'){
                        if (c == 'C'){
                            avatarName = "";
                            drawScreen(5 * 2, avatarName);
                            StdDraw.pause(PAUSERATE);
                            displayChooseNameScreen();
                            response = "";
                        } else if (c == 'Q'){
                            response = response + c;
                            break;
                        } else if (c == 'S'){
                            response = response + c;
                        }
                    } else {
                        avatarName = avatarName + c;
                        drawScreen(5 * 2, avatarName);
                    }

                }
            }
            }
        handleName(response);
        }

    private void displayChooseNameScreen(){
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 5, "Choose your name.");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 2, "Clear entry: (:C)");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 3, "Save entry: (:S)");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 4, "Continue without name: (:Q)");
    }

    private void handleName(String response){
        if (response.equals(":Q")){
            avatarName = ".";
            return;
        }
            StdDraw.clear(Color.BLACK);
            StdDraw.text(WIDTH / 2, HEIGHT - 5 * 2, "Welcome " + avatarName + "!");
            StdDraw.pause(PAUSERATE * 6);
            StdDraw.text(WIDTH / 2, HEIGHT - 5 * 3, "Let's make a world!");
            StdDraw.pause(PAUSERATE * 6);
    }


    private boolean isMove(String response) {
        return response.equals("W") || response.equals("A")
                || response.equals("S") || response.equals("D");
    }

    private void displayTitleScreen() {
        StdDraw.setCanvasSize(WIDTH * TILESIZE, HEIGHT * TILESIZE);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 5 * 6);
        StdDraw.setFont(fontBig);
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT - 5, "Fire Fight");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 2, "New Game: (N)");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 3, "Load Game: (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 4, "Replay: (R)");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 5, "Quit: (Q)");
    }

    private void displaySeedScreen() {
        StdDraw.clear(Color.black);
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 2, "Enter random numbers to"
                + " start your game and press S when finished");
        StdDraw.text(WIDTH / 2, HEIGHT - 5 * 3, "Example: 5242429S ");
    }

    private void drawScreen(int heightPos, String words) {
        StdDraw.clear(Color.black);
        StdDraw.text(WIDTH / 2, HEIGHT - heightPos, words);
    }

    private void generateHUD(TETile[][] world) {
        StdDraw.enableDoubleBuffering();

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(5, HEIGHT + 2, world[mouseX][mouseY].description());
        StdDraw.text(WIDTH - 5 * 4, HEIGHT + 2, "Quicksave (:Q)");
        if (!avatarName.equals(".")){
            StdDraw.text(WIDTH - 5 * 2, HEIGHT + 2, avatarName);
        }
        StdDraw.show();
    }

    private String getSeed() {
        String response = "";
        displaySeedScreen();

        while (!response.contains("S")) {
            if (StdDraw.hasNextKeyTyped()) {
                response = response + Character.toUpperCase(StdDraw.nextKeyTyped());
                drawScreen(5 * 3, response);
            }
            if (!testResponse(response)) {
                response = "";
            }
        }
        return response;
    }

    private boolean testResponse(String response) {
        boolean goodResp = true;
        int endIndex;
        if (response.contains("S")) {
            endIndex = response.indexOf("S");
        } else {
            endIndex = response.length();
        }
        if (!response.equals("")) {
            try {
                Long.parseLong(response.substring(0, endIndex));
            } catch (NumberFormatException s) {
                drawScreen(5 * 3, "You can only enter numbers.");
                goodResp = false;
            }
            if (response.indexOf("S") == 0) {
                drawScreen(5 * 3, "You can only end with S");
                goodResp = false;
            }
        }
        return goodResp;
    }


    private void renderWorld(TETile[][] world) {
        ter.initialize(WIDTH, HEIGHT + 3);
        ter.renderFrame(world);
        generateHUD(world);

    }

    private void handleSave() {
        try {
            FileWriter writer = new FileWriter("byow/Core/save_file.txt");
            seed = seed + moves;
            System.out.println(seed);
            writer.write(seed + "\n");
            writer.write(avatarName);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error.");
        }
    }

    private void handleMove(String response, TETile[][] world) {
        builder.handleMove(response);
        ter.renderFrame(world);

        handleMouseY();
        handleMouseX();

        generateHUD(world);
    }

    private String handleReplay() {
        try {
            File save = new File("byow/Core/save_file.txt");
            Scanner readSave = new Scanner(save);
            seed = readSave.nextLine();
            return "r" + seed;
        } catch (FileNotFoundException e) {
            System.out.println("No file.");
        }
        return null;
    }

    private String handleLoad(){
        try {
            File save = new File("byow/Core/save_file.txt");
            Scanner readSave = new Scanner(save);
            seed = readSave.nextLine();
            if(readSave.hasNext()) {
                avatarName = readSave.nextLine();
            }
            return seed;
        } catch (FileNotFoundException e) {
            System.out.println("No file.");
        }
        return null;
    }

    private void handleMouseY() {
        mouseY = (int) Math.round(StdDraw.mouseY());
        if (mouseY < 0) {
            mouseY = 0;
        } else if (mouseY >= HEIGHT) {
            mouseY = MOUSEYBOUND;
        }
    }

    private void handleMouseX() {
        mouseX = (int) Math.round(StdDraw.mouseX());
        if (mouseX < 0) {
            mouseX = 0;
        } else if (mouseX >= WIDTH) {
            mouseX = MOUSEXBOUND;
        }
    }

    private void handleExit() {
        System.exit(0);
    }



    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        input = input.toUpperCase();
        String newMoves = "";
        TETile[][] finalWorldFrame = null;
        boolean replay = false;

        if (input.charAt(0) == 'L') {
            seed = handleLoad();
            finalWorldFrame = interactWithInputString(seed);
            newMoves = input.substring(1);
        } else {
            if (input.charAt(0) == 'R'){
                replay = true;
                input = input.substring(1,input.length());
            }
            int sLocation = input.indexOf('S');
            if (input.length() > sLocation + 1) {
                newMoves = input.substring(sLocation + 1);
            }

            if (input.charAt(0) == ('N')) {
                seed = input.substring(0, sLocation + 1);
            }
            Long seedInt = Long.parseLong(seed.substring(1, sLocation));

            finalWorldFrame = new TETile[WIDTH][HEIGHT];

            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    finalWorldFrame[i][j] = Tileset.NOTHING;
                }
            }
            moves = "";
            builder = new HouseWorld(finalWorldFrame, seedInt);
            if (replay) {
                renderWorld(finalWorldFrame);
            }
        }


        String character = null;
        if (newMoves != null) {
            for (int i = 0; i < newMoves.length(); i++) {
                character = Character.toString(newMoves.charAt(i));
                if (isMove(character)) {
                    if (replay){
                        moves += character;
                        StdDraw.pause(PAUSERATE);
                        handleMove(character, finalWorldFrame);
                    } else {
                        moves += character;
                        builder.handleMove(character);
                    }
                } else if (character.equals(":")) {
                    if (newMoves.charAt(i + 1) == 'Q') {
                        handleSave();
                    }
                }
            }
        }
        if (!replay){
            renderWorld(finalWorldFrame);
        }

        return finalWorldFrame;
    }
}
