import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class Series {
	private final List<Pair<Double>> data = new LinkedList<Pair<Double>>();
	private String caption;
	private Color color;

	private double xmin, xmax;
	private double ymin, ymax;

	public Series(String caption, Color color) {
		this.caption = caption;
		this.color = color;
	}

	public Series() {
		this.color = Color.black;
	}

	public void add(double x, double y) {
		if (data.size()==0) {
			xmin = xmax = x;
			ymin = ymax = y;
		} else
			setMinMax(x, y);
		data.add(new Pair<Double>(x, y));
	}

	private void setMinMax(double x, double y) {
		if (x>xmax)
			xmax = x;
		if (x<xmin)
			xmin = x;
		if (y>ymax)
			ymax = y;
		if (y<ymin)
			ymin = y;
	}

	public Color getColor() {
		return color;
	}

	public String getCaption() {
		return caption;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<Pair<Double>> asList() {
		return data;
	}

	public double getXmin() {
		return xmin;
	}

	public double getXmax() {
		return xmax;
	}

	public double getYmin() {
		return ymin;
	}

	public double getYmax() {
		return ymax;
	}

	public int getSize() {
		return data.size();
	}

}
