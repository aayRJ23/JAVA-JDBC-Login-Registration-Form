import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;
    private JLabel lbPhoneAdd;
    private JLabel lbHello;
    private JButton btnExit;
    private JButton logOutButton;
    private JLabel lbDateTime;
    private JButton btnChangePass;
    private JButton btnDeleteUser;

    public DashboardForm() {
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1400, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();

        if (hasRegisteredUsers) {   //show login form
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            //slicing of name for hello
            String[] namearray=user.name.split(" ");
            String firstname=namearray[0];


            if (user != null) {
                lbAdmin.setText("Logged in as User : " + user.name);
                lbPhoneAdd.setText("Phone : " + user.phone + "  ,  " + " Address : " + user.address);
                lbHello.setText("\n\n       Hello "+firstname + "...!");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                lbDateTime.setText("        "+dtf.format(now));

                setLocationRelativeTo(null);
                setVisible(true);

                System.out.println("Successful Authentication of : "+ user.name);
                System.out.println("            Email : "+user.email);
                System.out.println("            Phone : "+user.phone);
                System.out.println("            Address : "+user.address);
                System.out.println("\n\n");
            } else {
                dispose();
            }
        } else {
            RegistrationFrom registrationFrom = new RegistrationFrom(this);
            User user = registrationFrom.user;

            if (user != null) {
                lbAdmin.setText("User : " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);

                System.out.println("Successful registration of : "+ user.name);
            } else {
                dispose();
            }
        }

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationFrom registrationFrom = new RegistrationFrom(DashboardForm.this);
                User user = registrationFrom.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New User : " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);

                    System.out.println("\nSuccessful registration of : "+ user.name);
                    System.out.println("\n\n");
                }
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // on construction
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm=new LoginForm(DashboardForm.this);
            }
        });
        btnChangePass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePassword changePassword=new ChangePassword(DashboardForm.this);
            }
        });


        btnDeleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteUserForm deleteUserForm=new DeleteUserForm(DashboardForm.this);

                dispose();
            }
        });
    }


    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

//        final String MYSQL_SERVER_URL= "jdbc:mysql://localhost/";
        final String DB_URL= "jdbc:mysql://127.0.0.1:3306/Users?user=root";
        final String USERNAME= "root";
        final String PASSWORD= "aayushraj23";


        try{
            //First , connect to MYSQL server and create the database if not created
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement statement =conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS MyStore");
            statement.close();
            conn.close();

            //Second , connect to the database and create the table "users" if not created
            conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            statement =conn.createStatement();

            String sql= "CREATE TABLE IF NOT EXISTS users("
                    + "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT ,"
                    + "name VARCHAR(200) NOT NULL ,"
                    + "email VARCHAR(200) NOT NULL UNIQUE ,"
                    + "phone VARCHAR(200) ,"
                    + "address VARCHAR(200) ,"
                    + "password VARCHAR(200) NOT NULL"
                    + ")";

            statement.executeUpdate(sql);

            //Check if we have users in the table users
            statement=conn.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT COUNT(*) FROM users");

            if(resultSet.next())
            {
                int numUsers = resultSet.getInt(1);
                if(numUsers>0)
                {
                    hasRegisteredUsers =true;
                }
            }

            statement.close();
            conn.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return hasRegisteredUsers ;
    }


    public static void main(String[] args) {
        DashboardForm dashboardForm = new DashboardForm();
    }
}