package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class Direction {
    private int WIDTH;
    private int HEIGHT;

    private int rangeX, rangeY;
    private TETile world[][];
    private HashMap<String, Boolean> dirMap;

    public Direction(TETile[][]newworld, int seed){
        world = newworld;
        WIDTH = world.length;
        HEIGHT = world[0].length;
        dirMap = new HashMap<>();
    }

    private String chooseDir() {
        Random direction = new Random();
        int dir = direction.nextInt(4);
        return switch (dir) {
            case 0 -> "up";
            case 1 -> "down";
            case 2 -> "left";
            case 3 -> "right";
            default -> "";
        };
    }

    public String validateDir(int x, int y){
        dirMap.clear();
        String dir;
        boolean validation;
        Random randRange;

        while(dirMap.size() != 4) {
            dir = chooseDir();
            randRange = new Random();
            if (dir.equals("up")){
                validation = validateUp(x, y, randRange);
                dirMap.put(dir, validation);
                if (validation) return dir;
            } else if(dir.equals("down")){
                validation = validateDown(x, y, randRange);
                dirMap.put(dir, validation);
                if (validation) return dir;
            } else if(dir.equals("left")){
                validation = validateLeft(x, y, randRange);
                dirMap.put(dir, validation);
                if (validation) return dir;
            } else {
                validation = validateRight(x, y, randRange);
                dirMap.put(dir, validation);
                if (validation) return dir;
            }
        }
        return "";
    }

    private boolean validateDown(int x, int y, Random randRange){
        int newX = x;
        int newY = y;
        for (int i = x; i >= 0; i--){
            if (!inBoundsX(i) || world[i][y] != Tileset.NOTHING){
                break;
            }
            newX = i;
        }
        if (newX < x - 2){
            rangeX = randRange.nextInt(newX,x - 2);
        }

        for (int j = y; j >= 0; j--){
            if (!inBoundsY(j) || world[x][j] != Tileset.NOTHING){
                break;
            }
            newY = j;
        }
        if (newY < y - 2){
            rangeY = randRange.nextInt(newY, y - 2);
        }
        return x - newX >= 3 && y - newY >= 3;

    }
    private boolean validateUp(int x, int y, Random randRange){
        int newX = x;
        int newY = y;
        for (int i = x; i < WIDTH; i++){
            if (!inBoundsX(i) || world[i][y] != Tileset.NOTHING){
                break;
            }
            newX = i;
        }
        if (x + 3 <= newX){
            rangeX = randRange.nextInt(x + 3,Math.min(newX + 1, x + 9));
        }

        for (int j = y; j < HEIGHT; j++){
            if (!inBoundsY(j) || world[x][j] != Tileset.NOTHING){
                break;
            }
            newY = j;
        }
        if (y + 3 <= newY){
            rangeY = randRange.nextInt(y + 3, Math.min(newY + 1, y + 9));
        }
        return newX - x >= 3 && newY - y >= 3;
    }
    private boolean validateRight(int x, int y, Random randRange){
        int newX = x;
        int newY = y;
        for (int i = x; i < WIDTH; i++){
            if (!inBoundsX(i) || world[i][y] != Tileset.NOTHING){
                break;
            }
            newX = i;
        }
        if (x + 3 <= newX){
            rangeX = randRange.nextInt(x + 3,Math.min(newX + 1, x + 9));
        }

        for (int j = y; j < HEIGHT; j++){
            if (!inBoundsY(j) || world[x][j] != Tileset.NOTHING){
                break;
            }
            newY = j;
        }
        if (y + 3 <= newY){
            rangeY = randRange.nextInt(y + 3, Math.min(newY + 1, y + 9));
        }
        return newX - x >= 3 && newY - y >= 3;
    }

    private boolean validateLeft(int x, int y, Random randRange){
        int newX = x;
        int newY = y;
        for (int i = x; i >= 0; i--){
            if (!inBoundsX(i) || world[i][y] != Tileset.NOTHING){
                break;
            }
            newX = i;
        }
        if (newX < x - 2) {
            rangeX = randRange.nextInt(newX, x - 2);
        }

        for (int j = y; j >= 0; j--){
            if (!inBoundsY(j) || world[x][j] != Tileset.NOTHING){
                break;
            }
            newY = j;
        }
        if (newY < y - 2){
            rangeY = randRange.nextInt(newY, y - 2);
        }

        return x - newX >= 3 && y - newY >= 3;
    }

    public String validateHWDir(int x, int y){
            dirMap.clear();
            String dir;
            boolean validation;
            Random randRange;

            while(dirMap.size() != 2) {
                dir = chooseHWDir();
                randRange = new Random();
                if (dir.equals("up")){
                    validation = validateHWUp(x, y, randRange);
                    dirMap.put(dir, validation);
                    if (validation) return dir;
                } else {
                    validation = validateHWRight(x, y, randRange);
                    dirMap.put(dir, validation);
                    if (validation) return dir;
                }
            }
            return "sideways";
        }

        private boolean validateHWUp(int x, int y, Random randRange){
            int newX = x;
            int newY = y;

            for (int i = x; i < x + 3; i++){
                if (!inBoundsX(i)){
                    return false;
                }
            }
            rangeX = x + 3;
            for (int j = y; j < HEIGHT; j++){
                newY = j;
            }
            if (newY - y > 3){
                rangeY = randRange.nextInt(y + 3, Math.min(newY + 1, y + 9));
            }
            return newY - y >= 3;
        }

    private boolean validateHWRight(int x, int y, Random randRange){
        int newX = x;
        int newY = y;

        for (int i = y; i < y + 3; i++){
            if (!inBoundsX(i)){
                return false;
            }
        }
        rangeY = y + 3;
        for (int j = x; j < WIDTH; j++){
            newX = j;
        }
        if (newX - x > 3){
            rangeX = randRange.nextInt(x + 3, Math.min(newX + 1, x + 15));
        }
        return newX - x >= 3;
    }


        private String chooseHWDir(){
            Random direction = new Random();
            int dir = direction.nextInt(2);
            return switch (dir) {
                case 0 -> "up";
                case 1 -> "sideways";
                default -> "";
            };
        }


    private boolean inBoundsX(int x){
        return x >= 0 && x < WIDTH;
    }

    private boolean inBoundsY(int y){
        return y >= 0 && y < HEIGHT;
    }

    public int getXRange(){
        return rangeX;
    }

    public int getYRange(){
        return rangeY;
    }

}
