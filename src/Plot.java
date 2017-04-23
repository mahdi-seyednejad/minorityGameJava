/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package geneticimprove;

import java.awt.Font;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author mehdi
 */
public class Plot {
    Plot2DPanel plot;

    public Plot (String title, double [] y)
    {
        plot = new Plot2DPanel();
        double [] x = new double [y.length];
        for (int i=0; i<x.length;i++)
            x[i]=i;
        plot.addLinePlot(title, x, y);
        Font myfont = new Font("AxesNum", 3, 30);
        plot.setFont(myfont);
        JFrame frame = new JFrame("A Plot Pannel");
        frame.setContentPane(plot);
        frame.setBounds(100, 100, 800, 550);
        frame.setVisible(true);
    }

    public Plot (String title, double [] x, double [] y)
    {
        Font myfont = new Font("MyAxeFont", Font.BOLD, 10);
        plot = new Plot2DPanel();
        plot.setFont(myfont);

        plot.addLinePlot(title, x, y);

        JFrame frame = new JFrame(title);

        frame.setContentPane(plot);
        frame.setBounds(100, 100, 800, 550);
        frame.setVisible(true);
    }

    public void addLine (double [] Y)
    {
        plot.addLinePlot("h", Y);
    }





}
