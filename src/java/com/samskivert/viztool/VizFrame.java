//
// $Id$

package com.samskivert.viztool;

import java.awt.BorderLayout;
import javax.swing.*;

import com.samskivert.viztool.viz.HierarchyVisualizer;

/**
 * The top-level frame in which visualizations are displayed.
 */
public class VizFrame extends JFrame
{
    public VizFrame (HierarchyVisualizer viz)
    {
        super("viztool");

        // quit if we're closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        VizPanel panel = new VizPanel(viz);
        getContentPane().add(panel, BorderLayout.CENTER);
    }
}
