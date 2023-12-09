import java.awt.*;

public class Button {

    private final int x;
    private final int y;
    private final int size = 5;
    private final Color color = Color.white;
    private boolean pressed;
    private boolean hovered;
    private final String label;

    public Button(int x, int y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    /**
     * draws the city on the map | O(1)
     * @param g is the graphics
     */
    public void draw(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - size, y - size, size*2, size*2);

        if (hovered) {
            g2.setColor(new Color(121, 190, 88));
            g2.fillOval(x - size, y - size, size*2, size*2);
            displayName(label, g);
        }

        if (pressed) {
            g2.setColor(new Color(0, 64, 26));
            g2.fillOval(x - size, y - size, size*2, size*2);
        }

        g2.setStroke(new BasicStroke(1));
    }

    /**
     * changes the state to pressed if clicked within the bounds of the button | O(1)
     * @param x is the horizontal component of the click
     * @param y is the vertical component of the click
     */
    public void isClicked(int x, int y){

        // euclidean distance
        if ((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y) < size*size)
            pressed = true;

    }

    /**
     * changes the state to hovered if clicked within the bounds of the button | O(1)
     * @param x is the horizontal component of the click
     * @param y is the vertical component of the click
     */
    public void isHovered(int x, int y){

        // euclidean distance
        hovered = (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) < size * size;

    }

    /**
     * displays the name of the city on top of its marker | O(1)
     * @param g is the graphics
     */
    private void displayName(String name, Graphics g){

        g.setColor(Color.black);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        FontMetrics m = g.getFontMetrics();
        int stringWidth = m.stringWidth(name);
        int stringHeight = m.getAscent() - m.getDescent();

        g.setColor(Color.black);
        g.drawString(name, x - (stringWidth/2), y - stringHeight/2 - size/2 - 1);

    }

    // getter/setter:

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }


}
