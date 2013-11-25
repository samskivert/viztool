//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * The element layout is used to lay a collection of elements out on a
 * page. It computes the desired position of each element and sets it via
 * <code>setBounds()</code> with the expectation that the location of the
 * elements will be used later in the rendering process.
 */
public interface ElementLayout
{
    /**
     * Lay out the supplied list of elements. Any elements that do not fit
     * into the allotted space should be added to the overflow list. The
     * supplied elements list should not be modified.
     *
     * @return the bounding dimensions of the collection of elements that
     * were laid out.
     */
    public <E extends Element> Rectangle2D layout (
        List<E> elements, double pageWidth, double pageHeight, List<E> overflow);
}
