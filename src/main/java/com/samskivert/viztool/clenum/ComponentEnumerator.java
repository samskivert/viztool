//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.clenum;

/**
 * A component enumerator knows how to enumerate all of the classes in a particular classpath
 * component. Examples include a zip file enumerator, a directory tree enumerator and a jar file
 * enumerator.
 */
public abstract class ComponentEnumerator
{
    /**
     * To determine which component enumerator should be used for a given classpath component, one
     * instance of each is maintained and used for the matching process.
     *
     * @return true if this enumerator should be used to enumerate the specified classpath
     * component; false otherwise.
     */
    public abstract boolean matchesComponent (String component);

    /**
     * Instantiates an instance of the underlying enumerator and configures it to enumerate the
     * specified classpath component.
     *
     * @exception EnumerationException thrown if some problem (like file or directory not existing
     * or being inaccessible) prevents the enumerator from enumerating the component.
     */
    public abstract ComponentEnumerator enumerate (String component)
        throws EnumerationException;

    /**
     * Returns true if there are more classes yet to be enumerated for this component.
     */
    public abstract boolean hasMoreClasses ();

    /**
     * Returns the next class in this component's enumeration.
     */
    public abstract String nextClass ();

    /**
     * Converts a classfile path to a class name (eg. foo/bar/Baz.class converts to foo.bar.Baz).
     */
    protected String pathToClassName (String path)
    {
        // strip off the .class suffix
        path = path.substring(0, path.length() - CLASS_SUFFIX.length());
        // convert slashes to dots
        return path.replace("/", ".");
    }

    /**
     * This is used to identify class files.
     */
    protected static final String CLASS_SUFFIX = ".class";
}
