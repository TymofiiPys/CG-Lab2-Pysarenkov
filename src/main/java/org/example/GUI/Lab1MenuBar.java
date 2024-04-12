package org.example.GUI;

import org.example.MainWindow;
import org.example.pointloc.Graph;
import org.example.regtree.RTree2D;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.Optional;

public class Lab1MenuBar extends JMenuBar {
    public StringBuilder filePath = new StringBuilder();

    public Lab1MenuBar(JFrame parent, MainWindow mw) {
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openMI = new JMenuItem("Відкрити");
        FileFilter textFilesFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "(*.txt) Текстовий файл";
            }
        };
        openMI.addActionListener(new OpenFileDialogActionListener(parent, textFilesFilter, filePath, () -> {
            mw.regTreeDrawer.setRTree(RTree2D.readFromFile(filePath.toString()));
//            mw.showDirGrButton.setEnabled(true);
//            mw.showChainsButton.setEnabled(true);
            mw.regSearchButton.setEnabled(true);
            mw.regTreeDrawer.drawPoints();
//            Boolean isChainMethodApplicable = mw.graphDrawer.graphChainMethodApplicable();
//            if (isChainMethodApplicable == null) {
//                mw.statusLabel.setForeground(Color.RED);
//                mw.statusLabel.setText("Помилка");
//                mw.showChainsButton.setEnabled(false);
//                mw.showDirGrButton.setEnabled(false);
//                mw.regSearchButton.setEnabled(false);
//            } else {
//                if (isChainMethodApplicable) {
//                    mw.statusLabel.setForeground(Color.BLACK);
//                    mw.statusLabel.setText("<html> До графу можна застосувати <br> метод ланцюгів </html>");
//                    mw.showChainsButton.setEnabled(true);
//                    mw.showDirGrButton.setEnabled(true);
//                    mw.regSearchButton.setEnabled(true);
//                    JDialog dialog = new JDialog();
//                    dialog.setTitle("Ланцюги");
//                    StringBuilder text = new StringBuilder();
//                    text.append("<html><center>");
//                    text.append(mw.graphDrawer.getChainList());
//                    text.append("</center></html>");
//                    JLabel label = new JLabel(text.toString());
//                    label.setHorizontalAlignment(SwingConstants.CENTER);
//                    dialog.add(label);
//                    dialog.setSize(400, 300);
//                    dialog.setResizable(true);
//                    dialog.setLocationRelativeTo(null);
//                    dialog.setVisible(true);
//                } else {
//                    mw.statusLabel.setForeground(Color.RED);
//                    mw.statusLabel.setText("<html> До графу НЕ можна <br> застосувати <br> метод ланцюгів </html>");
//                    mw.showChainsButton.setEnabled(false);
//                    mw.showDirGrButton.setEnabled(true);
//                    mw.regSearchButton.setEnabled(false);
//                }
//            }
        }));
        JMenuItem exitMI = new JMenuItem("Вийти");
        exitMI.addActionListener(e -> System.exit(0));
        fileMenu.add(openMI);
        fileMenu.add(exitMI);
        this.add(fileMenu);
    }
}
