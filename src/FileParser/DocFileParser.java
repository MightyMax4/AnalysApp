package FileParser;

/**
 * Created by Max on 02.12.2014.
 */

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class DocFileParser {
    private InputStream resived_data;
    private HSSFWorkbook wb;
    private ArrayList<Double> scores_lv1 = new ArrayList<Double>();
    private ArrayList<Double> scores_lv2 = new ArrayList<Double>();
    private ArrayList<Double> average_values_lv1 = new ArrayList<Double>();
    private ArrayList<Double> average_values_lv2 = new ArrayList<Double>();
    private ArrayList<Double> sigma_values_lv1 = new ArrayList<Double>();
    private ArrayList<Double> sigma_values_lv2 = new ArrayList<Double>();
    private String[] words = {"5-Oxo Pro", "Ala", "Arg", "Asp", "Cit", "Glu", "Gly", "His", "Leu", "Met", "Orn", "Phe", "Pro", "Ser", "Trp", "Tyr", "Val", "C0", "C2", "C3", "C4", "C5", "C6", "C8", "C10", "C12", "C14", "C16", "C18"};

    public DocFileParser(InputStream res) {
        this.resived_data = res;
        try {
            this.wb = new HSSFWorkbook(resived_data);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame("Wrong document type"), "Документ не соответствует требуемому");
        }
    }

    private String check_File_Type() {
        int qc_lvls_count = 0;
        int start_column = 0;
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();

//      Parsing the document
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                int cellType = cell.getCellType();
                switch (cellType) {
                    case Cell.CELL_TYPE_STRING:
                        if (cell.getStringCellValue().trim().equals("5-Oxo Pro"))
                            start_column = cell.getColumnIndex();
                        if ((cell.getStringCellValue().trim().equals("QC Lv I") || cell.getStringCellValue().trim().equals("QC Lv II") || cell.getStringCellValue().trim().equals("QC LV II")) && (cell.getColumnIndex() == start_column - 4))
                            qc_lvls_count++;
                        break;
                }
            }
        }

        if (qc_lvls_count == 4) return "Scores_File";
        if (qc_lvls_count == 20) return "Start_File";
        return "Unknown_File";
    }

    public ArrayList<ArrayList<Double>> parse() throws IOException {
//        Parsing document
        int start_column = 0;
        int start_row_num = 0;
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        ArrayList<ArrayList<Double>> scores_lv1_lv2 = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> final_parsed_data = new ArrayList<ArrayList<Double>>();
//      In case when the file - scores file
        if (check_File_Type().equals("Scores_File")) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            while (it.hasNext()) {
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                ArrayList<Double> values_row = new ArrayList<Double>();
                boolean k = false;
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case Cell.CELL_TYPE_STRING:
                            if (cell.getStringCellValue().trim().equals("5-Oxo Pro"))
                                start_column = cell.getColumnIndex();

                            for (int i = 0; i < words.length; i++) {
                                if (cell.getStringCellValue().trim().equals(words[i]))
                                    indexes.add(cell.getColumnIndex());
                            }

                            if (cell.getStringCellValue().trim().equals("QC Lv I") || cell.getStringCellValue().trim().equals("QC Lv II") || cell.getStringCellValue().trim().equals("QC LV II")) {
                                start_row_num = cell.getRowIndex();
                                k = true;
                            }

                            for (int i : indexes) {
                                if ((cell.getRowIndex() == start_row_num) && (cell.getColumnIndex() == i) && (start_row_num != 0) && (cell.getStringCellValue().trim().equals("No data")))
                                    values_row.add(0.0);
                            }

                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            for (int i : indexes) {
                                if ((cell.getColumnIndex() == i) && (cell.getRowIndex() == start_row_num) && (start_row_num != 0))
                                    values_row.add(cell.getNumericCellValue());
                            }

                            break;
                    }
                }
                if (k) scores_lv1_lv2.add(values_row);
            }

//          Fill scores_lv1
            for (int i = 0; i < 29; i++) {
                double sum = 0;
                for (int j = 0; j < 2; j++) {
                    sum += scores_lv1_lv2.get(j).get(i);
                }
                scores_lv1.add(sum / 2);
            }

//          Fill scores_lv2
            for (int i = 0; i < 29; i++) {
                double sum = 0;
                for (int j = 2; j < 4; j++) {
                    sum += scores_lv1_lv2.get(j).get(i);
                }
                scores_lv2.add(sum / 2);
            }

            final_parsed_data.add(scores_lv1);
            final_parsed_data.add(scores_lv2);
        }

//        In case when the file - start file
        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();

        if (check_File_Type().equals("Start_File")) {
            boolean k;
            ArrayList<Double> values_row;
            while (it.hasNext()) {
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                values_row = new ArrayList<Double>();
                k = false;
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int cellType = cell.getCellType();
                    switch (cellType) {
                        case Cell.CELL_TYPE_STRING:
                            if (cell.getStringCellValue().trim().equals("5-Oxo Pro"))
                                start_column = cell.getColumnIndex();

                            for (int i = 0; i < words.length; i++) {
                                if (cell.getStringCellValue().trim().equals(words[i]))
                                    indexes.add(cell.getColumnIndex());
                            }

                            if ((cell.getStringCellValue().trim().equals("QC Lv I") || cell.getStringCellValue().trim().equals("QC Lv II") || cell.getStringCellValue().trim().equals("QC LV II")) && cell.getColumnIndex() == start_column - 4) {
                                start_row_num = cell.getRowIndex();
                                k = true;
                            }

                            for (int i : indexes) {
                                if ((cell.getRowIndex() == start_row_num) && (cell.getColumnIndex() == i) && (start_row_num != 0) && (cell.getStringCellValue().trim().equals("No data")))
                                    values_row.add(0.0);
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            for (int i : indexes) {
                                if ((cell.getColumnIndex() == i) && (cell.getRowIndex() == start_row_num) && (start_row_num != 0))
                                    values_row.add(cell.getNumericCellValue());
                            }
                            break;
                    }
                }
                if (k) scores_lv1_lv2.add(values_row);
            }

//          Fill average values level1
            for (int i = 0; i < 29; i++) {
                double sum = 0;
                for (int j = 0; j < 10; j++) {
                    sum += scores_lv1_lv2.get(j).get(i);
                }
                average_values_lv1.add(sum / 10);
            }

//          Fill average values level1
            for (int i = 0; i < 29; i++) {
                double sum = 0;
                for (int j = 10; j < 20; j++) {
                    sum += scores_lv1_lv2.get(j).get(i);
                }
                average_values_lv2.add(sum / 10);
            }

//            Fill sigma values level1
            for (int i = 0; i < 29; i++) {
                double sum = 0;
                for (int j = 0; j < 10; j++) {
                    sum += Math.pow(average_values_lv1.get(i) - scores_lv1_lv2.get(j).get(i), 2);
                }
                sigma_values_lv1.add(Math.sqrt(sum / 10));
            }

//            Fill sigma values level2
            for (int i = 0; i < 29; i++) {
                double sum = 0;
                for (int j = 10; j < 20; j++) {
                    sum += Math.pow(average_values_lv2.get(i) - scores_lv1_lv2.get(j).get(i), 2);
                }
                sigma_values_lv2.add(Math.sqrt(sum / 10));
            }

            final_parsed_data.add(average_values_lv1);
            final_parsed_data.add(average_values_lv2);
            final_parsed_data.add(sigma_values_lv1);
            final_parsed_data.add(sigma_values_lv2);
        }

        return final_parsed_data;
    }
}


