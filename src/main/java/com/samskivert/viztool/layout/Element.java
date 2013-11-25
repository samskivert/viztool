//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.layout;

import java.awt.geom.Rectangle2D;

/**
 * A page is composed of elements which have some rectangular bounds.
 * They can be laid out by <code>ElementLayout</code> implementations in a
 * general purpose way.
 */
public interface Element
{
    /**
     * Returns the name of this element.
     */
    public String getName ();

    /**
     * Returns the bounds of this element.
     */
    public Rectangle2D getBounds ();

    /**
     * Sets the bounds of this element.
     */
    public void setBounds (double x, double y, double width, double height);
}
