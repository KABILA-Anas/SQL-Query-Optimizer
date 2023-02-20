package model;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;


public class Afficheur extends JFrame {
	private Node tree;

    public Afficheur(Node tree) {
        this.tree = tree;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new TreePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class TreePanel extends JPanel {
        private final int NODE_RADIUS = 20;
        private final int LEVEL_HEIGHT = 80;
        private final int HORIZONTAL_SPACING = 40;
        private final int VERTICAL_SPACING = 80;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (tree != null) {
                drawNode(g, tree, getWidth() / 2, NODE_RADIUS, getWidth() / 4);
            }
        }

        private void drawNode(Graphics g, Node node, int x, int y, int dx) {
            //g.drawOval(x - NODE_RADIUS, y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
            
            g.drawString(node.toString(), x - node.toString().length()*2, y + 5);

            if (node.getLeftChild() != null) {
                int childX = x - dx;
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
