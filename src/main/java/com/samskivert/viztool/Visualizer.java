//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool;

import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.util.Iterator;

/**
 * The interface via which the driver accesses whichever visualizer is
 * desired for a particular invocation.
 */
public interface Visualizer extends Printable
{
    /**
     * Provides the visualizer with the root package which it can use to
     * format package names relative to the root package.
     */
    public void setPackageRoot (String pkgroot);

    /**
     * Provides the visualizer with an iterator over all of the {@link
     * Class} instances that it will be visualizing.
     */
    public void setClasses (Iterator<Class<?>> iterator);

    /**
     * Requests that the visualization lay itself out in pages with the
     * specified dimensions. Subsequent calls to {@link #print} or {@link
     * #paint} will assume that things are laid out according to the most
     * recent call to this method.
     */
    public void layout (Graphics2D gfx, double x, double y,
                        double width, double height);

    /**
     * Renders the specified page of this visualization.
     */
    public void paint (Graphics2D gfx, int pageIndex);

    /**
     * Returns the number of pages occupied by the visualization.
     */
    public int getPageCount ();
}
