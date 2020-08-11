/*
 * Created by JFormDesigner on Tue Aug 11 12:31:58 PDT 2020
 */

package com.company;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.*;

/**
 * @author Aryan Puri
 * https://github.com/Puriaryan/finalExam.git
 */
public class Designer extends JFrame {
    public Designer() {
        initComponents();
    }
    // connection variables
    Connection con;
    PreparedStatement insert;
    String selID;

    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        Designer designer = new Designer();
        designer.setVisible(true);
        designer.loadDb();
    }


// method to hold data
    public LoanP tempDataHold(){
        if(!validateInput(loanAmt.getText()) || !validateInput(nYears.getText()) ||
                validateInput(clientName.getText())){
            JOptionPane.showMessageDialog(null, "Enter the correct format");
            return null;
        }
        else{
            String clientno = clientNo.getText();
            String clientname = clientName.getText();
            double loanamount = Double.parseDouble(loanAmt.getText());
            int nyears = Integer.parseInt(nYears.getText());
            String loanType= loanTypeComboBox.getSelectedItem().toString();
           // int noOfYears = Integer.parseInt(nYears.getText());
            LoanP loan = new LoanP(clientno, clientname, loanamount, nyears, loanType);
            return loan;
        }
    }

    //validating input method
    public static boolean validateInput(String userInput) {
        boolean numeric = true;
        try {
            double num = Double.parseDouble(userInput);
        }
        catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }

    // loading databse
    public void loadDb() throws ClassNotFoundException, SQLException{
        int colCount;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan","root","");
        insert = con.prepareStatement("select * from loantable");
        ResultSet rs = insert.executeQuery();
        ResultSetMetaData res = rs.getMetaData();
        colCount = res.getColumnCount();
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet numRow = stmt.executeQuery("select * from loantable");
        int rows = 0;
        if (numRow.last()) {
            rows = numRow.getRow();
            // Move to beginning
            numRow.beforeFirst();
        }

        String[][] row = new String[rows][colCount];
        String[] col = {"Number", "Name", "Amount", "Years", "Type of loan"};
        DefaultTableModel df = new DefaultTableModel(col, 0);
        UserListTable.setModel(df);

        while(rs.next()) {
            int count = 0;
            LoanP loan = new LoanP();
            for (int i = 0; i <= colCount; i++) {
                loan.setClientno(rs.getString("clientno"));
                loan.setClientName(rs.getString("clientname"));
                loan.setLoanAmt(rs.getDouble("loanamount"));
                loan.setNoOfYears(rs.getInt("years"));
                loan.setLoanType(rs.getString("loantype"));
            }

            row[count][0] = loan.getClientno();
            row[count][1] = loan.getClientName();
            row[count][2] = loan.getLoanAmt()+"";
            row[count][3] = loan.getNoOfYears()+"";
            row[count][4] = loan.getLoanType();

            df.addRow(new String[]{row[count][0],row[count][1],row[count][2],row[count][3],row[count][4]});
            count++;

        }
    }

    private void scrollPane1MouseClicked(MouseEvent e) {

    }


    // displaying data in table 1
    public void UserListTableMouseClicked(MouseEvent e) {
        DefaultTableModel def = (DefaultTableModel) UserListTable.getModel();

        int index = UserListTable.getSelectedRow();

        clientNo.setText(def.getValueAt(index,0).toString());
        selID=clientNo.getText();
        clientName.setText(def.getValueAt(index,1).toString());
        loanAmt.setText(def.getValueAt(index,2).toString());
        nYears.setText(def.getValueAt(index,3).toString());
        if(def.getValueAt(index,4).toString().equals("Personal")) {
            loanTypeComboBox.setSelectedIndex(1);
        }
        else{
            loanTypeComboBox.setSelectedIndex(0);
        }

        String[] col = {"Payment","Principal","Interest","Monthly payment", "Balance"};
        String[][]row = new String[Integer.parseInt(nYears.getText())][4];
        DefaultTableModel df = new DefaultTableModel(col,0);
        DetailsTable.setModel(df);

        if(loanTypeComboBox.getSelectedIndex() == 1) {
            Personal regObject = new Personal(def.getValueAt(index,0).toString(),
                    def.getValueAt(index,1).toString(),
                    Double.parseDouble((String) def.getValueAt(index,2))
                    , Integer.parseInt((String) def.getValueAt(index,3)),
                    def.getValueAt(index,4).toString());
            double[][] cArr = regObject.generateTable();

            for (int i = 0; i < cArr.length; i++) {
                df.addRow(new String[]{cArr[i][0]+"",cArr[i][1]+"",cArr[i][2]+"",cArr[i][3]+""});
            }
        }
        else {
            Business delObj = new Business(def.getValueAt(index,0).toString(),
                    def.getValueAt(index,1).toString(),
                    Double.parseDouble((String) def.getValueAt(index,2))
                    , Integer.parseInt((String) def.getValueAt(index,3)),
                    def.getValueAt(index,4).toString());

            double[][] cArr = delObj.generateTable();

            for (int i = 0; i < cArr.length; i++) {
                df.addRow(new String[]{cArr[i][0]+"",cArr[i][1]+"",cArr[i][2]+"",cArr[i][3]+""});
            }
        }
    }

    private void scrollPane2MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    // add button action method
    public void addActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        LoanP loan = tempDataHold();
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan","root","");

        insert = con.prepareStatement("Select * from loantable where clientno = ?");
        insert.setString(1,loan.getClientName());
        ResultSet rs = insert.executeQuery();
        if(rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "Data already exists");
        }

        insert = con.prepareStatement("insert into loantable values (?,?,?,?,?)");
        insert.setString(1,loan.getClientno());
        insert.setString(2,loan.getClientName());
        insert.setDouble(3,loan.getLoanAmt());
        insert.setInt(4,loan.getNoOfYears());
        insert.setString(5, loan.getLoanType());

        insert.executeUpdate();
        JOptionPane.showMessageDialog(null,"Records Added");

        clientNo.setText("");
        clientName.setText("");
        loanAmt.setText("");
        nYears.setText("");
        loantype.requestFocus();
        loadDb();
    }


    // edit button action method
    private void editActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost/loan", "root", "");
        insert = con.prepareStatement("update loantable set clientno=?,clientname=?,loanamount=?,years=?,loantype=? " +
                "where clientno =?");
        insert.setString(1, clientNo.getText());
        insert.setString(2, clientName.getText());
        insert.setString(3, loanAmt.getText());
        insert.setString(4, nYears.getText());
        insert.setString(5, loanTypeComboBox.getSelectedItem().toString());
        insert.setString(6, selID);
        insert.executeUpdate();
        JOptionPane.showMessageDialog(null,"Record Edited");
        clientNo.setText("");
        clientName.setText("");
        loanAmt.setText("");
        nYears.setText("");
        loantype.requestFocus();
        loadDb();
    }

    // delete button action method
    private void DeleteActionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
        LoanP loan = tempDataHold();
        Class.forName("com.mysql.jdbc.Driver");
        con= DriverManager.getConnection("jdbc:mysql://localhost/loan","root","");
        int result1 = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete?", "Delete",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(result1 == JOptionPane.YES_OPTION){
            insert=con.prepareStatement("delete from loantable where clientno=?");
            insert.setString(1,selID);
            System.out.println(selID);
            insert.execute();
            JOptionPane.showMessageDialog(null, "Record deleted");
        }
        loadDb();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Aryan Puri
        clientnolabel = new JLabel();
        clientNo = new JTextField();
        cname = new JLabel();
        clientName = new JTextField();
        loanamt = new JLabel();
        loanAmt = new JTextField();
        nyears = new JLabel();
        nYears = new JTextField();
        loantype = new JLabel();
        loanTypeComboBox = new JComboBox<>();
        UserListPanel = new JScrollPane();
        UserListTable = new JTable();
        AcctDetails = new JScrollPane();
        DetailsTable = new JTable();
        add = new JButton();
        edit = new JButton();
        Delete = new JButton();

        //======== this ========
        setTitle("Loan Projection");
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- clientnolabel ----
        clientnolabel.setText("Enter the client number");
        contentPane.add(clientnolabel, "cell 0 1");
        contentPane.add(clientNo, "cell 6 1 8 1");

        //---- cname ----
        cname.setText("Enter the client name");
        contentPane.add(cname, "cell 0 2");
        contentPane.add(clientName, "cell 6 2 8 1");

        //---- loanamt ----
        loanamt.setText("Enter the customer loan amount");
        contentPane.add(loanamt, "cell 0 3");
        contentPane.add(loanAmt, "cell 6 3 8 1");

        //---- nyears ----
        nyears.setText("Enter the number of years to pay");
        contentPane.add(nyears, "cell 0 4");
        contentPane.add(nYears, "cell 6 4 8 1");

        //---- loantype ----
        loantype.setText("Enter the loan type");
        contentPane.add(loantype, "cell 0 5");

        //---- loanTypeComboBox ----
        loanTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "Business",
            "Personal"
        }));
        contentPane.add(loanTypeComboBox, "cell 6 5 8 1");

        //======== UserListPanel ========
        {
            UserListPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    scrollPane1MouseClicked(e);
                }
            });

            //---- UserListTable ----
            UserListTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    UserListTableMouseClicked(e);
                }
            });
            UserListPanel.setViewportView(UserListTable);
        }
        contentPane.add(UserListPanel, "cell 0 6 6 1");

        //======== AcctDetails ========
        {
            AcctDetails.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    scrollPane2MouseClicked(e);
                }
            });
            AcctDetails.setViewportView(DetailsTable);
        }
        contentPane.add(AcctDetails, "cell 6 6 8 1");

        //---- add ----
        add.setText("Add");
        add.addActionListener(e -> addActionPerformed(e));
        contentPane.add(add, "cell 0 8");

        //---- edit ----
        edit.setText("Edit");
        edit.addActionListener(e -> editActionPerformed(e));
        contentPane.add(edit, "cell 1 8 4 1");

        //---- Delete ----
        Delete.setText("Delete");
        Delete.addActionListener(e -> DeleteActionPerformed(e));
        contentPane.add(Delete, "cell 5 8");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Aryan Puri
    private JLabel clientnolabel;
    private JTextField clientNo;
    private JLabel cname;
    private JTextField clientName;
    private JLabel loanamt;
    private JTextField loanAmt;
    private JLabel nyears;
    private JTextField nYears;
    private JLabel loantype;
    private JComboBox<String> loanTypeComboBox;
    private JScrollPane UserListPanel;
    private JTable UserListTable;
    private JScrollPane AcctDetails;
    private JTable DetailsTable;
    private JButton add;
    private JButton edit;
    private JButton Delete;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
