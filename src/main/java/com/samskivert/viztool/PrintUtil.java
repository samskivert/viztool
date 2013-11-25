//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;

public class PrintUtil
{
    public static boolean print (Printable pable, File output) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();

        // use sensible margins
        PageFormat format = job.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(72*0.5, 72*0.5, 72*7.5, 72*10);
        format.setPaper(paper);

        job.setPrintable(pable, format);

        // pop up a dialog to control printing
        if (!job.printDialog()) return false;

        // invoke the printing process
        job.print();
        return true;
    }
}
