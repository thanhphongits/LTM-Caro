/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caro.server;

import caro.common.Room;
import caro.common.Users;
import caro.database.DataFunc;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Nguyen Cao Ky
 */
public class Main extends javax.swing.JFrame {

    static public ArrayList<ClientHandler> lstClient;
    
    static public ArrayList<Room> lstRoom;

    static java.util.List<Users> uslist = new ArrayList<Users>();
    
    static DataFunc df = new DataFunc();
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        list = new java.awt.List();
        bt_refresh = new javax.swing.JButton();
        bt_update = new javax.swing.JButton();
        bt_add = new javax.swing.JButton();
        bt_del = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        bt_refresh.setText("Refresh");
        bt_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_refreshActionPerformed(evt);
            }
        });

        bt_update.setText("Update");
        bt_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_updateActionPerformed(evt);
            }
        });

        bt_add.setText("Add");
        bt_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_addActionPerformed(evt);
            }
        });

        bt_del.setText("Delete");
        bt_del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_delActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(list, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(bt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_del)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_update)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_refresh)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(list, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_add)
                    .addComponent(bt_del)
                    .addComponent(bt_update)
                    .addComponent(bt_refresh))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_refreshActionPerformed
        // TODO add your handling code here:

        uslist = df.getUserList();
        list.removeAll();
        for (Users users : uslist) {
            list.add("Id: " + users.getId()
                +" | Username: " + users.getUsername()
                + " | Password: " + users.getPassword()
                +" | Win:" +users.getWin()
                +" | Lose:" +users.getLose()
                +" | Score:" +users.getScore());
        }
    }//GEN-LAST:event_bt_refreshActionPerformed

    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_updateActionPerformed
        // TODO add your handling code here:
        int indx = list.getSelectedIndex();
        if(indx == -1)
        {
            JOptionPane.showMessageDialog(null, "Select user to update, Please!", "Err", 1);
            return;
        }
        Users us;
        us = uslist.get(indx);

        UpdateUser up = new UpdateUser(us,df);
        up.setVisible(true);
    }//GEN-LAST:event_bt_updateActionPerformed

    private void bt_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_addActionPerformed
        // TODO add your handling code here:
        
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddUser au = new AddUser(df);
        au.setVisible(true);
            }
        });

    }//GEN-LAST:event_bt_addActionPerformed

    private void bt_delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_delActionPerformed
        // TODO add your handling code here:
        int indx = list.getSelectedIndex();
        if(indx == -1)
        {
            JOptionPane.showMessageDialog(null, "Select user to delete, Please!", "Err", 1);
            return;
        }
        Users us;
        us = uslist.get(indx);
        try {
            df.DeleteUser(us.getId());
            JOptionPane.showMessageDialog(null, "User "+ us.getId()+" was deleted!", "Success", 1);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_bt_delActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
         uslist = df.getUserList();
        for (Users users : uslist) {
            list.add("Id: " + users.getId() 
                    +" | Username: " + users.getUsername() 
                    + " | Password: " + users.getPassword() 
                    +" | Win:" +users.getWin()
                    +" | Lose:" +users.getLose()
                    +" | Score:" +users.getScore());
        }                
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("SERVER");
                ServerListener server;
                try {
                    server = new ServerListener();
                    server.start();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                lstClient = new ArrayList<ClientHandler>();
                lstRoom = new ArrayList<Room>();
                for (int i=0; i<10; i++)
                {
                    Room temp = new Room(i);
                    lstRoom.add(temp);
                    
                }
                
                new Main().setVisible(true);
            }
        });
    }

    static class ServerListener extends Thread {

        private ServerSocket serverSocket;

        ServerListener() throws IOException {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(1234);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    final Socket socketToClient = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socketToClient);
                    lstClient.add(clientHandler);
                    System.out.println("Server: " + socketToClient.getInetAddress() + " is connecting");
                    clientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_add;
    private javax.swing.JButton bt_del;
    private javax.swing.JButton bt_refresh;
    private javax.swing.JButton bt_update;
    private java.awt.List list;
    // End of variables declaration//GEN-END:variables
}
