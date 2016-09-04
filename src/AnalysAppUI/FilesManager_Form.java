package AnalysAppUI;

import ActionListeners.Update_File_Action_Listener;
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
public class FilesManager_Form extends JFrame {

    public static DefaultTableModel filesTableModel;
    private int screen_Width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screen_Height = Toolkit.getDefaultToolkit().getScreenSize().height;
    private ListSelectionModel selectionModel;
    private JTable files_table = new JTable();
    private JScrollPane jScrollPane;
    private JPanel table_panel = new JPanel();
    private JPanel buttons_panel = new JPanel();
    private JButton add_button = new JButton("Додати файл");
    private JButton update_button = new JButton("Змінити файл");
    private JButton cancel_button = new JButton("Назад");

    public FilesManager_Form() throws HeadlessException {
        setTitle("Управління файлами");
        setLocation(screen_Width / 4, screen_Height / 10);
        setLayout(new BorderLayout());
        setSize(getDimension(screen_Width / 2, screen_Height * 4 / 5));

        filesTableModel = getTableModel_With_FilesData();
        files_table.setModel(filesTableModel);
        selectionModel = files_table.getSelectionModel();
        selectionModel.addListSelectionListener(new SelectTableRow_Action_Listener());
        jScrollPane = new JScrollPane(files_table);
        jScrollPane.setSize(getDimension(screen_Width / 2 - screen_Width / 15, screen_Height * 3 / 5 - screen_Height / 10));
        table_panel.add(jScrollPane);

        add_button.addActionListener(new Update_File_Action_Listener(this, "Add_File"));
        update_button.addActionListener(new Update_File_Action_Listener(this, "Update_File"));
        cancel_button.addActionListener(new MyCancelActionListener());
        buttons_panel.add(add_button);
        buttons_panel.add(update_button);
        buttons_panel.add(cancel_button);
        getContentPane().add(table_panel, BorderLayout.CENTER);
        getContentPane().add(buttons_panel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static DefaultTableModel getFilesTableModel() {
        return filesTableModel;
    }

    public static void setFilesTableModel(DefaultTableModel filesTableModel) {
        FilesManager_Form.filesTableModel = filesTableModel;
    }

    private DefaultTableModel getTableModel_With_FilesData() {
        ArrayList<ArrayList<String>> files = Queries.select_Files();
        Object[][] filesMatrix;
        Object[] headers = {"Номер", "Місце розташування файлу", "Дата"};
        if (files.size() != 0) {
            filesMatrix = new Object[files.size()][3];
            for (int i = 0; i < files.size(); i++) {
                filesMatrix[i][0] = i + 1;
                filesMatrix[i][1] = files.get(i).get(2);
                filesMatrix[i][2] = files.get(i).get(3);
            }
        } else
            filesMatrix = new Object[0][0];
        return (new DefaultTableModel(filesMatrix, headers));
    }

    private Dimension getDimension(int width, int height) {
        return (new Dimension(width, height));
    }

    private class MyCancelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    private class SelectTableRow_Action_Listener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            Update_File_Action_Listener.setSelected_row(files_table.getSelectedRow());
        }
    }

}
