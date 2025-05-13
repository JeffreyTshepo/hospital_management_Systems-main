/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hospital_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author tshep
 */
public class frmViewAllAppointments extends javax.swing.JFrame {

    /**
     * Creates new form frmViewAllAppointments
     */
    public frmViewAllAppointments() {
        initComponents();
    }
 
    Boolean boolRecordExist=false;
    String PatientLastName = null;
    String DoctorLastName = null;
    String AppointmentDate= null;
    String Description = null;

     private void mLoadDetails() {
    String con = "jdbc:mysql://localhost:3306/hospital_management";
    String user = "root";
    String password = "Ramogale1000";
    Connection conMySQLConnectionString = null;
    Statement smt = null;
    ResultSet rs = null;

    try {
        conMySQLConnectionString = DriverManager.getConnection(con, user, password);
        smt = conMySQLConnectionString.createStatement();
         String strQuery = "SELECT * FROM appointments";
        rs = smt.executeQuery(strQuery);

        // DefaultTable model to populate the table
        DefaultTableModel tableModel = (DefaultTableModel) appointmentsTable.getModel();
        tableModel.setRowCount(0);
        
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            tableModel.addRow(row);
        
            }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (smt != null) {
                smt.close();
            }
            if (conMySQLConnectionString != null) {
                conMySQLConnectionString.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
        }
        
    }
     }
    public void mDeleteSelectedAppointment(){
    
        String con = "jdbc:mysql://localhost:3306/hospital_management";
    String user = "root";
    String password = "Ramogale1000";
    Connection conMySQLConnectionString = null;
    PreparedStatement pstmt = null;

    try {
        // Get selected row
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            return;
        }

        // Get the ID of the selected patient (assuming it's in column 0)
        Object id = appointmentsTable.getValueAt(selectedRow, 0);

        // Connect to the database
        conMySQLConnectionString = DriverManager.getConnection(con, user, password);

        // Create the SQL DELETE query
        String deleteQuery = "DELETE FROM appointments WHERE appointmentID = ?";
        pstmt = conMySQLConnectionString.prepareStatement(deleteQuery);
        pstmt.setObject(1, id);
        
        
         // Execute the query
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(null, "Appointment deleted successfully.");
            
            // Refresh the table
            mLoadDetails();
            
            } else {
            JOptionPane.showMessageDialog(null, "Failed to delete the record.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    } finally {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conMySQLConnectionString != null) {
                conMySQLConnectionString.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
        }
    }
    }
    
        private void mGetValuesFromGUI(){
        PatientLastName = txtPatientLastName.getText();
        DoctorLastName = txtDoctorLastName.getText();
        AppointmentDate = txtAppointmentDate.getText();
        Description = txtDescription.getText();      
    }
        
        private void mSetValuesToUpperCase(){
        DoctorLastName = DoctorLastName.toUpperCase();
        PatientLastName = PatientLastName.toUpperCase();
        Description = Description.toUpperCase();
    }
        
     /* private void mCheckIfAppointmentExistInTable(){
        String con = "jdbc:mysql://localhost:3306/hospital_management";
        String user = "root";
        String password = "Ramogale1000";
        Connection conMySQLConnectionString = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            conMySQLConnectionString = DriverManager.getConnection(con, user, password);
            String strQuery = "SELECT * FROM appointments WHERE PatientID = ? AND DoctorID = ?";
            smt = conMySQLConnectionString.prepareStatement(strQuery);
            
            smt.setString(1, PatientID);
            smt.setString(2, DoctorID);
            
            rs = smt.executeQuery();
            rs.next();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error: "+ e.getMessage());
        } finally{
            try{
                if (smt != null) {
                    smt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conMySQLConnectionString != null) {
                    conMySQLConnectionString.close();
                }
            } catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
            }
        }
    }
    */
        
        private void mCreateAppointment(){
        String url = "jdbc:mysql://localhost:3306/hospital_management";
        String user = "root";
        String password = "Ramogale1000";
        
        String SQLInsert = "INSERT INTO Appointments (PatientID, DoctorID, AppointmentDate, Description) VALUE (?, ?, ?, ?)";
        try(Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = con.prepareStatement(SQLInsert)) {
            stmt.setString(1, PatientLastName);
            stmt.setString(2, DoctorLastName);
            stmt.setString(3, AppointmentDate);
            stmt.setString(4, Description);
            stmt.executeUpdate();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Patient ID or Doctor ID does not exist, enter exixting ID's.");
        }
    }
        
        private void mClearTextFields(){
        txtPatientLastName.setText("");
        txtDoctorLastName.setText("");
        txtAppointmentDate.setText("");
        txtDescription.setText("");
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
        appointmentsTable = new javax.swing.JTable();
        txtHeading = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        tvDate = new javax.swing.JLabel();
        txtAppointmentDate = new javax.swing.JTextField();
        txtDoctorLastName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPatientLastName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        appointmentsTable.setBackground(new java.awt.Color(255, 255, 204));
        appointmentsTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        appointmentsTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "AppointmentsID", "PatientID", "DoctorID", "Appointment Date", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(appointmentsTable);

        txtHeading.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        txtHeading.setForeground(new java.awt.Color(0, 0, 153));
        txtHeading.setText("Hospital Management System");

        btnRefresh.setBackground(new java.awt.Color(255, 204, 102));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnRefresh.setText("Refresh Table");
        btnRefresh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(102, 102, 255));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hospital_management_system/back.jpeg"))); // NOI18N
        btnBack.setText("Back");
        btnBack.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBack.setFocusable(false);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnCreate.setBackground(new java.awt.Color(153, 255, 204));
        btnCreate.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnCreate.setText("Create Appointment");
        btnCreate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCreate.setFocusable(false);
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnDelete.setText("Delete Appointment");
        btnDelete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Description: ");

        tvDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tvDate.setText("Appointment Date:(yyyy/mm/dd) ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Doctor ID:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Patient ID:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCreate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tvDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDoctorLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .addComponent(txtDescription)
                            .addComponent(txtAppointmentDate)
                            .addComponent(txtPatientLastName))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(357, 357, 357)
                .addComponent(txtHeading)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtHeading, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtPatientLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDoctorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tvDate)
                            .addComponent(txtAppointmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtDescription))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCreate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:\
        mLoadDetails();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        mDeleteSelectedAppointment();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        
        frmMain frmM = new frmMain();
        frmM.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        if (txtPatientLastName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Patient ID can not be empty");
            txtPatientLastName.requestFocusInWindow();
        } else if (txtDoctorLastName.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Doctor ID can not be empty");
            txtDoctorLastName.requestFocusInWindow();
        } else if(txtAppointmentDate.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Appointment date can not be empty");
            txtAppointmentDate.requestFocusInWindow();
        }else if(txtDescription.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Add Description.");
        } else{
            mGetValuesFromGUI();
            mSetValuesToUpperCase();
           // mCheckIfAppointmentExistInTable();
            
            if (boolRecordExist == true) {
                boolRecordExist = false;
                JOptionPane.showMessageDialog(null, "Appointment already exist.");
            }else if(boolRecordExist == false){
                mCreateAppointment();
                mClearTextFields();
            }
        }
    }//GEN-LAST:event_btnCreateActionPerformed

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
            java.util.logging.Logger.getLogger(frmViewAllAppointments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmViewAllAppointments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmViewAllAppointments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmViewAllAppointments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmViewAllAppointments().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable appointmentsTable;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel tvDate;
    private javax.swing.JTextField txtAppointmentDate;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtDoctorLastName;
    private javax.swing.JLabel txtHeading;
    private javax.swing.JTextField txtPatientLastName;
    // End of variables declaration//GEN-END:variables
}
