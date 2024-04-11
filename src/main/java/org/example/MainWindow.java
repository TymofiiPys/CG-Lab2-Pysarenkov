package org.example;

import org.example.GUI.Lab1MenuBar;
import org.example.GUI.RegTreeDrawer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class MainWindow extends Container {
    public JButton pointLocButton;
    private JPanel mainPanel;
    private JPanel controlsPanel;
    private JPanel graphicsPanel;
    private JLabel contrPanCoordLabel;
    private JTextField contrPanX1TextField;
    private JLabel contrPanX1Label;
    private JLabel contrPanY1Label;
    private JTextField contrPanY1TextField;
    private JPanel controlsInsidePanel;
    public JButton showDirGrButton;
    public JButton showChainsButton;
    public JLabel statusLabel;
    private JTextField contrPanX2TextField;
    private JLabel contrPanX2Label;
    private JTextField contrPanY2TextField;
    private JLabel contrPanY2Label;
    public final Dimension mainWindowDims = new Dimension(600, 500);
    public final RegTreeDrawer regTreeDrawer;

    private boolean showChains = false;
    private boolean showDir = false;

    private Point2D.Double p1, p2;

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
//            Point2D.Float p = new Point2D.Float(Float.parseFloat(contrPanX1TextField.getText()), Float.parseFloat(contrPanY1TextField.getText()));
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
                    if (p1 != null && p2 != null) {
                        regTreeDrawer.drawRectangle(p1, p2);
                    }
                }
                ;
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
//                    contrPanX1TextField.setText(pointOnGraph.x + "");
//                    contrPanY1TextField.setText(pointOnGraph.y + "");
//                }
//            }


            @Override
            public void mousePressed(MouseEvent e) {
                if (regTreeDrawer.rtreeSet()) {
                    super.mousePressed(e);
                    p1 = new Point2D.Double(e.getX(), e.getY());
                    Point2D.Double pointOnPanel = regTreeDrawer.adaptFromPanel(new Point2D.Double(p1.x, p1.y));
                    contrPanX1TextField.setText(pointOnPanel.x + "");
                    contrPanY1TextField.setText(pointOnPanel.y + "");
                }
            }
        });

        graphicsPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (regTreeDrawer.rtreeSet()) {
                    super.mouseDragged(e);
                    p2 = new Point2D.Double(e.getX(), e.getY());;
                    regTreeDrawer.drawRectangle(p1, p2);
                    Point2D.Double pointOnPanel = regTreeDrawer.adaptFromPanel(new Point2D.Double(p2.x, p2.y));
                    contrPanX2TextField.setText(pointOnPanel.x + "");
                    contrPanY2TextField.setText(pointOnPanel.y + "");
                }
            }
        });

        contrPanX1TextField.getDocument().addDocumentListener(regTFListener());
//                new DocumentListener() {
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
//                    if(!contrPanX1TextField.getText().isBlank() && !contrPanY1TextField.getText().isBlank()) {
//                        Point2D.Float pt = new Point2D.Float(Float.parseFloat(contrPanX1TextField.getText()),
//                                Float.parseFloat(contrPanY1TextField.getText()));
//                        graphDrawer.drawPoint(graphDrawer.adaptToPanel(pt));
//                    }
//                }
//                if(regTreeDrawer.rtreeSet()) {
//                    if(!contrPanX1TextField.getText().isBlank() &&
//                            !contrPanY1TextField.getText().isBlank() &&
//                            !contrPanX2TextField.getText().isBlank() &&
//                            !contrPanY2TextField.getText().isBlank()) {
//                        p1 = new Point2D.Double(Double.parseDouble(contrPanX1TextField.getText()),
//                                Double.parseDouble(contrPanY1TextField.getText()));
//                        p2 = new Point2D.Double(Double.parseDouble(contrPanX1TextField.getText()),
//                                Double.parseDouble(contrPanY1TextField.getText()));
//                        regTreeDrawer.drawRectangle(regTreeDrawer.adaptToPanel(p1), regTreeDrawer.adaptToPanel(p2));
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

        contrPanY1TextField.getDocument().addDocumentListener(regTFListener());

        contrPanX2TextField.getDocument().addDocumentListener(regTFListener());

        contrPanY2TextField.getDocument().addDocumentListener(regTFListener());
    }

    public DocumentListener regTFListener(){
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(regTreeDrawer.rtreeSet()) {
                    if(!contrPanX1TextField.getText().isBlank() &&
                            !contrPanY1TextField.getText().isBlank() &&
                            !contrPanX2TextField.getText().isBlank() &&
                            !contrPanY2TextField.getText().isBlank()) {
                        p1 = new Point2D.Double(Double.parseDouble(contrPanX1TextField.getText()),
                                Double.parseDouble(contrPanY1TextField.getText()));
                        p2 = new Point2D.Double(Double.parseDouble(contrPanX2TextField.getText()),
                                Double.parseDouble(contrPanY2TextField.getText()));
                        regTreeDrawer.drawRectangle(regTreeDrawer.adaptToPanel(p1), regTreeDrawer.adaptToPanel(p2));
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };
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
