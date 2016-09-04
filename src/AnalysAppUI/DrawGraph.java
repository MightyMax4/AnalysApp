package AnalysAppUI;

import DataClasses.CurrentParameter_Values;
import org.apache.poi.hssf.util.HSSFColor;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kost on 01.12.2014.
 */
public class DrawGraph extends JPanel {

    private static int PREF_W = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 15;
    private static int PREF_H  = Toolkit.getDefaultToolkit().getScreenSize().height - 110;

    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(255, 0, 0, 254);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static final Stroke GRAPH_STROKE_BOLD = new BasicStroke(3f);
    private int pointWidth = 5;
    private int numberYDivisions = 10;
    private FontMetrics metrics;
    private Font font = new Font("default", Font.ROMAN_BASELINE, 9);
    private Font fontbold = new Font("default", Font.BOLD, 12);

    private String level;
    private double sigma;
    private double average;
    private ArrayList<Double> scores = new ArrayList<Double>();
    private ArrayList<String> fl_date = new ArrayList<String>();

    public DrawGraph(String level) {
        this.level = level;
        this.fl_date = CurrentParameter_Values.getFiledate();
        if (level.equals("level1")) {
            this.average = CurrentParameter_Values.getAverage_level1();
            this.sigma = CurrentParameter_Values.getSigma_level1();
            this.scores = CurrentParameter_Values.getScores_level1();
        }
        if (level.equals("level2")) {
            this.average = CurrentParameter_Values.getAverage_level2();
            this.sigma = CurrentParameter_Values.getSigma_level2();
            this.scores = CurrentParameter_Values.getScores_level2();
        }
    }

    double getMinScore() {
        double min = average;
        for (int i = 0; i < scores.size(); i++) {
            if (min > scores.get(i)) {
                min = scores.get(i);
            }
        }
        for (int i = -2; i < 3;i++){
            if (min > average + sigma * i) {
                min = average + sigma * i;
            }
        }
        return min;
    }

    double getMaxScore(){
        double max = average;
        for (int i = 0; i < scores.size(); i++) {
            if (max < scores.get(i)) {
                max = scores.get(i);
            }
        }
        for (int i = -2; i < 3;i++){
            if (max < average + sigma * i) {
                max = average + sigma * i;
            }
        }
        return max;
    }

