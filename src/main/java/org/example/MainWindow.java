package org.example;

import org.example.GUI.GraphDrawer;
import org.example.GUI.Lab1MenuBar;
import org.example.GUI.RegTreeDrawer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MainWindow extends Container {
    public JButton pointLocButton;
    private JPanel mainPanel;
    private JPanel controlsPanel;
    private JPanel graphicsPanel;
    private JLabel contrPanCoordLabel;
    private JTextField contrPanXTextField;
    private JLabel contrPanXLabel;
    private JLabel contrPanYLabel;
    private JTextField contrPanYTextField;
    private JPanel controlsInsidePanel;
    public JButton showDirGrButton;
    public JButton showChainsButton;
    public JLabel statusLabel;
    public final Dimension mainWindowDims = new Dimension(600, 500);
    public final RegTreeDrawer regTreeDrawer;

    private boolean showChains = false;
    private boolean showDir = false;

    private Point p1, p2;

    public MainWindow() {
        // button texts
        pointLocButton.setText("Локалізація точки");
        showDirGrButton.setText("<html> <center> Показати орієнтований <br> граф <br> і номери вершин </center> </html>");
        showChainsButton.setText("Показати ланцюги");

        //
//        graphDrawer = new GraphDrawer(graphicsPanel);
        regTreeDrawer = new RegTreeDrawer(graphicsPanel);

        pointLocButton.setEnabled(false);
        showDirGrButton.setEnabled(false);
        showChainsButton.setEnabled(false);

        //button action listeners
//        pointLocButton.addActionListener(e -> {
//            Point2D.Float p = new Point2D.Float(Float.parseFloat(contrPanXTextField.getText()), Float.parseFloat(contrPanYTextField.getText()));
//            graphDrawer.drawPoint(graphDrawer.adaptToPanel(p));
//            ArrayList<Integer>[] chains = graphDrawer.pointLocation(p);
//            JDialog dialog = new JDialog();
//            dialog.setTitle("Локалізація точки");
//            StringBuilder text = new StringBuilder();
//            text.append("<html><center> Точка знаходиться ");
//            if (chains[1].isEmpty()) {
//                if (chains[0].getFirst() == -1) {
//                    text.append("ззовні від графа, справа");
//                } else if (chains[0].getFirst() == -3) {
//                    text.append("ззовні від графа, зверху від найвищої точки");
//                } else if (chains[0].getFirst() == -4) {
//                    text.append("ззовні від графа, знизу від найнижчої точки");
//                } else {
//                    text.append("на ланцюгах ");
//                    for (int chainInd : chains[0]) {
//                        text.append(chainInd).append(", ");
//                    }
//                    text.setLength(text.length() - 2);
//                }
//            } else {
//                if (chains[1].getFirst() == 0) {
//                    text.append("ззовні від графа, зліва");
//                } else {
//                    text.append("між ланцюгами ")
//                            .append(chains[0].getFirst())
//                            .append(" і ")
//                            .append(chains[1].getFirst());
//                }
//            }
//            text.append("</center></html>");
//            JLabel label = new JLabel(text.toString());
//            label.setHorizontalAlignment(SwingConstants.CENTER);
//            dialog.add(label);
//            dialog.setSize(200, 200);
//            dialog.setResizable(true);
//            dialog.setLocationRelativeTo(null);
//            dialog.setVisible(true);
//        });
//        showDirGrButton.addActionListener(e -> {
//            if (!showDir) {
//                graphDrawer.drawDirectedEnumeratedGraph(true);
//                showDir = true;
//            } else {
//                graphDrawer.drawGraph(true);
//                showDir = false;
//            }
//        });
//        showChainsButton.addActionListener(e -> {
//            if (!showChains) {
//                graphDrawer.drawChains();
//                showChains = true;
//            } else {
//                if (showDir) {
//                    graphDrawer.drawDirectedEnumeratedGraph(true);
//                } else {
//                    graphDrawer.drawGraph(true);
//                }
//                showChains = false;
//            }
//        });

        //graphics panel listeners
        graphicsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (regTreeDrawer.rtreeSet()) {
//                    if (showChains) {
//                        graphDrawer.drawChains();
//                    } else {
//                        if (showDir) {
//                            graphDrawer.drawDirectedEnumeratedGraph(true);
//                        } else {
//                            graphDrawer.drawGraph(true);
//                        }
//                    }
                    regTreeDrawer.drawPoints();
                    if(p1 != null && p2 != null) {
                        regTreeDrawer.drawRectangle(p1, p2);
                    }
                };
            }
        });
        graphicsPanel.addMouseListener(new MouseAdapter() {

//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mouseClicked(e);
//                if (graphDrawer.graphSet()) {
//                    if (showChains) {
//                        graphDrawer.drawChains();
//                    } else {
//                        if (showDir) {
//                            graphDrawer.drawDirectedEnumeratedGraph(true);
//                        } else {
//                            graphDrawer.drawGraph(true);
//                        }
//                    }
//                    graphDrawer.drawPoint(e.getPoint());
//                    Point2D.Float pointOnGraph = graphDrawer.adaptFromPanel(new Point2D.Float(e.getX(), e.getY()));
//                    contrPanXTextField.setText(pointOnGraph.x + "");
//                    contrPanYTextField.setText(pointOnGraph.y + "");
//                }
//            }


            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                p1 = e.getPoint();
            }
        });

        graphicsPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                p2 = e.getPoint();
                regTreeDrawer.drawRectangle(p1, p2);
            }
        });

//        contrPanXTextField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                if (graphDrawer.graphSet()) {
//                    if (showChains) {
//                        graphDrawer.drawChains();
//                    } else {
//                        if (showDir) {
//                            graphDrawer.drawDirectedEnumeratedGraph(true);
//                        } else {
//                            graphDrawer.drawGraph(true);
//                        }
//                    }
//                    if(!contrPanXTextField.getText().isBlank() && !contrPanYTextField.getText().isBlank()) {
//                        Point2D.Float pt = new Point2D.Float(Float.parseFloat(contrPanXTextField.getText()),
//                                Float.parseFloat(contrPanYTextField.getText()));
//                        graphDrawer.drawPoint(graphDrawer.adaptToPanel(pt));
//                    }
//                }
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                insertUpdate(e);
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//
//            }
//        });
//
//        contrPanYTextField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                if (graphDrawer.graphSet()) {
//                    if (showChains) {
//                        graphDrawer.drawChains();
//                    } else {
//                        if (showDir) {
//                            graphDrawer.drawDirectedEnumeratedGraph(true);
//                        } else {
//                            graphDrawer.drawGraph(true);
//                        }
//                    }
//                    if(!contrPanXTextField.getText().isBlank() && !contrPanYTextField.getText().isBlank()) {
//                        Point2D.Float pt = new Point2D.Float(Float.parseFloat(contrPanXTextField.getText()),
//                                Float.parseFloat(contrPanYTextField.getText()));
//                        graphDrawer.drawPoint(graphDrawer.adaptToPanel(pt));
//                    }
//                }
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                insertUpdate(e);
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//
//            }
//        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ЛР№2. Регіональний пошук. Метод дерева регіонів.");
        MainWindow mw = new MainWindow();
        Lab1MenuBar menuBar = new Lab1MenuBar(frame, mw);
        frame.setJMenuBar(menuBar);
        frame.setContentPane(mw.mainPanel);
        frame.setMinimumSize(mw.mainWindowDims);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
