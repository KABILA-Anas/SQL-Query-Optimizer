package View;

import Controller.Optimizer;
import model.Node;

import java.awt.*;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;


public class Afficheur{
	//private Node tree;
    private Map<Integer, Vector<Node>> trees;
    private Map<Node, Vector<Node>> ptrees;
    private boolean printCout;

    public Afficheur(Node tree,JFrame frame) {
        //this.tree = tree;
        JDialog jDialog = new JDialog(frame,"Relational Tree",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jDialog.setContentPane(new JScrollPane(new TreePanel(tree, false)));
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
            if(pCout){
                //add(new JLabel("==> Cout avec Pipeline = " + Double.toString(Optimizer.getCoutPipeline(tree))), FlowLayout.LEFT);
                add(new JLabel("==> Cout avec Pipeline = " + Double.toString(Optimizer.getCoutPipeline(tree)) + "  Cout totale = " + Double.toString(Optimizer.getCoutTotale(tree))), FlowLayout.LEFT);
            }
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1300, 500);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

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



            if (node.getLeftChild() != null) {
                int childX = x;
                if(node.getRightChild() != null)
                    childX = x - dx;
                int childY = y + LEVEL_HEIGHT;
                g.drawLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                drawNode(g, node.getLeftChild(), childX, childY, dx / 2);
            }

            if (node.getRightChild() != null) {
                int childX = x + dx;
                int childY = y + LEVEL_HEIGHT;
                g.drawLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
                drawNode(g, node.getRightChild(), childX, childY, dx / 2);
            }
        }
    }



    private class TitlePanel extends JPanel {

        public TitlePanel(String title1, String title2) {
            // Set the layout of the panel
            setLayout(new BorderLayout());

            // Create the two labels
            JLabel label1 = new JLabel(title1);
            label1.setFont(new Font("Arial", Font.BOLD, 24));
            label1.setForeground(Color.black);
            JLabel label2 = new JLabel(title2);
            label2.setFont(new Font("Arial", Font.BOLD, 24));
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
