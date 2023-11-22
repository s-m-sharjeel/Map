import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Scanner;

public class Map extends JPanel implements ActionListener , MouseInputListener {

    private final int B_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width/2;
    private final int B_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private Graph pakistan;
    private Polygon[] regions;
    private City fromCity;
    private City toCity;

    public Map() {
        initMap();
    }

    /**
     * initializes the cities by reading the file
     */
    private void InitializeAssets() {

        // no. of cities for which the data is available
        int size = 66;
        pakistan = new Graph(size);

        try{

            File file = new File("./src/data.csv");
            Scanner input1 = new Scanner(file);

            input1.nextLine();

            Node[] nodes = new Node[size];

            for (int i = 0; i < size; i++) {
                String line = input1.nextLine();
                String[] data = line.split(",");

                City city = (new City(data[0], Float.parseFloat(data[1]), Float.parseFloat(data[2])));

                Node node = new Node(city);
                nodes[i] = node;
            }

            input1.close();

            Scanner input2 = new Scanner(file);

            input2.nextLine();

            // for each city
            for (int i = 0; i < size; i++) {

                Node node = nodes[i];
                String line = input2.nextLine();
                String[] data = line.split(",");

                for (int j = 3; j < data.length; j++) {

                    for (int k = 0; k < size; k++) {

                        if (nodes[k].getCity().getName().equals(data[j].trim())) {
                            node.addAdjacentNode(nodes[k], getDistance(node.getCity(), nodes[k].getCity()));
                            break;
                        }

                    }

                }

                pakistan.addNode(node);

            }

            input2.close();

        } catch (Exception e){
            System.out.println("File not found!");
        }

        File file = new File("./src/pakgeojson.wkt");
        String data;

        regions = new Polygon[8];

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
                System.out.println(points);

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

        setBackground(new Color(0, 64, 26));

        drawBoundary(g);
        drawCities(g);
        paintText(g);
        drawPath(g);

    }

    /**
     * paints text on the panel
     * @param g is the graphics
     */
    public void paintText(Graphics g) {

        g.setColor(Color.white);
        g.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        String str = "PAKISTAN";
        g.drawString(str, B_WIDTH/3 - g.getFontMetrics().stringWidth(str)/2, 60 + 40);

        g.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        str = "From:";
        if (fromCity != null)
            str += " " + fromCity;
        g.drawString(str, B_WIDTH/3 - g.getFontMetrics().stringWidth(str)/2, 100 + 40);

        g.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        str = "To:";
        if (toCity != null)
            str += " " + toCity;
        g.drawString(str, B_WIDTH/3 - g.getFontMetrics().stringWidth(str)/2, 130 + 40);

    }

    /**
     * draws the boundary of pakistan
     * @param g is the graphics
     */
    public void drawBoundary(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));

        for (Polygon region : regions) {
            g.setColor(new Color(121, 190, 88));
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

        for (Node v : pakistan.getNodes()) {
            v.getCity().draw(g);
        }

    }

    public void drawLine(Graphics g, City c1, City c2){

        if (c1 == null || c2 == null)
            return;

        // using formula to calculate the distance between two cities using their coordinates

//        float num = getDistance(c1, c2);
//        DecimalFormat decimalFormator = new DecimalFormat("0.000");
//        String distance = decimalFormator.format(num) + " km";

//        g.setColor(Color.red);
//        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
//        FontMetrics m = g.getFontMetrics();
//        int stringWidth = m.stringWidth(distance);
//        int stringHeight = m.getAscent() - m.getDescent();
//        g.drawString(distance, (fromX + toX)/2 - stringWidth/2, (fromY + toY)/2 + stringHeight/2);

        int fromX = c1.getX();
        int fromY = c1.getY();
        int toX = c2.getX();
        int toY = c2.getY();


        g.setColor(Color.white);
        g.drawLine(fromX, fromY, toX, toY);

    }

    /**
     * finds the shortest path between the source and destination city
     * @return the nodes in a linked list
     */
    public LinkedList<Node> getPath() {

        if (fromCity != null && toCity != null) {

            LinkedList<Node> path = new LinkedList<>();

            Node fromNode = null;
            Node toNode = null;

            for (Node node : pakistan.getNodes()) {
                if (node.getCity().getName().equals(fromCity.getName()))
                    fromNode = node;
                else if (node.getCity().getName().equals(toCity.getName()))
                    toNode = node;
            }

            assert fromNode != null;

            path = Dijkstra.getShortestPath(fromNode, toNode);

            return path;

        }

        return null;

    }

    private void drawPath(Graphics g) {

        LinkedList<Node> path = getPath();

        if (path == null)
            return;

        for (int i = 0; i < path.size() - 1; i++)
            drawLine(g, path.get(i).getCity(), path.get(i + 1).getCity());

    }

    /**
     * monitors the mouse presses (click + release)
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        Dijkstra.reset(pakistan);

        // resetting if none of the cities has been selected
        if (fromCity != null && toCity != null){
            for (Node v : pakistan.getNodes()) {
                v.getCity().setPressed(false);
            }
            fromCity = null;
            toCity = null;
        }

        // setting the source city as the one pressed (if any)
        if (fromCity == null){
            for (Node v : pakistan.getNodes()) {
                v.getCity().isClicked(x, y);
                if (v.getCity().isPressed()) {
                    fromCity = v.getCity();
                    break;
                }
            }

        } else {
            // setting the destination
            for (Node v : pakistan.getNodes()) {
                City city = v.getCity();
                city.isClicked(x, y);
                if (city.isPressed() && !city.equals(fromCity)) {
                    toCity = city;
                    break;
                }
            }
        }

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

        for (Node v : pakistan.getNodes()) {
            v.getCity().isHovered(e.getX(), e.getY());
        }

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
        return Toolkit.getDefaultToolkit().getScreenSize().height + 1050 - (Math.round(lat*50));
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