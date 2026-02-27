-- =========================================================
-- EduConnect seed data (MySQL, snake_case columns)
-- UUIDs used as PRIMARY KEYS (BINARY(16))
-- All extra *_uuid columns removed
-- =========================================================

USE educonnect;

-- Optional: clean reseed (uncomment if desired)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE `parent_student_mapping`;
-- TRUNCATE TABLE `audit`;
-- TRUNCATE TABLE `notification`;
-- TRUNCATE TABLE `student_document`;
-- TRUNCATE TABLE `enrollment`;
-- TRUNCATE TABLE `assessment`;
-- TRUNCATE TABLE `course_module`;
-- TRUNCATE TABLE `course`;
-- TRUNCATE TABLE `admin`;
-- TRUNCATE TABLE `teacher`;
-- TRUNCATE TABLE `student`;
-- TRUNCATE TABLE `parent`;
-- TRUNCATE TABLE `users`;
-- SET FOREIGN_KEY_CHECKS = 1;

START TRANSACTION;

-- =========================================================
-- 1) Base users (JOINED inheritance root)
--    Define UUIDs up-front so we can reuse them
-- =========================================================

-- Admin users
SET @u_admin_iyer  = UUID_TO_BIN(UUID());
SET @u_admin_menon = UUID_TO_BIN(UUID());

-- Teacher users
SET @u_teacher_meera = UUID_TO_BIN(UUID());
SET @u_teacher_arun  = UUID_TO_BIN(UUID());
SET @u_teacher_ravi  = UUID_TO_BIN(UUID());

-- Student users
SET @u_student_priya   = UUID_TO_BIN(UUID());
SET @u_student_vignesh = UUID_TO_BIN(UUID());
SET @u_student_sneha   = UUID_TO_BIN(UUID());
SET @u_student_rahul   = UUID_TO_BIN(UUID());
SET @u_student_ananya  = UUID_TO_BIN(UUID());

-- Parent users
SET @u_parent_sharma = UUID_TO_BIN(UUID());
SET @u_parent_nair   = UUID_TO_BIN(UUID());
SET @u_parent_rao    = UUID_TO_BIN(UUID());

INSERT INTO `users`
(`user_id`, `email`, `password`, `full_name`, `role`, `is_active`, `created_at`) VALUES
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
-- 2) Subclass tables (JOINED) - PK = FK to users.user_id
-- =========================================================

-- Admins
INSERT INTO `admin` (`admin_id`) VALUES
(@u_admin_iyer),
(@u_admin_menon);

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

-- Parent-Student mappings
-- INSERT INTO `parent_student_mapping` (`parent_id`, `student_id`) VALUES
-- (@u_parent_sharma, @u_student_priya),
-- (@u_parent_sharma, @u_student_sneha),
-- (@u_parent_nair,   @u_student_vignesh),
-- (@u_parent_nair,   @u_student_ananya),
-- (@u_parent_rao,    @u_student_rahul);

-- =========================================================
-- 3) Courses and modules
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
SET @mod_cs101_graphs = UUID_TO_BIN(UUID());
SET @mod_ec201_comb   = UUID_TO_BIN(UUID());
SET @mod_ec201_seq    = UUID_TO_BIN(UUID());
SET @mod_ma110_vec    = UUID_TO_BIN(UUID());
SET @mod_ma110_eigen  = UUID_TO_BIN(UUID());

INSERT INTO `course_module` (`module_id`, `title`, `content_url`, `sequence_order`, `course_id`) VALUES
(@mod_cs101_arrays, 'Arrays & Lists',      'https://cdn.edu/files/cs101/arrays.pdf',        1, @course_cs101),
(@mod_cs101_trees,  'Trees',               'https://cdn.edu/files/cs101/trees.pdf',         2, @course_cs101),
(@mod_cs101_graphs, 'Graphs',              'https://cdn.edu/files/cs101/graphs.pdf',        3, @course_cs101),
(@mod_ec201_comb,   'Combinational Logic', 'https://cdn.edu/files/ec201/comb_logic.pptx',   1, @course_ec201),
(@mod_ec201_seq,    'Sequential Logic',    'https://cdn.edu/files/ec201/seq_logic.pptx',    2, @course_ec201),
(@mod_ma110_vec,    'Vector Spaces',       'https://cdn.edu/files/ma110/vector_spaces.pdf', 1, @course_ma110),
(@mod_ma110_eigen,  'Eigen Analysis',      'https://cdn.edu/files/ma110/eigen.pdf',         2, @course_ma110);

-- =========================================================
-- 4) Assessments
-- =========================================================

SET @as_cs101_midterm = UUID_TO_BIN(UUID());
SET @as_cs101_quiz1   = UUID_TO_BIN(UUID());
SET @as_ec201_assign  = UUID_TO_BIN(UUID());
SET @as_ma110_final   = UUID_TO_BIN(UUID());

INSERT INTO `assessment` (`assessment_id`, `max_score`, `title`, `type`, `course_id`) VALUES
(@as_cs101_midterm, 100.0, 'CS101 Midterm',    'EXAM',       @course_cs101),
(@as_cs101_quiz1,    20.0, 'CS101 Quiz 1',     'QUIZ',       @course_cs101),
(@as_ec201_assign,   50.0, 'EC201 Assignment', 'ASSIGNMENT', @course_ec201),
(@as_ma110_final,   100.0, 'MA110 Final',      'EXAM',       @course_ma110);

