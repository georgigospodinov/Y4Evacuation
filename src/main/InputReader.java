package main;

import problem.Intersection;
import util.Props;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static main.MainSearch.L;

class InputReader {
    private static final String SEPARATOR = Props.getString("separator");
    private static final String LINE_START = Props.getString("line start");
    private static final String LINE_END = Props.getString("line end");

    // Codes:
    private static final long THIS = Props.getLong("this intersection code");
    private static final long CONNECTED = Props.getLong("connected code");
    private static final long NOT_CONNECTED = Props.getLong("not connected code");
    private static final long INIT_CODE = Props.getLong("init code");
    private static final long GOAL_CODE = Props.getLong("goal code");

    private static String[] getParts(String line) {
        String clearLine = line.trim();
        clearLine = clearLine.substring(LINE_START.length());
        int l = clearLine.length();
        clearLine = clearLine.substring(0, l - LINE_END.length());
        String[] parts = clearLine.split(SEPARATOR);
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            parts[i] = part.trim();
        }

        return parts;
    }

    private static void parseCoordinates(String line) {
        String[] coordinates = getParts(line);
        try {
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            Intersection.createIntersection(x, y);
        }
        catch (NumberFormatException e) {
            L.log(e);
        }
    }

    static void readCoordinates(String filename) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException e) {
            L.log(e);
            return;
        }

        reader.lines().forEach(InputReader::parseCoordinates);

        try {
            reader.close();
        }
        catch (IOException e) {
            L.log(e);
        }
    }

    private static void parseCode(long v, Intersection i, int index) {
        if (v == CONNECTED)
            i.connect(Intersection.get(index));
        else if (v == INIT_CODE)
            Intersection.setInit(i);
        else if (v == GOAL_CODE)
            Intersection.setGoal(i);
        else if (v != (THIS) && v != (NOT_CONNECTED))
            L.log("Could not recognise code \"" + v + "\" for intersection " + i + ".");
    }

    private static void parseCodes(String line, Intersection intersection) {
        String[] vals = getParts(line);
        for (int i = 0; i < vals.length; i++) {
            long v;
            try {
                v = Long.parseLong(vals[i]);
            }
            catch (NumberFormatException e) {
                L.log(e);
                continue;
            }
            parseCode(v, intersection, i);
        }
    }

    static void readConnections(String filename) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException e) {
            L.log(e);
            return;
        }

        Intersection.forEach(intersection -> {
            try {
                parseCodes(reader.readLine(), intersection);
            }
            catch (IOException e) {
                L.log(e);
            }
        });


        try {
            reader.close();
        }
        catch (IOException e) {
            L.log(e);
        }
    }
}
