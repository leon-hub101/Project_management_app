package com.example.project;

import java.sql.*;
import java.util.Date;
import java.math.BigDecimal;

/**
 * The ProjectManager class contains methods to manage projects in the database.
 */
public class ProjectManager {

    /**
     * Reads all projects (i.e. all open and closed) from the database.
     */
    public void readProjects() {
        String query = "SELECT Projects.project_id, Projects.project_name, Projects.building_type, Projects.address, Projects.erf_number, "
                +
                "Projects.total_fee, Projects.amount_paid, Projects.due_date, Projects.status, Projects.completion_date, "
                +
                "Customers.customer_name, Customers.customer_surname, Customers.business_name, Customers.is_business, Customers.contact_info AS customer_contact, "
                +
                "Architects.architect_name, Architects.contact_info AS architect_contact, " +
                "Contractors.contractor_name, Contractors.contact_info AS contractor_contact, " +
                "ProjectManagers.manager_name AS project_manager_name, ProjectManagers.contact_info AS manager_contact, "
                +
                "StructuralEngineers.engineer_name AS structural_engineer_name, StructuralEngineers.contact_info AS engineer_contact "
                +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN Contractors ON Projects.contractor_id = Contractors.contractor_id " +
                "JOIN ProjectManagers ON Projects.project_manager_id = ProjectManagers.manager_id " +
                "JOIN StructuralEngineers ON Projects.structural_engineer_id = StructuralEngineers.engineer_id " +
                "ORDER BY Projects.project_id";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("\nProject ID: " + rs.getInt("project_id"));
                System.out.println("Project: " + rs.getString("project_name"));
                System.out.println("Building Type: " + rs.getString("building_type"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("ERF Number: " + rs.getString("erf_number"));
                System.out.println("Total Fee: " + rs.getBigDecimal("total_fee"));
                System.out.println("Amount Paid: " + rs.getBigDecimal("amount_paid"));
                System.out.println("Due Date: " + rs.getDate("due_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Completion Date: " + rs.getDate("completion_date"));
                System.out.println("Customer: " + rs.getString("customer_name") + " " + rs.getString("customer_surname")
                        + " - Contact: "
                        + rs.getString("customer_contact"));
                if (rs.getBoolean("is_business")) {
                    System.out.println("Business Name: " + rs.getString("business_name"));
                }
                System.out.println("Architect: " + rs.getString("architect_name") + " - Contact: "
                        + rs.getString("architect_contact"));
                System.out.println("Contractor: " + rs.getString("contractor_name") + " - Contact: "
                        + rs.getString("contractor_contact"));
                System.out.println("Project Manager: " + rs.getString("project_manager_name") + " - Contact: "
                        + rs.getString("manager_contact"));
                System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name") + " - Contact: "
                        + rs.getString("engineer_contact"));
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
     * @param buildingType         the type of building
     * @param address              the address of the project
     * @param erfNumber            the ERF number
     * @param totalFee             the total fee for the project
     * @param amountPaid           the total amount paid to date
     * @param dueDate              the due date of the project
     * @param architectId          the ID of the architect
     * @param contractorId         the ID of the contractor
     * @param customerId           the ID of the customer
     * @param structuralEngineerId the ID of the structural engineer
     * @param projectManagerId     the ID of the project manager
     * @param customerName         the first name of the customer
     * @param customerSurname      the surname of the customer
     * @param businessName         the name of the business if the customer is a
     *                             business
     * @param isBusiness           whether the customer is a business or not
     */
    public void addProject(String projectName, String buildingType, String address, String erfNumber,
            BigDecimal totalFee, BigDecimal amountPaid, Date dueDate,
            int architectId, int contractorId, int customerId, int structuralEngineerId, int projectManagerId,
            String customerName, String customerSurname, String businessName, boolean isBusiness) {

        // If project name is not provided, construct it
        if (projectName == null || projectName.trim().isEmpty()) {
            if (isBusiness) {
                projectName = buildingType + " - " + businessName;
            } else {
                projectName = buildingType + " - " + customerSurname;
            }
        }

        String customerQuery = "INSERT INTO Customers (customer_name, customer_surname, business_name, is_business) VALUES (?, ?, ?, ?)";
        String projectQuery = "INSERT INTO Projects (project_name, building_type, address, erf_number, total_fee, amount_paid, due_date, architect_id, contractor_id, customer_id, structural_engineer_id, project_manager_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            // Insert customer details
            try (PreparedStatement customerPstmt = conn.prepareStatement(customerQuery,
                    Statement.RETURN_GENERATED_KEYS)) {
                customerPstmt.setString(1, customerName);
                customerPstmt.setString(2, customerSurname);
                customerPstmt.setString(3, businessName);
                customerPstmt.setBoolean(4, isBusiness);
                customerPstmt.executeUpdate();

                // Get the generated customer ID
                try (ResultSet generatedKeys = customerPstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customerId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating customer failed, no ID obtained.");
                    }
                }
            }

            // Insert project details
            try (PreparedStatement projectPstmt = conn.prepareStatement(projectQuery)) {
                projectPstmt.setString(1, projectName);
                projectPstmt.setString(2, buildingType);
                projectPstmt.setString(3, address);
                projectPstmt.setString(4, erfNumber);
                projectPstmt.setBigDecimal(5, totalFee);
                projectPstmt.setBigDecimal(6, amountPaid);
                projectPstmt.setDate(7, new java.sql.Date(dueDate.getTime()));
                projectPstmt.setInt(8, architectId);
                projectPstmt.setInt(9, contractorId);
                projectPstmt.setInt(10, customerId); // Use the generated customer ID
                projectPstmt.setInt(11, structuralEngineerId);
                projectPstmt.setInt(12, projectManagerId);
                projectPstmt.executeUpdate();
            }

            // Commit transaction
            conn.commit();
            System.out.println("Project and customer added successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DatabaseManager.getConnection()) {
                // Rollback transaction on error
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    /**
     * Updates details of an existing project in the database.
     *
     * @param projectId            the ID of the project to update
     * @param totalFee             the new total fee for the project
     * @param amountPaid           the new amount paid to date
     * @param dueDate              the new due date of the project
     * @param architectId          the new ID of the architect
     * @param contractorId         the new ID of the contractor
     * @param structuralEngineerId the new ID of the structural engineer
     * @param projectManagerId     the new ID of the project manager
     */
    public void updateProjectDetails(int projectId, BigDecimal totalFee, BigDecimal amountPaid, Date dueDate,
            int architectId, int contractorId, int structuralEngineerId, int projectManagerId) {
        String query = "UPDATE Projects SET total_fee = ?, amount_paid = ?, due_date = ?, architect_id = ?, contractor_id = ?, structural_engineer_id = ?, project_manager_id = ? WHERE project_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBigDecimal(1, totalFee);
            pstmt.setBigDecimal(2, amountPaid);
            pstmt.setDate(3, new java.sql.Date(dueDate.getTime()));
            pstmt.setInt(4, architectId);
            pstmt.setInt(5, contractorId);
            pstmt.setInt(6, structuralEngineerId);
            pstmt.setInt(7, projectManagerId);
            pstmt.setInt(8, projectId);
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
        String query = "SELECT Projects.project_id, Projects.project_name, Projects.building_type, Projects.address, Projects.erf_number, "
                +
                "Projects.total_fee, Projects.amount_paid, Projects.due_date, Projects.status, Projects.completion_date, "
                +
                "Customers.customer_name, Customers.customer_surname, Customers.business_name, Customers.is_business, Customers.contact_info AS customer_contact, "
                +
                "Architects.architect_name, Architects.contact_info AS architect_contact, " +
                "Contractors.contractor_name, Contractors.contact_info AS contractor_contact, " +
                "ProjectManagers.manager_name AS project_manager_name, ProjectManagers.contact_info AS manager_contact, "
                +
                "StructuralEngineers.engineer_name AS structural_engineer_name, StructuralEngineers.contact_info AS engineer_contact "
                +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN Contractors ON Projects.contractor_id = Contractors.contractor_id " +
                "JOIN ProjectManagers ON Projects.project_manager_id = ProjectManagers.manager_id " +
                "JOIN StructuralEngineers ON Projects.structural_engineer_id = StructuralEngineers.engineer_id " +
                "WHERE Projects.due_date < ? AND Projects.status != 'finalized'";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, new java.sql.Date(currentDate.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("\nOverdue Project ID: " + rs.getInt("project_id"));
                    System.out.println("Project: " + rs.getString("project_name"));
                    System.out.println("Building Type: " + rs.getString("building_type"));
                    System.out.println("Address: " + rs.getString("address"));
                    System.out.println("ERF Number: " + rs.getString("erf_number"));
                    System.out.println("Total Fee: " + rs.getBigDecimal("total_fee"));
                    System.out.println("Amount Paid: " + rs.getBigDecimal("amount_paid"));
                    System.out.println("Due Date: " + rs.getDate("due_date"));
                    System.out.println("Status: " + rs.getString("status"));
                    System.out.println("Completion Date: " + rs.getDate("completion_date"));
                    System.out.println("Customer: " + rs.getString("customer_name") + " "
                            + rs.getString("customer_surname") + " - Contact: "
                            + rs.getString("customer_contact"));
                    if (rs.getBoolean("is_business")) {
                        System.out.println("Business Name: " + rs.getString("business_name"));
                    }
                    System.out.println("Architect: " + rs.getString("architect_name") + " - Contact: "
                            + rs.getString("architect_contact"));
                    System.out.println("Contractor: " + rs.getString("contractor_name") + " - Contact: "
                            + rs.getString("contractor_contact"));
                    System.out.println("Project Manager: " + rs.getString("project_manager_name") + " - Contact: "
                            + rs.getString("manager_contact"));
                    System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name")
                            + " - Contact: " + rs.getString("engineer_contact"));
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
        String query = "SELECT Projects.project_id, Projects.project_name, Projects.building_type, Projects.address, Projects.erf_number, "
                +
                "Projects.total_fee, Projects.amount_paid, Projects.due_date, Projects.status, Projects.completion_date, "
                +
                "Customers.customer_name, Customers.customer_surname, Customers.business_name, Customers.is_business, Customers.contact_info AS customer_contact, "
                +
                "Architects.architect_name, Architects.contact_info AS architect_contact, " +
                "Contractors.contractor_name, Contractors.contact_info AS contractor_contact, " +
                "ProjectManagers.manager_name AS project_manager_name, ProjectManagers.contact_info AS manager_contact, "
                +
                "StructuralEngineers.engineer_name AS structural_engineer_name, StructuralEngineers.contact_info AS engineer_contact "
                +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN Contractors ON Projects.contractor_id = Contractors.contractor_id " +
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
                    System.out.println("\nProject ID: " + rs.getInt("project_id"));
                    System.out.println("Project: " + rs.getString("project_name"));
                    System.out.println("Building Type: " + rs.getString("building_type"));
                    System.out.println("Address: " + rs.getString("address"));
                    System.out.println("ERF Number: " + rs.getString("erf_number"));
                    System.out.println("Total Fee: " + rs.getBigDecimal("total_fee"));
                    System.out.println("Amount Paid: " + rs.getBigDecimal("amount_paid"));
                    System.out.println("Due Date: " + rs.getDate("due_date"));
                    System.out.println("Status: " + rs.getString("status"));
                    System.out.println("Completion Date: " + rs.getDate("completion_date"));
                    System.out.println("Customer: " + rs.getString("customer_name") + " "
                            + rs.getString("customer_surname") + " - Contact: "
                            + rs.getString("customer_contact"));
                    if (rs.getBoolean("is_business")) {
                        System.out.println("Business Name: " + rs.getString("business_name"));
                    }
                    System.out.println("Architect: " + rs.getString("architect_name") + " - Contact: "
                            + rs.getString("architect_contact"));
                    System.out.println("Contractor: " + rs.getString("contractor_name") + " - Contact: "
                            + rs.getString("contractor_contact"));
                    System.out.println("Project Manager: " + rs.getString("project_manager_name") + " - Contact: "
                            + rs.getString("manager_contact"));
                    System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name")
                            + " - Contact: " + rs.getString("engineer_contact"));
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
        String query = "SELECT Projects.project_id, Projects.project_name, Projects.building_type, Projects.address, Projects.erf_number, "
                +
                "Projects.total_fee, Projects.amount_paid, Projects.due_date, Projects.status, Projects.completion_date, "
                +
                "Customers.customer_name, Customers.customer_surname, Customers.business_name, Customers.is_business, Customers.contact_info AS customer_contact, "
                +
                "Architects.architect_name, Architects.contact_info AS architect_contact, " +
                "Contractors.contractor_name, Contractors.contact_info AS contractor_contact, " +
                "ProjectManagers.manager_name AS project_manager_name, ProjectManagers.contact_info AS manager_contact, "
                +
                "StructuralEngineers.engineer_name AS structural_engineer_name, StructuralEngineers.contact_info AS engineer_contact "
                +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN Contractors ON Projects.contractor_id = Contractors.contractor_id " +
                "JOIN ProjectManagers ON Projects.project_manager_id = ProjectManagers.manager_id " +
                "JOIN StructuralEngineers ON Projects.structural_engineer_id = StructuralEngineers.engineer_id " +
                "WHERE Projects.status != 'finalized'";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("\nOpen Project ID: " + rs.getInt("project_id"));
                System.out.println("Project: " + rs.getString("project_name"));
                System.out.println("Building Type: " + rs.getString("building_type"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("ERF Number: " + rs.getString("erf_number"));
                System.out.println("Total Fee: " + rs.getBigDecimal("total_fee"));
                System.out.println("Amount Paid: " + rs.getBigDecimal("amount_paid"));
                System.out.println("Due Date: " + rs.getDate("due_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Completion Date: " + rs.getDate("completion_date"));
                System.out.println("Customer: " + rs.getString("customer_name") + " " + rs.getString("customer_surname")
                        + " - Contact: "
                        + rs.getString("customer_contact"));
                if (rs.getBoolean("is_business")) {
                    System.out.println("Business Name: " + rs.getString("business_name"));
                }
                System.out.println("Architect: " + rs.getString("architect_name") + " - Contact: "
                        + rs.getString("architect_contact"));
                System.out.println("Contractor: " + rs.getString("contractor_name") + " - Contact: "
                        + rs.getString("contractor_contact"));
                System.out.println("Project Manager: " + rs.getString("project_manager_name") + " - Contact: "
                        + rs.getString("manager_contact"));
                System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name") + " - Contact: "
                        + rs.getString("engineer_contact"));
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
        String query = "SELECT Projects.project_id, Projects.project_name, Projects.building_type, Projects.address, Projects.erf_number, "
                +
                "Projects.total_fee, Projects.amount_paid, Projects.due_date, Projects.status, Projects.completion_date, "
                +
                "Customers.customer_name, Customers.customer_surname, Customers.business_name, Customers.is_business, Customers.contact_info AS customer_contact, "
                +
                "Architects.architect_name, Architects.contact_info AS architect_contact, " +
                "Contractors.contractor_name, Contractors.contact_info AS contractor_contact, " +
                "ProjectManagers.manager_name AS project_manager_name, ProjectManagers.contact_info AS manager_contact, "
                +
                "StructuralEngineers.engineer_name AS structural_engineer_name, StructuralEngineers.contact_info AS engineer_contact "
                +
                "FROM Projects " +
                "JOIN Customers ON Projects.customer_id = Customers.customer_id " +
                "JOIN Architects ON Projects.architect_id = Architects.architect_id " +
                "JOIN Contractors ON Projects.contractor_id = Contractors.contractor_id " +
                "JOIN ProjectManagers ON Projects.project_manager_id = ProjectManagers.manager_id " +
                "JOIN StructuralEngineers ON Projects.structural_engineer_id = StructuralEngineers.engineer_id " +
                "WHERE Projects.status = 'finalized'";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("\nClosed Project ID: " + rs.getInt("project_id"));
                System.out.println("Project: " + rs.getString("project_name"));
                System.out.println("Building Type: " + rs.getString("building_type"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("ERF Number: " + rs.getString("erf_number"));
                System.out.println("Total Fee: " + rs.getBigDecimal("total_fee"));
                System.out.println("Amount Paid: " + rs.getBigDecimal("amount_paid"));
                System.out.println("Due Date: " + rs.getDate("due_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Completion Date: " + rs.getDate("completion_date"));
                System.out.println("Customer: " + rs.getString("customer_name") + " " + rs.getString("customer_surname")
                        + " - Contact: "
                        + rs.getString("customer_contact"));
                if (rs.getBoolean("is_business")) {
                    System.out.println("Business Name: " + rs.getString("business_name"));
                }
                System.out.println("Architect: " + rs.getString("architect_name") + " - Contact: "
                        + rs.getString("architect_contact"));
                System.out.println("Contractor: " + rs.getString("contractor_name") + " - Contact: "
                        + rs.getString("contractor_contact"));
                System.out.println("Project Manager: " + rs.getString("project_manager_name") + " - Contact: "
                        + rs.getString("manager_contact"));
                System.out.println("Structural Engineer: " + rs.getString("structural_engineer_name") + " - Contact: "
                        + rs.getString("engineer_contact"));
                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new customer to the database.
     *
     * @param customerName    the name of the customer
     * @param customerSurname the surname of the customer
     * @param contactInfo     the contact information of the customer
     * @param businessName    the business name if the customer is a business
     * @param isBusiness      whether the customer is a business
     */
    public void addCustomer(String customerName, String customerSurname, String contactInfo, String businessName,
            boolean isBusiness) {
        String query = "INSERT INTO Customers (customer_name, customer_surname, contact_info, business_name, is_business) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, customerSurname);
            pstmt.setString(3, contactInfo);
            pstmt.setString(4, businessName);
            pstmt.setBoolean(5, isBusiness);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all customers.
     */
    public void listAllCustomers() {
        String query = "SELECT * FROM Customers";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nCustomers:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("customer_id") + ", Name: " + rs.getString("customer_name") + " "
                        + rs.getString("customer_surname") +
                        ", Contact Info: " + rs.getString("contact_info") + ", Business Name: "
                        + rs.getString("business_name") +
                        ", Is Business: " + rs.getBoolean("is_business"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new architect to the database.
     *
     * @param architectName the name of the architect
     * @param contactInfo   the contact information of the architect
     */
    public void addArchitect(String architectName, String contactInfo) {
        String query = "INSERT INTO Architects (architect_name, contact_info) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, architectName);
            pstmt.setString(2, contactInfo);
            pstmt.executeUpdate();
            System.out.println("Architect added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all architects.
     */
    public void listAllArchitects() {
        String query = "SELECT * FROM Architects";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nArchitects:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("architect_id") + ", Name: " + rs.getString("architect_name") +
                        ", Contact Info: " + rs.getString("contact_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new project manager to the database.
     *
     * @param managerName the name of the project manager
     * @param contactInfo the contact information of the project manager
     */
    public void addProjectManager(String managerName, String contactInfo) {
        String query = "INSERT INTO ProjectManagers (manager_name, contact_info) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, managerName);
            pstmt.setString(2, contactInfo);
            pstmt.executeUpdate();
            System.out.println("Project Manager added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all project managers.
     */
    public void listAllProjectManagers() {
        String query = "SELECT * FROM ProjectManagers";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nProject Managers:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("manager_id") + ", Name: " + rs.getString("manager_name") +
                        ", Contact Info: " + rs.getString("contact_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new contractor to the database.
     *
     * @param contractorName the name of the contractor
     * @param contactInfo    the contact information of the contractor
     */
    public void addContractor(String contractorName, String contactInfo) {
        String query = "INSERT INTO Contractors (contractor_name, contact_info) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, contractorName);
            pstmt.setString(2, contactInfo);
            pstmt.executeUpdate();
            System.out.println("Contractor added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all contractors.
     */
    public void listAllContractors() {
        String query = "SELECT * FROM Contractors";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nContractors:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("contractor_id") + ", Name: " + rs.getString("contractor_name") +
                        ", Contact Info: " + rs.getString("contact_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new structural engineer to the database.
     *
     * @param engineerName the name of the structural engineer
     * @param contactInfo  the contact information of the structural engineer
     */
    public void addStructuralEngineer(String engineerName, String contactInfo) {
        String query = "INSERT INTO StructuralEngineers (engineer_name, contact_info) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, engineerName);
            pstmt.setString(2, contactInfo);
            pstmt.executeUpdate();
            System.out.println("Structural Engineer added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all structural engineers.
     */
    public void listAllStructuralEngineers() {
        String query = "SELECT * FROM StructuralEngineers";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nStructural Engineers:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("engineer_id") + ", Name: " + rs.getString("engineer_name") +
                        ", Contact Info: " + rs.getString("contact_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
