package GUI;

import javax.swing.*;
import java.awt.*;

public class EntranceDialog {
    private JPanel mainPanel;
    private JButton newFileBtn;
    private JButton connectBtn;


    public EntranceDialog(){
        init();
    }
    private void init(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,1));

        newFileBtn = new JButton("New File");
        newFileBtn.addActionListener(e -> setNewFile());

        connectBtn = new JButton("Collaborate");
        connectBtn.addActionListener(e -> setConnect());
    }

    private void setNewFile(){
        NotePadGUI.instance.setVisible(true);
    }

    private void setConnect(){
        ConnectionInfo.getInstance();
    }
}
