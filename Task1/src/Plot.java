import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Plot extends JFrame {
	private static final long serialVersionUID = -1366409360021795101L;

	/** Default width of the frame */
	private final static int WIDTH = 320;

	/** Default height of the frame */
	private final static int HEIGHT = 240;

	private final PlotPanel panel;

	/** Number of graphs previously created */
	private static int graphs = 0;

	public Plot(Task t) {
		super();

		graphs++;
		setTitle("График №" + graphs);
		setSize(WIDTH, HEIGHT);

		panel = new PlotPanel(t);
		getContentPane().add(panel);

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println(e.getX() + " " + e.getY());
			}

		});
	}

	public void add(Series s) {
		panel.addSeries(s);
	}

	public void show() {
		panel.show();
		super.show();
	}
}