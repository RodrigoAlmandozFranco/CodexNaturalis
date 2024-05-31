package it.polimi.ingsw.am42.view.tui;

import it.polimi.ingsw.am42.network.Client;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



/**
 * A utility class for handling input and output operations.
 * This class provides methods for reading strings, integers, and boolean values
 * from the standard input, and for printing messages to the standard output.
 *
 * @author Tommaso Crippa
 */
public class IOHandler {

    private final Scanner in;
    private final PrintStream out;

    /**
     * Constructor for the class, initializes variables
     */
    public IOHandler() {
        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
    }


    /**
     * Prints the specified string to the standard output.
     *
     * @param s the string to be printed
     */
    public void print(String s) {
        out.println(s);
    }


    /**
     * Prints the specified integer to the standard output.
     *
     * @param i the integer to be printed
     */
    public void print(int i) {
        out.println(i);
    }


    /**
     * Asks the user a question and reads an integer from the
     * standard input.
     *
     * @param question the question to be displayed to the user
     * @return the integer value read from the input
     */
    public int getInt(String question) {
        print(question);
        return getInt();
    }

    /**
     * Reads an integer from the standard input.
     * If the input is not a valid integer, prompts the user to insert a valid
     * integer and tries again.
     *
     * @return the integer value read from the input
     */
    public int getInt() {
        int i;
        try {
            i = Integer.parseInt(getString());
        } catch (NumberFormatException e) {
            print("Insert an integer");
            i = getInt();
        }
        return i;
    }


    /**
     * Asks the user a question and reads a string from the
     * standard input.
     *
     * @param question the question to be displayed to the user
     * @return the string value read from the input
     */
    public String getString(String question) {
        print(question);
        return getString();
    }


    /**
     * Reads a non-empty string from the standard input.
     * Waits until a valid (non-empty) string is entered.
     *
     * @return the string value read from the input
     */
    public String getString() {
        String s = null;
        while (s == null || s.isEmpty()) {
            if(in.hasNextLine())
                s = in.nextLine();
            else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return s;
    }

    /**
     * Prompts the user with the specified question and reads a boolean value from
     * the standard input.
     * Expects "y" or "n" (or variations) as input.
     *
     * @param question the question to be displayed to the user
     * @return the boolean value read from the input
     */
    public boolean getBoolean(String question) {
        print(question + " (y/n)");
        return getBoolean();
    }

    /**
     * Reads a boolean value from the standard input.
     * Expects "y" or "n" (or variations) as input. Keeps prompting until a valid
     * input is provided.
     *
     * @return the boolean value read from the input
     */
    public boolean getBoolean() {
        String c = in.nextLine();
        List<String> yes = Arrays.asList("y", "yes", "si", "oui", "duh", "1");
        List<String> no = Arrays.asList("n", "no", "non", "bah", "0");
        while (!yes.contains(c.toLowerCase()) && !no.contains(c.toLowerCase())) {
            print("(y/n)");
            c = in.nextLine();

        }
        if (yes.contains(c.toLowerCase()))
            return true;
        return false;
    }

}
