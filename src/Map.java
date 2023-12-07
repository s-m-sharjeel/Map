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
    private Image pointer;

    private float time;

    public Map() {
        initMap();
    }

    /**
     * initializes the entire map, including cities and boundaries
     * O(E * V), where V and E are total number of vertices and edges respectively
     */
    private void InitializeAssets() {

        constructGraph();
        constructRegions();

        ImageIcon icon = new ImageIcon("./src/location.png");
        pointer = icon.getImage();

    }

    /**
     * constructs the graph from the data file
     * O(E * V), where V and E are total number of vertices and edges respectively
     */
    private void constructGraph() {

        // no. of cities for which the data is available
        int size = 73;
        pakistan = new Graph(size);

        try{

            File file = new File("./src/data.csv");
            Scanner input1 = new Scanner(file);
            input1.nextLine();

            // O(V)

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

            // O(E), where E is the total number of edges

            for (int i = 0; i < size; i++) {

                Vertex vertex = pakistan.getVertices()[i];
                String line = input2.nextLine();
                String[] data = line.split(",");

                for (int j = 3; j < data.length; j++)
                    pakistan.addEdge(vertex.getCity().getName(), data[j].trim());   // O(V)

            }

            input2.close();

        } catch (FileNotFoundException e){
            System.out.println("File not found!");
        }

    }

    /**
     * constructs the regions from the data file
     * O(n), where n is the total number of coordinates
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
            System.out.println("File not found!");
        }


    }

    /**
     * O(E * V), where V and E are total number of vertices and edges respectively
     */
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

    /**
     * repaints the panel | O(V)
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        time += 0.06F;

        setBackground(dark_green);

        drawBoundary(g);
        drawPath(g);
        drawCities(g);
        paintText(g);
        drawPointers(g);

    }

    /**
     * paints text on the panel | O(1)
     * @param g is the graphics
     */
    private void paintText(Graphics g) {

        String font_style = Font.SERIF;
        Color font_color = Color.white;
        Color shadow = Color.black;

        // for shadow effect
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
        if (toVertex != null && time > path.size)
            str += " " + toVertex.getShortestDistance() + " km";
        else str += " \t-";

        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 230);

    }

    /**
     * draws the boundary of pakistan and the regions with a separate color for each | O(n)
     * @param g is the graphics
     */
    private void drawBoundary(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));

        g.setColor(new Color(0, 100, 255));
        g.fillPolygon(regions[0]);

        g.setColor(Color.yellow);
        g.fillPolygon(regions[1]);

        g.setColor(Color.magenta);
        g.fillPolygon(regions[2]);

        g.setColor(Color.cyan);
        g.fillPolygon(regions[3]);

        g.setColor(Color.orange);
        g.fillPolygon(regions[4]);

        g.setColor(Color.pink);
        g.fillPolygon(regions[5]);

        g.setColor(light_green);
        g.fillPolygon(regions[6]);

        g.setColor(new Color(255, 100, 100));
        g.fillPolygon(regions[7]);

        g2.setStroke(new BasicStroke(1));

    }

    /**
     * draws all the cities | O(V)
     * @param g is the graphics
     */
    private void drawCities(Graphics g) {

        for (Vertex v : pakistan.getVertices())
            v.getCity().getButton().draw(g);

    }

    /**
     * draws a line between two cities | O(1)
     *
     * @param g  is the graphics
     * @param c1 is the first city
     * @param c2 is the second city
     */
    private void drawLine(Graphics g, City c1, City c2, float i){

        if (c1 == null || c2 == null)
            return;

        int fromX = c1.getButton().getX();
        int fromY = c1.getButton().getY();
        int toX = c2.getButton().getX();
        int toY = c2.getButton().getY();

        float progress = time - i;
        Point endPoint;

        if (progress > 1)
            endPoint = interpolate(new Point(fromX, fromY), new Point(toX, toY), 1);
        else endPoint = interpolate(new Point(fromX, fromY), new Point(toX, toY), time % 1);

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.red);
        g2.drawLine(fromX, fromY, endPoint.x, endPoint.y);

        g2.setStroke(new BasicStroke(1));

    }

    /**
     * finds the shortest path between the source and destination city
     * O(E * logV), where V and E are the total number of vertices and edges respectively
     */
    private void getPath() {

        if (fromVertex != null && toVertex != null) {

            for (Vertex vertex : pakistan.getVertices())
                vertex.resetVertex();

            path = Dijkstra.getShortestPath(fromVertex, toVertex);

        }

    }

    /**
     * draws the complete shortest path between the source and destination city | O(n)
     * @param g is the graphics
     */
    private void drawPath(Graphics g) {

        if (fromVertex == null || toVertex == null || path == null) {
            time = 0;
            return;
        }

        Node<Vertex> current = path.head;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        float i = 0;
        int j = 0;

        while (current != null && current.next != null) {
            City c1 = current.data.getCity();
            City c2 = current.next.data.getCity();
            if (i < time) {
                drawLine(g, c1, c2, i);
                writePathDetails(g, c1, c2, (int)i);
            }
            current = current.next;
            i++;
        }

        g2.setStroke(new BasicStroke(1));

    }

    /**
     * writes the path details between two cities
     * @param g is the graphics
     * @param c1 is the source city
     * @param c2 is the destination city
     * @param i is the city number
     */
    private void writePathDetails(Graphics g, City c1, City c2, int i) {

        g.setColor(Color.white);

        String str = c1.getName() + " -> " + c2.getName() + " | " + getDistance(c1, c2) + " km";
        g.setColor(Color.white);
        g.drawString(str, 3 * B_WIDTH / 4 - g.getFontMetrics().stringWidth(str) / 2 - 50, 300 + 30 * i);

    }

    /**
     * draws the location markers | O(1)
     * @param g is the graphics
     */
    private void drawPointers(Graphics g) {

        if (fromVertex != null)
            g.drawImage(pointer, fromVertex.getCity().getButton().getX() - pointer.getWidth(this)/2, fromVertex.getCity().getButton().getY() - pointer.getHeight(this), this);

        if (toVertex != null)
            g.drawImage(pointer, toVertex.getCity().getButton().getX() - pointer.getWidth(this)/2, toVertex.getCity().getButton().getY() - pointer.getHeight(this), this);

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
                v.getCity().getButton().setPressed(false);
            }
            fromVertex = null;
            toVertex = null;
            path = null;
        }

        // setting the source city as the one pressed (if any)
        if (fromVertex == null){
            for (Vertex v : pakistan.getVertices()) {
                v.getCity().getButton().isClicked(x, y);
                if (v.getCity().getButton().isPressed()) {
                    fromVertex = v;
                    break;
                }
            }

        } else {
            // setting the destination
            for (Vertex v : pakistan.getVertices()) {
                City city = v.getCity();
                city.getButton().isClicked(x, y);
                if (city.getButton().isPressed() && !v.equals(fromVertex)) {
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
            v.getCity().getButton().isHovered(e.getX(), e.getY());

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
     * calculates the distance between two cities assuming we reside on the equator
     * <a href="https://www.thoughtco.com/degree-of-latitude-and-longitude-distance-4070616">...</a>
     * @param c1 is the city1
     * @param c2 is the city2
     * @return the distance in kms
     */
    public static float getDistance(City c1, City c2) {

        // https://www.thoughtco.com/degree-of-latitude-and-longitude-distance-4070616

        double lng = (Math.abs(c1.getLng() - c2.getLng())*111.321);
        double lat = (Math.abs(c1.getLat() - c2.getLat())*68.703);

        return (float) Math.sqrt((lng*lng + lat*lat));

    }

    /**
     * linearly interpolate between two points at t percent
     * @param p1 is the initial point
     * @param p2 is the final point
     * @param t is the percentage
     * @return the interpolated point
     */
    private Point interpolate(Point p1, Point p2, float t) {

        int x = lerp(p1.x, p2.x, t);
        int y = lerp(p1.y, p2.y, t);

        return new Point(x, y);

    }

    /**
     * returns a value at t percent between the init and final value (linear interpolation)
     */
    private int lerp(int z1, int z2, float t){
        return z1 + (int)((z2 - z1) * t);
    }
}