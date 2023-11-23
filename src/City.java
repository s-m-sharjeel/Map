import java.awt.*;

public class City{

    private final String name;
    private final float lat;
    private final float lng;
    private final int x;
    private final int y;
    private final int size = 4;
    private final Color color = Color.white;
    private boolean pressed;
    private boolean hovered;

    public City(String name, float lat, float lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.x = Map.getXFromLNG(lng);
        this.y = Map.getYFromLAT(lat);
    }

    public void draw(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - size, y - size, size*2, size*2);

        if (hovered) {
            g2.setColor(new Color(121, 190, 88));
            g2.fillOval(x - size, y - size, size*2, size*2);
            displayName(g);
        }

        if (pressed) {
            g2.setColor(new Color(0, 64, 26));
            g2.fillOval(x - size, y - size, size*2, size*2);
        }

        g2.setStroke(new BasicStroke(1));
    }

    public void isClicked(int x, int y){

        // euclidean distance
        if ((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y) < size*size)
            pressed = true;

    }

    public void isHovered(int x, int y){

        // euclidean distance
        hovered = (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) < size * size;

    }

    private void displayName(Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        FontMetrics m = g.getFontMetrics();
        int stringWidth = m.stringWidth(name);
        int stringHeight = m.getAscent() - m.getDescent();

//        g.setColor(Color.white);
//        g.fillRect(x - (stringWidth + 6), y - (stringHeight + 6), stringWidth + 6, stringHeight + 6);
//
//        g.setColor(Color.black);
//        g.drawRect(x - (stringWidth + 6), y - (stringHeight + 6), stringWidth + 6, stringHeight + 6);

        g.setColor(Color.black);
        g.drawString(name, x - (stringWidth/2), y - stringHeight/2 - size/2 - 1);

    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public String toString() {
        return name;
    }
}
