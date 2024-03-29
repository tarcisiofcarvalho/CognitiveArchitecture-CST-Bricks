/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

package support;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;

class MVTimerTask extends TimerTask {
    MindView mv;
    boolean enabled = true;
    
    public MVTimerTask(MindView mvi) {
        mv = mvi;
    }
    
    public void run() {
        if (enabled) mv.tick();
    }
    
    public void setEnabled(boolean value) {
        enabled = value;
    }
}
/**
 *
 * @author rgudwin
 */
public class MindView extends javax.swing.JFrame {

    Timer t;
    List<Memory> mol = new ArrayList<>();
    int j=0;
    Random r = new Random();
    /**
     * Creates new form NewJFrame
     */
    public MindView(String name) {
        initComponents();
        setTitle(name);
    }
    
    public void addMO(Memory moi) {
        mol.add(moi);
    }
    
    public void StartTimer() {
        t = new Timer();
        MVTimerTask tt = new MVTimerTask(this);
        t.scheduleAtFixedRate(tt,0,500);
    }
    
    public void tick() {
        String alltext = "";
        if (mol.size() != 0) 
            for (Memory mo : mol) {
                if (mo.getI() != null) {
                    //Class cl = mo.getT();
                    //Object k = cl.cast(mo.getI());
                    Object k = mo.getI();
                    String moName = mo.getName();
                    if (moName.equals("KNOWN_APPLES") || moName.equals("VISION")) {
                        //alltext += mo.getName()+": "+k+"<-> ";
                        alltext += mo.getName()+": [ ";
                        CopyOnWriteArrayList<Thing> l = new CopyOnWriteArrayList<>((List<Thing>)k);
                        for (Thing t : l) {
                            String kindofthing = "t";
                            if (t.getCategory() == Constants.categoryPFOOD) kindofthing = "a";
                            alltext += kindofthing+"("+(int)(t.getX1()+t.getX2())/2+","+(int)(t.getY1()+t.getY2())/2+") ";
                        }    
                        alltext += "]\n";
                    }
                    else if (moName.equals("CLOSEST_APPLE")) {
                        Thing t = (Thing)k;
                        String kindofthing = "t";
                        if (t.getCategory() == 21) kindofthing = "a";
                        alltext += moName+": "+kindofthing+"("+(int)(t.getX1()+t.getX2())/2+","+(int)(t.getY1()+t.getY2())/2+")\n";
                    }
                    else     
                    alltext += mo.getName()+": "+k+"\n";
                }
                else
                    //alltext += mo.getName()+": "+mo.getI()+"\n";
                    alltext += mo.getName()+":\n";
            }   
        text.setText(alltext);
        j++;
//        if (j == 7) {
//            try {
//              World.createFood(0,r.nextInt(800) , r.nextInt(600));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            j = 0;
//        }
        //System.out.println("i");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        text.setColumns(20);
        text.setRows(5);
        jScrollPane1.setViewportView(text);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MindView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MindView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MindView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MindView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MindView mv;
                mv = new MindView("Teste");
                mv.setVisible(true);
                mv.StartTimer();
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea text;
    // End of variables declaration//GEN-END:variables
}
