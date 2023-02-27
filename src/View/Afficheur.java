package View;

import model.Node;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;


public class Afficheur{
	//private Node tree;
    private Map<Integer, Vector<Node>> trees;

    public Afficheur(Node tree,JFrame frame) {
        //this.tree = tree;
        JDialog jDialog = new JDialog(frame,"Relational Tree",true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jDialog.setContentPane(new JScrollPane(new TreePanel(tree)));
        jDialog.pack();
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    public Afficheur(Map<Integer, Vector<Node>> trees,JFrame frame) {
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
    }

    private class TreePanel extends JPanel {
        private Node tree;
        private final int NODE_RADIUS = 20;
        private final int LEVEL_HEIGHT = 80;
        private final int HORIZONTAL_SPACING = 40;
        private final int VERTICAL_SPACING = 80;

        public TreePanel(Node tree){
            this.tree = tree;
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1300, 600);
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
            
            g.drawString(node.toString(), x - node.toString().length()*2, y + 5);



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
	
	
	
	
}
