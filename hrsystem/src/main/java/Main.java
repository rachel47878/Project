import crud.AnnouncementCRUD;
import crud.DepartmentCRUD;
import crud.EmployeeCRUD;
import crud.EmployeeTrainingCRUD;
import crud.LeaveCRUD;
import crud.PayrollCRUD;
import crud.TrainingCRUD;
import crud.UserCRUD;

public class Main {

    public static void main(String[] args) {

        System.out.println("=============================");
        System.out.println("   HR System - CRUD Tests    ");
        System.out.println("=============================\n");

        // ---- DEPARTMENT (must be first - Employee depends on it) ----
        System.out.println("--- Department Tests ---");
        DepartmentCRUD departmentCRUD = new DepartmentCRUD();
        departmentCRUD.insertDepartment(1, "Human Resources", 1);
        departmentCRUD.retrieveDepartment(1, null, null);
        departmentCRUD.updateDepartment(1, "HR Department", 1);
        departmentCRUD.retrieveDepartment(1, null, null);
        System.out.println();

        // ---- USER ----
        System.out.println("--- User Tests ---");
        UserCRUD userCRUD = new UserCRUD();
        userCRUD.createUser(1, "rachelo", "password123", "admin");
        userCRUD.retrieveUser(1);
        userCRUD.updateUser(1, "rachelo_updated", "admin");
        userCRUD.retrieveUser(1);
        userCRUD.deleteUser(1);
        System.out.println();

        // ---- EMPLOYEE (depends on Department) ----
        System.out.println("--- Employee Tests ---");
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        employeeCRUD.insertEmployee("Rachel", "OShea", "Manager", "HR Manager", "2024-01-15", "rachel@company.com", 1);
        employeeCRUD.retrieveEmployee("Rachel", "OShea");
        employeeCRUD.updateEmployee("Rachel", "OShea", "rachel@company.com", 1, "Senior HR Manager");
        employeeCRUD.retrieveEmployee("Rachel", "OShea");
        System.out.println();

        // ---- LEAVE (depends on Employee) ----
        System.out.println("--- Leave Tests ---");
        LeaveCRUD leaveCRUD = new LeaveCRUD();
        leaveCRUD.createLeave(1, 7, "2024-06-01", "2024-06-07", "Annual", "Pending");
        leaveCRUD.retrieveLeave(1);
        leaveCRUD.updateLeave(1, "Approved");
        leaveCRUD.retrieveAllLeave();
        System.out.println();

        // ---- EMPLOYEE TRAINING (depends on Employee) ----
        System.out.println("--- Employee Training Tests ---");
        EmployeeTrainingCRUD employeeTrainingCRUD = new EmployeeTrainingCRUD();
        employeeTrainingCRUD.setTraining(1, 1, 1, "2024-02-15");
        employeeTrainingCRUD.retrieveTraining(1);
        employeeTrainingCRUD.updateTraining(1, 1, 1, "2024-03-15");
        employeeTrainingCRUD.retrieveTraining(1);
        System.out.println();

        // ---- PAYROLL (depends on Employee) ----
        System.out.println("--- Payroll Tests ---");
        PayrollCRUD payrollCRUD = new PayrollCRUD();
        payrollCRUD.insertPayroll(1, 7, "2024-01-31", 5000.00, 1000.00, 200.00, 3800.00, 500.00);
        payrollCRUD.retrievePayroll(1);
        payrollCRUD.updatePayroll(1, "2024-01-31", 5500.00, 1100.00, 200.00, 4200.00, 600.00);
        payrollCRUD.retrievePayroll(1);
        payrollCRUD.deletePayroll(1);
        System.out.println();

        // ---- ANNOUNCEMENT ----
        System.out.println("--- Announcement Tests ---");
        AnnouncementCRUD announcementCRUD = new AnnouncementCRUD();
        announcementCRUD.insertAnnouncement(1, "Welcome", "Welcome to the HR System", "2024-01-15");
        announcementCRUD.retrieveAnnouncement(1, null, null);
        announcementCRUD.updateAnnouncement(1, "Welcome Updated", "Updated welcome message");
        announcementCRUD.retrieveAnnouncement(1, null, null);
        announcementCRUD.deleteAnnouncement(1, "Welcome Updated");
        System.out.println();

        // ---- TRAINING ----
        System.out.println("--- Training Tests ---");
        TrainingCRUD trainingCRUD = new TrainingCRUD();
        trainingCRUD.insertTraining(1, "Health & Safety", "Workplace safety training", "2024-01-15", "2024-02-15");
        trainingCRUD.retrieveTraining(1);
        trainingCRUD.updateTraining(1, "Health & Safety Advanced", "Advanced workplace safety", "2024-01-15", "2024-03-15");
        trainingCRUD.retrieveTraining(1);
        trainingCRUD.deleteTraining(1);
        System.out.println();

        // Delete employee first, then department
        employeeCRUD.deleteEmployee("Rachel", "OShea", "rachel@company.com", 1, "HR Manager");
        departmentCRUD.deleteDepartment(1);

        // ---- DELETE DEPARTMENT LAST ----
        departmentCRUD.deleteDepartment(1);

        System.out.println("=============================");
        System.out.println("      All Tests Complete     ");
        System.out.println("=============================");
    }
}