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


public class Afficheur{
	//private Node tree;
    private Map<Integer, Vector<Node>> trees;
    private Map<Node, Vector<Node>> ptrees;
    Query query;
    private boolean printCout;

    public Afficheur(Node tree, JFrame frame, Map<Integer, Vector<Node>> trees, Query query) {
        //this.tree = tree;
        this.query = query;
        JDialog jDialog = new JDialog(frame,"L'arbre algébrique initiale",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container content = jDialog.getContentPane();
        //content.setLayout(new BoxLayout(jDialog, BoxLayout.X_AXIS));

        //JPanel mainPanel = new JPanel();
        int nrbLtrees = 0;
        for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet())
            nrbLtrees += ((Vector<Node>)entry.getValue()).size();

        Transformer transformer = new Transformer();
        Vector<Node> initptrees = transformer.generatePTrees(tree);
        int nbrPtrees = initptrees.size();

        //TitlePanel titlePanel = new TitlePanel();

        JButton B1 = new JButton("Afficher les variantes logiques");
        B1.setFocusable(false);
        JButton B2 = new JButton("Afficher les variantes physiques");
        B2.setFocusable(false);
        JPanel titlePanel = new JPanel(new BorderLayout());
        // Create the two labels
        JPanel title1 = new JPanel();
        title1.setLayout(new BoxLayout(title1,BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel("Nombre des arbres logiques : " + nrbLtrees);
        label1.setFont(new Font("Arial", Font.BOLD, 20));
        label1.setForeground(Color.black);
        title1.add(Box.createVerticalStrut(25));
        title1.add(label1);
        title1.add(Box.createVerticalStrut(10));
        title1.add(B1);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        B1.setAlignmentX(Component.CENTER_ALIGNMENT);
        //title1.setBackground(Color.red);
        //
        JPanel title2 = new JPanel();
        title2.setLayout(new BoxLayout(title2,BoxLayout.Y_AXIS));
        JLabel label2 = new JLabel("Nombre des arbres physiques : " + nbrPtrees);
        label2.setFont(new Font("Arial", Font.BOLD, 20));
        label2.setForeground(Color.black);
        title2.add(Box.createVerticalStrut(25));
        title2.add(label2);
        title2.add(Box.createVerticalStrut(10));
        title2.add(B2);
        title2.setBackground(Color.green);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        B2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel to hold the labels
        JPanel titlePanel1 = new JPanel(new GridLayout(1, 2));
        titlePanel1.add(title1);
        titlePanel1.add(title2);

        // Add the title panel to the top of the main panel
        titlePanel.add(titlePanel1, BorderLayout.NORTH);

        TreePanel treePanel = new TreePanel(tree, false);

        /*JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel,BoxLayout.Y_AXIS));
        JButton B1 = new JButton("Afficher les variantes logiques");
        JButton B2 = new JButton("Afficher les variantes physiques");
        B1.setBackground(Color.BLACK);
        B1.setForeground(Color.WHITE);
        btnPanel.add(B1);
        btnPanel.add(B2);*/
        //mainTree.setLayout(new FlowLayout(FlowLayout.LEFT));
        /*treePanel.add(B1, FlowLayout.LEFT);
        treePanel.add(B2, FlowLayout.LEFT);
        */

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(35));
        //treePanel.add(titlePanel,FlowLayout.LEFT);
        content.add(treePanel);

        B1.addActionListener(e -> {
            // Toggle the visibility of the panel
            new LogicalTrees(trees, jDialog);
        });

        B2.addActionListener(e -> {
            // Toggle the visibility of the panel
            //Transformer transformer = new Transformer();
            new PhysicalTrees(initptrees, jDialog);
        });

        /*JPanel jp = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(jp);
        //jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        TreePanel treePanel = new TreePanel(tree, false);
        treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        jp.add(treePanel);
        treePanel = new TreePanel(tree, false);
        treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        jp.add(treePanel);*/


        /*jp.add(new TreePanel(tree, false));
        jp.add(new TreePanel(tree, false));
        jp.add(new TreePanel(tree, false));
        jp.add(new TreePanel(tree, false));
        jp.add(new TreePanel(tree, false));*/
        //content.add(jp);
        //jScrollPane.add(jp);

        //jDialog.setContentPane(jScrollPane);
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    /*public Afficheur(Map<Integer, Vector<Node>> trees, JFrame frame) {
        this.trees = trees;
        JDialog jDialog = new JDialog(frame,"Relational Tree",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel JP = new JPanel();
        JP.setLayout(new BoxLayout(JP, BoxLayout.Y_AXIS));
        JScrollPane SP = new JScrollPane(JP);
        int i = 0;
        for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet())
            for (Node n : entry.getValue()) {
                JP.add(new TreePanel(n));
                i++;
            }
        System.out.println("==> " + i);
        System.out.println("--------------------------------------------------------");

        jDialog.setContentPane(SP);
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }*/

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
            mainTree = new TreePanel(entry.getKey(), false);
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
                JPI.add(new TreePanel(n, printCout));
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

    /*private class LogicalTrees {

        public LogicalTrees(Map<Integer, Vector<Node>> trees, JDialog jDialog) {

            //this.trees = trees;
            //JDialog jDialog = new JDialog(frame,"Relational Tree",true);
            //jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP = new JPanel();
            JP.setLayout(new BoxLayout(JP, BoxLayout.X_AXIS));
            JScrollPane SP = new JScrollPane(JP);
            TreePanel treePanel;
            for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet())
                for (Node n : entry.getValue()) {
                    treePanel = new TreePanel(n, false);
                    treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
                    JP.add(treePanel);
                }

            JDialog jd = new JDialog(jDialog, "test", true);
            Container content = jd.getContentPane();
            jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //TreePanel treePanel = new TreePanel(tree, false);
            content.add(SP);
            jd.pack();
            jd.setLocationRelativeTo(jDialog);
            jd.setVisible(true);
        }
    }*/

    private class LogicalTrees implements ActionListener {
        JPanel cardPanel;
        JButton nextButton, prevButton;

        public LogicalTrees(Map<Integer, Vector<Node>> trees, JDialog jDialog) {
            JDialog jd = new JDialog(jDialog, "Physical trees", true);
            jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Container container = jd.getContentPane();

            cardPanel = new JPanel(new CardLayout());
            TreePanel treePanel;
            for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet())
                for (Node n : entry.getValue()) {
                    JButton B2 = new JButton("Afficher les variantes physiques");
                    B2.setFocusable(false);
                    treePanel = new TreePanel(n, false);
                    treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
                    treePanel.add(B2, FlowLayout.LEFT);
                    B2.addActionListener(e -> {
                        // Toggle the visibility of the panel
                        Transformer transformer = new Transformer();
                        //Transformer transformer = new Transformer();
                        new PhysicalTrees(transformer.generatePTrees(n), jd);
                    });
                    cardPanel.add(treePanel);
                }
            /*cardPanel.add(new JLabel("Card 1"), "card1");
            cardPanel.add(new JLabel("Card 2"), "card2");
            cardPanel.add(new JLabel("Card 3"), "card3");*/

            nextButton = new JButton("Next");
            nextButton.addActionListener(this);

            prevButton = new JButton("Previous");
            prevButton.addActionListener(this);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            buttonPanel.add(prevButton);
            buttonPanel.add(nextButton);

            container.add(cardPanel, BorderLayout.CENTER);
            container.add(buttonPanel, BorderLayout.SOUTH);

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

        public PhysicalTrees(Node node, JDialog jDialog) {
            JPanel JP = new JPanel();
            JP.setLayout(new BoxLayout(JP, BoxLayout.X_AXIS));
            JScrollPane SP = new JScrollPane(JP);
            JDialog jd = new JDialog(jDialog, "test", true);

            TreePanel treePanel;
            Estimator estimator = new Estimator(node, query);
            double[] pipeline = {0};
            estimator.estimate(pipeline);
            treePanel = new TreePanel(node, true);
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
            JDialog jd = new JDialog(jDialog, "test", true);
            TreePanel treePanel;
            for (Node n : nodes) {
                treePanel = new TreePanel(n, false);
                treePanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JButton B2 = new JButton("Calculer le cout");
                B2.setFocusable(false);
                treePanel.add(B2, FlowLayout.LEFT);
                B2.addActionListener(e -> {
                    // Toggle the visibility of the panel
                    Transformer transformer = new Transformer();
                    //Transformer transformer = new Transformer();
                    new PhysicalTrees(n, jd);
                });
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

    private class TreePanel extends JPanel {
        private Node tree;
        private boolean pCout;
        private final int NODE_RADIUS = 20;
        private final int LEVEL_HEIGHT = 80;
        private final int HORIZONTAL_SPACING = 40;
        private final int VERTICAL_SPACING = 80;

        public TreePanel(Node tree, boolean pCout){
            this.tree = tree;
            this.pCout = pCout;
            setLayout(new FlowLayout(FlowLayout.LEFT));
            /*if(pCout){
                //add(new JLabel("==> Cout avec Pipeline = " + Double.toString(Optimizer.getCoutPipeline(tree))), FlowLayout.LEFT);
                add(new JLabel("==> Cout avec Pipeline = " + Double.toString(Optimizer.getCoutPipeline(tree)) + "  Cout totale = " + Double.toString(Optimizer.getCoutTotale(tree))), FlowLayout.LEFT);
            }*/
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
                drawNode(g, tree, getWidth() / 2, NODE_RADIUS, getWidth() / 2);
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
                drawNode(g, node.getRightChild(), childX, childY, dx / 2);
            }

            if (node.getLeftChild() != null) {
                int childX = x;
                if(node.getRightChild() != null)
                    childX = x - dx;
                int childY = y + LEVEL_HEIGHT;
                g.drawLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                drawNode(g, node.getLeftChild(), childX, childY, dx / 2);
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
