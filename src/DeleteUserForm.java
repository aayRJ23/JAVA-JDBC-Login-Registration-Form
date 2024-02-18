import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DeleteUserForm extends JDialog{
    private JPanel deleteUserPanel;
    private JTextField tfPassword;
    private JButton cancelButton;
    private JButton confirmButton;

    public DeleteUserForm(JFrame parent)
    {
        super(parent);
        setTitle("Delete Account");
        setContentPane(deleteUserPanel);
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

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String pswd=tfPassword.getText();

                final String DB_URL= "jdbc:mysql://127.0.0.1:3306/Users?user=root";
                final String USERNAME= "root";
                final String PASSWORD= "aayushraj23";

                try
                {
                    Connection conn= DriverManager.getConnection(DB_URL , USERNAME , PASSWORD);
                    //connected to database successful

                    Statement stmt=conn.createStatement();

                    String sql="DELETE FROM Users WHERE password=?";
                    PreparedStatement preparedStatement=conn.prepareStatement(sql);
                    preparedStatement.setString(1,pswd);

                    preparedStatement.executeUpdate();

                    stmt.close();
                    conn.close();

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(DeleteUserForm.this,
                        "Your account has been deleted successfully" ,
                        "Delete Account",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();

            }
        });

        setVisible(true);
    }


    public static void main(String[] args) {
        DeleteUserForm deleteUserForm=new DeleteUserForm(null);
    }
}
