import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ChangePassword extends JDialog{
    private JPanel changePasswordPanel;
    private JTextField tfNewPassword;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField tfEmail;

    public ChangePassword(JFrame parent)
    {
        super(parent);
        setTitle("Change Password");
        setContentPane(changePasswordPanel);
        setMinimumSize(new Dimension(500,220));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userEmail=tfEmail.getText();
                String newPassword=tfNewPassword.getText();

                final String DB_URL= "jdbc:mysql://127.0.0.1:3306/Users?user=root";
                final String USERNAME= "root";
                final String PASSWORD= "aayushraj23";

                try
                {
                    Connection conn= DriverManager.getConnection(DB_URL , USERNAME , PASSWORD);
                    //connected to database successful

                    Statement stmt=conn.createStatement();

                    String sql="UPDATE Users SET password=? WHERE email=?";
                    PreparedStatement preparedStatement=conn.prepareStatement(sql);
                    preparedStatement.setString(1,newPassword);
                    preparedStatement.setString(2,userEmail);

                    preparedStatement.executeUpdate();

                    stmt.close();
                    conn.close();

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(ChangePassword.this,
                        "Your password has been changed successfully" ,
                        "Update Password",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();

            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        ChangePassword changePassword=new ChangePassword(null);



    }
}
