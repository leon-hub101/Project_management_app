import java.sql.Date; // Importing java.sql.Date for the valueOf method
import java.util.Scanner;

/**
 * The Main class is the entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        ProjectManager projectManager = new ProjectManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Read all projects");
            System.out.println("2. Add project");
            System.out.println("3. Update project");
            System.out.println("4. Delete project");
            System.out.println("5. Finalize project");
            System.out.println("6. Find overdue projects");
            System.out.println("7. Find project by number or name");
            System.out.println("8. List all open projects");
            System.out.println("9. List all finalized projects");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    projectManager.readProjects();
                    break;
                case 2:
                    System.out.print("Enter project name: ");
                    String projectName = scanner.nextLine();
                    System.out.print("Enter structural engineer ID: ");
                    int structuralEngineerId = scanner.nextInt();
                    System.out.print("Enter project manager ID: ");
                    int projectManagerId = scanner.nextInt();
                    System.out.print("Enter architect ID: ");
                    int architectId = scanner.nextInt();
                    System.out.print("Enter customer ID: ");
                    int customerId = scanner.nextInt();
                    System.out.print("Enter due date (yyyy-mm-dd): ");
                    String dueDateString = scanner.next();
                    Date dueDate = Date.valueOf(dueDateString);
                    projectManager.addProject(projectName, structuralEngineerId, projectManagerId, architectId,
                            customerId, dueDate);
                    break;
                case 3:
                    System.out.print("Enter project ID to update: ");
                    int projectIdToUpdate = scanner.nextInt();
                    System.out.print("Enter new due date (yyyy-mm-dd): ");
                    String newDueDateString = scanner.next();
                    Date newDueDate = java.sql.Date.valueOf(newDueDateString);
                    System.out.print("Enter new architect ID: ");
                    int newArchitectId = scanner.nextInt();
                    System.out.print("Enter new structural engineer ID: ");
                    int newStructuralEngineerId = scanner.nextInt();
                    System.out.print("Enter new project manager ID: ");
                    int newProjectManagerId = scanner.nextInt();
                    projectManager.updateProjectDetails(projectIdToUpdate, newDueDate, newArchitectId,
                            newStructuralEngineerId, newProjectManagerId);
                    break;
                case 4:
                    System.out.print("Enter project ID to delete: ");
                    int projectIdToDelete = scanner.nextInt();
                    projectManager.deleteProject(projectIdToDelete);
                    break;
                case 5:
                    System.out.print("Enter project ID to finalize: ");
                    int projectIdToFinalize = scanner.nextInt();
                    System.out.print("Enter completion date (yyyy-mm-dd): ");
                    String dateInput = scanner.next();
                    Date completionDate = java.sql.Date.valueOf(dateInput);
                    projectManager.finalizeProject(projectIdToFinalize, completionDate);
                    break;
                case 6:
                    System.out.print("Enter current date (yyyy-mm-dd): ");
                    String currentDateInput = scanner.next();
                    Date currentDate = java.sql.Date.valueOf(currentDateInput);
                    projectManager.findOverdueProjects(currentDate);
                    break;
                case 7:
                    System.out.print("Enter project number or name: ");
                    String projectIdentifier = scanner.nextLine();
                    projectManager.findProject(projectIdentifier);
                    break;
                case 8:
                    projectManager.findOpenProjects();
                    break;
                case 9:
                    projectManager.findClosedProjects();
                    break;
                case 10:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
