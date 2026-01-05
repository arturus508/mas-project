CREATE TABLE user_section_visibility (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  section_key VARCHAR(100) NOT NULL,
  hidden BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_section (user_id, section_key),
  CONSTRAINT fk_user_section_user
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
    ON DELETE CASCADE
);
