package GUI;

import javax.swing.*;
import java.awt.*;

public class EntranceDialog extends JDialog {
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

        mainPanel.add(newFileBtn);
        mainPanel.add(connectBtn);
        add(mainPanel);

        setTitle("CRDT Application - Collaborative Writing");
        setVisible(true);
        setSize(300, 100);
    }

    private void setNewFile(){
        NotePadGUI.instance.setVisible(true);
        hideDialog();
    }

    private void setConnect(){
        ConnectionInfo.getInstance().setVisible(true);
        hideDialog();
    }

    private void hideDialog(){
//        setVisible(false);
    }

}
