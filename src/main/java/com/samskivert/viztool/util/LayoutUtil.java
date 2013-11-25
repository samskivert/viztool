//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool.util;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Layout related utility functions.
 */
public class LayoutUtil
{
    /**
     * The number of points surrounding the name of the chain.
     */
    public static double HEADER_BORDER = 3;

    /**
     * The number of points of spacing between each child chain.
     */
    public static double GAP = 4;

    /**
     * The number of points that interfaces, inner classes and generally
     * any text that is subordinate to other text is indented.
     */
    public static double SUBORDINATE_INSET = 3;

    /**
     * Returns a rectangle that contains the supplied text with space
     * around the text for an aesthetically pleasing border.
     */
    public static Rectangle2D getTextBox (
        Font font, FontRenderContext frc, String text)
    {
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle2D bounds = layout.getBounds();
        // incorporate room for the border in the bounds
        bounds.setRect(bounds.getX(), bounds.getY(),
                       bounds.getWidth() + 2*HEADER_BORDER, 
                       bounds.getHeight() + 2*HEADER_BORDER);
        return bounds;
    }

    /**
     * Returns a rectangle that accomodates the specified text at the
     * bottom of the supplied rectangle, taking into account the preferred
     * text spacing and the specified inset for the accomodated text.
     */
    public static Rectangle2D accomodate (
        Rectangle2D bounds, Font font, FontRenderContext frc, double inset,
        String text)
    {
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle2D tbounds = layout.getBounds();
        bounds.setRect(bounds.getX(), bounds.getY(),
                       Math.max(bounds.getWidth(), tbounds.getWidth()+inset),
                       bounds.getHeight() + tbounds.getHeight());
        return bounds;
    }

    /**
     * Returns a rectangle that accomodates the specified lines of text at
     * the bottom of the supplied rectangle, taking into account the
     * preferred text spacing and the specified inset for the accomodated
     * text.
     */
    public static Rectangle2D accomodate (
        Rectangle2D bounds, Font font, FontRenderContext frc, double inset,
        String[] text)
    {
        double maxwid = bounds.getWidth();
        double height = 0;

        for (int i = 0; i < text.length; i++) {
            TextLayout layout = new TextLayout(text[i], font, frc);
            Rectangle2D tbounds = layout.getBounds();
            maxwid = Math.max(maxwid, tbounds.getWidth()+inset);
            height += tbounds.getHeight();
        }

        bounds.setRect(bounds.getX(), bounds.getY(),
                       maxwid, bounds.getHeight() + height);
        return bounds;
    }

    /**
     * Returns a rectangle that accomodates the two specified columns of
     * text at the bottom of the supplied rectangle, taking into account
     * the preferred text spacing and the specified inset for the
     * accomodated text.
     */
    public static Rectangle2D accomodate (
        Rectangle2D bounds, Font font, FontRenderContext frc, double inset,
        String[] left, String[] right)
    {
        double maxleft = 0, maxwid = bounds.getWidth();
        double height = 0;

        Rectangle2D[] bndl = new Rectangle2D[left.length];
        Rectangle2D[] bndr = new Rectangle2D[right.length];

        // first compute our dimensions
        for (int i = 0; i < left.length; i++) {
            bndl[i] = new TextLayout(left[i], font, frc).getBounds();
            bndr[i] = new TextLayout(right[i], font, frc).getBounds();
            maxleft = Math.max(maxleft, bndl[i].getWidth());
        }

        // now that we have the maxleft width we can calculate the rest
        for (int i = 0; i < left.length; i++) {
            maxwid = Math.max(maxwid, maxleft+GAP+bndr[i].getWidth()+inset);
            height += Math.max(bndl[i].getHeight(), bndr[i].getHeight());
        }

        bounds.setRect(bounds.getX(), bounds.getY(),
                       maxwid, bounds.getHeight() + height);
        return bounds;
    }
}
