package com.example.project;

import java.sql.Date; // Importing java.sql.Date for the valueOf method
import java.util.Scanner;
import java.math.BigDecimal;

/**
 * The Main class is the entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        ProjectManager projectManager = new ProjectManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nPrimary Menu:");
            System.out.println("1. Display data");
            System.out.println("2. Manage data");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int primaryChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (primaryChoice == 1) {
                displayReadMenu(projectManager, scanner);
            } else if (primaryChoice == 2) {
                displayAddMenu(projectManager, scanner);
            } else if (primaryChoice == 3) {
                scanner.close();
                System.exit(0);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayReadMenu(ProjectManager projectManager, Scanner scanner) {
        while (true) {
            System.out.println("\nDisplay Data Menu:");
            System.out.println("1. List all projects");
            System.out.println("2. Find overdue projects");
            System.out.println("3. Find project by number or name");
            System.out.println("4. List all open projects");
            System.out.println("5. List all finalized projects");
            System.out.println("6. List all customers");
            System.out.println("7. List all architects");
            System.out.println("8. List all contractors");
            System.out.println("9. List all project managers");
            System.out.println("10. List all structural engineers");
            System.out.println("11. Back to main menu");
            System.out.print("Choose an option: ");
            int readChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (readChoice) {
                case 1:
                    projectManager.readProjects();
                    break;
                case 2: {
                    System.out.print("\nEnter current date (yyyy-mm-dd): ");
                    String currentDateInput = scanner.next();
                    if (currentDateInput.equalsIgnoreCase("cancel"))
                        break;
                    Date currentDate = Date.valueOf(currentDateInput);

                    projectManager.findOverdueProjects(currentDate);
                    break;
                }
                case 3: {
                    System.out.print("\nEnter project number or name (or type 'cancel' to go back): ");
                    String projectIdentifier = scanner.nextLine();
                    if (projectIdentifier.equalsIgnoreCase("cancel"))
                        break;
                    projectManager.findProject(projectIdentifier);
                    break;
                }
                case 4:
                    projectManager.findOpenProjects();
                    break;
                case 5:
                    projectManager.findClosedProjects();
                    break;
                case 6:
                    projectManager.listAllCustomers();
                    break;
                case 7:
                    projectManager.listAllArchitects();
                    break;
                case 8:
                    projectManager.listAllContractors();
                    break;
                case 9:
                    projectManager.listAllProjectManagers();
                    break;
                case 10:
                    projectManager.listAllStructuralEngineers();
                    break;
                case 11:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayAddMenu(ProjectManager projectManager, Scanner scanner) {
        while (true) {
            System.out.println("\nManage Data Menu:");
            System.out.println("1. Add project");
            System.out.println("2. Update project");
            System.out.println("3. Delete project");
            System.out.println("4. Finalize project");
            System.out.println("5. Add new customer");
            System.out.println("6. Add new architect");
            System.out.println("7. Add new project manager");
            System.out.println("8. Add new contractor");
            System.out.println("9. Add new structural engineer");
            System.out.println("10. Back to main menu");
            System.out.print("Choose an option: ");
            int addChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (addChoice) {
                case 1: {
                    System.out.print("\nEnter project name (or type 'cancel' to go back): ");
                    String projectName = scanner.nextLine();
                    if (projectName.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter building type: ");
                    String buildingType = scanner.nextLine();
                    if (buildingType.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();
                    if (address.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter ERF number: ");
                    String erfNumber = scanner.nextLine();
                    if (erfNumber.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter total fee: ");
                    BigDecimal totalFee = scanner.nextBigDecimal();
                    if (totalFee.compareTo(BigDecimal.ZERO) < 0)
                        break;

                    System.out.print("Enter amount paid: ");
                    BigDecimal amountPaid = scanner.nextBigDecimal();
                    if (amountPaid.compareTo(BigDecimal.ZERO) < 0)
                        break;

                    // Consume the newline left-over
                    scanner.nextLine();

                    System.out.print("Enter due date (yyyy-mm-dd): ");
                    String dueDateString = scanner.nextLine();
                    if (dueDateString.equalsIgnoreCase("cancel"))
                        break;
                    Date dueDate = Date.valueOf(dueDateString);

                    System.out.print("Enter architect ID: ");
                    String architectIdInput = scanner.nextLine();
                    if (architectIdInput.equalsIgnoreCase("cancel"))
                        break;
                    int architectId;
                    try {
                        architectId = Integer.parseInt(architectIdInput);
                        if (architectId < 0)
                            break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid architect ID. Please enter a valid number.");
                        break;
                    }

                    System.out.print("Enter contractor ID: ");
                    String contractorIdInput = scanner.nextLine();
                    if (contractorIdInput.equalsIgnoreCase("cancel"))
                        break;
                    int contractorId;
                    try {
                        contractorId = Integer.parseInt(contractorIdInput);
                        if (contractorId < 0)
                            break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid contractor ID. Please enter a valid number.");
                        break;
                    }

                    System.out.print("Enter customer ID: ");
                    String customerIdInput = scanner.nextLine();
                    if (customerIdInput.equalsIgnoreCase("cancel"))
                        break;
                    int customerId;
                    try {
                        customerId = Integer.parseInt(customerIdInput);
                        if (customerId < 0)
                            break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid customer ID. Please enter a valid number.");
                        break;
                    }

                    System.out.print("Enter structural engineer ID: ");
                    String structuralEngineerIdInput = scanner.nextLine();
                    if (structuralEngineerIdInput.equalsIgnoreCase("cancel"))
                        break;
                    int structuralEngineerId;
                    try {
                        structuralEngineerId = Integer.parseInt(structuralEngineerIdInput);
                        if (structuralEngineerId < 0)
                            break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid structural engineer ID. Please enter a valid number.");
                        break;
                    }

                    System.out.print("Enter project manager ID: ");
                    String projectManagerIdInput = scanner.nextLine();
                    if (projectManagerIdInput.equalsIgnoreCase("cancel"))
                        break;
                    int projectManagerId;
                    try {
                        projectManagerId = Integer.parseInt(projectManagerIdInput);
                        if (projectManagerId < 0)
                            break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid project manager ID. Please enter a valid number.");
                        break;
                    }

                    // Consume any remaining newline
                    scanner.nextLine();

                    System.out.print("Is the customer a business? (y/n): ");
                    String isBusinessInput = scanner.nextLine();
                    boolean isBusiness = isBusinessInput.equalsIgnoreCase("y");

                    String customerName = "";
                    String customerSurname = "";
                    String businessName = "";

                    if (isBusiness) {
                        System.out.print("Enter business name: ");
                        businessName = scanner.nextLine();
                        if (businessName.equalsIgnoreCase("cancel"))
                            break;
                    } else {
                        System.out.print("Enter customer first name: ");
                        customerName = scanner.nextLine();
                        if (customerName.equalsIgnoreCase("cancel"))
                            break;

                        System.out.print("Enter customer surname: ");
                        customerSurname = scanner.nextLine();
                        if (customerSurname.equalsIgnoreCase("cancel"))
                            break;
                    }

                    projectManager.addProject(projectName, buildingType, address, erfNumber, totalFee, amountPaid,
                            dueDate, architectId, contractorId, customerId, structuralEngineerId, projectManagerId,
                            customerName, customerSurname, businessName, isBusiness);
                    break;
                }
                case 2: {
                    System.out.print("\nEnter project ID to update (or type 'cancel' to go back): ");
                    String projectIdInput = scanner.next();
                    if (projectIdInput.equalsIgnoreCase("cancel"))
                        break;

                    int projectIdToUpdate;
                    try {
                        projectIdToUpdate = Integer.parseInt(projectIdInput);
                        if (projectIdToUpdate < 0)
                            break;

                        System.out.print("Enter new total fee: ");
                        BigDecimal newTotalFee = scanner.nextBigDecimal();
                        if (newTotalFee.compareTo(BigDecimal.ZERO) < 0)
                            break;

                        System.out.print("Enter new amount paid: ");
                        BigDecimal newAmountPaid = scanner.nextBigDecimal();
                        if (newAmountPaid.compareTo(BigDecimal.ZERO) < 0)
                            break;

                        System.out.print("Enter new due date (yyyy-mm-dd): ");
                        String newDueDateString = scanner.next();
                        if (newDueDateString.equalsIgnoreCase("cancel"))
                            break;
                        Date newDueDate = Date.valueOf(newDueDateString);

                        System.out.print("Enter new architect ID: ");
                        String newArchitectIdInput = scanner.next();
                        if (newArchitectIdInput.equalsIgnoreCase("cancel"))
                            break;
                        int newArchitectId;
                        try {
                            newArchitectId = Integer.parseInt(newArchitectIdInput);
                            if (newArchitectId < 0)
                                break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid architect ID. Please enter a valid number.");
                            break;
                        }

                        System.out.print("Enter new contractor ID: ");
                        String newContractorIdInput = scanner.next();
                        if (newContractorIdInput.equalsIgnoreCase("cancel"))
                            break;
                        int newContractorId;
                        try {
                            newContractorId = Integer.parseInt(newContractorIdInput);
                            if (newContractorId < 0)
                                break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid contractor ID. Please enter a valid number.");
                            break;
                        }

                        System.out.print("Enter new structural engineer ID: ");
                        String newStructuralEngineerIdInput = scanner.next();
                        if (newStructuralEngineerIdInput.equalsIgnoreCase("cancel"))
                            break;
                        int newStructuralEngineerId;
                        try {
                            newStructuralEngineerId = Integer.parseInt(newStructuralEngineerIdInput);
                            if (newStructuralEngineerId < 0)
                                break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid structural engineer ID. Please enter a valid number.");
                            break;
                        }

                        System.out.print("Enter new project manager ID: ");
                        String newProjectManagerIdInput = scanner.next();
                        if (newProjectManagerIdInput.equalsIgnoreCase("cancel"))
                            break;
                        int newProjectManagerId;
                        try {
                            newProjectManagerId = Integer.parseInt(newProjectManagerIdInput);
                            if (newProjectManagerId < 0)
                                break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid project manager ID. Please enter a valid number.");
                            break;
                        }

                        projectManager.updateProjectDetails(projectIdToUpdate, newTotalFee, newAmountPaid, newDueDate,
                                newArchitectId, newContractorId, newStructuralEngineerId, newProjectManagerId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid project ID. Please enter a valid number.");
                    }
                    break;
                }
                case 3: {
                    System.out.print("\nEnter project ID to delete (or type 'cancel' to go back): ");
                    String deleteInput = scanner.next();
                    if (deleteInput.equalsIgnoreCase("cancel"))
                        break;

                    int projectIdToDelete;
                    try {
                        projectIdToDelete = Integer.parseInt(deleteInput);
                        if (projectIdToDelete < 0)
                            break;
                        projectManager.deleteProject(projectIdToDelete);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid project ID. Please enter a valid number.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("\nEnter project ID to finalize (or type 'cancel' to go back): ");
                    String finalizeInput = scanner.next();
                    if (finalizeInput.equalsIgnoreCase("cancel"))
                        break;

                    int projectIdToFinalize;
                    try {
                        projectIdToFinalize = Integer.parseInt(finalizeInput);
                        if (projectIdToFinalize < 0)
                            break;

                        System.out.print("Enter completion date (yyyy-mm-dd): ");
                        String dateInput = scanner.next();
                        if (dateInput.equalsIgnoreCase("cancel"))
                            break;
                        Date completionDate = Date.valueOf(dateInput);

                        projectManager.finalizeProject(projectIdToFinalize, completionDate);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid project ID. Please enter a valid number.");
                    }
                    break;
                }
                case 5: {
                    System.out.print("\nIs the customer a business? (y/n): ");
                    String isBusinessInput = scanner.nextLine();
                    boolean isBusiness = isBusinessInput.equalsIgnoreCase("y");

                    String customerName = "";
                    String customerSurname = "";
                    String businessName = "";
                    String contactInfo;

                    if (isBusiness) {
                        System.out.print("Enter business name (or type 'cancel' to go back): ");
                        businessName = scanner.nextLine();
                        if (businessName.equalsIgnoreCase("cancel"))
                            break;

                        System.out.print("Enter contact information: ");
                        contactInfo = scanner.nextLine();
                        if (contactInfo.equalsIgnoreCase("cancel"))
                            break;
                    } else {
                        System.out.print("Enter customer first name (or type 'cancel' to go back): ");
                        customerName = scanner.nextLine();
                        if (customerName.equalsIgnoreCase("cancel"))
                            break;

                        System.out.print("Enter customer surname: ");
                        customerSurname = scanner.nextLine();
                        if (customerSurname.equalsIgnoreCase("cancel"))
                            break;

                        System.out.print("Enter contact information: ");
                        contactInfo = scanner.nextLine();
                        if (contactInfo.equalsIgnoreCase("cancel"))
                            break;
                    }

                    projectManager.addCustomer(customerName, customerSurname, contactInfo, businessName, isBusiness);
                    break;
                }
                case 6: {
                    System.out.print("\nEnter architect name (or type 'cancel' to go back): ");
                    String architectName = scanner.nextLine();
                    if (architectName.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter contact information: ");
                    String architectContactInfo = scanner.nextLine();
                    if (architectContactInfo.equalsIgnoreCase("cancel"))
                        break;

                    projectManager.addArchitect(architectName, architectContactInfo);
                    break;
                }
                case 7: {
                    System.out.print("\nEnter project manager name (or type 'cancel' to go back): ");
                    String managerName = scanner.nextLine();
                    if (managerName.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter contact information: ");
                    String managerContactInfo = scanner.nextLine();
                    if (managerContactInfo.equalsIgnoreCase("cancel"))
                        break;

                    projectManager.addProjectManager(managerName, managerContactInfo);
                    break;
                }
                case 8: {
                    System.out.print("\nEnter contractor name (or type 'cancel' to go back): ");
                    String contractorName = scanner.nextLine();
                    if (contractorName.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter contact information: ");
                    String contractorContactInfo = scanner.nextLine();
                    if (contractorContactInfo.equalsIgnoreCase("cancel"))
                        break;

                    projectManager.addContractor(contractorName, contractorContactInfo);
                    break;
                }
                case 9: {
                    System.out.print("\nEnter structural engineer name (or type 'cancel' to go back): ");
                    String engineerName = scanner.nextLine();
                    if (engineerName.equalsIgnoreCase("cancel"))
                        break;

                    System.out.print("Enter contact information: ");
                    String engineerContactInfo = scanner.nextLine();
                    if (engineerContactInfo.equalsIgnoreCase("cancel"))
                        break;

                    projectManager.addStructuralEngineer(engineerName, engineerContactInfo);
                    break;
                }
                case 10:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
