# Project Management System

## Description
The Project Management System (PoisePMS) is an application designed to manage construction projects. It handles various project details such as project names, building types, addresses, fees, due dates, and the involvement of various stakeholders like customers, architects, contractors, project managers, and structural engineers.

## Table of Contents
1. [Installation Instructions](#installation-instructions)
2. [Usage](#usage)
3. [Features](#features)
4. [Contributing](#contributing)
5. [License](#license)
6. [Contact Information](#contact-information)

## Installation Instructions
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/PoisePMS.git
   cd PoisePMS
   ```
2. Setup the database:
   - Ensure you have MySQL installed.
   - Run the script to create the database and tables:
     ```sql
     -- Create the database
     CREATE DATABASE PoisePMS;

     -- Use the database
     USE PoisePMS;

     -- Create the tables
     -- Add the provided SQL script content here to create tables and insert sample data
     ```
3. Update database credentials:
   - Update the `DatabaseManager.java` file with your MySQL database credentials.
4. Build the project:
   ```sh
   mvn clean install
   ```
5. Run the project:
   ```sh
   java -cp target/PoisePMS-1.0-SNAPSHOT.jar com.example.project.Main
   ```

## Usage
Upon running the application, the user will be presented with a menu to manage and display project data.

### Primary Menu:
1. Display data
2. Manage data
3. Exit

### Display Data Menu:
1. List all projects
2. Find overdue projects
3. Find project by number or name
4. List all open projects
5. List all finalized projects
6. List all customers
7. List all architects
8. List all contractors
9. List all project managers
10. List all structural engineers
11. Back to main menu

### Manage Data Menu:
1. Add project
2. Update project
3. Delete project
4. Finalize project
5. Add new customer
6. Add new architect
7. Add new project manager
8. Add new contractor
9. Add new structural engineer
10. Back to main menu

## Features
- **Project Management**: Add, update, delete, and finalize projects.
- **Stakeholder Management**: Manage customers, architects, contractors, project managers, and structural engineers.
- **Data Display**: Display detailed information about projects and stakeholders.
- **Overdue Projects**: Identify projects that are overdue.

## Contributing
1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a pull request.

## License
This project is licensed under the MIT License.

## Contact Information
For any queries or support, please contact:
- Email: L.Pretorius07@gmail.com
```
