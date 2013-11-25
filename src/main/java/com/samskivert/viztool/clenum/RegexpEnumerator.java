//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.clenum;

import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * The regex enumerator filters classes based on a regular expression.
 */
public class RegexpEnumerator extends FilterEnumerator
{
    public RegexpEnumerator (String regexp, String exregex, Iterator<String> source)
        throws PatternSyntaxException
    {
        super(source);
        _regexp = Pattern.compile(regexp);
        if (exregex != null) {
            _exreg = Pattern.compile(exregex);
        }
    }

    protected boolean filterClass (String clazz)
    {
        return !(_regexp.matcher(clazz).matches() &&
                 (_exreg == null || !_exreg.matcher(clazz).matches()));
    }

    protected Pattern _regexp;
    protected Pattern _exreg;
}
