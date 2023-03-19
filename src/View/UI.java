package View;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import Controller.Decomposer;
import Controller.Optimizer;
import Controller.Transformer;
import model.Node;
import model.Query;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;

import java.awt.*;

public class UI extends JFrame{

	public UI() {
        initComponents();
        setTitle("Optimiseur de requete");
        setLocation(400,200);
        setVisible(true);
    }

   
   
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Bold", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(104, 131, 186));
        jLabel1.setText("Optimiseur de requete ");

        jTextField1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
       /* jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });*/

        jButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 153, 153));
        jButton1.setText("Varier");


        jButton4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255,255,255));
        jButton4.setBackground(new java.awt.Color(104, 131, 186));
        jButton4.setText("Generer l'arbre optimale");


        jButton3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255,255,255));
        jButton3.setBackground(new java.awt.Color(104, 131, 186));
        jButton3.setText("Génerer l'arbre initiale");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255,255,255));
        jButton2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(104, 131, 186));
        jButton2.setText("Réinitialiser");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        //.addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        //.addGap(10, 10, 10)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addGap(98, 98, 98))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        /*.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)*/.addGap(98, 98, 98))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    //.addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                    //.addComponent(jButton4))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        pack();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        
    }





    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        try {
            Query query = Decomposer.SplitQuery(jTextField1.getText());
            Transformer transformer = new Transformer(query);
            //P = new Afficheur(transformer.TransformQuery(),this);
            Node node = transformer.TransformQuery();
            transformer.TransformerTree();
            P = new Afficheur(node, this, transformer, query);
            //P.printTree();
        } catch (SyntaxeException | SemantiqueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    e,
                    " titre ",
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        try {
            Query query = Decomposer.SplitQuery(jTextField1.getText());
            Transformer transformer = new Transformer(query);
            //P = new Afficheur(transformer.TransformQuery(),this);
            Node node = transformer.TransformQuery();
            transformer.TransformerTree();
            transformer.generatePTrees();
            Optimizer optimizer = new Optimizer(transformer.getPtrees(), transformer.getQ());
            Decomposer.MyPair<Node, Node> optimalTree[] = optimizer.getOptimalTree();
            P = new Afficheur(optimalTree, this, transformer.getQ());
            //P.printTree();
        } catch (SyntaxeException | SemantiqueException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    e,
                    " titre ",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

   
    public static void run() {
        new UI();
    }


    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextArea jTextField1;
    private Afficheur P;
	
}
