package AnalysAppUI;

import ActionListeners.Files_Manager_Action_Listener;
import ActionListeners.PrintAL;
import ActionListeners.Project_Manager_Action_Listener;
import DataClasses.CurrentParameter_Values;
import Database.Queries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kost on 01.12.2014.
 */
public class Main_Form extends JFrame {

    private String names = "5-Oxo Pro , Ala , Arg , Asp , Cit , Glu , Gly , His , Leu , Met , Orn , Phe , Pro , Ser , Trp , Tyr , Val , C0 , C2 , C3 , C4 , C5 , C6 , C8 , C10 , C12 , C14 , C16 , C18";
    private String[] names_str = names.split(" , ");
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private Date date = new Date();
    private JComboBox choice = new JComboBox(names_str);
    private static ArrayList<String> filedate = new ArrayList<String>();

    private JLabel today_data_label = new JLabel("Сьогоднішня дата: " + dateFormat.format(date), SwingConstants.RIGHT);
    public static JLabel lot_label = new JLabel("Лот: " + CurrentParameter_Values.getLot(), SwingConstants.CENTER);
    public static JLabel project_data_label = new JLabel("Дата проекту: " + CurrentParameter_Values.getLotdate());
    private static DrawGraph drawGraph1, drawGraph2;
    public static JPanel graphics_panel = new JPanel();
    private JPanel texts_panel = new JPanel();
    private JPanel buttons_panel = new JPanel();
    private JButton projects_manager_button = new JButton("Управління проектами");
    private JButton files_manager_button = new JButton("Управління файлами");
    private JButton print_button = new JButton("Друкувати");
    private JButton clear_db_button = new JButton("Очистить все таблицы в БД");

    public Main_Form() {
        setTitle("Оперативний внутрішньолабораторний контроль");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-40);
        setExtendedState(JFrame.MAXIMIZED_BOTH);    //fullscreen

        print_button.addActionListener(new PrintAL());
        projects_manager_button.addActionListener(new Project_Manager_Action_Listener());
        files_manager_button.addActionListener(new Files_Manager_Action_Listener());
        clear_db_button.addActionListener(new ClearDB_Action_Listener());
        choice.addActionListener(new SwitchParameterActionListener());

        texts_panel.setLayout(new GridLayout(1, 3, 10, 10));
        texts_panel.add(today_data_label);
        texts_panel.add(lot_label);
        texts_panel.add(project_data_label);

        buttons_panel.add(projects_manager_button);
        buttons_panel.add(files_manager_button);
        buttons_panel.add(print_button);
        buttons_panel.add(choice);
        buttons_panel.add(clear_db_button);

        drawGraph1 = new DrawGraph("level1");
        drawGraph2 = new DrawGraph("level2");
        graphics_panel.add(drawGraph1);
        graphics_panel.add(drawGraph2);

        add(texts_panel, BorderLayout.NORTH);
        add(buttons_panel, BorderLayout.SOUTH);
        add(graphics_panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void update() {
        lot_label.setText("Лот: " + CurrentParameter_Values.getLot());
        int i = 0;
        while (Queries.select_Lot().get(i).get(0).equals(CurrentParameter_Values.getLot()) != true){
            i++;
        }
        CurrentParameter_Values.setLotdate(Queries.select_Lot().get(i).get(2));
        project_data_label.setText("Дата проекту: " + CurrentParameter_Values.getLotdate());

        drawGraph1.repaint();
        drawGraph2.repaint();
    }

    private class SwitchParameterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CurrentParameter_Values.setChoice(choice.getSelectedItem().toString());
            CurrentParameter_Values.setScores_level1(Queries.select_FilesData(CurrentParameter_Values.getChoice()).get(0));
            CurrentParameter_Values.setScores_level2(Queries.select_FilesData(CurrentParameter_Values.getChoice()).get(1));
            CurrentParameter_Values.setAverage_level1(Queries.select_LotData(CurrentParameter_Values.getChoice()).get(0));
            CurrentParameter_Values.setAverage_level2(Queries.select_LotData(CurrentParameter_Values.getChoice()).get(1));
            CurrentParameter_Values.setSigma_level1(Queries.select_LotData(CurrentParameter_Values.getChoice()).get(2));
            CurrentParameter_Values.setSigma_level2(Queries.select_LotData(CurrentParameter_Values.getChoice()).get(3));
            update();
        }
    }

    private class ClearDB_Action_Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Queries.clear_AllTables();
        }
    }
}