import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains graphical representation of <x, y> list
 */
public class Series implements Cloneable {
	private final List<Pair<Double>> data = new LinkedList<Pair<Double>>();
	private String caption;
	private Color color;
	private int width = 1;

	private double xmin, xmax;
	private double ymin, ymax;

	public Series(String caption, Color color) {
		this.caption = caption;
		this.color = color;
	}

	public Series(String caption) {
		this.caption = caption;
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

	private void addManual(double x, double y) {
		if (data.size()==0) {
			xmin = xmax = x;
			ymin = ymax = y;
		}
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

	private void setXmin(double xmin) {
		this.xmin = xmin;
	}

	private void setXmax(double xmax) {
		this.xmax = xmax;
	}

	private void setYmin(double ymin) {
		this.ymin = ymin;
	}

	private void setYmax(double ymax) {
		this.ymax = ymax;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void incWidth() {
		width++;
	}

	public void decWidth() {
		if (width>1)
			width--;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Series clone = new Series(caption);

		for (Pair<Double> pair : this.asList())
			clone.addManual(new Double(pair.getX()), new Double(pair.getY()));

		clone.setXmin(getXmin());
		clone.setXmax(getXmax());

		clone.setYmin(getYmin());
		clone.setYmax(getYmax());

		clone.setColor(color);

		return clone;
	}

}
