INSERT INTO public.tt_role(
	id,deleted, name)
	VALUES 	(1,false,'ADMIN'),
			(2,false,'MANAGER'),
			(3,false,'EMPLOYEE'),
			(4,false,'TEAMLEAD'),
			(5,false,'PROJECT_OWNER');

INSERT INTO public.tt_permission(
	 id,deleted, etat)
	VALUES
			(1, false, 'ADD_PROJECT'),
			(2, false, 'EDIT_PROJECT'),
			(3, false, 'DELETE_PROJECT'),
			(4, false, 'VIEW_PROJECT'),
			(5, false, 'ADD_SPRINT'),
   		 	(6, false, 'EDIT_SPRINT'),
    		(7, false, 'DELETE_SPRINT'),
    		(8, false, 'VIEW_SPRINT'),
    		(9, false, 'ADD_TASK'),
    		(10, false, 'EDIT_TASK'),
    		(11, false, 'DELETE_TASK'),
    		(12, false, 'VIEW_TASK'),
    		(13, false, 'ADD_TEAM'),
		    (14, false, 'EDIT_TEAM'),
		    (15, false, 'DELETE_TEAM'),
		    (16, false, 'VIEW_TEAM'),
		    (17, false, 'ADD_COMMENT'),
		    (18, false, 'EDIT_COMMENT'),
		    (19, false, 'DELETE_COMMENT'),
		    (20, false, 'VIEW_COMMENT'),
			(25, false, 'ADD_USER'),
		    (26, false, 'EDIT_USER'),
		    (27, false, 'DELETE_USER'),
		    (28, false, 'VIEW_USER'),
			(29, false, 'LIST_PROJECT'),
			(30, false, 'LIST_SPRINT'),
			(31, false, 'LIST_TEAM'),
			(32, false, 'LIST_TASK'),
			(33, false, 'LIST_USER'),
			(34, false, 'LIST_COMMENT');

INSERT INTO public.tt_role_permission(
	permission_id, role_id)
	VALUES 	(1,1),(2,1),(3,1),(4,1),(29,1),(1,2),(2,2),(3,2),(4,2),(29,2),(4,3),(4,4),(4,5),(29,3),(29,4),(29,5),
			(5,1),(5,2),(6,1),(6,2),(7,1),(7,2),(8,1),(8,2),(30,1),(30,2),(6,3),(8,3),(30,3),(6,4),(8,4),(30,4),(6,5),(8,5),(30,5),
			(9,1),(10,1),(11,1),(12,1),(32,1),(9,2),(10,2),(11,2),(12,2),(32,2),(10,3),(12,3),(32,3),(10,4),(12,4),(32,4),(10,5),(12,5),(32,5),
			(13,1),(14,1),(15,1),(16,1),(31,1),(13,2),(14,2),(15,2),(16,2),(31,2),(16,3),(16,4),(16,5),(31,3),(31,4),(31,5),
			(17,1),(18,1),(19,1),(20,1),(34,1),
			(18,2),(17,2),(19,2),(20,2),(34,2),
			(17,3),(18,3),(19,3),(20,3),(34,3),
			(17,4),(18,4),(19,4),(20,4),(34,4),
			(17,4),(18,4),(19,4),(20,4),(34,4),
			(25,1),(25,2),(25,3),(25,4),(25,5),(26,1),(26,2),(26,3),(26,4),(26,5),(27,1),(28,2),(28,3),(28,4),(28,5),(28,1),(33,1)
;
INSERT INTO public.tt_speciality(id, deleted, name)
VALUES
(1, false, 'Développement Backend'),
(2, false, 'Développement Frontend'),
(3, false, 'Test QA'),
(4, false, 'Gestion de projet'),
(5, false, 'Développement Mobile'),
(6, false, 'DevOps'),
(7, false, 'Design UX/UI'),
(8, false, 'Data Science'),
(9, false, 'Sécurité informatique'),
(10, false, 'Support technique');

INSERT INTO public.tt_user(
	id, deleted, email, name, password, phone, pre_name, role_id)
	VALUES
			(1,false, 'NourADMIN@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','ADMIN', 1 ),
			(2,false, 'NourManager@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','Manager', 2 ),
			(3,false, 'NourDevMobile@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','DEV', 3 ),
			(4,false, 'NourTL@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','TEAMLEAD', 4 ),
			(5,false, 'NourPO@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','PROJECT_OWNER', 5 ),
			(6,false, 'NourTest@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','DEV', 3 ),
			(7,false, 'NourDevWeb@gmail.com','Nour','$2a$10$k4eHRv8NtElEQlqqRtRCD.mMVhh0YOZXv/u/8OtfAQOMJR63F0FBm', '28462286','DEV', 3 )
;
INSERT INTO public.tt_user_speciality(
	speciality_id, user_id)
	VALUES (1, 4),(1,10),
	        (2,4),(2,10),
	        (3,5),(3,6),
	        (4,9),
	        (5,4),
	        (6,3),(6,2),
	        (7,2),(7,1),(7,6);
INSERT INTO public.tt_team(
	id, deleted, name,speciality_id)
	VALUES
			(1,false,'TeamWeb',1),
	 		(2,false,'TeamMobile',5);

INSERT INTO public.tt_team_user(
	team_id, user_id)
	VALUES
			(1,3),(1,4),(1,5),(1,6),
			(2,7),(2,4),(2,5),(2,6);

INSERT INTO public.tt_projects(
	id , deleted, description, end_date, name, start_date,team_id)
	VALUES
		(1,false, 'App Web Spring Boot Angular','2024-07-18T15:16:12.972571', 'Application WEB','2024-07-18T15:16:12.972571',2),
		(2,false, 'App Mobile flutter ','2024-07-18T15:16:12.972571', 'Application Mobile','2024-07-18T15:16:12.972571',1);

INSERT INTO public.tt_sprint (id, deleted, description, end_date, name, start_date, project_id)
VALUES
    (1, false, 'Sprint 1 for Web App', '2024-08-30', 'Sprint 1', '2024-07-20', 1),
    (2, false, 'Sprint 1 for Mobile App', '2024-08-30', 'Sprint 1', '2024-07-20', 2);

INSERT INTO public.tt_task (id, deleted, description, end_date, name, start_date, sprint_id, user_id)
VALUES
    (1, false, 'Task 1 for Sprint 1 Web', '2024-08-25', 'Task 1', '2024-07-21', 1, 7),
    (2, false, 'Task 2 for Sprint 1 Web', '2024-08-25', 'Task 2', '2024-07-21', 1, 6),
    (3, false, 'Task 1 for Sprint 1 Mobile', '2024-08-25', 'Task 1', '2024-07-21', 2, 3),
    (4, false, 'Task 2 for Sprint 1 Mobile', '2024-08-25', 'Task 2', '2024-07-21', 2, 6);