package ActionListeners;

import DataClasses.CurrentParameter_Values;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;

/**
 * Created by kost on 12/7/2014.
 */
public class PrintAL implements ActionListener {

    PrinterJob printJob;
    PageFormat pageFormat;

    public void actionPerformed(ActionEvent e) {

        printJob = PrinterJob.getPrinterJob();
        pageFormat = printJob.defaultPage();
        pageFormat.setOrientation(PageFormat.LANDSCAPE);
        pageFormat = printJob.pageDialog(pageFormat);
        printJob.validatePage(pageFormat);

        printJob.setPrintable(new PrintableCanvas(pageFormat));

        boolean ok = printJob.printDialog();
        if (ok) {
            try {
                printJob.print();
            } catch (Exception pe) {
                System.out.println("Ошибка при попытке печати!");
                pe.printStackTrace();
            }
        }
    }

    class DrawingCanvas extends JPanel {

        private Font font, fontbold, fontsmall;
        private FontMetrics metrics;
        private int padding = 15;
        private int labelPadding = 20;
        private Color lineColor = new Color(44, 102, 230, 180);
        private Color pointColor = new Color(255, 0, 0, 254);
        private Color gridColor = new Color(200, 200, 200, 200);
        private final Stroke GRAPH_STROKE = new BasicStroke(1f);
        private final Stroke GRAPH_STROKE_BOLD = new BasicStroke(2f);
        private int pointWidth = 3;
        private int numberYDivisions = 10;

        private double sigma1 = CurrentParameter_Values.getSigma_level1();
        private double sigma2 = CurrentParameter_Values.getSigma_level2();
        private double average1 = CurrentParameter_Values.getAverage_level1();
        private double average2 = CurrentParameter_Values.getAverage_level2();
        private String choice = CurrentParameter_Values.getChoice();
        private String lot = CurrentParameter_Values.getLot();
        private String lotdate = CurrentParameter_Values.getLotdate();
        private ArrayList<Double> scores1= new ArrayList<Double>();
        private ArrayList<Double> scores2 = new ArrayList<Double>();
        private ArrayList<String> fl_date = new ArrayList<String>();

        DrawingCanvas(int w, int h) {
            setSize(w, h);
            font = new Font("default", Font.ROMAN_BASELINE, 10);
            fontbold = new Font("default", Font.BOLD, 8);
            fontsmall = new Font("default", Font.ROMAN_BASELINE, 8);
            metrics = getFontMetrics(font);
            scores1 = CurrentParameter_Values.getScores_level1();
            scores2 = CurrentParameter_Values.getScores_level2();
            fl_date = CurrentParameter_Values.getFiledate();
        }

        double getMinScore1() {
            double min = average1;
            for (int i = 0; i < scores1.size(); i++) {
                if (min > scores1.get(i)) {
                    min = scores1.get(i);
                }
            }
            for (int i = -2; i < 3;i++){
                if (min > average1 + sigma1 * i) {
                    min = average1 + sigma1 * i;
                }
            }
            return min;
        }

        double getMaxScore1(){
            double max = average1;
            for (int i = 0; i < scores1.size(); i++) {
                if (max < scores1.get(i)) {
                    max = scores1.get(i);
                }
            }
            for (int i = -2; i < 3;i++){
                if (max < average1 + sigma1 * i) {
                    max = average1 + sigma1 * i;
                }
            }
            return max;
        }

        double getMinScore2() {
            double min = average2;
            for (int i = 0; i < scores2.size(); i++) {
                if (min > scores2.get(i)) {
                    min = scores2.get(i);
                }
            }
            for (int i = -2; i < 3;i++){
                if (min > average2 + sigma2 * i) {
                    min = average2 + sigma2 * i;
                }
            }
            return min;
        }

