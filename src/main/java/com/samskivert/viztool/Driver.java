//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool;

import java.util.ArrayList;
import java.util.List;

import com.samskivert.swing.util.SwingUtil;
import com.samskivert.viztool.clenum.*;

import com.samskivert.viztool.summary.SummaryVisualizer;
import com.samskivert.viztool.util.FontPicker;

/**
 * The application driver. This class parses the command line arguments
 * and invokes the visualization code.
 */
public class Driver
{
    public static void main (String[] args)
    {
        if (args.length < 1) {
            System.err.println(USAGE);
            System.exit(-1);
        }

        // parse our arguments
        String pkgroot = "";
        String regexp = null;
        boolean print = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-print")) {
                print = true;
            } else if (regexp == null) {
                regexp = args[i];
            }
        }

        // run ourselves on the classpath
        String classpath = System.getProperty("java.class.path");
        // System.err.println("Scanning " + classpath + ".");
        ClassEnumerator clenum = new ClassEnumerator(classpath);

        // print out the warnings
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

        // Visualizer viz = new HierarchyVisualizer(pkgroot, penum);
        Visualizer viz = new SummaryVisualizer();
        viz.setPackageRoot(pkgroot);
        viz.setClasses(classes.iterator());

        if (print) {
            try {
                if (!PrintUtil.print(viz, null)) {
                    Log.info("Printing cancelled.");
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            // printing starts up the AWT threads, so we have to explicitly exit at this point
            System.exit(0);

        } else {
            VizFrame frame = new VizFrame(viz);
            frame.pack();
            SwingUtil.centerWindow(frame);
            frame.setVisible(true);
        }
    }

    protected static final String USAGE =
        "Usage: Driver [-mode hier|sum] [-print] package_regexp " +
        "[package_root]\n" +
        "       hier = class hierarchy visualization\n" +
        "       sum = class summary visualization\n"
        ;
}
