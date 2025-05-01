ALTER TABLE tt_task
ADD COLUMN historical_id BIGINT;

ALTER TABLE tt_task
ADD CONSTRAINT fk_task_history
FOREIGN KEY (historical_id)
REFERENCES tt_task_history(id);
