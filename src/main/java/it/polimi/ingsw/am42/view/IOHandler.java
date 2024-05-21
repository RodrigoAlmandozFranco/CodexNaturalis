package it.polimi.ingsw.am42.view;

import it.polimi.ingsw.am42.network.Client;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IOHandler {

    private final Scanner in;
    private final PrintStream out;


    public IOHandler() {
        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);
    }

    public void print(String s) {
        out.println(s);
    }

    public void print(int i) {
        out.println(i);
    }

    public int getInt(String question) {
        print(question);
        return getInt();
    }

    public int getInt() {
        int i;
        try {
            i = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            print("Insert an integer");
            i = getInt();
        }
        return i;
    }

    public String getString(String question) {
        print(question);
        return getString();
    }

    public String getString() {
        String s = in.nextLine();
        while (s == null || s.isEmpty()) {
            s = in.nextLine();
        }
        return s;
    }

    public boolean getBoolean(String question) {
        print(question + " (y/n)");
        return getBoolean();
    }

    public boolean getBoolean() {
        String c = in.nextLine();
        List<String> yes = Arrays.asList("y", "yes", "si", "oui", "duh");
        List<String> no = Arrays.asList("n", "no", "non", "bah");
        while (!yes.contains(c.toLowerCase()) && !no.contains(c.toLowerCase())) {
            print("(y/n)");
            c = in.nextLine();

        }
        if (yes.contains(c.toLowerCase()))
            return true;
        return false;
    }

}
