package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.model.BodyStats;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.BodyStatsRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.BodyStatsForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BodyStatsServiceTest {

    @Autowired private BodyStatsService bodyStatsService;
    @Autowired private BodyStatsRepository bodyStatsRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void addBodyStatsToUserPersistsEntry() {
        User user = new User();
        user.setName("Body User");
        user.setEmail("body@local.test");
        user.setPassword("secret");
        user = userRepository.save(user);

        BodyStatsForm form = new BodyStatsForm();
        form.setDateRecorded(LocalDate.now());
        form.setWeight(80.0);
        form.setBodyFatPercent(15.0);

        bodyStatsService.addBodyStatsToUser(user.getUserId(), form);

        var results = bodyStatsRepository.findByUserUserIdOrderByDateRecordedDesc(user.getUserId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getWeight()).isEqualTo(80.0);
    }

    @Test
    void deleteBodyStatsForOtherUserThrowsForbidden() {
        User user1 = new User();
        user1.setName("Body User 1");
        user1.setEmail("body1@local.test");
        user1.setPassword("secret");
        user1 = userRepository.save(user1);
        final Long user1Id = user1.getUserId();

        User user2 = new User();
        user2.setName("Body User 2");
        user2.setEmail("body2@local.test");
        user2.setPassword("secret");
        user2 = userRepository.save(user2);

        BodyStats stats = new BodyStats(LocalDate.now(), 70, 12);
        stats.setUser(user2);
        stats = bodyStatsRepository.save(stats);
        final Long statsId = stats.getBodyStatsId();

        assertThatThrownBy(() -> bodyStatsService.deleteBodyStats(user1Id, statsId))
                .isInstanceOf(ForbiddenException.class);
    }
}
