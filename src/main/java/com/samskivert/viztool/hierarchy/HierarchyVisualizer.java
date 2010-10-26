//
// $Id$
// 
// viztool - a tool for visualizing collections of java classes
// Copyright (C) 2001 Michael Bayne
// 
// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation; either version 2.1 of the License, or (at your
// option) any later version.
// 
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.
// 
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.samskivert.viztool.hierarchy;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;
import java.util.*;

import com.samskivert.util.CollectionUtil;

import com.samskivert.viztool.Visualizer;

/**
 * The hierarchy visualizer displays inheritance hierarchies in a compact
 * representation so that an entire package can be displayed on a single
 * page (or small number of pages).
 */
public class HierarchyVisualizer implements Visualizer
{
    // documentation inherited
    public void setPackageRoot (String pkgroot)
    {
        _pkgroot = pkgroot;
    }

    // documentation inherited
    public void setClasses (Iterator<Class<?>> iter)
    {
        // dump all the classes into an array list so that we can repeatedly scan through the list
        CollectionUtil.addAll(_classes, iter);

        // compile a list of all packages in our collection
        Set<String> pkgset = new HashSet<String>();
        for (Class<?> cl : _classes) {
            pkgset.add(ChainUtil.pkgFromClass(cl.getName()));
        }

        // sort our package names
        pkgset.toArray(_packages = new String[pkgset.size()]);
        Arrays.sort(_packages);
        // System.err.println("Scanned " + _packages.length + " packages.");

        // now create chain groups for each package
        _groups = new ArrayList<ChainGroup>();
        for (String pkg : _packages) {
            _groups.add(new ChainGroup(_pkgroot, pkg, _classes.iterator()));
        }
    }

    /**
     * Lays out and renders each of the chain groups that make up this package hierarchy
     * visualization.
     */
    public int print (Graphics g, PageFormat pf, int pageIndex)
        throws PrinterException
    {
        Graphics2D gfx = (Graphics2D)g;

        // adjust the stroke
        gfx.setStroke(new BasicStroke(0.1f));

        // only relay things out if the page format has changed
        if (!pf.equals(_format)) {
            // keep this around
            _format = pf;

            // and do the layout
            layout(gfx, pf.getImageableX(), pf.getImageableY(),
                   pf.getImageableWidth(), pf.getImageableHeight());
        }

        // render the groups on the requested page
        int rendered = 0;
        for (ChainGroup group : _groups) {
            if (group.getPage() != pageIndex) {
                continue; // skip groups not on this page
            }
            Rectangle2D bounds = group.getBounds();
            group.render(gfx, bounds.getX(), bounds.getY());
            rendered++;
        }

        return (rendered > 0) ? PAGE_EXISTS : NO_SUCH_PAGE;
    }

    public void layout (Graphics2D gfx, double x, double y, double width, double height)
    {
        double starty = y;
        int pageno = 0;

        // lay out our groups
        for (int ii = 0; ii < _groups.size(); ii++) {
            ChainGroup group = _groups.get(ii);
            // lay out the group in question
            ChainGroup ngrp = group.layout(gfx, width, height);
            // if the process of laying this group out caused it to become split across pages,
            // insert this new group into the list
            if (ngrp != null) {
                _groups.add(ii+1, ngrp);
            }

            // determine if we need to skip to the next page or not
            Rectangle2D bounds = group.getBounds();
            if ((y > starty) && (y + bounds.getHeight() > height + starty)) {
                y = starty;
                pageno++;
            }

            // assign x and y coordinates to this group
            group.setPosition(x, y);
            // make a note of our page index
            group.setPage(pageno);

            // increment our y location
            y += (bounds.getHeight() + GAP);
        }

        // our page count is one more than the highest page number
        _pageCount = pageno+1;
    }

    public void paint (Graphics2D gfx, int pageIndex)
    {
        // render the groups on the requested page
        for (ChainGroup group : _groups) {
            if (group.getPage() != pageIndex) {
                continue; // skip groups not on this page
            }
            Rectangle2D bounds = group.getBounds();
            group.render(gfx, bounds.getX(), bounds.getY());
        }
    }

    /**
     * Returns the number of pages occupied by this visualization. This is only valid after a call
     * to {@link #layout}.
     *
     * @return the page count or -1 if we've not yet been laid out.
     */
    public int getPageCount ()
    {
        return _pageCount;
    }

    protected String _pkgroot;
    protected List<Class<?>> _classes = new ArrayList<Class<?>>();

    protected String[] _packages;
    protected List<ChainGroup> _groups;
    protected int _pageCount = -1;

    protected PageFormat _format;

    protected static final int GAP = 72/4;
}
