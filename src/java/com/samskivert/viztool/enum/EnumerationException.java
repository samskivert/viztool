//
// $Id$

package com.samskivert.viztool.enum;

/**
 * An enumeration exception is thrown when some problem occurs while
 * attempting to enumerate over a classpath component. This may be when
 * initially attempting to read a zip or jar file, or during the process
 * of enumeration.
 */
public class EnumerationException extends Exception
{
    public EnumerationException (String message)
    {
        super(message);
    }
}
