import java.util.Scanner;
import static java.util.stream.Collectors.joining;

class Solution {

    public static void main(final String args[]) {
        final Scanner in = new Scanner(System.in);
        final String MESSAGE = in.nextLine();

        System.err.println(MESSAGE);
        String data = toBinary(MESSAGE);
        System.err.println(data);
        printChuckNorris(data);
    }

    static String toBinary(String message) {
        return message.chars()
                .mapToObj(Integer::toBinaryString)
                .map(s -> String.format("%7s", s))
                .map(s -> s.replace(" ", "0"))
                .collect(joining(""));
    }
    
    static void printChuckNorris(String data) {
        for (int i = 0; i < data.length(); i++) {
            System.out.print(data.charAt(i) == '1' ? "0 0" : "00 0");
            for (int j = i + 1; j < data.length() && data.charAt(i) == data.charAt(j); i++, j++) {
                System.out.print("0");
            }
            if (i < data.length() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

}
