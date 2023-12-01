import java.awt.*;

public class City{

    private final String name;
    private final float lat;
    private final float lng;

    private final Button button;

    public City(String name, float lat, float lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.button = new Button(Map.getXFromLNG(lng), Map.getYFromLAT(lat));
    }

    /**
     * draws the city on the map | O(1)
     * @param g is the graphics
     */
    public void draw(Graphics g){
        button.draw(name, g);
    }

    /**
     * changes the state to pressed if clicked within the bounds of the button | O(1)
     * @param x is the horizontal component of the click
     * @param y is the vertical component of the click
     */
    public void isClicked(int x, int y){
        button.isClicked(x, y);
    }

    /**
     * changes the state to hovered if clicked within the bounds of the button | O(1)
     * @param x is the horizontal component of the hover
     * @param y is the vertical component of the hover
     */
    public void isHovered(int x, int y){
        button.isHovered(x, y);
    }

    // getter/setter:

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getX() {
        return button.getX();
    }

    public boolean isPressed() {
        return button.isPressed();
    }

    public void setPressed(boolean pressed) {
        button.setPressed(pressed);
    }

    public int getY() {
        return button.getY();
    }

    @Override
    public String toString() {
        return name;
    }
}
