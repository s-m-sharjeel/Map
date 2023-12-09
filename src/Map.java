import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map extends JPanel implements ActionListener, MouseInputListener, KeyListener {

    private static final int B_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int B_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private Graph pakistan;
    private Polygon[] regions;
    private Vertex fromVertex;
    private Vertex toVertex;
    private final Color dark_green = new Color(0, 64, 26);
    private final Color light_green = new Color(121, 190, 88);
    private Color[] bg_colors;
    private Color bg_color;
    private int bg_color_selected;
    private Color font_color;
    private String font_style;
    private LinkedList<Vertex> path;
    private Image pointer;

    private float animation_counter;
    private float animation_speed;

    public Map() {
        initMap();
    }

    /**
     * O(E * V), where V and E are total number of vertices and edges respectively
     */
    private void initMap() {

        initializeAssets();

        addMouseListener( this );
        addMouseMotionListener( this );
        addKeyListener( this );
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setFocusable(true);

        int DELAY = 0;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * initializes the entire map, including cities and boundaries
     * O(E * V), where V and E are total number of vertices and edges respectively
     */
    private void initializeAssets() {

        constructGraph();
        constructRegions();

        // quick sorting in O(V*logV)
        pakistan.sort();

        // initializing the pointer icon to mark a selected city
        ImageIcon icon = new ImageIcon("./src/location.png");
        pointer = icon.getImage();

        // initializing the colors and fonts
        bg_colors = new Color[]{Color.white, Color.black, dark_green};
        bg_color_selected = 2;
        bg_color = bg_colors[bg_color_selected];
        font_color = Color.white;
        font_style = Font.DIALOG_INPUT;

        // initializing animation speed
        animation_speed = 0.075F;

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
     * repaints the panel | O(V)
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        setBackground(bg_color);

        drawBoundary(g);
        drawPath(g);
        drawCities(g);
        writeText(g);
        drawPointers(g);

    }

    /**
     * paints the general text on the panel | O(1)
     * @param g is the graphics
     */
    private void writeText(Graphics g) {

        Color shadow = Color.gray;

        // for shadow effect
        int gap = 2;

        g.setFont(new Font(font_style, Font.BOLD, 40));
        String str = "PAKISTAN";
        g.setColor(shadow);
        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - gap - 50, 100 + gap);
        g.setColor(font_color);
        g.drawString(str, 3 * B_WIDTH/4 - g.getFontMetrics().stringWidth(str)/2 - 50, 100);

        g.setFont(new Font(font_style, Font.BOLD, 25));

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

    }

    /**
     * draws the boundary of pakistan and the regions with a separate color for each | O(n)
     * @param g is the graphics
     */
    private void drawBoundary(Graphics g) {

        // kashmir
        g.setColor(new Color(0, 100, 255));
        g.fillPolygon(regions[0]);

        // balochistan
        g.setColor(Color.yellow);
        g.fillPolygon(regions[1]);

        // federally administered tribal areas (FATA)
        g.setColor(Color.magenta);
        g.fillPolygon(regions[2]);

        // federal capital
        g.setColor(Color.cyan);
        g.fillPolygon(regions[3]);

        // gilgit baltistan
        g.setColor(Color.orange);
        g.fillPolygon(regions[4]);

        // federally administered northern areas (FANA)
        g.setColor(Color.pink);
        g.fillPolygon(regions[5]);

        // punjab
        g.setColor(light_green);
        g.fillPolygon(regions[6]);

        // sindh
        g.setColor(new Color(255, 100, 100));
        g.fillPolygon(regions[7]);

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
     * draws the complete shortest path between the source and destination city | O(n)
     * @param g is the graphics
     */
    private void drawPath(Graphics g) {

        // no path exists
        if (path == null) {
            animation_counter = 0;
            return;
        }

        // traversing path linked list using current (temp) vertex
        Node<Vertex> current = path.head;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        // drawing path and writing details for each partial path
        for (int i = 0; i < path.size - 1; i++) {
            City c1 = current.data.getCity();
            City c2 = current.next.data.getCity();
            // animating the path drawing and details writing using a count (local timer)
            if (i < animation_counter) {
                drawLine(g, c1, c2, i);
                writePathDetails(g, c1, c2, i);
            }
            current = current.next;
        }

        // shows the path distance once the animation is complete
        if (animation_counter > path.size - 1) {
            String str = "Distance: " + toVertex.getShortestDistance() + " km";
            g2.setFont(new Font(font_style, Font.BOLD, 25));
            g2.setColor(font_color);
            g2.drawString("Distance: " + toVertex.getShortestDistance() + " km", (float) (3 * B_WIDTH) / 4 - (float) g2.getFontMetrics().stringWidth(str) / 2 - 50, 270 + 30 * (path.size - 1));
        }

        g2.setStroke(new BasicStroke(1));

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

        float progress = animation_counter - i;
        Point endPoint;

        Graphics2D g2 = (Graphics2D) g;

        if (progress <= 1)
            endPoint = interpolate(new Point(fromX, fromY), new Point(toX, toY), animation_counter % 1);
        else endPoint = new Point(toX, toY);


        if (animation_counter > path.size - 1)
            g2.setColor(Color.green);
        else g2.setColor(Color.red);

        g2.setStroke(new BasicStroke(5));
        g2.drawLine(fromX, fromY, endPoint.x, endPoint.y);
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

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(4));

        g2.setFont(new Font(font_style, Font.BOLD, 20));

        g2.setColor(font_color);

        String str = c1.getName() + " -> " + c2.getName() + " | " + getDistance(c1, c2) + " km";
        g2.drawString(str, 3 * B_WIDTH / 4 - g2.getFontMetrics().stringWidth(str) / 2 - 50, 250 + 30 * i);

        g2.setStroke(new BasicStroke(1));

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
     * finds the shortest path between the source and destination city
     * O(E * logV), where V and E are the total number of vertices and edges respectively
     */
    private void getPath() {

        if (fromVertex == null || toVertex == null)
            return;

        for (Vertex vertex : pakistan.getVertices())
            vertex.resetVertex();


        path = Dijkstra.getShortestPath(fromVertex, toVertex);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().sync();

        // the gui is repainted everytime any action is performed
        repaint();

        // local timer incremented by some value
        animation_counter += animation_speed;
    }

    /**
     * monitors the mouse presses (click + release) | O(E*logV)
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        // resetting if none of the cities has been selected
        if (fromVertex != null && toVertex != null){
            path = null;
            fromVertex.getCity().getButton().setPressed(false);
            fromVertex = null;
            toVertex.getCity().getButton().setPressed(false);
            toVertex = null;
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

    /**
     * monitors the key presses (click + release) | O(E*logV)
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_D)
            toggle_bg_color();

        if (e.getKeyCode() == KeyEvent.VK_C)
            select_bg_color();

        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_F) {

            path = null;
            Vertex vertex;

            if (e.getKeyCode() == KeyEvent.VK_S) {
                String city_name = JOptionPane.showInputDialog("Please enter a city name: ");
                if (city_name == null)
                    return;
                vertex = searchCityByName(city_name);

            } else vertex = (Vertex) JOptionPane.showInputDialog(null, "Please select a city", "City Selection", JOptionPane.PLAIN_MESSAGE, null, pakistan.getVertices(), null);

            if (vertex == null) {
                if (e.getKeyCode() == KeyEvent.VK_S)
                    JOptionPane.showMessageDialog(null, "City not found!");
                else JOptionPane.showMessageDialog(null, "No city selected!");
                return;
            }

            if (fromVertex == null) {
                fromVertex = vertex;
                fromVertex.getCity().getButton().setPressed(true);
                return;
            }

            if (toVertex == null) {
                toVertex = vertex;
                toVertex.getCity().getButton().setPressed(true);
                getPath();
                return;
            }

            // resetting previously selected vertices and path
            path = null;
            fromVertex.getCity().getButton().setPressed(false);
            toVertex.getCity().getButton().setPressed(false);
            toVertex = null;

            // setting source vertex
            fromVertex = vertex;
            fromVertex.getCity().getButton().setPressed(true);

        }

        if (e.getKeyCode() == KeyEvent.VK_R) {

            path = null;

            if (fromVertex != null) {
                fromVertex.getCity().getButton().setPressed(false);
                fromVertex = null;
            }

            if (toVertex != null) {
                toVertex.getCity().getButton().setPressed(false);
                toVertex = null;
            }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
        return (Math.round(lng*40)) - 2300;
    }

    /**
     * finds the y value on the GUI corresponding to the latitude
     * @param lat is the latitude of the map-coordinates
     * @return the y-value
     */
    public static int getYFromLAT(float lat){
        return B_HEIGHT + 1050 - (Math.round(lat*50));
    }

    /**
     * calculates the distance between two cities assuming we reside on the equator
     * <a href="https://www.thoughtco.com/degree-of-latitude-and-longitude-distance-4070616">...</a>
     * @param c1 is the city1
     * @param c2 is the city2
     * @return the distance in kms
     */
    public static float getDistance(City c1, City c2) {

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

    /**
     * toggles the background color between the 3 presets: white, black, green
     */
    private void toggle_bg_color() {

        bg_color_selected = ++bg_color_selected % bg_colors.length;

        if (bg_color_selected == 0)
            font_color = Color.black;
        else font_color = Color.white;

        bg_color = bg_colors[bg_color_selected];

    }

    /**
     * sets bg color of user's choice | O(1)
     */
    private void select_bg_color() {

        Color color = JColorChooser.showDialog(this, "Please select a color:", null);

        if (color == null)
            return;

        // setting font as white/black depending on the intensity of the bg color
        if (color.getRed()*0.299 + color.getGreen()*0.587 + color.getBlue()*0.114 > 186)
            font_color = Color.black;
        else font_color = Color.white;

        bg_color = color;

    }

    /**
     * searches a city by its name through binary search | log(V)
     */
    private Vertex searchCityByName(String city_name) {

        Vertex vertex = null;
        Vertex[] vertices = pakistan.getVertices();

        int start = 0;
        int end = vertices.length - 1;

        while (start <= end) {
            int mid = (start + end) / 2;
            if (city_name.compareToIgnoreCase(vertices[mid].getCity().getName()) > 0)
                start = mid + 1;
            else if (city_name.compareToIgnoreCase(vertices[mid].getCity().getName()) < 0)
                end = mid - 1;
            else {
                vertex = vertices[mid];
                break;
            }
        }

        return vertex;
    }

}