import ActionListeners.Update_File_Action_Listener;
import AnalysAppUI.Main_Form;
import AnalysAppUI.ProjectsManager_Form;
import DataClasses.CurrentParameter_Values;
import Database.DBConnector;
import Database.Queries;
import sun.awt.im.SimpleInputMethodWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * Created by Max on 01.12.2014.
 */
public class AnalyseAppMainClass {

    public static void main(String arg[]) throws ClassNotFoundException {

        DBConnector.createConnection();

        //get values from database for current parameter
        if (Queries.select_Lot().size()!= 0) {
            CurrentParameter_Values.setLot(Queries.select_Lot().get(Queries.select_Lot().size()-1).get(0));
            CurrentParameter_Values.setLotdate(Queries.select_Lot().get(Queries.select_Lot().size()-1).get(2));
            CurrentParameter_Values.updateValues();
        }

        Main_Form mainform = new Main_Form();
        JOptionPane.showMessageDialog(null, messageLabel,"Початок роботи",JOptionPane.INFORMATION_MESSAGE,icon);

        if (Queries.select_Lot().size()== 0){
            ProjectsManager_Form projects_form = new ProjectsManager_Form();
        }
    }

    static Icon icon = new Icon() {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Image image = new ImageIcon("src\\logo.png").getImage();
            g.drawImage(image,x,y,100,100,null);
        }
        @Override
        public int getIconWidth() {
            return 100;
        }
        @Override
        public int getIconHeight() {
            return 100;
        }
    };//width='200px'
    static String message = "<html><body><div align='center'>МІНІСТЕРСТВО ОХОРОНИ ЗДОРОВ’Я УКРАЇНИ<br>" +
            "НАЦІОНАЛЬНА ДИТЯЧА СПЕЦІАЛІЗОВАНА ЛІКАРНЯ «ОХМАТДИТ»<br>ЦЕНТР МЕТАБОЛІЧНИХ ЗАХВОРЮВАНЬ<br>" +
            "\"Автоматизованна система внутрішньолабораторного контролю якості лабораторних досліджень\"<br><br>" +
            "Програму створено за технічної підтримки НТУУ \"КПІ\" КБІС та ФІОТ</div></body></html>";
    static JLabel messageLabel = new JLabel(message);
}
