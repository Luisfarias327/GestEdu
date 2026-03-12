# GestEdu – Technical Documentation

**Responsible:** Luis Daniel Farias Cordero  
**System Name:** GestEdu (Educational Manager)  
**Version:** 1.0  

---

## 1. Introduction

GestEdu is a system developed to facilitate the management of a school's academic information. The system enables full student registration, storage of quarterly grades, and automatic calculation of the final grade for each subject throughout the academic year.

The objective is to assist in the management of students, classes, and grades, ensuring privacy, organization, and efficiency in the process of recording and monitoring academic performance.

---

## 2. Project Description and Users

GestEdu is designed with multiple features to serve different types of users. Since the main goal is to store academic information in a database, structured records and user permissions are implemented to ensure proper control of data flow.

### 2.1 Registered Entities

The system includes the following records:

- Students  
- Guardians  
- Classes  
- Academic Years  
- Enrollments  
- Subjects  
- Teachers  
- Terms (Trimesters)  
- Assessments  
- Quarterly Grades  
- Final Grades  
- Users  
- Access Profiles  
- User Profiles  

---

## 3. User Roles and Permissions

- **Director** – Full access to all records, reports, and tables.  
- **Secretary** – Access to student, guardian, and enrollment registration, as well as grade consultation, subjects, classes, and basic reports.  
- **Teacher** – Access to grade entry, assessments, and terms. Can view students according to class and subject.  
- **Student** – Access to view their own grades, subjects, and report cards.  

The system will perform daily backups containing data related to students, guardians, classes, teachers, subjects, and grades. A daily update report will also be generated and sent to the school's server and to the Director’s email.

---

## 4. Functional Requirements

**RF001 – Student Registration**  
Allows registering a student after completing required fields: name, CPF, date of birth, academic year, class, guardian data, and contact information.

**RF002 – Guardian Registration**  
Allows registering the legal guardian, including name, CPF, phone number, address, and relationship with the student.

**RF003 – Teacher Registration**  
Allows registering teachers, including full name, area of expertise, subjects taught, and contact information.

**RF004 – Class Registration**  
Allows creating classes, specifying academic year, grade level, and association with subjects and teachers.

**RF005 – Subject Registration**  
Allows registering school subjects and linking them to a specific academic year or class.

**RF006 – Enrollment Registration**  
Allows enrolling a student in a specific class and academic year, linking them to that year’s subjects.

**RF007 – Quarterly Grade Entry**  
Allows teachers to enter quarterly grades for each student by selecting class and term.

**RF008 – Final Grade Calculation**  
The system must automatically calculate the annual final grade based on the average of the three quarterly grades.

**RF009 – Student Performance Consultation**  
Allows viewing all quarterly and final grades of a student, organized by subject and academic year.

**RF010 – Daily Backup System**  
The system must generate a daily backup and send an update report to the school server and to the Director’s email.

---

## 5. Non-Functional Requirements

**RNF001 – User Permissions**  
The system must ensure that each user accesses only the features allowed for their role.

**RNF002 – Automatic Daily Backup**  
The system must automatically perform daily backups of stored data.

**RNF003 – Data Security**  
All data must be protected against unauthorized access, ensuring confidentiality and integrity.

**RNF004 – Interface Usability**  
The interface must be simple, clear, and easy to use for all user types.

**RNF005 – Performance**  
System screens must load efficiently without delays that affect usability.

---

## 6. Technical Notes

The system will be developed using Java and MySQL as the database management system.

The connection between the application and the database will be implemented using a direct and structured approach, ensuring reliable data storage and retrieval.

An automatic daily backup mechanism will be implemented to guarantee data recovery in case of failure.

The interface will be designed to be intuitive and accessible for directors, secretaries, teachers, and students.

The goal is to create a lightweight, organized, and functional system that supports academic management and allows future improvements without complex maintenance.
