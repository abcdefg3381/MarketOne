/**
 * JTimePopup.java
 *
 * @author Don Corley <don@donandann.com>
 * @version 1.0.0
 */

package org.sourceforge.jcalendarbutton;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import maggie.network.gui.JCalendarPopup;

/**
 * A JCalendarPopup is a popup calendar the user can click on to change a date.
 * 
 * @author Administrator
 * @version 1.0.0
 */
public class JTimePopup extends JList implements ListSelectionListener {
	/**
	 * Constant - Milliseconds in a day.
	 */
	public static final long KMS_IN_A_DAY = 24 * 60 * 60 * 1000; // Milliseconds
	// in a day

	private static final long serialVersionUID = 1L;

	/**
	 * The name of the calendar popup icon.
	 */
	public static final String TIME_ICON = "Time";

	/**
	 * Create this calendar in a popup menu and synchronize the text field on
	 * change.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to "date").
	 * @param dateTarget
	 *            The initial date for this button.
	 */
	public static JButton createCalendarButton(String strDateParam, Date dateTarget) {
		JTimeButton button = new JTimeButton(strDateParam, dateTarget);
		// button.setMargin(NO_INSETS);
		button.setOpaque(false);

		return button;
	}

	/**
	 * Create this calendar in a popup menu and synchronize the text field on
	 * change.
	 * 
	 * @param dateTarget
	 *            The initial date for this button.
	 * @param button
	 *            The calling button.
	 */
	public static JTimePopup createTimePopup(Date dateTarget, Component button) {
		return JTimePopup.createTimePopup(null, dateTarget, button, null);
	}

	/**
	 * Create this calendar in a popup menu and synchronize the text field on
	 * change.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to "date").
	 * @param dateTarget
	 *            The initial date for this button.
	 * @param button
	 *            The calling button.
	 */
	public static JTimePopup createTimePopup(String strDateParam, Date dateTarget,
			Component button) {
		return JTimePopup.createTimePopup(null, dateTarget, button, null);
	}

	/**
	 * Create this calendar in a popup menu and synchronize the text field on
	 * change.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to "date").
	 * @param dateTarget
	 *            The initial date for this button.
	 * @param strLanguage
	 *            The language to use.
	 * @param button
	 *            The calling button.
	 */
	public static JTimePopup createTimePopup(String strDateParam, Date dateTarget,
			Component button, String strLanguage) {
		JPopupMenu popup = new JPopupMenu();
		JComponent c = popup; // ?.getContentPane();
		c.setLayout(new BorderLayout());
		JTimePopup calendar = new JTimePopup(strDateParam, dateTarget, strLanguage);
		JScrollPane scrollPane = new JScrollPane(calendar);
		if (calendar.getSelectedIndex() != -1) {
			calendar.ensureIndexIsVisible(calendar.getSelectedIndex());
		}
		c.add(scrollPane, BorderLayout.CENTER);
		popup.show(button, button.getBounds().width, 0);
		return calendar;
	}

	/**
	 * Transfer the focus after selecting the date (default = true).
	 */
	protected boolean m_bTransferFocus = true;

	protected Calendar m_calendar = Calendar.getInstance();
	/**
	 * The date param for this popup.
	 */
	protected String m_strDateParam = JCalendarPopup.DATE_PARAM;

	protected DateFormat m_tf = DateFormat.getTimeInstance(DateFormat.SHORT);
	protected Date m_timeNow = null;
	protected Date m_timeSelected = null;
	protected Date m_timeTarget = null;

	/**
	 * Creates new form TimePopup.
	 */
	public JTimePopup() {
		super();
	}

	/**
	 * Creates new form TimePopup.
	 * 
	 * @param date
	 *            The initial date for this button.
	 */
	public JTimePopup(Date date) {
		this();
		this.init(null, date, null);
	}

	/**
	 * Creates new form TimePopup.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to "date").
	 * @param date
	 *            The initial date for this button.
	 */
	public JTimePopup(String strDateParam, Date date) {
		this();
		this.init(strDateParam, date, null);
	}

