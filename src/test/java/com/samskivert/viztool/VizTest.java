//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import com.samskivert.swing.util.SwingUtil;
import com.samskivert.viztool.clenum.ClassEnumerator;
import com.samskivert.viztool.clenum.FilterEnumerator;
import com.samskivert.viztool.clenum.RegexpEnumerator;
import com.samskivert.viztool.hierarchy.HierarchyVisualizer;
import com.samskivert.viztool.summary.SummaryVisualizer;
import com.samskivert.viztool.util.FontPicker;

public class VizTest {

    public static void main (String[] args) {
        // parse our arguments
        String pkgroot = "com.samskivert.viztool";
        String regexp = "com.samskivert.viztool.*";
        boolean print = false;

        // run ourselves on the classpath
        String classpath = System.getProperty("java.class.path");
        System.err.println("Scanning " + classpath + ".");
        ClassEnumerator clenum = new ClassEnumerator(classpath);
        for (String warning : clenum.getWarningStrings()) {
            System.err.println("Warning: " + warning);
        }

        // initialize the font picker
        FontPicker.init(print);

        // and finally generate the visualization
        FilterEnumerator fenum = null;
        try {
            fenum = new RegexpEnumerator(regexp, null, clenum);
        } catch  (Exception e) {
            Log.warning("Invalid package regular expression " +
                        "[regexp=" + regexp + ", error=" + e + "].");
            System.exit(-1);
        }

        List<Class<?>> classes = new ArrayList<Class<?>>();
        while (fenum.hasNext()) {
            String cname = fenum.next();
            // skip inner classes, the visualizations pick those up
            // themselves
            if (cname.indexOf("$") != -1) {
                continue;
            }
            try {
                classes.add(Class.forName(cname));
            } catch (Throwable t) {
                Log.warning("Unable to introspect class [class=" + cname +
                            ", error=" + t + "].");
            }
        }

        Visualizer viz = new HierarchyVisualizer();
        // Visualizer viz = new SummaryVisualizer();
        viz.setPackageRoot(pkgroot);
        viz.setClasses(classes.iterator());

        if (print) {
            // we use the print system to render things
            PrinterJob job = PrinterJob.getPrinterJob();

            // pop up a dialog to format our pages
            // PageFormat format = job.pageDialog(job.defaultPage());
            PageFormat format = job.defaultPage();

            // use sensible margins
            Paper paper = new Paper();
            paper.setImageableArea(72*0.5, 72*0.5, 72*7.5, 72*10);
            format.setPaper(paper);

            // use our configured page format
            job.setPrintable(viz, format);

            // pop up a dialog to control printing
            if (job.printDialog()) {
                try {
                    // invoke the printing process
                    job.print();
                } catch (PrinterException pe) {
                    pe.printStackTrace(System.err);
                }

            } else {
                Log.info("Printing cancelled.");
            }

            // printing starts up the AWT threads, so we have to
            // explicitly exit at this point
            System.exit(0);

        } else {
            VizFrame frame = new VizFrame(viz);
            frame.pack();
            SwingUtil.centerWindow(frame);
            frame.setVisible(true);
        }
    }
}
