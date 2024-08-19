package Hospital_Management_System_Package;

import java.util.*;
import java.sql.*;

public class Doctor_class {

    private final Connection con;
    private final Scanner sc;

    public Doctor_class(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;
    }

    public void viewDoctor()
    {
        try{
            String query="SELECT * FROM doctor;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                int id=rs.getInt("id");
                String name=rs.getString("name");
                String specilization =rs.getString("specilization");
                System.out.println("Doctor ID : "+id+"\nDoctor Name : "+name+"\nSpecilization : "+specilization);
                System.out.println("\n");
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkDoctor(int id)
    {
        try{
            String query="SELECT * FROM doctor WHERE id=? ;";
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
