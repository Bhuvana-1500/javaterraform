import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.annotation.WebServlet;
@WebServlet("/MyClass")
public class MyClass extends GenericServlet{
	private String query;
	private Connection connect;
	private ResultSet rs;
	private PreparedStatement ps;
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
	PrintWriter out=res.getWriter();
	res.setContentType("Text/html");
	out.println("done"+"<br>"); 
	String loc=req.getParameter("loc");
	String rname=req.getParameter("rname");
	out.println(rname+"<br>");
	out.println(loc+"<br>");
	createTerraformVarsFile(rname, loc);

        // Run Terraform commands
        executeTerraformCommand("terraform init");
        executeTerraformCommand("terraform apply -auto-approve");
		out.println("Deployment completed successfully"+"<br>");
	}
	private static void createTerraformVarsFile(String resourceGroupName, String location) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("terraform/terraform.tfvars"))) {
            writer.write("resource_group_name = \"" + resourceGroupName + "\"\n");
            writer.write("location = \"" + location + "\"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeTerraformCommand(String command) {
        try {
            Process process = new ProcessBuilder(command.split(" "))
                .directory(new java.io.File("terraform"))
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
	
}
