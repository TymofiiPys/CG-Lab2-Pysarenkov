package org.example.GUI;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenFileDialogActionListener implements ActionListener {
    private final JFrame parent;
    private final StringBuilder openedFilePath;
    private final Runnable taskToDoAfterOpening;
    private final FileFilter filter;
    public OpenFileDialogActionListener(JFrame parent,
                                        FileFilter filter,
                                        StringBuilder openedFilePath,
                                        Runnable task) {
        this.parent = parent;
        this.filter = filter;
        this.openedFilePath = openedFilePath;
        this.taskToDoAfterOpening = task;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser openFileDialog = new JFileChooser();
        openFileDialog.setDialogTitle("Відкрити файл...");
        openFileDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openFileDialog.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if(filter != null) openFileDialog.addChoosableFileFilter(filter);
        int result = openFileDialog.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            openedFilePath.setLength(0);
            openedFilePath.append(openFileDialog.getSelectedFile().getAbsolutePath());
            taskToDoAfterOpening.run();
        }
    }
}
