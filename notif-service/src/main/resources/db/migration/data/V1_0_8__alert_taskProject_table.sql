ALTER TABLE tt_task
ADD COLUMN project_id BIGINT;

ALTER TABLE tt_task
ADD CONSTRAINT fk_task_project
FOREIGN KEY (project_id)
REFERENCES tt_projects(id);