        double getMaxScore2(){
            double max = average2;
            for (int i = 0; i < scores2.size(); i++) {
                if (max < scores2.get(i)) {
                    max = scores2.get(i);
                }
            }
            for (int i = -2; i < 3;i++){
                if (max < average2 + sigma2 * i) {
                    max = average2 + sigma2 * i;
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

        public void paintContent(Graphics2D g2, int left, int up, int width, int height) {
            int right = width + left;
            int down = height + up;
            int font_height = metrics.getHeight();
            g2.setFont(font);
            // Картника Логотиип
            Image image = new ImageIcon("src\\logo.png").getImage();
            g2.drawImage(image, width / 6, up, 6 * font_height, 6 * font_height, null);
            String s = "МІНІСТЕРСТВО ОХОРОНИ ЗДОРОВ’Я УКРАЇНИ";
            int stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + font_height);
            s = "НАЦІОНАЛЬНА ДИТЯЧА СПЕЦІАЛІЗОВАНА ЛІКАРНЯ «ОХМАТДИТ»";
            stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + 2 * font_height);
            s = "ЦЕНТР МЕТАБОЛІЧНИХ ЗАХВОРЮВАНЬ";
            stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + 3 * font_height);
            s = "Лабораторія метаболічних захворювань";
            stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + 4 * font_height);
            s = "КОНТРОЛЬНІ КАРТИ";
            stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + 5 * font_height);
            s = "ТАНДЕМНА МАС-СПЕКТРОМЕТРІЯ АМІНОКИСЛОТ ТА АЦИЛКАРНИТІНІВ";
            stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + 6 * font_height);
            s = choice;
            stringLen = (int) (width / 2 - g2.getFontMetrics().getStringBounds(s, g2).getWidth() / 2 + left);
            g2.drawString(s, stringLen, up + 7 * font_height);
            s = "Lot: " + lot;
            stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
            g2.drawString(s, width / 4 - stringLen / 2 + left, up + 7 * font_height);
            s = "Дата побудови: " + lotdate;
            stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
            g2.drawString(s, 3 * width / 4 - stringLen / 2 + left, up + 7 * font_height);
            s = "Level 1";
            stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
            g2.drawString(s, width / 4 - stringLen / 2 + left, up + 8 * font_height);
            s = "Level 2";
            g2.drawString(s, 3 * width / 4 - stringLen / 2 + left, up + 8 * font_height);

            //DrawGraphs
            int graph_up = up + 9 * font_height;
            int graph_down = down - font_height;
            int graph_left1 = left;
            int graph_right1 = width / 2 + left;
            int graph_left2 = width / 2 + left;
            int graph_right2 = right;
            int graph_width = width / 2;
            int graph_height =  graph_down - graph_up;

