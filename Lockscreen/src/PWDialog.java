import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class PWDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String pw = "123456";
	private JPasswordField pwField;
	private JPanel mainPanel;
	private JPanel pwMainPanel;
	private ImagePanel imagePanel;
	private WindowsSecurity security;
	static final String PWMAINPANEL = "Card with Password Field";
	static final String IMAGEPANEL = "Card with Images";
	public static Dimension SCREENDIM;
	public static boolean accepting;
	public static boolean denying;
	public static PWDialog instance;

	public PWDialog() {
		accepting = false;
		denying = false;
		SCREENDIM = getToolkit().getScreenSize();

		JPanel pwPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				try {
					Image img = ImageIO.read(getClass().getResource(
							"images/background.jpg"));
					g.drawImage(img, 0, 0, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		pwPanel.setSize(new Dimension(259, 180));
		pwPanel.setLayout(new BoxLayout(pwPanel, 1));

		pwField = new JPasswordField(10);
		pwField.setAlignmentX(0.5F);
		pwField.setMaximumSize(pwField.getPreferredSize());
		pwField.addActionListener(this);

		pwPanel.add(Box.createRigidArea(new Dimension(259, 120)));
		pwPanel.add(pwField);
		pwPanel.add(Box.createRigidArea(new Dimension(1, 50)));

		pwMainPanel = new JPanel();
		pwMainPanel.setBackground(new Color(1.0F, 1.0F, 1.0F, 0.0F));
		pwMainPanel.setLayout(new BoxLayout(pwMainPanel, 1));
		pwMainPanel.add(Box.createRigidArea(new Dimension(getToolkit()
				.getScreenSize().width,
				getToolkit().getScreenSize().height / 2 - 90)));
		pwMainPanel.add(pwPanel);

		imagePanel = new ImagePanel("images/Locked.jpg",
				new Dimension(259, 180));
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent me) {
			}

			public void mouseMoved(MouseEvent me) {
				if ((PWDialog.denying) || (PWDialog.accepting)) {
					return;
				}
				int x = me.getLocationOnScreen().x;
				int y = me.getLocationOnScreen().y;
				Point p = imagePanel.getPoint();
				Dimension d = imagePanel.getDims();

				if ((x >= p.x) && (x <= p.x + d.width) && (y >= p.y)
						&& (y <= p.y + d.height))
					showPanel("Card with Password Field");
				else
					showPanel("Card with Images");
			}
		});
		mainPanel = new JPanel(new CardLayout());
		mainPanel.add(imagePanel, "Card with Images");
		mainPanel.add(pwMainPanel, "Card with Password Field");

		setContentPane(mainPanel);
		setPreferredSize(getToolkit().getScreenSize());
		setDefaultCloseOperation(0);
		setFocusable(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setBackground(new Color(0.1F, 0.1F, 0.1F, 0.25F));
		pack();
		setVisible(true);
		requestFocus();
		security = new WindowsSecurity(this);
	}

	public boolean checkPassword(String in) {
		return in.equals(pw);
	}

	public void clearPassword() {
		pwField.setText(null);
	}

	public void accept() {
		accepting = true;

		imagePanel.setImage("images/Unlocked.jpg", new Dimension(259, 180));
		showPanel("Card with Images");

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				PWDialog.instance.dispose();
				PWLauncher.toggleIcon();
				security.stop();
			}
		}, 2000L);
	}

	public void displayImage(String path, Dimension dims, Point point) {
		try {
			Image i = ImageIO.read(getClass().getResource(path));
			getGraphics().drawImage(i, point.x, point.y, dims.width,
					dims.height, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deny() {
		denying = true;
		imagePanel.setImage("images/Locked.jpg", new Dimension(259, 180));
		showPanel("Card with Images");

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				clearPassword();
				showPanel("Card with Password Field");
				PWDialog.denying = false;
			}
		}, 2000L);
	}

	public void showPanel(String title) {
		CardLayout cl = (CardLayout) mainPanel.getLayout();
		cl.show(mainPanel, title);
		if (title.equals("Card with Password Field"))
			pwField.requestFocus();
	}

	public void actionPerformed(ActionEvent e) {
		if (checkPassword(e.getActionCommand())) {
			accept();
		} else
			deny();
	}

	public static void main(String[] args) throws AWTException {
		instance = new PWDialog();
	}
}