	/**
	 * Creates new form TimePopup.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to "date").
	 * @param date
	 *            The initial date for this button.
	 * @param strLanguage
	 *            The language to use.
	 */
	public JTimePopup(String strDateParam, Date date, String strLanguage) {
		this();
		this.init(strDateParam, date, strLanguage);
	}

	/**
	 * Get the parent popup menu.
	 * 
	 * @return The popup menu.
	 */
	private JPopupMenu getJPopupMenu() {
		Container parent = this.getParent();
		while (parent != null) {
			if (parent instanceof JPopupMenu)
				return (JPopupMenu) parent;
			parent = parent.getParent();
		}
		return null;
	}

	/**
	 * Creates new form TimePopup.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to "date").
	 * @param date
	 *            The initial date for this button.
	 * @param strLanguage
	 *            The language to use.
	 */
	public void init(String strDateParam, Date timeTarget, String strLanguage) {
		if (strDateParam != null) {
			m_strDateParam = strDateParam; // Property name
		}

		m_timeNow = new Date();
		if (timeTarget == null) {
			timeTarget = m_timeNow;
		}
		m_timeSelected = timeTarget;

		if (strLanguage != null) {
			Locale locale = new Locale(strLanguage, "");
			if (locale != null) {
				m_calendar = Calendar.getInstance(locale);
				m_tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
			}
		}
		m_timeTarget = new Date(timeTarget.getTime());
		this.layoutCalendar(m_timeTarget);

		this.addListSelectionListener(this);
	}

	/**
	 * Add all the components to this calendar panel.
	 * 
	 * @param dateTarget
	 *            This date needs to be in the calendar.
	 */
	public void layoutCalendar(Date timeTarget) {
		m_calendar.setTime(timeTarget);

		int hour = m_calendar.get(Calendar.HOUR_OF_DAY);
		int minute = m_calendar.get(Calendar.MINUTE);

		String[] array = new String[24 * 2];
		m_calendar.set(Calendar.HOUR_OF_DAY, 0);
		m_calendar.set(Calendar.MINUTE, 0);
		m_calendar.set(Calendar.SECOND, 0);
		m_calendar.set(Calendar.MILLISECOND, 0);
		int selectedIndex = -1;
		for (int i = 0; i < array.length; i++) {
			if (hour == m_calendar.get(Calendar.HOUR_OF_DAY))
				if (minute == m_calendar.get(Calendar.MINUTE)) {
					selectedIndex = i;
				}
			Date time = m_calendar.getTime();
			String strTime = m_tf.format(time);
			array[i] = strTime;
			m_calendar.add(Calendar.MINUTE, 30);
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(array);
		this.setVisibleRowCount(10);
		this.setModel(model);
		if (selectedIndex != -1) {
			this.setSelectedIndex(selectedIndex);
		}
	}

	/**
	 * Enable/Disable the transfer of focus after selecting date.
	 */
	public void setTransferFocus(boolean bTransferFocus) {
		m_bTransferFocus = bTransferFocus;
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(ListSelectionEvent e) {
		int index = this.getSelectedIndex();
		if (index != -1) {
			int hour = index / 2;
			int minute = (int) (((float) index / 2 - hour) * 60);
			m_calendar.setTime(m_timeTarget);
			m_calendar.set(Calendar.HOUR_OF_DAY, hour);
			m_calendar.set(Calendar.MINUTE, minute);
			Date date = m_calendar.getTime();
			JPopupMenu popupMenu = this.getJPopupMenu();
			if (popupMenu != null) { // I'm not sure this is the correct code,
				// but here it is!
				Component invoker = popupMenu.getInvoker();
				this.getParent().remove(this); // Just being careful
				Container container = popupMenu.getParent();
				container.remove(popupMenu);
				popupMenu.setVisible(false);
				if (invoker != null)
					if (m_bTransferFocus) {
						invoker.transferFocus(); // Focus on next component
						// after invoker
					}
			}
			Date oldTime = m_timeSelected;
			if (m_timeSelected == m_timeTarget) {
				oldTime = null;
			}
			this.firePropertyChange(m_strDateParam, oldTime, date);
		}
	}
}
