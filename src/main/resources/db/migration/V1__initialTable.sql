-- =========================================================
-- EduConnect Seed Data (MySQL)
-- Optimized for Hibernate-generated tables (snake_case)
-- UUIDs stored as BINARY(16)
-- =========================================================

-- Disable checks to prevent order-of-operation issues during seed
SET FOREIGN_KEY_CHECKS = 0;

START TRANSACTION;

-- =========================================================
-- 1) Generate and Store UUIDs in Variables
-- =========================================================

-- Admin Users
SET @u_admin_iyer  = UUID_TO_BIN(UUID());
SET @u_admin_menon = UUID_TO_BIN(UUID());

-- Teacher Users
SET @u_teacher_meera = UUID_TO_BIN(UUID());
SET @u_teacher_arun  = UUID_TO_BIN(UUID());
SET @u_teacher_ravi  = UUID_TO_BIN(UUID());

-- Student Users
SET @u_student_priya   = UUID_TO_BIN(UUID());
SET @u_student_vignesh = UUID_TO_BIN(UUID());
SET @u_student_sneha   = UUID_TO_BIN(UUID());
SET @u_student_rahul   = UUID_TO_BIN(UUID());
SET @u_student_ananya  = UUID_TO_BIN(UUID());

-- Parent Users
SET @u_parent_sharma = UUID_TO_BIN(UUID());
SET @u_parent_nair   = UUID_TO_BIN(UUID());
SET @u_parent_rao    = UUID_TO_BIN(UUID());

-- =========================================================
-- 2) Insert into Base Table: 'users'
-- =========================================================

INSERT INTO `users` (`user_id`, `email`, `password`, `full_name`, `role`, `is_active`, `created_at`) VALUES
(@u_admin_iyer,       'admin.iyer@educonnect.in',   '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Lakshmi Iyer',    'ADMIN',   TRUE, NOW()),
(@u_admin_menon,      'admin.menon@educonnect.in',  '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Rohit Menon',     'ADMIN',   TRUE, NOW()),
(@u_teacher_meera,    'meera.iyer@educonnect.in',   '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Meera Iyer',      'TEACHER', TRUE, NOW()),
(@u_teacher_arun,     'arun.kumar@educonnect.in',   '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Arun Kumar',      'TEACHER', TRUE, NOW()),
(@u_teacher_ravi,     'ravi.menon@educonnect.in',   '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Ravi Menon',      'TEACHER', TRUE, NOW()),
(@u_student_priya,    'priya.sharma@student.in',    '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Priya Sharma',    'STUDENT', TRUE, NOW()),
(@u_student_vignesh,  'vignesh.nair@student.in',    '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Vignesh Nair',    'STUDENT', TRUE, NOW()),
(@u_student_sneha,    'sneha.rao@student.in',       '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Sneha Rao',       'STUDENT', TRUE, NOW()),
(@u_student_rahul,    'rahul.verma@student.in',     '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Rahul Verma',     'STUDENT', TRUE, NOW()),
(@u_student_ananya,   'ananya.krish@student.in',    '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Ananya Krishnan', 'STUDENT', TRUE, NOW()),
(@u_parent_sharma,    'parent.sharma@guardian.in',  '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Amit Sharma',     'PARENT',  TRUE, NOW()),
(@u_parent_nair,      'parent.nair@guardian.in',    '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Deepa Nair',      'PARENT',  TRUE, NOW()),
(@u_parent_rao,       'parent.rao@guardian.in',     '$2a$10$9xjQ7jI9xjQ7jI9xjQ7jO', 'Kiran Rao',       'PARENT',  TRUE, NOW());

-- =========================================================
-- 3) Insert into JOINED Subclass Tables
-- =========================================================

-- Admins
INSERT INTO `admin` (`admin_id`) VALUES (@u_admin_iyer), (@u_admin_menon);

-- Teachers
INSERT INTO `teacher` (`teacher_id`, `department`, `qualification`) VALUES
(@u_teacher_meera, 'computer science', 'M.Tech'),
(@u_teacher_arun,  'electronics',      'Ph.D'),
(@u_teacher_ravi,  'mathematics',      'M.Sc');

-- Students
INSERT INTO `student` (`student_id`, `date_of_birth`, `enrollment_number`) VALUES
(@u_student_priya,   '2004-05-12', 'ENR-2024-001'),
(@u_student_vignesh, '2003-11-23', 'ENR-2024-002'),
(@u_student_sneha,   '2004-01-30', 'ENR-2024-003'),
(@u_student_rahul,   '2005-03-18', 'ENR-2024-004'),
(@u_student_ananya,  '2004-07-09', 'ENR-2024-005');

-- Parents
INSERT INTO `parent` (`parent_id`, `phone_number`) VALUES
(@u_parent_sharma, '+91-98765-00011'),
(@u_parent_nair,   '+91-98765-00012'),
(@u_parent_rao,    '+91-98765-00013');

-- =========================================================
-- 4) Courses and modules
-- =========================================================

SET @course_cs101 = UUID_TO_BIN(UUID());
SET @course_ec201 = UUID_TO_BIN(UUID());
SET @course_ma110 = UUID_TO_BIN(UUID());

INSERT INTO `course` (`course_id`, `title`, `description`, `course_code`, `teacher_id`) VALUES
(@course_cs101, 'Data Structures', 'Arrays, Linked Lists, Trees, Graphs', 'CS101', @u_teacher_meera),
(@course_ec201, 'Digital Circuits', 'Logic gates, K-map, FSM design',     'EC201', @u_teacher_arun),
(@course_ma110, 'Linear Algebra',   'Vectors, Matrices, Eigenvalues',     'MA110', @u_teacher_ravi);

-- Modules
SET @mod_cs101_arrays = UUID_TO_BIN(UUID());
SET @mod_cs101_trees  = UUID_TO_BIN(UUID());

INSERT INTO `course_module` (`module_id`, `title`, `content_url`, `sequence_order`, `course_id`) VALUES
(@mod_cs101_arrays, 'Arrays & Lists',      'https://cdn.edu/files/cs101/arrays.pdf', 1, @course_cs101),
(@mod_cs101_trees,  'Trees',               'https://cdn.edu/files/cs101/trees.pdf',  2, @course_cs101);

-- =========================================================
-- 5) Assessments and Enrollments
-- =========================================================

SET @as_cs101_midterm = UUID_TO_BIN(UUID());

INSERT INTO `assessment` (`assessment_id`, `max_score`, `title`, `type`, `course_id`) VALUES
(@as_cs101_midterm, 100.0, 'CS101 Midterm', 'EXAM', @course_cs101);

SET @enr_priya_cs101 = UUID_TO_BIN(UUID());

INSERT INTO `enrollment` (`enrollment_id`, `student_id`, `course_id`, `is_active`, `final_grade`) VALUES
(@enr_priya_cs101, @u_student_priya, @course_cs101, TRUE, 84.5);

-- =========================================================
-- 6) Notifications and Audits
-- =========================================================

SET @notif1 = UUID_TO_BIN(UUID());
INSERT INTO `notification` (`notification_id`, `entity_id`, `message`, `category`, `status`, `user_id`) VALUES
(@notif1, @course_cs101, 'You have been assigned to teach CS101', 'GREEN', TRUE, @u_teacher_meera);

SET @audit1 = UUID_TO_BIN(UUID());
INSERT INTO `audit` (`audit_id`, `scope`, `findings`, `date`, `status`, `admin_id`) VALUES
(@audit1, 'User Management', 'Password policy needs strengthening', '2025-12-15', FALSE, @u_admin_iyer);

COMMIT;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;