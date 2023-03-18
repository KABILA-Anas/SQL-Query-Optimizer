package View;

import Controller.Estimator;
import Controller.Optimizer;
import Controller.Transformer;
import model.Node;
import model.Query;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


public class Afficheur{
	//private Node tree;
    private Map<Integer, Vector<Node>> trees;
    private Map<Node, Vector<Node>> ptrees;
    Query query;
    Transformer transformer;
    private boolean printCout;
    private static Color title2_color = new Color(104, 131, 186);
    private static Color title1_color = new Color(61, 59, 142);
    private static Color button_color = new Color(249, 249, 249);
    private static Color rules_box_color = new Color(104, 131, 186);
    private static Color rules_title_color = new Color(249,249,249);
    private static Color rules_color = new Color(249,249,249);

    public Afficheur(Node tree, JFrame frame, Transformer transformer, Query query) {
        //this.tree = tree;
        this.query = query;
        this.transformer = transformer;
        JDialog jDialog = new JDialog(frame,"L'arbre algébrique initiale",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container content = jDialog.getContentPane();
        //content.setLayout(new BoxLayout(jDialog, BoxLayout.X_AXIS));

        //JPanel mainPanel = new JPanel();
        int nrbLtrees = 0;
        for (Map.Entry<Integer, Vector<Node>> entry : transformer.getTrees().entrySet())
            nrbLtrees += ((Vector<Node>)entry.getValue()).size();

        //Transformer transformer = new Transformer();
        Vector<Node> initptrees = transformer.generatePTrees(tree);
        int nbrPtrees = initptrees.size();

        //TitlePanel titlePanel = new TitlePanel();

        JButton B1 = new JButton("Afficher les variantes logiques");
        B1.setFocusable(false);
        B1.setBackground(button_color);
        JButton B2 = new JButton("Afficher les variantes physiques");
        B2.setFocusable(false);
        B2.setBackground(button_color);
        JPanel titlePanel = new JPanel(new BorderLayout());
        // Create the two labels
        JPanel title1 = new JPanel();
        title1.setLayout(new BoxLayout(title1,BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel("Nombre des arbres logiques : " + nrbLtrees);
        label1.setFont(new Font("Arial", Font.BOLD, 20));
        label1.setForeground(Color.WHITE);
        title1.add(Box.createVerticalStrut(25));
        title1.add(label1);
        title1.add(Box.createVerticalStrut(10));
        title1.add(B1);
        title1.add(Box.createVerticalStrut(10));
        title1.setBackground(title1_color);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        B1.setAlignmentX(Component.CENTER_ALIGNMENT);
        //title1.setBackground(Color.red);
        //
        JPanel title2 = new JPanel();
        title2.setLayout(new BoxLayout(title2,BoxLayout.Y_AXIS));
        JLabel label2 = new JLabel("Nombre des arbres physiques : " + nbrPtrees);
        label2.setFont(new Font("Arial", Font.BOLD, 20));
        label2.setForeground(Color.WHITE);
        title2.add(Box.createVerticalStrut(25));
        title2.add(label2);
        title2.add(Box.createVerticalStrut(10));
        title2.add(B2);
        title2.setBackground(title2_color);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        B2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel to hold the labels
        JPanel titlePanel1 = new JPanel(new GridLayout(1, 2));
        titlePanel1.add(title1);
        titlePanel1.add(title2);

        // Add the title panel to the top of the main panel
        titlePanel.add(titlePanel1, BorderLayout.NORTH);
        //tree panel
        TreePanel treePanel = new TreePanel(tree, false, 0);


        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(35));
        content.setBackground(Color.white);
        //treePanel.add(titlePanel,FlowLayout.LEFT);
        content.add(treePanel);

        B1.addActionListener(e -> {
            // Toggle the visibility of the panel
            new LogicalTrees(transformer.getTrees(), jDialog);
        });

        B2.addActionListener(e -> {
            // Toggle the visibility of the panel
            //Transformer transformer = new Transformer();
            new PhysicalTrees(initptrees, (JPanel) content, jDialog);
        });

        //jDialog.setContentPane(jScrollPane);
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }


    public Afficheur(Node logicalTree, Node physicalTree, JFrame frame, Query query) {
        //this.tree = tree;
        this.query = query;
        JDialog jDialog = new JDialog(frame,"L'arbre algébrique optimale",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container content = jDialog.getContentPane();
        //content.setLayout(new BoxLayout(jDialog, BoxLayout.X_AXIS));

        JButton B2 = new JButton("Afficher les variantes physiques");
        B2.setFocusable(false);
        B2.setBackground(button_color);
        // Create the two labels
        //title1.setBackground(Color.red);
        //
        B2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel to hold the labels

        // Add the title panel to the top of the main panel
        //tree panel
        TreePanel treePanel = new TreePanel(logicalTree, false, 0);


        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Box.createVerticalStrut(35));
        content.setBackground(Color.white);
        //treePanel.add(titlePanel,FlowLayout.LEFT);
        content.add(treePanel);

        B2.addActionListener(e -> {
            // Toggle the visibility of the panel
            //Transformer transformer = new Transformer();
            new PhysicalTrees(physicalTree, jDialog, 0);
        });

        treePanel.add(B2, FlowLayout.LEFT);

        //jDialog.setContentPane(jScrollPane);
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }


    public Afficheur(Map<Node, Vector<Node>> ptrees, JFrame frame, boolean printCout) {
        this.ptrees = ptrees;
        this.printCout = printCout;

        int nrbLtrees, nbrPtrees = 0;
        nrbLtrees = ptrees.size();
        for (Map.Entry<Node, Vector<Node>> entry : ptrees.entrySet()){
            nbrPtrees = entry.getValue().size();
            break;
        }

        //JPanel

        //System.out.println("nrbLtrees " + nrbLtrees + "  nbrPtrees " + nbrPtrees);
        TitlePanel titlePanel = new TitlePanel("Nombre des arbres logiques : " + nrbLtrees, "Nombre des arbres physiques : " + nbrPtrees);

        JDialog jDialog = new JDialog(frame,"Relational Tree",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel JP = new JPanel();
        JP.setLayout(new BoxLayout(JP, BoxLayout.Y_AXIS));
        JScrollPane SP = new JScrollPane(JP);
        JP.add(titlePanel);
        //int i = 0;
        TreePanel mainTree;
        for (Map.Entry<Node, Vector<Node>> entry : ptrees.entrySet()){
            mainTree = new TreePanel(entry.getKey(), false, 0);
            mainTree.setBackground(Color.PINK);
            mainTree.setBorder(BorderFactory.createLoweredBevelBorder());
            //JButton B = new Button("Afficher les arbres physiques");
            JToggleButton B = new JToggleButton("Afficher les arbres physiques");
            B.setBackground(Color.BLACK);
            B.setForeground(Color.WHITE);
            mainTree.setLayout(new FlowLayout(FlowLayout.LEFT));
            mainTree.add(B, FlowLayout.LEFT);
            JP.add(mainTree);

            JPanel JPI = new JPanel();
            JPI.setLayout(new BoxLayout(JPI, BoxLayout.Y_AXIS));
            for (Node n : entry.getValue()) {
                JPI.add(new TreePanel(n, printCout, 0));
                //i++;
            }
            JPI.setVisible(false);
            JP.add(JPI);
            B.addActionListener(e -> {
                // Toggle the visibility of the panel
                JPI.setVisible(!JPI.isVisible());
            });
        }
        //System.out.println("==> " + i);
        System.out.println("--------------------------------------------------------");

        jDialog.setContentPane(SP);
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    private class LogicalTrees implements ActionListener {
        JPanel cardPanel;
        JButton nextButton, prevButton;

        public LogicalTrees(Map<Integer, Vector<Node>> trees, JDialog jDialog) {
            JDialog jd = new JDialog(jDialog, "Les arbres logiques", true);
            jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Container container = jd.getContentPane();

            cardPanel = new JPanel(new CardLayout());
            TreePanel treePanel;
            for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet())
                for (Node n : entry.getValue()) {

                    JPanel mainPanel = new JPanel();
                    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));

                    JPanel rulesPanel = new JPanel();
                    //rulesPanel.setLayout(new BoxLayout(rulesPanel,BoxLayout.Y_AXIS));
                    //rulesPanel.setLayout(new FlowLayout());
                    Box box = Box.createVerticalBox();
                    Vector<String> rules = transformer.getRules(n);
                    JLabel rules_title = new JLabel(" Les règles appliquées : ");
                    rules_title.setFont(new Font("Arial",Font.BOLD,22));
                    JPanel rules_title_panel = new JPanel();
                    rules_title_panel.setBackground(rules_title_color);
                    rules_title_panel.add(rules_title);
                    box.add(rules_title_panel);
                    //box.add(rules_title);
                    box.add(Box.createVerticalStrut(15));

                    int etape = 0;
                    if(rules != null) {
                        JLabel rule_label;

                        for (String rule : rules) {
                            JPanel rule_panel = new JPanel();
                            rule_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
                            rule_panel.setBackground(rules_box_color);

                            rule_label = new JLabel("     "+(++etape)+" - "+rule);
                            rule_label.setFont(new Font("Arial",Font.BOLD,16));
                            rule_label.setForeground(rules_color);
                            rule_panel.add(rule_label);
                            box.add(rule_panel);
                            box.add(Box.createVerticalStrut(10));
                        }
                    }
                    rulesPanel.setBackground(rules_box_color);
                    rulesPanel.add(box);


                    JPanel groupPanel = new JPanel();
                    groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
                    JButton B2 = new JButton("Afficher les variantes physiques");
                    B2.setFocusable(false);
                    B2.setForeground(rules_title_color);
                    B2.setBackground(rules_box_color);
                    treePanel = new TreePanel(n, false, 0);
                    treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
                    treePanel.add(B2, FlowLayout.LEFT);
                    B2.addActionListener(e -> {
                        // Toggle the visibility of the panel
                        Transformer transformer = new Transformer();
                        //Transformer transformer = new Transformer();
                        new PhysicalTrees(transformer.generatePTrees(n), groupPanel, jd);
                    });
                    mainPanel.add(treePanel);
                    mainPanel.add(rulesPanel);
                    groupPanel.add(mainPanel);
                    //groupPanel.setVisible(true);
                    //JScrollPane groupPanelSp = new JScrollPane(groupPanel);
                    //groupPanelSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    cardPanel.add(groupPanel);
                }
            /*cardPanel.add(new JLabel("Card 1"), "card1");
            cardPanel.add(new JLabel("Card 2"), "card2");
            cardPanel.add(new JLabel("Card 3"), "card3");*/

            nextButton = new JButton("→");
            nextButton.setFont(new Font("",Font.BOLD,22));
            nextButton.setBackground(new Color(176, 226, 152));
            nextButton.addActionListener(this);

            prevButton = new JButton("←");
            prevButton.setFont(new Font("",Font.BOLD,22));
            prevButton.setBackground(new Color(176, 226, 152));
            prevButton.addActionListener(this);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            buttonPanel.add(prevButton);
            buttonPanel.add(nextButton);

            container.add(cardPanel, BorderLayout.CENTER);
            container.add(buttonPanel, BorderLayout.SOUTH);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            container.setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) (screenSize.getHeight() - screenSize.getHeight()*0.1)));

