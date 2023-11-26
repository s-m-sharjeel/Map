import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map extends JPanel implements ActionListener , MouseInputListener {

    private static final int B_WIDTH = 760 * 2;
    private static final int B_HEIGHT = 760;

    private Graph pakistan;
    private Polygon[] regions;
    private Vertex fromVertex;
    private Vertex toVertex;
    private final Color dark_green = new Color(0, 64, 26);
    private final Color light_green = new Color(121, 190, 88);
    private LinkedList<Vertex> path;

    public Map() {
        initMap();
    }

    /**
     * initializes the cities by reading the file
     */
    private void InitializeAssets() {

        constructGraph();
        constructRegions();

    }

    /**
     * constructs the graph from the data file
     */
    private void constructGraph() {

        // no. of cities for which the data is available
        int size = 73;
        pakistan = new Graph(size);

        try{

            File file = new File("./src/data.csv");
            Scanner input1 = new Scanner(file);

            input1.nextLine();

            for (int i = 0; i < size; i++) {
                String line = input1.nextLine();
                String[] data = line.split(",");

                City city = (new City(data[0], Float.parseFloat(data[1]), Float.parseFloat(data[2])));

                Vertex vertex = new Vertex(city);
                vertex.setSize(data.length - 3);
                pakistan.addVertex(vertex);
            }

            input1.close();

            Scanner input2 = new Scanner(file);

            input2.nextLine();

            // for each city
            for (int i = 0; i < size; i++) {

                Vertex vertex = pakistan.getVertices()[i];
                String line = input2.nextLine();
                String[] data = line.split(",");

                for (int j = 3; j < data.length; j++)
                    pakistan.addEdge(vertex.getCity().getName(), data[j].trim());

            }

            input2.close();

        } catch (Exception e){
            System.out.println("File not found!");
        }

    }

    /**
     * constructs the regions from the data file
     */
    private void constructRegions() {

        File file = new File("./src/pakgeojson.wkt");
        int size = 8;

        regions = new Polygon[size];
        String data;

        try {
            Scanner input = new Scanner(file);
            for (int i = 0; i < regions.length; i++) {
                input.next();
                data = input.nextLine();
                String[] coordinates = data.split(",");
                int points = coordinates.length;

                int[] x = new int[points];
                int[] y = new int[points];

                for (int j = 0; j < points; j++) {
                    coordinates[j] = coordinates[j].trim();
                    String[] arr = coordinates[j].split(" ");
                    float lng = Float.parseFloat(arr[0].trim());
                    float lat = Float.parseFloat(arr[1].trim());
                    x[j] = getXFromLNG(lng);
                    y[j] = getYFromLAT(lat);
                }

                regions[i] = new Polygon(x, y, points);

            }

            input.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private void initMap() {

        /*

         for city coordinates:
         https://simplemaps.com/data/world-cities

         for verification of neighbouring cities
         https://data.humdata.org/dataset/a64d1ff2-7158-48c7-887d-6af69ce21906

         for pakistan boundary:
         https://cartographyvectors.com/map/998-pakistan-detailed-boundary

         */

        InitializeAssets();

        addMouseListener( this );
        addMouseMotionListener( this );
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setFocusable(true);

        int DELAY = 10;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(dark_green);

        drawBoundary(g);
        drawRoads(g);
        drawPath(g);
        drawCities(g);
        paintText(g);

    }

    /**
     * paints text on the panel
     * @param g is the graphics
     */
    private void paintText(Graphics g) {

        String font_style = Font.SERIF;
        Color font_color = Color.white;
        Color shadow = Color.black;
        int gap = 4;

        g.setFont(new Font(font_style, Font.BOLD, 40));
        String str = "PAKISTAN";
        g.setColor(shadow);
        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - gap - 50, 100 + gap);
        g.setColor(font_color);
        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 100);

        g.setFont(new Font(font_style, Font.PLAIN, 20));

        str = "From:";
        if (fromVertex != null)
            str += " " + fromVertex;
        else str += " \t-";

        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 170);

        str = "To:";
        if (toVertex != null)
            str += " " + toVertex;
        else str += " \t-";

        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 200);

        str = "Distance:";
        if (toVertex != null)
            str += " " + toVertex.getShortestDistance() + " km";
        else str += " \t-";

        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 230);

    }

    /**
     * draws the boundary of pakistan
     * @param g is the graphics
     */
    private void drawBoundary(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));

        for (Polygon region : regions) {
            g.setColor(light_green);
            g.fillPolygon(region);
            g.setColor(Color.black);
            g.drawPolygon(region);
        }

        g2.setStroke(new BasicStroke(1));

    }

    /**
     * draws all the cities
     * @param g is the graphics
     */
    private void drawCities(Graphics g) {

        for (Vertex v : pakistan.getVertices()) {
            v.getCity().draw(g);
        }

    }

    /**
     * draws the edges of the graph (roads)
     * @param g is the graphics
     */
    private void drawRoads(Graphics g) {

        for (Vertex v1 : pakistan.getVertices()) {
            for (Vertex v2: v1.getAdjacentVertices()) {
                drawLine(g, v1.getCity(), v2.getCity(), Color.black);
            }
        }

    }

    /**
     * draws a line between two cities
     * @param g is the graphics
     * @param c1 is the first city
     * @param c2 is the second city
     * @param color is the color of the line
     */
    private void drawLine(Graphics g, City c1, City c2, Color color){

        if (c1 == null || c2 == null)
            return;

        int fromX = c1.getX();
        int fromY = c1.getY();
        int toX = c2.getX();
        int toY = c2.getY();

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2));
        g2.setColor(color);
        g2.drawLine(fromX, fromY, toX, toY);
        g2.setStroke(new BasicStroke(1));

    }

    /**
     * finds the shortest path between the source and destination city
     */
    private void getPath() {

        if (fromVertex != null && toVertex != null) {

            for (Vertex vertex : pakistan.getVertices())
                vertex.resetNode();
            path = Dijkstra.getShortestPath(fromVertex, toVertex);

        }

    }

    /**
     * draws the complete shortest path between the source and destination city
     * @param g is the graphics
     */
    private void drawPath(Graphics g) {

        if (fromVertex == null || toVertex == null || path == null) {
            path = null;
            return;
        }

        g.setColor(Color.white);
        for (int i = 0; i < path.size() - 1; i++)
            drawLine(g, path.get(i).getCity(), path.get(i + 1).getCity(), Color.white);

        writePathDetails(g);

    }

    /**
     * writes the sub-paths along with their distances on the right
     * @param g is the graphics
     */
    private void writePathDetails(Graphics g) {

        g.setColor(Color.white);
        for (int i = 0; i < path.size() - 1; i++) {
            City c1 = path.get(i).getCity();
            City c2 = path.get(i + 1).getCity();
            String str = c1.getName() + " -> " + c2.getName() + " | " + getDistance(c1, c2) + " km";
            g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 300 + 30*i);
        }


    }

    /**
     * monitors the mouse presses (click + release)
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        // resetting if none of the cities has been selected
        if (fromVertex != null && toVertex != null){
            for (Vertex v : pakistan.getVertices()) {
                v.getCity().setPressed(false);
            }
            fromVertex = null;
            toVertex = null;
        }

        // setting the source city as the one pressed (if any)
        if (fromVertex == null){
            for (Vertex v : pakistan.getVertices()) {
                v.getCity().isClicked(x, y);
                if (v.getCity().isPressed()) {
                    fromVertex = v;
                    break;
                }
            }

        } else {
            // setting the destination
            for (Vertex v : pakistan.getVertices()) {
                City city = v.getCity();
                city.isClicked(x, y);
                if (city.isPressed() && !v.equals(fromVertex)) {
                    toVertex = v;
                    break;
                }
            }
        }

        getPath();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().sync();
        // the gui is repainted everytime any action is performed
        repaint();
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {

        // displays the name of the city when hovered

        for (Vertex v : pakistan.getVertices())
            v.getCity().isHovered(e.getX(), e.getY());

	}

    /**
     * finds the x value on the GUI corresponding to the longitude
     * @param lng is the longitude of the map-coordinates
     * @return the x-value
     */
    public static int getXFromLNG(float lng){
        return (Math.round(lng*40)) - 2375;
    }

    /**
     * finds the y value on the GUI corresponding to the latitude
     * @param lat is the latitude of the map-coordinates
     * @return the y-value
     */
    public static int getYFromLAT(float lat){
        return B_HEIGHT + 1150 - (Math.round(lat*50));
    }

    /**
     * calculates the distance between two cities
     * @param c1 is the city1
     * @param c2 is the city2
     * @return the distance in kms
     */
    public static float getDistance(City c1, City c2) {

        double lng = (Math.abs(c1.getLng() - c2.getLng())*111.321);
        double lat = (Math.abs(c1.getLat() - c2.getLat())*68.703);

        return (float) Math.sqrt((lng*lng + lat*lat));

    }
}