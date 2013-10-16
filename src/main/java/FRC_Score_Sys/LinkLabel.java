package FRC_Score_Sys;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class LinkLabel extends JTextField implements MouseListener, FocusListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	/** The target or href of this link. */
	private URI target;
	public Color standardColor = new Color(0, 0, 255);
	public Color hoverColor = new Color(255, 0, 0);
	public Color activeColor = new Color(128, 0, 128);

	public Color transparent = new Color(0, 0, 0, 0);

	public boolean underlineVisible = true;
	private Border activeBorder;
	private Border hoverBorder;

	private Border standardBorder;

	/**
	 * Construct a LinkLabel that points to the given target. The URI will be
	 * used as the link text.
	 */
	public LinkLabel(URI target) {
		this(target, target.toString());
	}

	/**
	 * Construct a LinkLabel that points to the given target, and displays the
	 * text to the user.
	 */
	public LinkLabel(URI target, String text) {
		super(text);
		this.target = target;
	}

	/** Browse to the target. */
	@Override
	public void actionPerformed(ActionEvent ae) {
		browse();
	}

	/**
	 * Browse to the target URI using the Desktop.browse(URI) method. For visual
	 * indication, change to the active color at method start, and return to the
	 * standard color once complete. This is usually so fast that the active
	 * color does not appear, but it will take longer if there is a problem
	 * finding/loading the browser or URI (e.g. for a File).
	 */
	public void browse() {
		setForeground(activeColor);
		setBorder(activeBorder);
		try {
			Desktop.getDesktop().browse(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setForeground(standardColor);
		setBorder(standardBorder);
	}

	/** Set the color to the hover color. */
	@Override
	public void focusGained(FocusEvent fe) {
		setForeground(hoverColor);
		setBorder(hoverBorder);
	}

	/** Set the color to the standard color. */
	@Override
	public void focusLost(FocusEvent fe) {
		setForeground(standardColor);
		setBorder(standardBorder);
	}

	/*
	 * Add the listeners, configure the field to look and act like a link.
	 */
	public void init() {
		addMouseListener(this);
		addFocusListener(this);
		addActionListener(this);
		setToolTipText(target.toString());

		if (underlineVisible) {
			activeBorder = new MatteBorder(0, 0, 1, 0, activeColor);
			hoverBorder = new MatteBorder(0, 0, 1, 0, hoverColor);
			standardBorder = new MatteBorder(0, 0, 1, 0, transparent);
		} else {
			activeBorder = new MatteBorder(0, 0, 0, 0, activeColor);
			hoverBorder = new MatteBorder(0, 0, 0, 0, hoverColor);
			standardBorder = new MatteBorder(0, 0, 0, 0, transparent);
		}

		// make it appear like a label/link
		setEditable(false);
		setForeground(standardColor);
		setBorder(standardBorder);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/** Browse to the target. */
	@Override
	public void mouseClicked(MouseEvent me) {
		browse();
	}

	/** Set the color to the hover color. */
	@Override
	public void mouseEntered(MouseEvent me) {
		setForeground(hoverColor);
		setBorder(hoverBorder);
	}

	/** Set the color to the standard color. */
	@Override
	public void mouseExited(MouseEvent me) {
		setForeground(standardColor);
		setBorder(standardBorder);
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	/* Set the active color for this link (default is purple). */
	public void setActiveColor(Color active) {
		activeColor = active;
	}

	/* Set the hover/focused color for this link (default is red). */
	public void setHoverColor(Color hover) {
		hoverColor = hover;
	}

	/*
	 * Set the standard (non-focused, non-active) color for this link (default
	 * is blue).
	 */
	public void setStandardColor(Color standard) {
		standardColor = standard;
	}

	/** Determines whether the */
	public void setUnderlineVisible(boolean underlineVisible) {
		this.underlineVisible = underlineVisible;
	}

}
