package template.ui;

import template.creator.TemplateCreator;

import javax.swing.*;
import java.awt.event.*;

public class TemplateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JCheckBox checkBoxHandler;
    private JCheckBox checkBoxInstance;

    public TemplateDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here

        if (checkBoxHandler.isSelected()) {
            TemplateCreator.getHandlerTemplate();
        }

        if (checkBoxInstance.isSelected()) {
            TemplateCreator.getInstanceTemplate();
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        TemplateDialog dialog = new TemplateDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