-- =========================================================
-- 5) Enrollments (unique student_id + course_id)
-- =========================================================

SET @enr_priya_cs101   = UUID_TO_BIN(UUID());
SET @enr_priya_ma110   = UUID_TO_BIN(UUID());
SET @enr_vignesh_cs101 = UUID_TO_BIN(UUID());
SET @enr_vignesh_ec201 = UUID_TO_BIN(UUID());
SET @enr_sneha_ma110   = UUID_TO_BIN(UUID());
SET @enr_rahul_cs101   = UUID_TO_BIN(UUID());
SET @enr_ananya_ec201  = UUID_TO_BIN(UUID());

INSERT INTO `enrollment` (`enrollment_id`, `student_id`, `course_id`, `is_active`, `final_grade`) VALUES
(@enr_priya_cs101,   @u_student_priya,   @course_cs101, TRUE,  84.5),
(@enr_priya_ma110,   @u_student_priya,   @course_ma110, TRUE,  90.0),
(@enr_vignesh_cs101, @u_student_vignesh, @course_cs101, TRUE,  76.0),
(@enr_vignesh_ec201, @u_student_vignesh, @course_ec201, TRUE,  NULL),
(@enr_sneha_ma110,   @u_student_sneha,   @course_ma110, TRUE,  88.0),
(@enr_rahul_cs101,   @u_student_rahul,   @course_cs101, FALSE, 65.0),
(@enr_ananya_ec201,  @u_student_ananya,  @course_ec201, TRUE,  72.5);

-- =========================================================
-- 6) Student documents
-- =========================================================

SET @doc_priya_id     = UUID_TO_BIN(UUID());
SET @doc_priya_photo  = UUID_TO_BIN(UUID());
SET @doc_vignesh_tr   = UUID_TO_BIN(UUID());
SET @doc_sneha_ppt    = UUID_TO_BIN(UUID());
SET @doc_rahul_stmt   = UUID_TO_BIN(UUID());

-- INSERT INTO `student_document`
-- (`student_document_id`, `student_id`, `doc_type`, `fileuri`, `uploaded_date`, `verification_status`) VALUES
-- (@doc_priya_id,    @u_student_priya,   'PDF',   's3://educonnect/docs/ENR-2024-001/id-proof.pdf',     NOW(), 'VERIFIED'),
-- (@doc_priya_photo, @u_student_priya,   'IMAGE', 's3://educonnect/docs/ENR-2024-001/photo.jpg',         NOW(), 'VERIFIED'),
-- (@doc_vignesh_tr,  @u_student_vignesh, 'PDF',   's3://educonnect/docs/ENR-2024-002/transcript.pdf',    NOW(), 'UNVERIFIED'),
-- (@doc_sneha_ppt,   @u_student_sneha,   'PPT',   's3://educonnect/docs/ENR-2024-003/presentation.pptx', NOW(), 'REJECTED'),
-- (@doc_rahul_stmt,  @u_student_rahul,   'TEXT',  's3://educonnect/docs/ENR-2024-004/statement.txt',     NOW(), 'UNVERIFIED');
-- NOT NEEDED
-- =========================================================
-- 7) Notifications (to any user)
--    entity_id references related entity (course/enrollment/document)
-- =========================================================

SET @notif1 = UUID_TO_BIN(UUID());
SET @notif2 = UUID_TO_BIN(UUID());
SET @notif3 = UUID_TO_BIN(UUID());
SET @notif4 = UUID_TO_BIN(UUID());
SET @notif5 = UUID_TO_BIN(UUID());

INSERT INTO `notification`
(`notification_id`, `entity_id`, `message`, `category`, `status`, `user_id`) VALUES
(@notif1, @course_cs101,    'You have been assigned to teach CS101',    'GREEN', TRUE,  @u_teacher_meera),
(@notif2, @course_cs101,    'CS101 enrollment limit nearing capacity',  'AMBER', TRUE,  @u_teacher_meera),
(@notif3, @enr_priya_cs101, 'Grade published for CS101 Midterm',        'GREEN', TRUE,  @u_student_priya),
(@notif4, @doc_vignesh_tr,  'Document verification pending',            'AMBER', FALSE, @u_student_vignesh),
(@notif5, @course_ec201,    'Schedule conflict detected for EC201 lab', 'RED',   TRUE,  @u_teacher_arun);

-- =========================================================
-- 8) Audits (by admins)
-- =========================================================

SET @audit1 = UUID_TO_BIN(UUID());
SET @audit2 = UUID_TO_BIN(UUID());

INSERT INTO `audit` (`audit_id`, `scope`, `findings`, `date`, `status`, `admin_id`) VALUES
(@audit1, 'User Management', 'Password policy needs strengthening', '2025-12-15', FALSE, @u_admin_iyer),
(@audit2, 'Course Catalog',  'Redundant modules detected',          '2025-12-18', TRUE,  @u_admin_menon);

COMMIT;