    public void drawRotate(Graphics2D g2, double x, double y, int angle, String text){
        g2.translate((float)x,(float)y);
        g2.rotate(Math.toRadians(angle));
        g2.drawString(text,0,0);
        g2.rotate(-Math.toRadians(angle));
        g2.translate(-(float)x,-(float)y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (level.equals("level1")) {
            this.average = CurrentParameter_Values.getAverage_level1();
            this.sigma = CurrentParameter_Values.getSigma_level1();
            this.scores = CurrentParameter_Values.getScores_level1();
        }

        if (level.equals("level2")) {
            this.average = CurrentParameter_Values.getAverage_level2();
            this.sigma = CurrentParameter_Values.getSigma_level2();
            this.scores = CurrentParameter_Values.getScores_level2();
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        metrics = g2.getFontMetrics();

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        double xScale, yScale;

        if (average != 0)
        {
            yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

            // create hatch marks and grid lines for y axis.
            for (int i = 0; i < numberYDivisions + 1; i++) {
                int x0 = padding + labelPadding;
                int x1 = pointWidth + padding + labelPadding;
                int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
                int y1 = y0;
                if (scores.size() > 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                    String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                    int labelWidth = metrics.stringWidth(yLabel);
                    g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                }
                g2.drawLine(x0, y0, x1, y1);
                if (i == numberYDivisions){
                    g2.setColor(Color.BLACK);
                    g2.setFont(fontbold);
                    g2.drawString("Кп", x0 - 25, padding - metrics.getHeight());
                    g2.drawString(level, (getWidth() - (2 * padding) - labelPadding) / 2 + padding + labelPadding - metrics.stringWidth(level) / 2, padding - metrics.getHeight());
                }
            }

            // average and sigma
            for (int i = -2; i < 3; i++) {
                int x0 = padding + labelPadding;
                int graph_average = (int) ((getMaxScore() - average - sigma * i) * yScale + padding);
                g2.setStroke(new BasicStroke(1.0f, // Width
                        BasicStroke.CAP_SQUARE, // End cap
                        BasicStroke.JOIN_MITER, // Join style
                        10.0f, // Miter limit
                        new float[]{16.0f, 20.0f}, // Dash pattern
                        0.0f)); // Dash phase);
                if (i == 0) {
                    g2.setStroke(GRAPH_STROKE_BOLD);
                }
                String value = String.format("%.2f", average + sigma * i);

                int labelWidth = metrics.stringWidth(value);
                g2.drawString(value, x0 - labelWidth - 5, graph_average + (metrics.getHeight() / 2) - 3);
                g2.drawLine(padding + labelPadding, graph_average, getWidth() - padding, graph_average);
            }

        }

        g2.setStroke(GRAPH_STROKE_BOLD);
        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        g2.setStroke(GRAPH_STROKE);
        if (scores.size() > 0) {

            xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size());
            yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

            ArrayList<Point> graphPoints = new ArrayList<Point>();
            for (int i = 0; i < scores.size(); i++) {
                int x1 = (int) ((i + 1) * xScale + padding + labelPadding);
                int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
                graphPoints.add(new Point(x1, y1));
            }

            // and for x axis
            for (int i = 1; i <= scores.size(); i++) {
                if (scores.size() > 0) {
                    int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size()) + padding + labelPadding;
                    int x1 = x0;
                    int y0 = getHeight() - padding - labelPadding;
                    int y1 = y0 - pointWidth;
                    if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                        g2.setColor(gridColor);
                        g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                        g2.setFont(font);
                        g2.setColor(Color.BLACK);
                        String xLabel = fl_date.get(i-1).substring(0,5);
                        int labelWidth = metrics.stringWidth(xLabel);
                        drawRotate(g2, x0 - labelWidth / 2, y0 + metrics.getHeight() + 10, -45, xLabel);
                    }
                    g2.drawLine(x0, y0, x1, y1);
                }
            }

            // draw lines between points
            Stroke oldStroke = g2.getStroke();
            g2.setColor(lineColor);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }

            // draw points
            g2.setStroke(oldStroke);
            g2.setColor(pointColor);
            for (int i = 0; i < graphPoints.size(); i++) {
                int x = graphPoints.get(i).x - pointWidth / 2;
                int y = graphPoints.get(i).y - pointWidth / 2;
                int ovalW = pointWidth;
                int ovalH = pointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
                // draw point name
                String point = String.format("%.2f", scores.get(i));
                int labelWidth = metrics.stringWidth(point);
                g2.setFont(fontbold);
                if (i == graphPoints.size() - 1) {
                    x = x - labelWidth / 2;
                }
                if (i > 0) {
                    if (scores.get(i) > scores.get(i - 1)) {
                        y -= 26;
                    }
                }
                if (y > getHeight() - ((10 * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding)) {
                    y -= 26;
                }
                if (y < getHeight() - ((1 * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding)) {
                    y += 26;
                }
                g2.setColor(Color.WHITE);
                g2.fillRect(x - labelWidth / 2, y + 6, labelWidth, metrics.getHeight() + 3);
                g2.setColor(pointColor);
                g2.drawString(point, x - labelWidth / 2, y + metrics.getHeight() + 3);
            }
            g2.setColor(Color.BLACK);
            g2.setFont(font);
            g2.drawString("Дні досліджень", (getWidth() - (2 * padding) - labelPadding) / 2 + padding + labelPadding - metrics.stringWidth("Дні досліджень") / 2, getHeight() - metrics.getHeight());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }
}