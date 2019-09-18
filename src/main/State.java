package main;

public enum State {
    READY,  // When MainSearch is ready to run algorithms on the next file.
    SEARCHING,  // When MainSearch is actively using an algorithm.
    DONE,  // When MainSearch is done with all files.
}