/*            g2.drawRect(graph_left1, graph_up, graph_width, graph_height);
            g2.drawRect(graph_left2, graph_up, graph_width, graph_height);*/

            // draw white background
            g2.setColor(Color.WHITE);
            g2.fillRect(graph_left1 + padding + labelPadding, graph_up + padding, graph_width - (2 * padding) - labelPadding, graph_height - 2 * padding - labelPadding);
            g2.fillRect(graph_left2 + padding + labelPadding, graph_up + padding, graph_width - (2 * padding) - labelPadding, graph_height - 2 * padding - labelPadding);
            double xScale, yScale1, yScale2;
            metrics = getFontMetrics(fontsmall);
            g2.setFont(fontsmall);

            if (average1 != 0)
            {
                yScale1 = ((double) graph_height - 2 * padding - labelPadding) / (getMaxScore1() - getMinScore1());
                yScale2 = ((double) graph_height - 2 * padding - labelPadding) / (getMaxScore2() - getMinScore2());

                // create hatch marks and grid lines for y axis.
                for (int i = 0; i < numberYDivisions + 1; i++) {    //level1
                    int x0 = graph_left1 + padding + labelPadding;
                    int x1 = graph_left1 + pointWidth + padding + labelPadding;
                    int y = graph_up + graph_height - ((i * (graph_height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
                    if (scores1.size() > 0) {
                        g2.setColor(gridColor);
                        g2.drawLine(graph_left1 + padding + labelPadding + 1 + pointWidth, y, graph_left1 + graph_width - padding, y);
                        String yLabel = ((int) ((getMinScore1() + (getMaxScore1() - getMinScore1()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                        int labelWidth = metrics.stringWidth(yLabel);
                        g2.drawString(yLabel, x0 - labelWidth - 5, y + (metrics.getHeight() / 2) - 3);
                    }
                    g2.drawLine(x0, y, x1, y);
                    if (i == numberYDivisions){
                        g2.setColor(Color.BLACK);
                        g2.setFont(fontbold);
                        g2.drawString("Кп", x0 - metrics.stringWidth("Кп"), graph_up + padding - metrics.getHeight());
                    }
                }
                for (int i = 0; i < numberYDivisions + 1; i++) {    //level2
                    int x0 = graph_left2 + padding + labelPadding;
                    int x1 = graph_left2 + pointWidth + padding + labelPadding;
                    int y = graph_up + graph_height - ((i * (graph_height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
                    if (scores2.size() > 0) {
                        g2.setColor(gridColor);
                        g2.drawLine(graph_left2 + padding + labelPadding + 1 + pointWidth, y, graph_left2 + graph_width - padding, y);
                        String yLabel = ((int) ((getMinScore2() + (getMaxScore2() - getMinScore2()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                        int labelWidth = metrics.stringWidth(yLabel);
                        g2.drawString(yLabel, x0 - labelWidth - 5, y + (metrics.getHeight() / 2) - 3);
                    }
                    g2.drawLine(x0, y, x1, y);
                    if (i == numberYDivisions){
                        g2.setColor(Color.BLACK);
                        g2.setFont(fontbold);
                        g2.drawString("Кп", x0 - metrics.stringWidth("Кп"), graph_up + padding - metrics.getHeight());
                    }
                }

                // average and sigma
                for (int i = -2; i < 3; i++) {  // level1
                    int x0 = graph_left1 + padding + labelPadding;
                    int graph_average = (int) ((getMaxScore1() - average1 - sigma1 * i) * yScale1 + padding + graph_up);
                    g2.setStroke(new BasicStroke(1.0f, // Width
                            BasicStroke.CAP_SQUARE, // End cap
                            BasicStroke.JOIN_MITER, // Join style
                            10.0f, // Miter limit
                            new float[]{16.0f, 20.0f}, // Dash pattern
                            0.0f)); // Dash phase);
                    if (i == 0) {
                        g2.setStroke(GRAPH_STROKE_BOLD);
                    }
                    String value = String.format("%.2f", average1 + sigma1 * i);
                    int labelWidth = metrics.stringWidth(value);
                    g2.drawString(value, x0 - labelWidth - 5, graph_average + (metrics.getHeight() / 2) - 3);
                    g2.drawLine(graph_left1 + padding + labelPadding, graph_average,graph_left1 + graph_width - padding, graph_average);
                }
                for (int i = -2; i < 3; i++) {  // level2
                    int x0 = graph_left2 + padding + labelPadding;
                    int graph_average = (int) ((getMaxScore2() - average2 - sigma2 * i) * yScale2 + padding + graph_up);
                    g2.setStroke(new BasicStroke(1.0f, // Width
                            BasicStroke.CAP_SQUARE, // End cap
                            BasicStroke.JOIN_MITER, // Join style
                            10.0f, // Miter limit
                            new float[]{16.0f, 20.0f}, // Dash pattern
                            0.0f)); // Dash phase);
                    if (i == 0) {
                        g2.setStroke(GRAPH_STROKE_BOLD);
                    }
                    String value = String.format("%.2f", average2 + sigma2 * i);
                    int labelWidth = metrics.stringWidth(value);
                    g2.drawString(value, x0 - labelWidth - 5, graph_average + (metrics.getHeight() / 2) - 3);
                    g2.drawLine(graph_left2 + padding + labelPadding, graph_average,graph_left2 + graph_width - padding, graph_average);
                }
            }

            g2.setStroke(GRAPH_STROKE_BOLD);
            // create x and y axes
            //level1
            g2.drawLine(graph_left1 + padding + labelPadding, graph_up + graph_height - padding - labelPadding, graph_left1 + padding + labelPadding, graph_up + padding);
            g2.drawLine(graph_left1 + padding + labelPadding, graph_up + graph_height - padding - labelPadding, graph_left1 + graph_width - padding, graph_up + graph_height - padding - labelPadding);
            //level2
            g2.drawLine(graph_left2 + padding + labelPadding, graph_up + graph_height - padding - labelPadding, graph_left2 + padding + labelPadding, graph_up + padding);
            g2.drawLine(graph_left2 + padding + labelPadding, graph_up + graph_height - padding - labelPadding, graph_left2 + graph_width - padding, graph_up + graph_height - padding - labelPadding);

            g2.setStroke(GRAPH_STROKE);
            if (scores1.size() > 0) {

                xScale = ((double) graph_width - (2 * padding) - labelPadding) / (scores1.size());
                yScale1 = ((double) graph_height - 2 * padding - labelPadding) / (getMaxScore1() - getMinScore1());
                yScale2 = ((double) graph_height - 2 * padding - labelPadding) / (getMaxScore2() - getMinScore2());

                ArrayList<Point> graphPoints1 = new ArrayList<Point>(); // level1
                for (int i = 0; i < scores1.size(); i++) {
                    int x1 = (int) ((i + 1) * xScale + padding + labelPadding);
                    int y1 = (int) ((getMaxScore1() - scores1.get(i)) * yScale1 + padding );
                    graphPoints1.add(new Point(x1, y1));
                }
                ArrayList<Point> graphPoints2 = new ArrayList<Point>(); // level2
                for (int i = 0; i < scores2.size(); i++) {
                    int x1 = (int) ((i + 1) * xScale + padding + labelPadding);
                    int y1 = (int) ((getMaxScore2() - scores2.get(i)) * yScale2 + padding);
                    graphPoints2.add(new Point(x1, y1));
                }

                // and for x axis
                for (int i = 1; i <= scores1.size(); i++) { // level1
                    if (scores1.size() > 0) {
                        int x = graph_left1 + i * (graph_width - padding * 2 - labelPadding) / (scores1.size()) + padding + labelPadding;
                        int y0 = graph_up + graph_height - padding - labelPadding;
                        int y1 = y0 - pointWidth;
                        if ((i % ((int) ((scores1.size() / 20.0)) + 1)) == 0) {
                            g2.setColor(gridColor);
                            g2.drawLine(x, graph_up + graph_height - padding - labelPadding - 1 - pointWidth, x, graph_up + padding);
                            g2.setFont(fontsmall);
                            g2.setColor(Color.BLACK);
                            String xLabel = fl_date.get(i-1).substring(0,5);
                            int labelWidth = metrics.stringWidth(xLabel);
                            drawRotate(g2, x - labelWidth / 2, y0 + metrics.getHeight() + 10, -45, xLabel);
                        }
                        g2.drawLine(x, y0, x, y1);
                    }
                }
                for (int i = 1; i <= scores2.size(); i++) { // level2
                    if (scores2.size() > 0) {
                        int x = graph_left2 + i * (graph_width - padding * 2 - labelPadding) / (scores2.size()) + padding + labelPadding;
                        int y0 = graph_up + graph_height - padding - labelPadding;
                        int y1 = y0 - pointWidth;
                        if ((i % ((int) ((scores2.size() / 20.0)) + 1)) == 0) {
                            g2.setColor(gridColor);
                            g2.drawLine(x, graph_up + graph_height - padding - labelPadding - 1 - pointWidth, x, graph_up + padding);
                            g2.setFont(fontsmall);
                            g2.setColor(Color.BLACK);
                            String xLabel = fl_date.get(i-1).substring(0,5);
                            int labelWidth = metrics.stringWidth(xLabel);
                            drawRotate(g2, x - labelWidth / 2, y0 + metrics.getHeight() + 10, -45, xLabel);
                        }
                        g2.drawLine(x, y0, x, y1);
                    }
                }

                // draw lines between points
                Stroke oldStroke = g2.getStroke();
                g2.setColor(lineColor);
                g2.setStroke(GRAPH_STROKE);
                for (int i = 0; i < graphPoints1.size() - 1; i++) {  // level1
                    int x1 = graph_left1 + graphPoints1.get(i).x;
                    int y1 = graph_up + graphPoints1.get(i).y;
                    int x2 = graph_left1 + graphPoints1.get(i + 1).x;
                    int y2 = graph_up + graphPoints1.get(i + 1).y;
                    g2.drawLine(x1, y1, x2, y2);
                }
                for (int i = 0; i < graphPoints2.size() - 1; i++) {  // level2
                    int x1 = graph_left2 + graphPoints2.get(i).x;
                    int y1 = graph_up + graphPoints2.get(i).y;
                    int x2 = graph_left2 + graphPoints2.get(i + 1).x;
                    int y2 = graph_up + graphPoints2.get(i + 1).y;
                    g2.drawLine(x1, y1, x2, y2);
                }

                // draw points
                g2.setStroke(oldStroke);
                g2.setColor(pointColor);
                for (int i = 0; i < graphPoints1.size(); i++) {  //level1
                    int x = graph_left1 + graphPoints1.get(i).x - pointWidth / 2;
                    int y = graphPoints1.get(i).y - pointWidth / 2;
                    int ovalW = pointWidth;
                    int ovalH = pointWidth;
                    g2.fillOval(x, y + graph_up, ovalW, ovalH);
                    // draw point name
                    String point = String.format("%.2f", scores1.get(i));
                    int labelWidth = metrics.stringWidth(point);
                    g2.setFont(fontbold);
                    if (i == graphPoints1.size() - 1) {
                        x = x - labelWidth / 2;
                    }
                    if (i > 0) {
                        if (scores1.get(i) > scores1.get(i - 1)) {
                            y -= 20;
                        }
                    }
                    if (y > graph_height - ((10 * (graph_height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding)) {
                        y -= 20;
                    }
                    if (y < graph_height - ((1 * (graph_height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding)) {
                        y += 20;
                    }
                    y += graph_up;
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x - labelWidth / 2, y + 6, labelWidth, metrics.getHeight() + 3);
                    g2.setColor(pointColor);
                    g2.drawString(point, x - labelWidth / 2, y + metrics.getHeight() + 3);
                }
                for (int i = 0; i < graphPoints2.size(); i++) {  //level2
                    int x = graph_left2 + graphPoints2.get(i).x - pointWidth / 2;
                    int y = graphPoints2.get(i).y - pointWidth / 2;
                    int ovalW = pointWidth;
                    int ovalH = pointWidth;
                    g2.fillOval(x, y + graph_up, ovalW, ovalH);
                    // draw point name
                    String point = String.format("%.2f", scores2.get(i));
                    int labelWidth = metrics.stringWidth(point);
                    g2.setFont(fontbold);
                    if (i == graphPoints2.size() - 1) {
                        x = x - labelWidth / 2;
                    }
                    if (i > 0) {
                        if (scores2.get(i) > scores2.get(i - 1)) {
                            y -= 20;
                        }
                    }
                    if (y > graph_height - ((10 * (graph_height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding)) {
                        y -= 20;
                    }
                    if (y < graph_height - ((1 * (graph_height - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding)) {
                        y += 20;
                    }
                    y += graph_up;
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x - labelWidth / 2, y + 6, labelWidth, metrics.getHeight() + 3);
                    g2.setColor(pointColor);
                    g2.drawString(point, x - labelWidth / 2, y + metrics.getHeight() + 3);
                }

                g2.setColor(Color.BLACK);
                g2.setFont(fontsmall);
                g2.drawString("Дні досліджень", graph_left1 + (graph_width - (2 * padding) - labelPadding) / 2 + padding + labelPadding - metrics.stringWidth("Дні досліджень") / 2, graph_up + graph_height - metrics.getHeight());
                g2.drawString("Дні досліджень", graph_left2 + (graph_width - (2 * padding) - labelPadding) / 2 + padding + labelPadding - metrics.stringWidth("Дні досліджень") / 2, graph_up + graph_height - metrics.getHeight());
            }


            g2.setFont(font);
            g2.setColor(Color.black);
            s = "Завідуюча лабораторією";
            stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
            g2.drawString(s, 3 * width / 4 - stringLen + left, down - 3);
            s = "_________________ (Ольхович Н.В.)";
            stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
            g2.drawString(s, width - stringLen + left, down - 3);
        }
    }

    class PrintableCanvas implements Printable {

        DrawingCanvas canvas;
        PageFormat pageFormat;

        public PrintableCanvas(PageFormat pf) {
            pageFormat = pf;
        }

        public int print(Graphics g, PageFormat pageFormat, int pageIndex)
                throws PrinterException {
            if (pageIndex >= 1) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2D = (Graphics2D) g;

            //imageable area
            int x0 = (int) pageFormat.getImageableX();
            int y0 = (int) pageFormat.getImageableY();
            int w0 = (int) pageFormat.getImageableWidth();
            int h0 = (int) pageFormat.getImageableHeight();

            canvas = new DrawingCanvas(w0, h0);
            canvas.paintContent(g2D, x0, y0, w0, h0);

            // successful printing of the page
            return Printable.PAGE_EXISTS;
        }
    }
}