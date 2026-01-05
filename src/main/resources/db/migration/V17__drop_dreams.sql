ALTER TABLE `dream_entry`
  DROP FOREIGN KEY `fk_dream_entry_sleep_entry`,
  DROP FOREIGN KEY `fk_dream_entry_user`;

DROP TABLE IF EXISTS `dream_entry`;
