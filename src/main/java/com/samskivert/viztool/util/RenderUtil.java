//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.util;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Rendering related utility functions.
 */
public class RenderUtil
{
    /**
     * Renders a string to the specified graphics context, in the specified font at the specified
     * coordinates.
     *
     * @return the bounds occupied by the rendered string.
     */
    public static Rectangle2D renderString (Graphics2D gfx, FontRenderContext frc, Font font,
                                            boolean withLeading, double x, double y, String text)
    {
        // do the rendering
        TextLayout ilay = new TextLayout(text, font, frc);
        float dy = withLeading ? ilay.getLeading() : 0;
        Rectangle2D ibounds = ilay.getBounds();
        ilay.draw(gfx, (float)(x - ibounds.getX()), (float)(y + dy + ilay.getAscent()));

        // return the dimensions occupied by the rendered string
        return new Rectangle2D.Double(
            x, y, ibounds.getWidth(), dy + ilay.getAscent() + ilay.getDescent());
    }

    /**
     * Renders an array of strings to the specified graphics context, in the specified font at the
     * specified coordinates.
     *
     * @return the bounds occupied by the rendered strings.
     */
    public static Rectangle2D renderStrings (Graphics2D gfx, FontRenderContext frc, Font font,
                                             boolean withLeading, double x, double y, String[] text)
    {
        return renderStrings(gfx, frc, font, withLeading, x, y, text, (String)null);
    }

    /**
     * Renders an array of strings to the specified graphics context, in the specified font at the
     * specified coordinates. If prefix is non-null, it will be prefixed to the first string and
     * subsequent strings will be rendered with the space necessary to line them up with the first
     * string.
     *
     * @return the bounds occupied by the rendered strings.
     */
    public static Rectangle2D renderStrings (Graphics2D gfx, FontRenderContext frc, Font font,
                                             boolean withLeading, double x, double y, String[] text,
                                             String prefix)
    {
        double maxwid = 0, starty = y;
        double inset = 0;

        if (prefix != null) {
            TextLayout play = new TextLayout(prefix, font, frc);
            inset = play.getBounds().getWidth();
        }

        for (int i = 0; i < text.length; i++) {
            // figure some stuff out
            String string = (i == 0 && prefix != null) ? (prefix + text[i]) : text[i];
            double sinset = ((i == 0) ? 0 : inset);

            // do the rendering
            TextLayout ilay = new TextLayout(string, font, frc);
            if (i > 0 || withLeading) y += ilay.getLeading();
            Rectangle2D ibounds = ilay.getBounds();
            y += ilay.getAscent();
            ilay.draw(gfx, (float)(x - ibounds.getX() + sinset), (float)y);

            maxwid = Math.max(sinset + ibounds.getWidth(), maxwid);
            y += ilay.getDescent();
        }

        // return the dimensions occupied by the rendered strings
        return new Rectangle2D.Double(x, y, maxwid, y-starty);
    }

    /**
     * Renders a two column array of strings to the specified graphics context, in the specified
     * font at the specified coordinates. The left column is right align and the right, left
     * aligned.
     *
     * @return the bounds occupied by the rendered strings.
     */
    public static Rectangle2D renderStrings (Graphics2D gfx, FontRenderContext frc, Font font,
                                             boolean withLeading, double x, double y,
                                             String[] left, String[] right)
    {
        double maxleft = 0, maxwid = 0, starty = y;

        // first generate text layout instances and compute bounds for all entries in both columns
        TextLayout[] llay = new TextLayout[left.length];
        Rectangle2D[] lbnds = new Rectangle2D[left.length];
        TextLayout[] rlay = new TextLayout[right.length];
        Rectangle2D[] rbnds = new Rectangle2D[right.length];

        // compute the dimensions
        for (int i = 0; i < left.length; i++) {
            llay[i] = new TextLayout(left[i], font, frc);
            lbnds[i] = llay[i].getBounds();
            rlay[i] = new TextLayout(right[i], font, frc);
            rbnds[i] = rlay[i].getBounds();
            maxleft = Math.max(maxleft, lbnds[i].getWidth());
        }

        // do the rendering
        for (int i = 0; i < left.length; i++) {
            TextLayout ll = llay[i], rl = rlay[i];
            if (i > 0 || withLeading) y += Math.max(ll.getLeading(), rl.getLeading());
            double lw = lbnds[i].getWidth();
            // we use rl's ascent here for both strings because the right hand side (usually being
            // the method declaration), tends to be taller than the left hand side (because of the
            // parenthesis) and would appear a bit lower than the left hand side if we didn't use
            // it's y offset
            y += rl.getAscent();
            ll.draw(gfx, (float)(x - lbnds[i].getX() + maxleft - lw), (float)y);
            rl.draw(gfx, (float)(x - rbnds[i].getX() + maxleft + LayoutUtil.GAP), (float)y);
            maxwid = Math.max(maxwid, maxleft + LayoutUtil.GAP + rbnds[i].getWidth());
            y += Math.max(ll.getDescent(), rl.getDescent());
        }

        // return the dimensions occupied by the rendered strings
        return new Rectangle2D.Double(x, y, maxwid, y-starty);
    }
}
