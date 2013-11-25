//
// viztool - a tool for visualizing collections of java classes
// Copyright (c) 2001-2013, Michael Bayne - All rights reserved.
// http://github.com/samskivert/viztool/blob/master/LICENSE

package com.samskivert.viztool;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.samskivert.swing.*;

/**
 * The top-level frame in which visualizations are displayed.
 */
public class VizFrame extends JFrame
{
    public VizFrame (Visualizer viz)
    {
        super("viztool");

        // quit if we're closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // create our controller and panel for displaying visualizations
        VizPanel vpanel = new VizPanel(viz);
        VizController vctrl = new VizController(vpanel);

        // create some control buttons
        GroupLayout gl = new HGroupLayout(GroupLayout.NONE);
        gl.setJustification(GroupLayout.RIGHT);
        JPanel bpanel = new JPanel(gl);
        JButton btn;

        btn = new JButton("Print");
        btn.setActionCommand(VizController.PRINT);
        btn.addActionListener(VizController.DISPATCHER);
        bpanel.add(btn);

        btn = new JButton("Previous page");
        btn.setActionCommand(VizController.BACKWARD_PAGE);
        btn.addActionListener(VizController.DISPATCHER);
        bpanel.add(btn);

        btn = new JButton("Next page");
        btn.setActionCommand(VizController.FORWARD_PAGE);
        btn.addActionListener(VizController.DISPATCHER);
        bpanel.add(btn);

        btn = new JButton("Quit");
        btn.setActionCommand(VizController.QUIT);
        btn.addActionListener(VizController.DISPATCHER);
        bpanel.add(btn);

        // create a content pane to contain everything
        JPanel content = new ContentPanel(vctrl);
        gl = new VGroupLayout(GroupLayout.STRETCH);
        content.setLayout(gl);
        content.setBorder(BorderFactory.createEmptyBorder(
            BORDER, BORDER, BORDER, BORDER));
        content.add(vpanel);
        content.add(bpanel, GroupLayout.FIXED);
        setContentPane(content);
    }

    protected static final class ContentPanel extends JPanel
        implements ControllerProvider
    {
        public ContentPanel (VizController ctrl)
        {
            _ctrl = ctrl;
        }

        public Controller getController ()
        {
            return _ctrl;
        }

        protected Controller _ctrl;
    }

    protected static final int BORDER = 5; // pixels
}