            jd.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                        cardLayout.previous(cardPanel);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                        cardLayout.next(cardPanel);
                    }
                }
            });

            jd.pack();
            jd.setLocationRelativeTo(null);
            jd.setVisible(true);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == nextButton) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.next(cardPanel);
            } else if (e.getSource() == prevButton) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.previous(cardPanel);
            }
        }
    }

    private class PhysicalTrees {

        public PhysicalTrees(Node node, JDialog jDialog, int type) {
            JPanel JP = new JPanel();
            JP.setLayout(new BoxLayout(JP, BoxLayout.X_AXIS));
            JScrollPane SP = new JScrollPane(JP);
            JDialog jd = new JDialog(jDialog, "Les arbres physiques", true);

            TreePanel treePanel1, treePanel2;
            //Estimator estimator = new Estimator(node, query);
            //double[] pipeline = {0};
            //estimator.estimate(pipeline);
            treePanel1 = new TreePanel(node, true, 0);
            treePanel1.setBorder(BorderFactory.createLoweredBevelBorder());

            JP.add(treePanel1);

            treePanel2 = new TreePanel(node, true, 1);
            treePanel1.setBorder(BorderFactory.createLoweredBevelBorder());
            treePanel2.setBorder(BorderFactory.createLoweredBevelBorder());

            JP.add(treePanel2);

            JPanel buttonsPanel = new JPanel();
            //buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

            JRadioButton radioButton1, radioButton2;
            ButtonGroup buttonGroup;

            radioButton1 = new JRadioButton("Cout avec pipeline");
            radioButton2 = new JRadioButton("Cout avec materialisation");

            radioButton1.setSelected(true);

            buttonGroup = new ButtonGroup();
            buttonGroup.add(radioButton1);
            buttonGroup.add(radioButton2);

            Box box = Box.createVerticalBox();

            box.add(radioButton1);
            box.add(radioButton2);

            buttonsPanel.add(box);

            treePanel1.setVisible(true);
            treePanel2.setVisible(false);

            radioButton1.addActionListener(e -> {
                treePanel1.setVisible(true);
                treePanel2.setVisible(false);
            });

            radioButton2.addActionListener(e -> {
                treePanel1.setVisible(false);
                treePanel2.setVisible(true);
            });

            JP.add(buttonsPanel);

            Container content = jd.getContentPane();
            jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //TreePanel treePanel = new TreePanel(tree, false);
            content.add(SP);
            jd.pack();
            jd.setLocationRelativeTo(jDialog);
            jd.setVisible(true);
        }

        public PhysicalTrees(Vector<Node> nodes, JPanel panel, JDialog jd) {
            JPanel JP = new JPanel();
            JP.setLayout(new BoxLayout(JP, BoxLayout.X_AXIS));
            JScrollPane SP = new JScrollPane(JP);
            //JDialog jd = new JDialog(jDialog, "Les arbres physiques", true);
            TreePanel treePanel;
            for (Node n : nodes) {
                treePanel = new TreePanel(n, false, 0);
                treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JButton B2 = new JButton("     Calculer le cout      ");
                B2.setFocusable(false);
                B2.setBackground(new Color(176, 226, 152));
                JPanel bPanel = new JPanel();
                bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.Y_AXIS));
                bPanel.setBackground(Color.white);
                bPanel.add(B2);
                B2.addActionListener(e -> {
                    // Toggle the visibility of the panel
                    Transformer transformer = new Transformer();
                    //Transformer transformer = new Transformer();
                    new PhysicalTrees(n, jd, 0);
                });
                /*JButton B3 = new JButton("Calculer le cout avec materialisation");
                B3.setFocusable(false);
                B3.setBackground(new Color(176, 226, 152));
                bPanel.add(Box.createVerticalStrut(5));
                bPanel.add(B3);
                B3.addActionListener(e -> {
                    // Toggle the visibility of the panel
                    Transformer transformer = new Transformer();
                    //Transformer transformer = new Transformer();
                    new PhysicalTrees(n, jd, 1);
                });*/
                treePanel.add(bPanel, FlowLayout.LEFT);
                JP.add(treePanel);
            }
            //Container content = jd.getContentPane();
            //jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //TreePanel treePanel = new TreePanel(tree, false);
            JP.setSize(panel.getSize());
            panel.add(SP);
            jd.pack();
            //jd.setLocationRelativeTo(jDialog);
            //jd.setVisible(true);
        }
    }


    /*
    private class PhysicalTrees {

        public PhysicalTrees(Node node, JDialog jDialog, int type) {
            JPanel JP = new JPanel();
            JP.setLayout(new BoxLayout(JP, BoxLayout.X_AXIS));
            JScrollPane SP = new JScrollPane(JP);
            JDialog jd = new JDialog(jDialog, "Les arbres physiques", true);

            TreePanel treePanel;
            Estimator estimator = new Estimator(node, query);
            double[] pipeline = {0};
            estimator.estimate(pipeline);
            treePanel = new TreePanel(node, true, type);
            treePanel.setBorder(BorderFactory.createLoweredBevelBorder());

            JP.add(treePanel);

            Container content = jd.getContentPane();
            jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //TreePanel treePanel = new TreePanel(tree, false);
            content.add(SP);
            jd.pack();
            jd.setLocationRelativeTo(jDialog);
            jd.setVisible(true);
        }

        public PhysicalTrees(Vector<Node> nodes, JDialog jDialog) {
            JPanel JP = new JPanel();
            JP.setLayout(new BoxLayout(JP, BoxLayout.X_AXIS));
            JScrollPane SP = new JScrollPane(JP);
            JDialog jd = new JDialog(jDialog, "Les arbres physiques", true);
            TreePanel treePanel;
            for (Node n : nodes) {
                treePanel = new TreePanel(n, false, 0);
                treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JButton B2 = new JButton("     Calculer le cout avec pipeline      ");
                B2.setFocusable(false);
                B2.setBackground(new Color(176, 226, 152));
                JPanel bPanel = new JPanel();
                bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.Y_AXIS));
                bPanel.setBackground(Color.white);
                bPanel.add(B2);
                B2.addActionListener(e -> {
                    // Toggle the visibility of the panel
                    Transformer transformer = new Transformer();
                    //Transformer transformer = new Transformer();
                    new PhysicalTrees(n, jd, 0);
                });
                JButton B3 = new JButton("Calculer le cout avec materialisation");
                B3.setFocusable(false);
                B3.setBackground(new Color(176, 226, 152));
                bPanel.add(Box.createVerticalStrut(5));
                bPanel.add(B3);
                B3.addActionListener(e -> {
                    // Toggle the visibility of the panel
                    Transformer transformer = new Transformer();
                    //Transformer transformer = new Transformer();
                    new PhysicalTrees(n, jd, 1);
                });
                treePanel.add(bPanel, FlowLayout.LEFT);
                JP.add(treePanel);
            }
            Container content = jd.getContentPane();
            jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //TreePanel treePanel = new TreePanel(tree, false);
            content.add(SP);
            jd.pack();
            jd.setLocationRelativeTo(jDialog);
            jd.setVisible(true);
        }
    }
     */

    private class TreePanel extends JPanel {
        private Node tree;
        private boolean pCout;
        int type;
        private final int NODE_RADIUS = 20;
        private final int LEVEL_HEIGHT = 80;
        private final int HORIZONTAL_SPACING = 40;
        private final int VERTICAL_SPACING = 80;

        public TreePanel(Node tree, boolean pCout, int type){
            this.tree = tree;
            this.pCout = pCout;
            this.type = type;
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBackground(Color.WHITE);
            if(pCout){
                //add(new JLabel("==> Cout avec Pipeline = " + Double.toString(Optimizer.getCoutPipeline(tree))), FlowLayout.LEFT);
                double[] pipeline_cout = {0};
                Estimator estimator = new Estimator(tree,query);
                double totalCout = estimator.estimate(pipeline_cout);
                if(type == 0)
                    add(new JLabel("==> Cout avec Pipeline = " + pipeline_cout[0]));
                else
                    add(new JLabel("==> Cout avec materialisation = " + totalCout, FlowLayout.LEFT));
            }
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 600);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("",Font.BOLD,14));
            ((Graphics2D)g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));;
            if (tree != null) {
                drawNode(g, tree, getWidth() / 2, NODE_RADIUS, getWidth() / 4);
            }
        }

        private void drawNode(Graphics g, Node node, int x, int y, int dx) {
            //g.drawOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            String nodeLabel = node.toString();
            if(pCout && !node.getName().equals("Relation"))
                nodeLabel += " (" + node.getCout() + ")";

            g.drawString(nodeLabel, x - node.toString().length()*2, y + 5);


            if (node.getRightChild() != null) {
                int childX = x + dx;
                int childY = y + LEVEL_HEIGHT;
                g.drawLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                if(node.getLeftChild().getLeftChild() != null) {
                    if (pCout) {
                        if (type == 1)
                            g.drawString("(1.1)", (x + childX) / 2, (y + childY) / 2);
                    }
                }
                drawNode(g, node.getRightChild(), childX, childY, dx / 2);
            }

            if (node.getLeftChild() != null) {
                int childY = y + LEVEL_HEIGHT;
                int childX = x;
                if(node.getRightChild() != null){
                    childX = x - dx;
                    g.drawLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                    if(node.getLeftChild().getLeftChild() != null) {
                        if (pCout) {
                            if (type == 1)
                                g.drawString("(1.1)", (x + childX) / 2, (y + childY) / 2);
                        }
                    }
                    drawNode(g, node.getLeftChild(), childX, childY, dx / 2);
                }
                else {
                    g.drawLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                    if(node.getLeftChild().getLeftChild() != null){
                        if(pCout){
                            if(type == 1)
                                g.drawString("(1.1)", (x + childX)/2, (y + childY)/2);
                        }
                    }
                    drawNode(g, node.getLeftChild(), childX, childY, dx);
                }
            }


        }
    }



    private class TitlePanel extends JPanel {

        public TitlePanel(String title1, String title2) {
            // Set the layout of the panel
            setLayout(new BorderLayout());

            // Create the two labels
            JLabel label1 = new JLabel(title1);
            label1.setFont(new Font("Arial", Font.BOLD, 20));
            label1.setForeground(Color.black);
            JLabel label2 = new JLabel(title2);
            label2.setFont(new Font("Arial", Font.BOLD, 20));
            label2.setForeground(Color.black);

            // Create a panel to hold the labels
            JPanel titlePanel = new JPanel(new GridLayout(1, 2));
            titlePanel.add(label1);
            titlePanel.add(label2);

            // Add the title panel to the top of the main panel
            add(titlePanel, BorderLayout.NORTH);
        }

        /*public static void main(String[] args) {
            JFrame frame = new JFrame("TitlePanel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(400, 300));

            JPanel contentPane = new JPanel(new BorderLayout());
            TitlePanel titlePanel = new TitlePanel("Title 1", "Title 2");
            contentPane.add(titlePanel, BorderLayout.NORTH);

            frame.setContentPane(contentPane);
            frame.pack();
            frame.setVisible(true);
        }*/
    }
	
	
	
	
}
