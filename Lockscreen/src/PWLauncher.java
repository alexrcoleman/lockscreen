import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDialog;

public class PWLauncher extends JDialog implements ActionListener {
	private static final long serialVersionUID = -3759856811214634419L;
	public static PWDialog pwScreen = null;
	private static boolean locked = false;
	private static TrayIcon trayIcon;

	public PWLauncher() {
		initSystemTray();

		setUndecorated(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		setDefaultCloseOperation(2);

		setFocusable(true);
		setVisible(true);
	}

	private void initSystemTray() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		PopupMenu popupMenu = new PopupMenu();
		SystemTray tray = SystemTray.getSystemTray();
		trayIcon = new TrayIcon(createImage("images/UnlockedIcon.png"));
		trayIcon.setToolTip("Screen locker - UNLOCKED");
		trayIcon.addActionListener(this);

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		popupMenu.add(exitItem);
		trayIcon.setPopupMenu(popupMenu);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static Image createImage(String path) {
		try {
			return ImageIO.read(ClassLoader.getSystemResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void toggleIcon() {
		locked = !locked;
		final String path;
		if (locked) {
			path = "images/LockedIcon.png";
			trayIcon.setToolTip("Screen locker - LOCKED");
		} else {
			path = "images/UnlockedIcon.png";
			trayIcon.setToolTip("Screen locker - UNLOCKED");
		}
		trayIcon.setImage(createImage(path));
	}

	public void actionPerformed(ActionEvent arg0) {
		toggleIcon();
		String[] s = null;
		try {
			PWDialog.main(s);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new PWLauncher();
	}
}