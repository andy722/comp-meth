import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


class PlotPanel extends JPanel {
	private static final long serialVersionUID = 4759113914335051698L;

	private final JPopupMenu popup;

	private final List<Series> data = Collections.synchronizedList(new LinkedList<Series>());

	private final static int RADIUS = 2;

	private volatile boolean allowDraw = true;

	private volatile boolean showGrid = false;
	private volatile boolean showNodeLabels = false;
	private volatile boolean showLabels = true;

	private double U_a, U_b;
	private double k_a, k_b;

	private final static int CELLS = 10;

	/** Minimal and maximal Y-values of all graphs */
	private double min, max;
	/** Minimal and maximal X-values of all graphs */
	private double xmin, xmax;

	public PlotPanel(Task task) {
		super();

		this.U_a = task.U_a;
		this.U_b = task.U_b;

		this.k_a = task.k_a;
		this.k_b = task.k_b;

		popup = initPopup();

		addMouseListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent event) {
				super.mouseMoved(event);
			}

			public void mousePressed(MouseEvent event) {
	        	if (event.isPopupTrigger())
	        		popup.show(event.getComponent(), event.getX(), event.getY());
	        }

	        public void mouseReleased(MouseEvent event) {
	        	if (event.isPopupTrigger())
	        		popup.show(event.getComponent(), event.getX(), event.getY());
	        }
		});
	}

	private JPopupMenu initPopup() {
		JPopupMenu popup = new JPopupMenu();

		popup.add( new AbstractAction("Показать/скрыть сетку") {
			private static final long serialVersionUID = 0L;

			public void actionPerformed(ActionEvent arg0) {
				showGrid = !showGrid;
				repaint();
			}
		});
		popup.add( new AbstractAction("Подписи графиков") {
			private static final long serialVersionUID = 0L;

			public void actionPerformed(ActionEvent arg0) {
				showLabels = !showLabels;
				repaint();
			}
		});

		popup.add( new AbstractAction("Подписи узлов") {
			private static final long serialVersionUID = 0L;

			public void actionPerformed(ActionEvent arg0) {
				showNodeLabels = !showNodeLabels;
				repaint();
			}
		});
		return popup;
	}

	public synchronized void addSeries(Series s) {
		allowDraw = false;

		data.add(s);
		if (data.size()==1) {
			/* first series */
			min = s.getYmin();
			max = s.getYmax();
			xmin = s.getXmin();
			xmax = s.getXmax();
		}
		else
			minMax(s);

		allowDraw = true;
	}

	/**
	 * Generates minimal and maximal values of all functions
	 */
	private void minMax(Series s) {
		if (max<s.getYmax())
			max = s.getYmax();
		if (min>s.getYmin())
			min = s.getYmin();

		if (xmax>s.getXmax())
			xmax = s.getXmax();
		if (xmin>s.getXmin())
			xmin = s.getXmin();
	}

	private int xToScreen(double x) {
		return (int) Math.round( (x - xmin) * this.getWidth()/(xmax-xmin) );
	}

	private int yToScreen(double y) {
		return (int) Math.round(y * ( this.getHeight() / (min-max) ) - max * this.getHeight()/(min-max));
	}

	public void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);

		if (!allowDraw)
			return;

		Graphics2D g = (Graphics2D) arg0;

		if (showGrid)
			paintGrid(g);
		paintSeries(g);
		if (showLabels)
			paintLabels(g);
	}

	private void paintLabels(Graphics2D g) {
		int i = 1;
		g.setPaint(Color.black);
		g.drawString("U_a = " + U_a  + ", U_b = " + U_b, 10, i*15);
		i++;
		g.drawString("k_a = " + k_a  + ", k_b = " + k_b, 10, i*15);
		i++;
		for (Series s : data) {
			g.setPaint(s.getColor());
			g.drawString(s.getCaption(), 10, i*15);
			i++;
		}

	}

	private void paintGrid(Graphics2D g) {
		g.setPaint(Color.gray);
		for (int i = 0; i<CELLS; i++) {
			/* horizontal */
			g.draw(new Line2D.Double(xToScreen(xmin), yToScreen(min + i*(max-min)/CELLS), xToScreen(xmax), yToScreen(min + i*(max-min)/CELLS)));
			g.drawString(new Pair<Double>(xmin, min + i*(max-min)/CELLS).toString(), xToScreen(xmin)+5, yToScreen(min + i*(max-min)/CELLS)-5);
			/* vertical */
			g.draw(new Line2D.Double(xToScreen(xmin + i*(xmax-xmin)/CELLS), yToScreen(min), xToScreen(xmin + i*(xmax-xmin)/CELLS), yToScreen(max)));
			g.drawString(new Pair<Double>(xmin + i*(xmax-xmin)/CELLS, min).toString(), xToScreen(xmin + i*(xmax-xmin)/CELLS)+5, yToScreen(min)-5);
		}
	}

	private void paintSeries(Graphics2D g) {
		int x, y;

		Font font = g.getFont();
		g.setFont(new Font("Serif", Font.PLAIN, 9));

		for (Series series : data) {
			g.setPaint(series.getColor());
			Pair<Integer> prev = null;
			for (Pair<Double> point : series.asList()) {
				x = xToScreen(point.getX());
				y = yToScreen(point.getY());

				if (showNodeLabels) {
					Ellipse2D node = new Ellipse2D.Double();
					node.setFrameFromCenter(x, y, x+RADIUS, y+RADIUS);
					g.fill(node);
					g.draw(node);
					g.drawString(point.toString(), x, y);
				}

				if (prev!=null)
					g.draw(new Line2D.Double(prev.getX(), prev.getY(), x, y));

				prev = new Pair<Integer>(x, y);
			}

		} /* series */
		g.setFont(font);
	}

	public void show() {
		repaint();
	}

}
