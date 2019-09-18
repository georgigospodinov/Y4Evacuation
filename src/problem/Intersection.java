package problem;

import util.Props;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

import static java.lang.Math.sqrt;

public class Intersection {

    private static final ArrayList<Intersection> INTERSECTION_ARRAY = new ArrayList<>();
    public static Intersection init, goal;
    private static final char FIRST_ID = Props.getString("first intersection id").charAt(0);
    private static char currentID = FIRST_ID;

    public static void setInit(Intersection i) {
        if (init == null) init = i;
    }

    public static void setGoal(Intersection g) {
        if (goal == null) goal = g;
    }

    static double euclidean(Intersection a, Intersection b) {
        int xd = a.x - b.x;
        xd *= xd;
        int yd = a.y - b.y;
        yd *= yd;
        return sqrt(xd + yd);
    }

    static double manhattan(Intersection a, Intersection b) {
        int xd = Math.abs(a.x - b.x);
        int yd = Math.abs(a.y - b.y);
        return xd + yd;
    }

    static double chebyshev(Intersection a, Intersection b) {
        int xd = Math.abs(a.x - b.x);
        int yd = Math.abs(a.y - b.y);
        return xd > yd ? xd : yd;
    }

    public static void clear() {
        INTERSECTION_ARRAY.clear();
        init = null;
        goal = null;
        currentID = FIRST_ID;
    }

    public static void createIntersection(int x, int y) {
        Intersection i = new Intersection(x, y, currentID);
        INTERSECTION_ARRAY.add(i);
        currentID++;
    }

    public static Intersection get(int index) {
        return INTERSECTION_ARRAY.get(index);
    }

    public static void forEach(Consumer<? super Intersection> action) {
        INTERSECTION_ARRAY.forEach(action);
    }

    public final int x, y;
    public final char id;
    private final LinkedList<Intersection> connected = new LinkedList<>();

    public boolean isGoal() {
        return this.id == goal.id;
    }

    public boolean isInit() {
        return this.id == init.id;
    }

    public void connect(Intersection other) {
        connected.addLast(other);
    }

    private Intersection(int x, int y, char id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void forEachConnected(Consumer<? super Intersection> action) {
        connected.forEach(action);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Intersection))
            return false;

        Intersection other = (Intersection) obj;
        return this.id == other.id;
    }

}
