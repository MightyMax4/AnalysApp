package AnalysAppUI;

import ActionListeners.Update_File_Action_Listener;
import DataClasses.CurrentParameter_Values;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by rikomakser on 13.12.2014.
 */
public class InputLot_Form extends JFrame {
    private String action_type;
    private int screen_Width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screen_Height = Toolkit.getDefaultToolkit().getScreenSize().height;
    private JPanel components_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JLabel input_text_label = new JLabel("Введіть лот:");
    private TextField lot_textField = new TextField();
    private JButton accept_button = new JButton("Далі");

    public InputLot_Form(String action_type){
        this.action_type = action_type;
        setTitle("Форма для введення лоту");
        setSize(screen_Width / 4, screen_Height / 8);
        setLocation(screen_Width / 2 - screen_Width / 8, screen_Height / 2 - screen_Height / 10);
        lot_textField.setColumns(18);
        accept_button.addActionListener(new Update_File_Action_Listener(this, this.action_type, lot_textField));
        accept_button.getActionCommand();
        components_panel.add(input_text_label);
        components_panel.add(lot_textField);
        components_panel.add(accept_button);
        add(components_panel);
        setVisible(true);
    }
}
