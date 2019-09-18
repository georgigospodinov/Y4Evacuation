package gui;

import main.MainSearch;
import problem.Intersection;
import problem.Node;
import searches.general.GraphSearch;
import util.Props;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;

public class Display extends JFrame {

    private static final int windowWidth, windowHeight;

    private static int getNumProp(String x, int def) {
        Integer w = null;
        try {
            w = (int) Props.getLong(x);
        }
        catch (NullPointerException ignored) {
        }
        try {
            w = (int) (def * Props.getDouble(x));
        }
        catch (NullPointerException ignored) {
        }

        return w != null ? w : def;
    }

    static {
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        windowWidth = getNumProp("window width", screen.width);
        windowHeight = getNumProp("window height", screen.height);
    }

    //<editor-fold desc="Component configuration">
    private void configWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension window = new Dimension(windowWidth, windowHeight);
        setMinimumSize(window);
        getContentPane().setLayout(null);
    }

    private final Graph map = new Graph();

    private void configMap() {
        int x = (int) (windowWidth * Props.getDouble("map x"));
        int y = (int) (windowHeight * Props.getDouble("map y"));
        int width = (int) (windowWidth * Props.getDouble("map width"));
        int height = (int) (windowHeight * Props.getDouble("map height"));
        map.setLocation(x, y);
        map.setSize(width, height);
        getContentPane().add(map);

    }

    private final JButton button = new JButton();

    private void configButton() {
        button.setFocusable(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Cantarell", Font.PLAIN, 18));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setText("Next");

        int x = (int) (windowWidth * Props.getDouble("button x"));
        int y = (int) (windowHeight * Props.getDouble("button y"));
        int width = (int) (windowWidth * Props.getDouble("button width"));
        int height = (int) (windowHeight * Props.getDouble("button height"));
        button.setLocation(x, y);
        button.setSize(width, height);
        getContentPane().add(button);
    }

    private final JLabel state = new JLabel();

    private void configState() {
        state.setFocusable(false);
        state.setFont(new Font("Cantarell", Font.PLAIN, 18));
        state.setHorizontalAlignment(SwingConstants.CENTER);
        state.setVerticalAlignment(SwingConstants.CENTER);
        state.setHorizontalTextPosition(SwingConstants.CENTER);
        state.setVerticalTextPosition(SwingConstants.CENTER);

        int x = (int) (windowWidth * Props.getDouble("state x"));
        int y = (int) (windowHeight * Props.getDouble("state y"));
        int width = (int) (windowWidth * Props.getDouble("state width"));
        int height = (int) (windowHeight * Props.getDouble("state height"));
        state.setLocation(x, y);
        state.setSize(width, height);

        formatStatus("", ' ', "", "", 0);
        getContentPane().add(state);
    }
    //</editor-fold>

    //<editor-fold desc="Status updating">
    public static final String BREAK = "<br/><br/>";

    private void formatStatus(String algName, char current, String frontier, String visited, double hValue) {
        String details = "<html>" +
                "Algorithm: " + algName + BREAK +
                "Current: " + current + BREAK +
                "Frontier: " + frontier + BREAK +
                "Visited: " + visited + BREAK;
        details += String.format("Heuristic value: %.2f%s</html>", hValue, BREAK);
        state.setText(details);
    }

    public void updateStatus(GraphSearch s) {
        String algName = s.getClass().getSimpleName();
        Node n = s.getLastNode();
        String frontier = s.stringFrontier();
        String visited = s.stringVisited();
        char id = n != null ? n.intersection.id : ' ';
        formatStatus(algName, id, frontier, visited, s.heuristic(n));

        Intersection i = n != null ? n.intersection : null;
        map.setCurrent(i);
        map.setVisited(s.getVisited());
        map.setFrontier(s.getFrontierIntersections());
        map.setSolution(null);
    }

    public void showPath(LinkedList<Intersection> path) {
        StringBuilder solution = new StringBuilder(state.getText());
        String suffix = "</html>";
        int index = solution.indexOf(suffix);
        solution.delete(index, solution.length());

        solution.append("Solution: ");
        if (path == null) {
            solution.append("none");
        }
        else {
            path.forEach(i -> solution.append(i.id).append(", "));
            solution.deleteCharAt(solution.length() - 1);
            solution.deleteCharAt(solution.length() - 1);
        }
        solution.append("!").append(BREAK).append(suffix);

        state.setText(solution.toString());
        map.setSolution(path);
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void clear() {
        formatStatus("", ' ', "", "", 0);
        map.setFrontier(null);
        map.setVisited(null);
        map.setCurrent(null);
        map.setSolution(null);
    }
    //</editor-fold>

    //<editor-fold desc="Interactiveness">
    private MainSearch search;

    private void nextClick() {
        button.addActionListener(event -> search.nextStep());
    }

    private void keyPress() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_ESCAPE:
                        System.exit(0);
                        break;
                    case VK_ENTER:
                        search.nextStep();
                        break;
                }
            }
        });
    }
    //</editor-fold>

    public Display(MainSearch search) {
        this.search = search;

        configWindow();
        getContentPane().setLayout(null);

        configMap();
        configButton();
        configState();
        pack();

        keyPress();
        nextClick();
        setLocationRelativeTo(null);
        setTitle("AI Search Algorithms");
        setVisible(true);
    }

}
