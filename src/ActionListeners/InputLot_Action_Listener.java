package ActionListeners;

import AnalysAppUI.InputLot_Form;

import javax.swing.*;
import javax.swing.text.StringContent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by rikomakser on 14.12.2014.
 */
public class InputLot_Action_Listener implements ActionListener {
    private String action_type;

    public InputLot_Action_Listener(String action_type) {
        this.action_type = action_type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        InputLot_Form inputLotForm = new InputLot_Form(action_type);
    }
}
