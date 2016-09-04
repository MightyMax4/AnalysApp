package AnalysAppUI;

import ActionListeners.Update_File_Action_Listener;
import ActionListeners.InputLot_Action_Listener;
import DataClasses.CurrentParameter_Values;
import Database.Queries;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Max on 10.12.2014.
 */
public class ProjectsManager_Form extends JFrame {

    public static DefaultTableModel projectsTableModel;
    private int selected_row;
    private int screen_Width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screen_Height = Toolkit.getDefaultToolkit().getScreenSize().height;
    private ListSelectionModel selectionModel;
    private JTable projects_table = new JTable();
    private JScrollPane jScrollPane;
    private JPanel table_panel = new JPanel();
    private JPanel buttons_panel = new JPanel();
    private JButton add_button = new JButton("Додати проект");
    private JButton update_button = new JButton("Змінити проект");
    private JButton choose_button = new JButton("Вибрать проект");
    private JButton cancel_button = new JButton("Назад");

    public ProjectsManager_Form() {
        setTitle("Управління проектами");
        setLocation(screen_Width / 4, screen_Height / 10);
        setLayout(new BorderLayout());
        setSize(getDimension(screen_Width / 2, screen_Height * 4 / 5));
        projectsTableModel = getTableModel_With_ProjectsData();
        projects_table.setModel(projectsTableModel);
        selectionModel = projects_table.getSelectionModel();
        selectionModel.addListSelectionListener(new SelectTableRow_Action_Listener());
        jScrollPane = new JScrollPane(projects_table);
        jScrollPane.setSize(getDimension(screen_Width / 2 - screen_Width / 15, screen_Height * 3 / 5 - screen_Height / 10));
        table_panel.add(jScrollPane);
        add_button.addActionListener(new InputLot_Action_Listener("Add_Start_File"));
        update_button.addActionListener(new InputLot_Action_Listener("Update_Start_File"));
        choose_button.addActionListener(new Choose_Project_Action_Listener());
        cancel_button.addActionListener(new MyCancelActionListener());
        buttons_panel.add(add_button);
        buttons_panel.add(update_button);
        buttons_panel.add(choose_button);
        buttons_panel.add(cancel_button);
        getContentPane().add(table_panel, BorderLayout.CENTER);
        getContentPane().add(buttons_panel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static DefaultTableModel getProjectsTableModel() {
        return projectsTableModel;
    }

    public static void setProjectsTableModel(DefaultTableModel projectTableModel) {
        setProjectsTableModel(projectsTableModel);
    }

    private DefaultTableModel getTableModel_With_ProjectsData() {
        ArrayList<ArrayList<String>> projects = Queries.select_Lot();
        Object[][] projectsMatrix;
        Object[] headers = {"Номер", "Лот", "Місце розташування початкового файлу", "Дата"};
        if (projects.size() != 0) {
            projectsMatrix = new Object[projects.size()][4];
            for (int i = 0; i < projects.size(); i++) {
                projectsMatrix[i][0] = i + 1;
                projectsMatrix[i][1] = projects.get(i).get(0);
                projectsMatrix[i][2] = projects.get(i).get(1);
                projectsMatrix[i][3] = projects.get(i).get(2);
            }
        } else
            projectsMatrix = new Object[0][0];
        return (new DefaultTableModel(projectsMatrix, headers));
    }

    private Dimension getDimension(int width, int height) {
        return (new Dimension(width, height));
    }

    private class MyCancelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    private class Choose_Project_Action_Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CurrentParameter_Values.setLot(Queries.select_Lot().get(selected_row).get(0));
            CurrentParameter_Values.updateValues();
            setVisible(false);
            Main_Form.update();
        }
    }

    private class SelectTableRow_Action_Listener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            Update_File_Action_Listener.setSelected_row(projects_table.getSelectedRow());
            selected_row = projects_table.getSelectedRow();
        }
    }
}
