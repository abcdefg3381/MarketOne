/**
 * TimeButton.java

 * @author Don Corley <don@donandann.com>
 * @version 1.0.0
 */

package org.sourceforge.jcalendarbutton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import maggie.network.gui.JCalendarPopup;

/**
 * A JTimeButton is a button that displays a popup time (A JTimePopup). Note:
 * This button doesn't use some of the global constants and methods because it
 * is used in other programs where they are not available.
 * 
 * @author Administrator
 * @version 1.0.0
 */
public class JTimeButton extends JButton implements PropertyChangeListener, ActionListener {
	/**
	 * The language property key.
	 */
	public static final String LANGUAGE_PARAM = "language";

	private static final long serialVersionUID = 1L;
	/**
	 * The language to use.
	 */
	protected String m_strLanguage = null;
	/**
	 * The name of the date property (defaults to "time").
	 */
	protected String m_strTimeParam = JCalendarPopup.DATE_PARAM;
	/**
	 * The initial date for this button.
	 */
	protected Date m_timeTarget = null;

	/**
	 * Creates new TimeButton.
	 */
	public JTimeButton() {
		super();
		// Get current classloader
		ClassLoader cl = this.getClass().getClassLoader();
		// Create icons
		try {
			Icon icon =
					new ImageIcon(cl.getResource("images/buttons/" + JTimePopup.TIME_ICON
							+ ".gif"));
			this.setIcon(icon);
		} catch (Exception ex) {
			this.setText("change");
		}

		this.setMargin(JCalendarPopup.NO_INSETS);
		this.setOpaque(false);

		this.addActionListener(this);
	}

	/**
	 * Creates new TimeButton.
	 * 
	 * @param dateTarget
	 *            The initial date for this button.
	 */
	public JTimeButton(Date timeTarget) {
		this();
		this.init(null, timeTarget, null);
	}

	/**
	 * Creates new TimeButton.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to 'date').
	 * @param dateTarget
	 *            The initial date for this button.
	 */
	public JTimeButton(String strDateParam, Date timeTarget) {
		this();
		this.init(strDateParam, timeTarget, null);
	}

	/**
	 * Creates new TimeButton.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to 'date').
	 * @param dateTarget
	 *            The initial date for this button.
	 * @param strLanguage
	 *            The language to use.
	 */
	public JTimeButton(String strDateParam, Date timeTarget, String strLanguage) {
		this();
		this.init(strDateParam, timeTarget, strLanguage);
	}

	/**
	 * The user pressed the button, display the JTimePopup.
	 * 
	 * @param e
	 *            The ActionEvent.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this) {
			Date dateTarget = this.getTargetDate();
			JTimePopup popup =
					JTimePopup.createTimePopup(this.getDateParam(), dateTarget, this,
							m_strLanguage);
			popup.addPropertyChangeListener(this);
		}
	}

	/**
	 * Get the name of the date property for this button.
	 */
	public String getDateParam() {
		return m_strTimeParam;
	}

	/**
	 * Get the current date.
	 */
	public Date getTargetDate() {
		return m_timeTarget;
	}

	/**
	 * Creates new TimeButton.
	 * 
	 * @param strDateParam
	 *            The name of the date property (defaults to 'date').
	 * @param dateTarget
	 *            The initial date for this button.
	 * @param strLanguage
	 *            The language to use.
	 */
	public void init(String strTimeParam, Date timeTarget, String strLanguage) {
		if (strTimeParam == null) {
			strTimeParam = JCalendarPopup.DATE_PARAM;
		}
		m_strTimeParam = strTimeParam;
		m_timeTarget = timeTarget;
		m_strLanguage = strLanguage;
	}

	/**
	 * Propogate the change to my listeners. Watch for date and language
	 * changes, so I can keep up to date.
	 * 
	 * @param evt
	 *            The property change event.
	 */
	public void propertyChange(final java.beans.PropertyChangeEvent evt) {
		this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		if (m_strTimeParam.equalsIgnoreCase(evt.getPropertyName()))
			if (evt.getNewValue() instanceof Date) {
				m_timeTarget = (Date) evt.getNewValue();
			}
		if (LANGUAGE_PARAM.equalsIgnoreCase(evt.getPropertyName()))
			if (evt.getNewValue() instanceof String) {
				m_strLanguage = (String) evt.getNewValue();
			}
	}

	/**
	 * Set the current date.
	 */
	public void setTargetDate(Date timeTarget) {
		m_timeTarget = timeTarget;
	}
}
