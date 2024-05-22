package it.polimi.ingsw.am42.view.tui;

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
            i = Integer.parseInt(getString());
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

    public boolean getBoolean(String question) {
        print(question + " (y/n)");
        return getBoolean();
    }

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
