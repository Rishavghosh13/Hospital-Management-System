package Hospital_Management_System_Package;
import java.util.*;
import java.sql.*;

public class Patient_class {

    private final Connection con;
    private final Scanner sc;

    public Patient_class(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;
    }

    public void addPatient()
    {
        System.out.println("Enter your name : ");
        String name=sc.nextLine();

        System.out.println("Enter your age : ");
        int age= sc.nextInt();
        sc.nextLine();

        System.out.println("Enter your gender : ");
        String gender=sc.nextLine();


        try
        {
            String query="INSERT INTO patient(name,age,gender) VALUES(?,?,?);";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);
            int rows = ps.executeUpdate();
            if(rows>0)
                System.out.println("ENTRY SUCCESSFUL !! ");
            else
                System.out.println("ENTRY UNSUCCESSFUL ");

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void viewPatient()
    {
        try{
            String query="SELECT * FROM patient;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                int id=rs.getInt("id");
                String name=rs.getString("name");
                int age = rs.getInt("age");
                String gender =rs.getString("gender");
                System.out.println("Patient ID : "+id+"\nPatient Name : "+name+"\nPatient Age : "+age+"\nPatient Gender : "+gender);
                System.out.println("\n");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkPatient(int id)
    {
        try{
            String query="SELECT * FROM patient WHERE id=? ;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                return true;
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
