package ActionListeners;

import AnalysAppUI.FilesManager_Form;
import AnalysAppUI.Main_Form;
import AnalysAppUI.ProjectsManager_Form;
import DataClasses.CurrentParameter_Values;
import Database.Queries;
import FileParser.DocFileParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Max on 07.12.2014.
 */
public class Update_File_Action_Listener implements ActionListener {
    private static int selected_row;
    private String lot;
    private String action_type;
    private JFrame parent_frame;
    private TextField lot_textField;

    public Update_File_Action_Listener(JFrame parent_frame, String action_type) {
        this.parent_frame = parent_frame;
        this.action_type = action_type;
    }

    public Update_File_Action_Listener(JFrame parent_frame, String action_type , TextField lot_textField)
    {
        this.parent_frame = parent_frame;
        this.action_type = action_type;
        this.lot_textField = lot_textField;
    }

    public static int getSelected_row() {
        return selected_row;
    }

    public static void setSelected_row(int selected_row) {
        Update_File_Action_Listener.selected_row = selected_row;
    }

    private String getDateFromFileName(String file_name) {
        String date = file_name;
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(date);
        date = "";
        int i = 0;
        while (m.find()) {
            if (i == 2) {
                date = date + "20";
                i = 0;
            }
            date = date + m.group() + ".";
            i++;
        }
        date = date.substring(0, date.length() - 1);
        return date;
    }

    // Проверяет базу данных проектов или файлов , в зависимости от типа action_type("Add_File" или "Add_Start_File") на нахождение в ней файла с такой же датой (date)
    private boolean isExist(String date) {
        ArrayList<ArrayList<String>> files_list = Queries.select_Files();
        ArrayList<ArrayList<String>> projects_list = Queries.select_Lot();
        boolean flag = false;

        if (action_type.equals("Add_File") || action_type.equals("Update_File")) {
            if (files_list != null) {
                for (ArrayList<String> one_file : files_list) {
                    if (one_file.get(2).equals(date)) {
                        flag = true;
                    }
                }
            }
        }

        if (action_type.equals("Add_Start_File") || action_type.equals("Update_Start_File")) {
            if (projects_list != null) {
                for (ArrayList<String> one_project : projects_list) {
                    if (one_project.get(2).equals(date)) {
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ( action_type.equals("Add_Start_File") || action_type.equals("Update_Start_File") ) this.lot = lot_textField.getText();
        boolean file_dialog_flag = true;
        ArrayList<ArrayList<Double>> parsed_data_lv1_lv2 = new ArrayList<ArrayList<Double>>();
//      create a file dialog frame in the parent frame---------------------------------------------------------------------------
        FileDialog fileDialog = new FileDialog(parent_frame, "Вибір файлу");
        parent_frame.setVisible(false);
        fileDialog.setVisible(true);
        String date = "01.12.2010";
        try {
            InputStream resivedData = new FileInputStream(fileDialog.getDirectory() + fileDialog.getFile());
            DocFileParser parser = new DocFileParser(resivedData);
            parsed_data_lv1_lv2 = parser.parse();
            date = getDateFromFileName(fileDialog.getFile());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.out.print("File can not be opened");
            file_dialog_flag = false;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

//  Performs actions(add or update) depending on @file_type (file or start_file) and @action type (add or update) ------------------------------------------------------------------------
        if ((parsed_data_lv1_lv2.size() == 2)) {
            if (!isExist(date)) {
                //  Insert values , recivede from excel file , in database
                if ((action_type.equals("Add_File"))) {
                    Queries.insertNewFile(fileDialog.getDirectory() + fileDialog.getFile(), date, parsed_data_lv1_lv2.get(0), parsed_data_lv1_lv2.get(1));
                    CurrentParameter_Values.updateValues();
                    Object[] row = {String.valueOf(Queries.select_Files().size()), fileDialog.getDirectory() + fileDialog.getFile(), date};
                    FilesManager_Form.filesTableModel.addRow(row);
                    Main_Form.update();
                }
                //  Update values , recivede from excel file , in database
                if ((action_type.equals("Update_File"))) {
                    Queries.updateFileData(selected_row, fileDialog.getDirectory() + fileDialog.getFile(), date, parsed_data_lv1_lv2.get(0), parsed_data_lv1_lv2.get(1));
                    CurrentParameter_Values.updateValues();
                    FilesManager_Form.filesTableModel.setValueAt(fileDialog.getDirectory() + fileDialog.getFile(), selected_row, 1);
                    FilesManager_Form.filesTableModel.setValueAt(date, selected_row, 2);
                    Main_Form.update();
                }
                if (action_type.equals("Add_Start_File") || action_type.equals("Update_Start_File"))
                    JOptionPane.showMessageDialog(parent_frame, "Обраний документ не є стартовим. Документи такого типу необхідні для додавання нових точок на графіки і додаються у вікні управління файлами");
            } else
                JOptionPane.showMessageDialog(parent_frame, "Документ вже був доданий раніше в поточному проекті");
        }

        if (parsed_data_lv1_lv2.size() == 4) {
            if (!isExist(date)) {
                if (action_type.equals("Add_Start_File")) {
                    CurrentParameter_Values.setLot(lot);
                    Queries.insertNewProject(lot, fileDialog.getDirectory() + fileDialog.getFile(), date, parsed_data_lv1_lv2.get(0), parsed_data_lv1_lv2.get(1), parsed_data_lv1_lv2.get(2), parsed_data_lv1_lv2.get(3));
                    CurrentParameter_Values.updateValues();
                    Object[] row = {String.valueOf(Queries.select_Lot().size()), CurrentParameter_Values.getLot().toString(), fileDialog.getDirectory() + fileDialog.getFile(), date};
                    ProjectsManager_Form.projectsTableModel.addRow(row);
                    Main_Form.update();
                }
                if (action_type.equals("Update_Start_File")) {
                    Queries.updateFilesLot(selected_row , lot);
                    Queries.updateProjectData(selected_row, lot , fileDialog.getDirectory() + fileDialog.getFile(), date , parsed_data_lv1_lv2.get(0), parsed_data_lv1_lv2.get(1), parsed_data_lv1_lv2.get(2), parsed_data_lv1_lv2.get(3));
                    CurrentParameter_Values.setLot(lot);
                    CurrentParameter_Values.updateValues();
                    ProjectsManager_Form.projectsTableModel.setValueAt(String.valueOf(Queries.select_Lot().size()), selected_row, 0);
                    ProjectsManager_Form.projectsTableModel.setValueAt(lot, selected_row, 1);
                    ProjectsManager_Form.projectsTableModel.setValueAt(fileDialog.getDirectory() + fileDialog.getFile(), selected_row, 2);
                    ProjectsManager_Form.projectsTableModel.setValueAt(date, selected_row, 3);
                    Main_Form.update();
                }
                if (action_type.equals("Add_File") || action_type.equals("Update_File"))
                    JOptionPane.showMessageDialog(parent_frame, "Обраний документ є стартовим і може бути доданий тільки при створенні нового проекту");

            } else
                JOptionPane.showMessageDialog(parent_frame, "Цей документ зі стартовими даними вже був доданий раніше в іншому проекті");
        }

        if ((parsed_data_lv1_lv2.size() != 2) && (parsed_data_lv1_lv2.size() != 4) && file_dialog_flag)
            JOptionPane.showMessageDialog(parent_frame, "Розташування даних в цьому документі не відповідає очікуваному шаблону");
    }
}



