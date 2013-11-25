//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.clenum;

import java.util.Iterator;

/**
 * The package enumerator filters out only classes from the specified
 * package from the class enumerator provided at construct time.
 */
public class PackageEnumerator extends FilterEnumerator
{
    public PackageEnumerator (String pkg, Iterator<String> source, boolean subpkgs)
    {
        super(source);
        _package = pkg;
        _subpkgs = subpkgs;
    }

    protected boolean filterClass (String clazz)
    {
        if (!clazz.startsWith(_package)) {
            return true;
        }

        return _subpkgs ? false:
            (clazz.substring(_package.length()+1).indexOf(".") != -1);
    }

    public static void main (String[] args)
    {
        // run ourselves on the classpath
        String classpath = System.getProperty("java.class.path");
        ClassEnumerator clenum = new ClassEnumerator(classpath);
        for (String warning : clenum.getWarningStrings()) {
            System.out.println("Warning: " + warning);
        }

        // enumerate over whatever classes match our package
        String pkg = "com.samskivert.viztool.clenum";
        PackageEnumerator penum = new PackageEnumerator(pkg, clenum, true);
        while (penum.hasNext()) {
            System.out.println("Class: " + penum.next());
        }
    }

    protected String _package;
    protected boolean _subpkgs;
}
