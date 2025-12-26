package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.SleepEntryRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.SleepForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SleepServiceTest {

    @Autowired private SleepService sleepService;
    @Autowired private UserRepository userRepository;
    @Autowired private SleepEntryRepository sleepEntryRepository;

    @Test
    void createBuildsEntryFromForm() {
        User user = saveUser("sleep@local.test");

        SleepForm form = new SleepForm();
        form.setDate(LocalDate.of(2025, 1, 2));
        form.setHours(7);
        form.setMinutes(30);
        form.setQuality(4);
        form.setNote("ok");

        SleepEntry entry = sleepService.create(user.getUserId(), form);

        assertThat(entry.getDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(entry.getSleepStart()).isEqualTo(LocalDateTime.of(2025, 1, 2, 22, 0));
        assertThat(entry.getSleepEnd()).isEqualTo(LocalDateTime.of(2025, 1, 3, 5, 30));
        assertThat(entry.getQuality()).isEqualTo(4);
    }

    @Test
    void durationMinutesHandlesOvernightSleep() {
        SleepEntry entry = new SleepEntry();
        entry.setSleepStart(LocalDateTime.of(2025, 1, 2, 23, 0));
        entry.setSleepEnd(LocalDateTime.of(2025, 1, 2, 7, 0));

        long minutes = sleepService.durationMinutes(entry);

        assertThat(minutes).isEqualTo(8 * 60);
    }

    @Test
    void deleteRejectsOtherUsersEntry() {
        User owner = saveUser("sleep-owner@local.test");
        User other = saveUser("sleep-other@local.test");

        SleepEntry entry = new SleepEntry();
        entry.setUser(owner);
        entry.setDate(LocalDate.now());
        entry.setSleepStart(LocalDateTime.now().minusHours(7));
        entry.setSleepEnd(LocalDateTime.now());
        entry.setQuality(3);
        SleepEntry savedEntry = sleepEntryRepository.save(entry);

        assertThatThrownBy(() -> sleepService.delete(other.getUserId(), savedEntry.getId()))
                .isInstanceOf(ForbiddenException.class);
    }

    private User saveUser(String email) {
        User user = new User();
        user.setName("User");
        user.setEmail(email);
        user.setPassword("secret");
        return userRepository.save(user);
    }
}
