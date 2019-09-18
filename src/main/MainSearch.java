package main;

import gui.Display;
import problem.Intersection;
import searches.general.GraphSearch;
import util.Logger;
import util.ProgressBar;
import util.Props;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import static util.PrintFormatting.print;

public abstract class MainSearch {

    static final Logger L = new Logger("log.txt");

    Display dis;
    private State state = State.READY;
    private String coordinatesFilename, connectionsFilename, extension;
    private int lastIndex, currentIndex, totalFiles;
    private int runningAlg;
    GraphSearch[] algs;

    public abstract void instantiateAlgsArray();

    void boot() {
        try {
            Props.load("assets/config.props");
            Props.load("assets/screen.props");
        }
        catch (FileNotFoundException e) {
            L.log(e);
        }

        coordinatesFilename = Props.getString("coordinates filename");
        connectionsFilename = Props.getString("connections filename");
        extension = Props.getString("file extension");
        lastIndex = (int) Props.getLong("last index");
        currentIndex = (int) Props.getLong("first index");
        if (currentIndex > lastIndex) {
            String msg = "Value of 'fist index' cannot be less than 'last index'. Please, check the properties file.";
            L.log(new IllegalArgumentException(msg));
            print(msg);
            L.close();
            return;
        }

        totalFiles = lastIndex - currentIndex + 1;
        dis = new Display(this);

        nextStep();
    }

    private void loadNextFile() {
        System.out.print(ProgressBar.formatBar(totalFiles - lastIndex + currentIndex - 1, totalFiles));
        if (currentIndex > lastIndex) {
            state = State.DONE;
            print("");
            dis.setButtonText("All files processed! Press to exit.");
            return;
        }

        Intersection.clear();
        InputReader.readCoordinates(coordinatesFilename + currentIndex + extension);
        InputReader.readConnections(connectionsFilename + currentIndex + extension);
        instantiateAlgsArray();
        runningAlg = 0;
        state = State.SEARCHING;
        dis.setButtonText("Start");
        dis.clear();
        currentIndex++;
    }

    private void checkProgress() {
        // If all algs have been run, ready for next file.
        if (runningAlg >= algs.length) {
            state = State.READY;
            dis.setButtonText("Load next file");
        }
        else dis.setButtonText("Next algorithm");
    }

    private void keepSearching() {
        GraphSearch alg = algs[runningAlg];

        if (alg.isSearching()) {
            dis.setButtonText("Next");
            alg.nextStep();
            return;
        }

        LinkedList<Intersection> path = alg.backTrack();
        dis.showPath(path);
        runningAlg++;

        checkProgress();
    }

    public void nextStep() {
        switch (state) {
            case READY:
                loadNextFile();
                break;
            case DONE:
                L.close();
                dis.dispose();
                return;
            case SEARCHING:
                keepSearching();
                break;
        }
    }

}
