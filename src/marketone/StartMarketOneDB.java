package marketone;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.hsqldb.Server;

/**
 * StratDB class create a system tray icon with pop-menu enabling users to
 * start/stop/switch between and create databases.
 * 
 * @author LIU Xiaofan
 * 
 */
public class StartMarketOneDB {

	public static void main(String[] args) throws IOException {
		new StartMarketOneDB();
	}

	private MenuItem currentDBName = null;

	private MenuItem exitMenuItem = null;

	private Server hsqldbServer = null;

	private PopupMenu menu = null;

	private MenuItem newMenuItem = null;

	private Menu startMenuItem = null;

	private MenuItem stopMenuItem = null;

	private SystemTray tray;

	public StartMarketOneDB() {
		this.tray = SystemTray.getSystemTray();
		TrayIcon trayIcon = new TrayIcon(createImage("icon.gif", "tray icon"));
		trayIcon.setPopupMenu(getMenu());
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private Image createImage(String path, String description) {
		URL imageURL = StartMarketOneDB.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else
			return (new ImageIcon(imageURL, description)).getImage();
	}

	private MenuItem getCurrentDBName() {
		if (currentDBName == null) {
			currentDBName = new MenuItem("No DB running");
		}
		return currentDBName;
	}

	private MenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new MenuItem("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stopDB();
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	public PopupMenu getMenu() {
		if (menu == null) {
			menu = new PopupMenu();
			menu.add(getCurrentDBName());
			menu.addSeparator();
			menu.add(getStartMenuItem());
			menu.add(getNewMenuItem());
			menu.add(getStopMenuItem());
			menu.add(getExitMenuItem());
		}
		return menu;
	}

	/**
	 * This method initializes newMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private MenuItem getNewMenuItem() {
		if (newMenuItem == null) {
			newMenuItem = new MenuItem("Creat new DB");
			newMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final String dbname = JOptionPane.showInputDialog("New database name:");
					if (dbname == null)
						return;
					MenuItem item = new MenuItem(dbname);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							stopDB();
							startDB(dbname);
							getStopMenuItem().setEnabled(true);
							getNewMenuItem().setEnabled(false);
						}
					});
					startMenuItem.add(item);
					startDB(dbname);
					getNewMenuItem().setEnabled(false);
					getStartMenuItem().setEnabled(true);
					getStopMenuItem().setEnabled(true);
				}
			});
		}
		return newMenuItem;
	}

	/**
	 * This method initializes startMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private Menu getStartMenuItem() {
		if (startMenuItem == null) {
			startMenuItem = new Menu("Start DB");
			for (final String name : selectDB()) {
				MenuItem item =
						new MenuItem(name.substring(0, name.lastIndexOf(".properties")));
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						stopDB();
						startDB(name.substring(0, name.lastIndexOf(".properties")));
						getStopMenuItem().setEnabled(true);
						getNewMenuItem().setEnabled(false);
					}
				});
				startMenuItem.add(item);
			}
		}
		return startMenuItem;
	}

	/**
	 * This method initializes stopMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private MenuItem getStopMenuItem() {
		if (stopMenuItem == null) {
			stopMenuItem = new MenuItem("Stop DB");
			stopMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stopDB();
					getNewMenuItem().setEnabled(true);
					getStartMenuItem().setEnabled(true);
					getStopMenuItem().setEnabled(false);
				}
			});
			stopMenuItem.setEnabled(false);
		}
		return stopMenuItem;
	}

	private String[] selectDB() {
		File dbmodels = new File("./resources/dbmodel");
		String[] dbnames = dbmodels.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("properties");
			}
		});
		if (dbnames != null)
			return dbnames;
		else
			return new String[] {};
	}

	private void startDB(String dbname) {
		hsqldbServer = new Server();
		hsqldbServer.setDatabasePath(0, "./resources/dbmodel/" + dbname);
		hsqldbServer.setDatabaseName(0, "MarketOne");
		hsqldbServer.setSilent(true);
		hsqldbServer.start();
		getCurrentDBName().setLabel(dbname);
	}

	private void stopDB() {
		if (hsqldbServer != null) {
			hsqldbServer.stop();
			hsqldbServer = null;
		}
		getCurrentDBName().setLabel("No DB running");
	}

}
