//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

/**
 * A very simple UI element for displaying visualizations on screen.
 */
public class VizPanel extends JPanel
{
    /**
     * Constructs a panel for displaying a particular visualization.
     */
    public VizPanel (Visualizer viz)
    {
        // we'll need this later
        _viz = viz;

        // set the font
        Font font = new Font("Courier", Font.PLAIN, 10);
        setFont(font);
    }

    public void doLayout ()
    {
        super.doLayout();
        Graphics2D gfx = (Graphics2D)getGraphics();
        Rectangle2D bounds = getBounds();
        _viz.layout(gfx, 0, 0, bounds.getWidth(), bounds.getHeight());
    }

    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);
        _viz.paint((Graphics2D)g, _currentPage);
    }

    public Dimension getPreferredSize ()
    {
        return new Dimension(PAGE_WIDTH, PAGE_HEIGHT);
    }

    /**
     * Returns the number of pages in our current visualization. This is
     * only valid after we've been rendered at least once because we
     * needed the rendering graphics to realize the visualization.
     */
    public int getPageCount ()
    {
        return _viz.getPageCount();
    }

    /**
     * Requests that the panel display the specified page number (indexed
     * from zero). Requests to display invalid page numbers will be
     * ignored.
     */
    public void setPage (int pageno)
    {
        if (pageno < _viz.getPageCount()) {
            _currentPage = pageno;
            repaint();

        } else {
            Log.warning("Requested to display invalid page " +
                        "[pageno=" + pageno +
                        ", pages=" + _viz.getPageCount() + "].");
        }
    }

    /**
     * Returns the index of the page that we're currently displaying.
     */
    public int getPage ()
    {
        return _currentPage;
    }

    /**
     * Returns the visualizer we're currently displaying.
     */
    public Visualizer getVisualizer ()
    {
        return _viz;
    }

    protected Visualizer _viz;
    protected int _currentPage = 0;

    // our preferred size is one page at 72 pixels per inch
    protected static final int PAGE_WIDTH = (int)(72 * 8.5);
    protected static final int PAGE_HEIGHT = (int)(72 * 11.0);
}
