//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.clenum;

/**
 * An enumeration exception is thrown when some problem occurs while attempting to enumerate over a
 * classpath component. This may be when initially attempting to read a zip or jar file, or during
 * the process of enumeration.
 */
public class EnumerationException extends Exception
{
    public EnumerationException (String message)
    {
        super(message);
    }
}
