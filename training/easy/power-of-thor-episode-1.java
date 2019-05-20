import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int lightX = in.nextInt(); // the X position of the light of power
        int lightY = in.nextInt(); // the Y position of the light of power
        int initialTX = in.nextInt(); // Thor's starting X position
        int initialTY = in.nextInt(); // Thor's starting Y position

        // game loop
        while (true) {
            int remainingTurns = in.nextInt(); // The remaining amount of turns Thor can move. Do not remove this line.
            
            String direction = "";
            
            if (initialTY < lightY) {
                direction += "S";
                initialTY++;
            } else if (initialTY > lightY) {
                direction += "N";
                initialTY--;
            }
            if (initialTX < lightX) {
                direction += "E";
                initialTX++;
            } else if (initialTX > lightX) {
                direction += "W";
                initialTX--;
            }
            
            System.out.println(direction);
        }
    }
}
