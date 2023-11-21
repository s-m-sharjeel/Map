import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Map extends JPanel implements ActionListener , MouseInputListener {

    private final int B_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width/2;
    private final int B_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private LinkedList<Node> pakistan;
    private City fromCity;
    private City toCity;

    public Map() {
        initMap();
    }

    /**
     * initializes the cities by reading the file
     */
    private void InitializeAssets() {

        Graph pak = new Graph();

        try{

            File file = new File("./src/data.csv");
            Scanner input1 = new Scanner(file);

            input1.nextLine();

            Node[] nodes = new Node[69];

            for (int i = 0; i < 69; i++) {
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
            for (int i = 0; i < 69; i++) {

                Node node = nodes[i];
                String line = input2.nextLine();
                String[] data = line.split(",");

                for (int j = 3; j < data.length; j++) {

                    for (int k = 0; k < 69; k++) {

                        if (nodes[k].getCity().getName().equals(data[j].trim())) {
                            node.addAdjacentNode(nodes[k], getDistance(node.getCity(), nodes[k].getCity()));
                            break;
                        }

                    }

                }

                pak.addNode(node);

            }

            input2.close();

        } catch (Exception e){
            System.out.println("File not found!");
        }

        pakistan = pak.getNodes();

    }

    private void initMap() {

        //https://simplemaps.com/data/world-cities
        //https://data.humdata.org/dataset/a64d1ff2-7158-48c7-887d-6af69ce21906

        InitializeAssets();

        addMouseListener( this );
        addMouseMotionListener( this );
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setFocusable(true);

        int DELAY = 25;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

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

        g.setColor(Color.black);
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

        File file = new File("./src/pakgeojson.wkt");
        StringBuilder data = new StringBuilder();
        try {
            Scanner input = new Scanner(file);
            while(input.hasNext()) {
                data.append(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        String[] coordinates = data.toString().split(",");
        LinkedList<Point> boundary = new LinkedList<>();

        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = coordinates[i].trim();
            String[] arr = coordinates[i].split(" ");
            float lng = Float.parseFloat(arr[0].trim());
            float lat = Float.parseFloat(arr[1].trim());
            boundary.add(new Point(getXFromLNG(lng), getYFromLAT(lat)));
            if (i != 0) {
                g2.drawLine(boundary.get(i - 1).x, boundary.get(i - 1).y, boundary.get(i).x, boundary.get(i).y);
            } else g2.fillOval(boundary.get(i).x, boundary.get(i).y, 2, 2);
        }
        g2.setStroke(new BasicStroke(1));

    }

    /**
     * draws all the cities
     * @param g is the graphics
     */
    private void drawCities(Graphics g) {

        for (Node v : pakistan) {
            v.getCity().draw(g);
        }

    }

    public void drawLine(Graphics g, City c1, City c2){

        if (c1 == null || c2 == null)
            return;

        // using formula to calculate the distance between two cities using their coordinates

        String distance =  "" + getDistance(c1, c2);
        String[] temp = distance.split("\\.");
        distance = temp[0] + "." + temp[1].substring(0, 3) + " km";

        g.setColor(Color.red);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        FontMetrics m = g.getFontMetrics();
        int stringWidth = m.stringWidth(distance);
        int stringHeight = m.getAscent() - m.getDescent();

        int fromX = c1.getX();
        int fromY = c1.getY();
        int toX = c2.getX();
        int toY = c2.getY();

//        g.drawString(distance, (fromX + toX)/2 - stringWidth/2, (fromY + toY)/2 + stringHeight/2);

        g.setColor(Color.gray);
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

            for (Node node : pakistan) {
                if (node.getCity().getName().equals(fromCity.getName()))
                    fromNode = node;
                else if (node.getCity().getName().equals(toCity.getName()))
                    toNode = node;
            }

            assert fromNode != null;

            Dijkstra.calculateShortestPath(fromNode);
            path = Dijkstra.getPath(pakistan, toNode);

            return path;

        }

        return null;

    }

    private void drawPath(Graphics g) {

        LinkedList<Node> path = getPath();

        if (path == null)
            return;

        for (int i = 0; i < path.size() - 1; i++) {
            drawLine(g, path.get(i).getCity(), path.get(i + 1).getCity());
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

        for (Node node: pakistan) {
            node.resetNode();
        }

        // resetting if none of the cities has been selected
        if (fromCity != null && toCity != null){
            for (Node v : pakistan) {
                v.getCity().setPressed(false);
            }
            fromCity = null;
            toCity = null;
        }

        // setting the source city as the one pressed (if any)
        if (fromCity == null){
            for (Node v : pakistan) {
                v.getCity().isClicked(x, y);
                if (v.getCity().isPressed()) {
                    fromCity = v.getCity();
                    break;
                }
            }

        } else {
            // setting the destination
            for (Node v : pakistan) {
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

        for (Node v : pakistan) {
            v.getCity().isHovered(e.getX(), e.getY());
        }

	}

    /**
     * finds the x value on the GUI corresponding to the longitude
     * @param lng is the longitude of the map-coordinates
     * @return the x-value
     */
    public static int getXFromLNG(float lng){
        return (Math.round(lng*40)) - 2350;
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