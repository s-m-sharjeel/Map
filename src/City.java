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
        this.button = new Button(Map.getXFromLNG(lng), Map.getYFromLAT(lat), name);
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

    public Button getButton() {
        return button;
    }

    @Override
    public String toString() {
        return name;
    }
}
