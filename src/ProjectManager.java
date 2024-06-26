import java.sql.*;
import java.util.Date;

/**
 * The ProjectManager class contains methods to manage projects in the database.
 */
public class ProjectManager {

    /**
     * Reads all projects (i.e. all open and closed) from the database.
     */
    public void readProjects() {
        String query = "SELECT Projects.project_id, Projects.project_name, Customers.customer_name, Customers.customer_id, "
                +
                "Architects.architect_name, Architects.architect_id, " +
                "ProjectManagers.manager_name AS project_manager_name, ProjectManagers.manager_id, " +
                "StructuralEngineers.engineer_name AS structural_engineer_name, StructuralEngineers.engineer_id, " +
                "Projects.status, Projects.completion_date, Projects.due_date " +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN ProjectManagers ON Projects.project_manager_id = ProjectManagers.manager_id " +
                "JOIN StructuralEngineers ON Projects.structural_engineer_id = StructuralEngineers.engineer_id " +
                "ORDER BY Projects.project_id";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("Project ID: " + rs.getInt("project_id"));
                System.out.println("Project: " + rs.getString("project_name"));
                System.out.println("Customer: " + rs.getString("customer_name") + " - ID: " + rs.getInt("customer_id"));
                System.out.println(
                        "Architect: " + rs.getString("architect_name") + " - ID: " + rs.getInt("architect_id"));
                System.out.println("Project Manager: " + rs.getString("project_manager_name") + " - ID: "
                        + rs.getInt("manager_id"));
                System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name") + " - ID: "
                        + rs.getInt("engineer_id"));
                System.out.println("Project Status: " + rs.getString("status"));
                System.out.println("Completion Date: " + rs.getDate("completion_date"));
                System.out.println("Due Date: " + rs.getDate("due_date"));
                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new project to the database.
     *
     * @param projectName          the name of the project
     * @param structuralEngineerId the ID of the structural engineer
     * @param projectManagerId     the ID of the project manager
     * @param architectId          the ID of the architect
     * @param customerId           the ID of the customer
     * @param dueDate              the due date of the project
     */
    public void addProject(String projectName, int structuralEngineerId, int projectManagerId, int architectId,
            int customerId, Date dueDate) {
        String query = "INSERT INTO Projects (project_name, structural_engineer_id, project_manager_id, architect_id, customer_id, due_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, projectName);
            pstmt.setInt(2, structuralEngineerId);
            pstmt.setInt(3, projectManagerId);
            pstmt.setInt(4, architectId);
            pstmt.setInt(5, customerId);
            pstmt.setDate(6, new java.sql.Date(dueDate.getTime()));
            pstmt.executeUpdate();

            System.out.println("Project added successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates details of an existing project in the database.
     *
     * @param projectId            the ID of the project to update
     * @param dueDate              the new due date of the project
     * @param architectId          the new ID of the architect
     * @param structuralEngineerId the new ID of the structural engineer
     * @param projectManagerId     the new ID of the project manager
     */
    public void updateProjectDetails(int projectId, Date dueDate, int architectId, int structuralEngineerId,
            int projectManagerId) {
        String query = "UPDATE Projects SET due_date = ?, architect_id = ?, structural_engineer_id = ?, project_manager_id = ? WHERE project_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(dueDate.getTime()));
            pstmt.setInt(2, architectId);
            pstmt.setInt(3, structuralEngineerId);
            pstmt.setInt(4, projectManagerId);
            pstmt.setInt(5, projectId);
            pstmt.executeUpdate();

            System.out.println("Project details updated successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a project from the database.
     *
     * @param projectId the ID of the project to delete
     */
    public void deleteProject(int projectId) {
        String query = "DELETE FROM Projects WHERE project_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();

            System.out.println("Project deleted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finalizes a project by marking it as finalized and setting the completion
     * date.
     *
     * @param projectId      the ID of the project to finalize
     * @param completionDate the completion date of the project
     */
    public void finalizeProject(int projectId, Date completionDate) {
        String query = "UPDATE Projects SET status = 'finalized', completion_date = ? WHERE project_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(completionDate.getTime()));
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();

            System.out.println("Project finalized successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all overdue projects.
     *
     * @param currentDate the current date
     */
    public void findOverdueProjects(Date currentDate) {
        String query = "SELECT * FROM Projects WHERE due_date < ? AND status != 'finalized'";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(currentDate.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Overdue Project ID: " + rs.getInt("project_id"));
                    System.out.println("Project Name: " + rs.getString("project_name"));
                    System.out.println("Customer: " + rs.getString("customer_name"));
                    System.out.println("Architect: " + rs.getString("architect_name"));
                    System.out.println("Project Manager: " + rs.getString("project_manager_name"));
                    System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name"));
                    System.out.println("----------------------------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a project by its number or name.
     *
     * @param projectIdentifier the project number or name
     */
    public void findProject(String projectIdentifier) {
        String query = "SELECT Projects.project_id, Projects.project_name, Customers.customer_name, " +
                "Architects.architect_name, ProjectManagers.manager_name AS project_manager_name, " +
                "StructuralEngineers.engineer_name AS structural_engineer_name, Projects.status, " +
                "Projects.due_date " +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN ProjectManagers ON Projects.project_manager_id = ProjectManagers.manager_id " +
                "JOIN StructuralEngineers ON Projects.structural_engineer_id = StructuralEngineers.engineer_id " +
                "WHERE Projects.project_id = ? OR Projects.project_name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            try {
                int projectId = Integer.parseInt(projectIdentifier);
                pstmt.setInt(1, projectId);
                pstmt.setString(2, "");
            } catch (NumberFormatException e) {
                pstmt.setInt(1, -1); // Invalid project ID to ensure it's not matched
                pstmt.setString(2, projectIdentifier);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Project ID: " + rs.getInt("project_id"));
                    System.out.println("Project Name: " + rs.getString("project_name"));
                    System.out.println("Customer: " + rs.getString("customer_name"));
                    System.out.println("Architect: " + rs.getString("architect_name"));
                    System.out.println("Project Manager: " + rs.getString("project_manager_name"));
                    System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name"));
                    System.out.println("Project Status: " + rs.getString("status"));
                    System.out.println("Due Date: " + rs.getDate("due_date"));
                    System.out.println("----------------------------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all open projects.
     */
    public void findOpenProjects() {
        String query = "SELECT Projects.project_id, Projects.project_name, Customers.customer_name, " +
                       "Projects.status, Projects.due_date " +
                       "FROM Projects " +
                       "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                       "WHERE Projects.status != 'finalized'";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("Open Project ID: " + rs.getInt("project_id"));
                System.out.println("Project: " + rs.getString("project_name"));
                System.out.println("Customer: " + rs.getString("customer_name"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Due Date: " + rs.getDate("due_date"));
                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all finalized projects.
     */
    public void findClosedProjects() {
        String query = "SELECT * FROM Projects WHERE status = 'finalized'";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("Closed Project ID: " + rs.getInt("project_id"));
                System.out.println("Project: " + rs.getString("project_name"));
                System.out.println("Customer: " + rs.getString("customer_name"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Date finalized: " + rs.getDate("completion_date"));
                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
