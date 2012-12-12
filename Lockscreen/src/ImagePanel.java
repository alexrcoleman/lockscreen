import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -6621495681556455257L;
	private BufferedImage image;
	private Dimension dims;
	private Point point;

	public ImagePanel(String path, Dimension dims) {
		this(path, dims, new Point(PWDialog.SCREENDIM.width / 2 - dims.width
				/ 2, PWDialog.SCREENDIM.height / 2 - dims.height / 2));
	}

	public ImagePanel(String path, Dimension dims, Point point) {
		image = newImage(path);
		this.dims = dims;
		this.point = point;
	}

	private BufferedImage newImage(String path) {
		BufferedImage out = null;
		try {
			out = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return out;
	}

	public Point getPoint() {
		return point;
	}

	public Dimension getDims() {
		return dims;
	}

	public void setImage(String path, Dimension dims) {
		setImage(path, dims, new Point(PWDialog.SCREENDIM.width / 2
				- dims.width / 2, PWDialog.SCREENDIM.height / 2 - dims.height
				/ 2));
	}

	public void setImage(String path, Dimension dims, Point point) {
		image = newImage(path);
		this.dims = dims;
		this.point = point;
	}

	public void setDimensions(Dimension dims) {
		this.dims = dims;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, point.x, point.y, dims.width, dims.height, null);

		if (PWDialog.accepting) {
			try {
				BufferedImage i = ImageIO.read(getClass().getResource(
						"images/Accept.jpg"));
				g.drawImage(i, PWDialog.SCREENDIM.width / 2 - i.getWidth() / 2,
						(int) (PWDialog.SCREENDIM.height * 0.75D - i
								.getHeight() / 2), 465, 112, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (PWDialog.denying)
			try {
				BufferedImage i = ImageIO.read(getClass().getResource(
						"images/Deny.jpg"));
				g.drawImage(i, PWDialog.SCREENDIM.width / 2 - i.getWidth() / 2,
						(int) (PWDialog.SCREENDIM.height * 0.75D - i
								.getHeight() / 2), 465, 112, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}