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
public class frmAddDoctors extends javax.swing.JFrame {

    /**
     * Creates new form frmAddDoctors
     */
    public frmAddDoctors() {
        initComponents();
    }
    Boolean boolRecordExist=false;
    
    String FirstNameD = null;
    String LastNameD = null;
    String Speciality = null;
    String PhoneD= null;
    String EmailD = null;
    
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

        String strQuery = "SELECT * FROM doctors";
        rs = smt.executeQuery(strQuery);

        // DefaultTable model to populate the table
        DefaultTableModel tableModel = (DefaultTableModel) patientsTable.getModel();
        tableModel.setRowCount(0); // Clear existing rows in the table

        // Retrieve column count
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Add rows to the table model
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
    
    private void mDeleteSelectedDoctor() {
    String con = "jdbc:mysql://localhost:3306/hospital_management";
    String user = "root";
    String password = "Ramogale1000";
    Connection conMySQLConnectionString = null;
    PreparedStatement pstmt = null;

    try {
        // Get selected row
        int selectedRow = patientsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            return;
        }

        // Get the ID of the selected patient (assuming it's in column 0)
        Object id = patientsTable.getValueAt(selectedRow, 0);

        // Connect to the database
        conMySQLConnectionString = DriverManager.getConnection(con, user, password);

        // Create the SQL DELETE query
        String deleteQuery = "DELETE FROM doctors WHERE DoctorID = ?";
        pstmt = conMySQLConnectionString.prepareStatement(deleteQuery);
        pstmt.setObject(1, id);

        // Execute the query
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(null, "Patient record deleted successfully.");
            
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
        FirstNameD = txtFirstNameD.getText();
        LastNameD = txtLastNameD.getText();
        Speciality = txtSpecialityD.getText();
        PhoneD = txtPhoneD.getText();
        EmailD = txtEmailD.getText();
    }
    
    private void mSetValuesToUpperCase(){
        FirstNameD = FirstNameD.toUpperCase();
        LastNameD = LastNameD.toUpperCase();
        Speciality = Speciality.toUpperCase();
        EmailD = EmailD.toUpperCase();
    }
    
    private void mCheckIfDoctorExistInTheTable(){
        String con = "jdbc:mysql://localhost:3306/hospital_management";
        String user = "root";
        String password = "Ramogale1000";
        Connection conMySQLConnectionString = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            conMySQLConnectionString = DriverManager.getConnection(con, user, password);
            String strQuery = "SELECT * FROM doctors WHERE FirstName = ? AND LastName = ?";
            smt = conMySQLConnectionString.prepareStatement(strQuery);
            
            smt.setString(1, FirstNameD);
            smt.setString(2, LastNameD);
            
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
    
    private void mCreateDoctor(){
        String url = "jdbc:mysql://localhost:3306/hospital_management";
        String user = "root";
        String password = "Ramogale1000";
        
        String SQLInsert = "INSERT INTO Doctors (FirstName, LastName, Speciality, Phone, Email) VALUE (?, ?, ?, ?, ?)";
        try(Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = con.prepareStatement(SQLInsert)) {
            stmt.setString(1, FirstNameD);
            stmt.setString(2, LastNameD);
            stmt.setString(3, Speciality);
            stmt.setString(4, PhoneD);
            stmt.setString(5, EmailD);
            stmt.executeUpdate();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Something is wrong: " + e.getMessage());
        }
    }
    
    private void mClearTextFields(){
         txtFirstNameD.setText("");
         txtLastNameD.setText("");
         txtSpecialityD.setText("");
         txtPhoneD.setText("");
         txtEmailD.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtHeading = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JLabel();
        txtEmail = new javax.swing.JLabel();
        btnCreateDoc = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        txtFirstNameD = new javax.swing.JTextField();
        txtLastNameD = new javax.swing.JTextField();
        txtSpecialityD = new javax.swing.JTextField();
        txtPhoneD = new javax.swing.JTextField();
        txtEmailD = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        patientsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtHeading.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        txtHeading.setForeground(new java.awt.Color(0, 0, 153));
        txtHeading.setText("Hospital Management System");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("First Name: ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Last Name:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Speciality:");

        txtPhone.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtPhone.setText("Phone:");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtEmail.setText("Email:");

        btnCreateDoc.setBackground(new java.awt.Color(102, 255, 0));
        btnCreateDoc.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnCreateDoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hospital_management_system/AddDoc.png"))); // NOI18N
        btnCreateDoc.setText("Create Doctor");
        btnCreateDoc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCreateDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateDocActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(153, 255, 204));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hospital_management_system/back.jpeg"))); // NOI18N
        btnBack.setText("Back");
        btnBack.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(255, 255, 102));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hospital_management_system/refresh.jpeg"))); // NOI18N
        btnRefresh.setText("Refresh Table");
        btnRefresh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRefresh.setFocusable(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 0, 51));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hospital_management_system/delete.jpeg"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        patientsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "DoctorID", "FirstName", "LastName", "Speciality", "Phone", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(patientsTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLastNameD, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFirstNameD, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSpecialityD, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPhoneD, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmailD, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnCreateDoc, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(txtHeading)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtHeading, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFirstNameD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtSpecialityD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPhone)
                            .addComponent(txtPhoneD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmailD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(btnCreateDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtLastNameD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        frmDoctors frmD = new frmDoctors();
        frmD.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnCreateDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateDocActionPerformed
        // TODO add your handling code here:
                if (txtFirstNameD.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "First name field cannot be empty");
            txtFirstNameD.requestFocusInWindow();
        } else if(txtLastNameD.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Last name field cannot be empty");
            txtLastNameD.requestFocusInWindow();
        } else if(txtSpecialityD.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Phone number field cannot be empty");
            txtSpecialityD.requestFocusInWindow();
        } else if(txtPhoneD.getText().equals("")){
            JOptionPane.showMessageDialog(null, "");
            txtPhoneD.requestFocusInWindow();
        }else if(txtEmailD.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Email field cannot be empty");
            txtEmailD.requestFocusInWindow();
        } else{
            mGetValuesFromGUI();
            mSetValuesToUpperCase();
            mCheckIfDoctorExistInTheTable();
            
            if (boolRecordExist == true) {
                boolRecordExist = false;
                JOptionPane.showMessageDialog(null, "Patient already exist");
            }else if(boolRecordExist == false){
                mCreateDoctor();
                mClearTextFields();
            }
        }
    }//GEN-LAST:event_btnCreateDocActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        mLoadDetails();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        mDeleteSelectedDoctor();
    }//GEN-LAST:event_btnDeleteActionPerformed

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
            java.util.logging.Logger.getLogger(frmAddDoctors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAddDoctors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAddDoctors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAddDoctors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAddDoctors().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCreateDoc;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable patientsTable;
    private javax.swing.JLabel txtEmail;
    private javax.swing.JTextField txtEmailD;
    private javax.swing.JTextField txtFirstNameD;
    private javax.swing.JLabel txtHeading;
    private javax.swing.JTextField txtLastNameD;
    private javax.swing.JLabel txtPhone;
    private javax.swing.JTextField txtPhoneD;
    private javax.swing.JTextField txtSpecialityD;
    // End of variables declaration//GEN-END:variables
}
