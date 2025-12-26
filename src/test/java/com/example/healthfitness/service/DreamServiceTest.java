package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.model.DreamEntry;
import com.example.healthfitness.model.SleepEntry;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.DreamEntryRepository;
import com.example.healthfitness.repository.SleepEntryRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.DreamForm;
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
class DreamServiceTest {

    @Autowired private DreamService dreamService;
    @Autowired private UserRepository userRepository;
    @Autowired private SleepEntryRepository sleepEntryRepository;
    @Autowired private DreamEntryRepository dreamEntryRepository;

    @Test
    void createBindsSleepEntryWhenOwned() {
        User user = saveUser("owner@local.test");
        SleepEntry sleep = new SleepEntry();
        sleep.setUser(user);
        sleep.setDate(LocalDate.now());
        sleep.setSleepStart(LocalDateTime.now().minusHours(7));
        sleep.setSleepEnd(LocalDateTime.now());
        sleep.setQuality(4);
        sleep = sleepEntryRepository.save(sleep);

        DreamForm form = new DreamForm();
        form.setDate(LocalDate.now());
        form.setTitle("Dream");
        form.setContent("Details");
        form.setSleepEntryId(sleep.getId());

        DreamEntry created = dreamService.create(user.getUserId(), form);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(created.getSleepEntry().getId()).isEqualTo(sleep.getId());
    }

    @Test
    void createRejectsSleepEntryFromAnotherUser() {
        User owner = saveUser("owner2@local.test");
        User other = saveUser("other@local.test");

        SleepEntry sleep = new SleepEntry();
        sleep.setUser(other);
        sleep.setDate(LocalDate.now());
        sleep.setSleepStart(LocalDateTime.now().minusHours(6));
        sleep.setSleepEnd(LocalDateTime.now());
        sleep.setQuality(3);
        sleep = sleepEntryRepository.save(sleep);

        DreamForm form = new DreamForm();
        form.setDate(LocalDate.now());
        form.setTitle("Dream");
        form.setContent("Details");
        form.setSleepEntryId(sleep.getId());

        assertThatThrownBy(() -> dreamService.create(owner.getUserId(), form))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void deleteRejectsOtherUsersDream() {
        User owner = saveUser("owner3@local.test");
        User other = saveUser("other3@local.test");

        DreamForm form = new DreamForm();
        form.setDate(LocalDate.now());
        form.setTitle("Dream");
        form.setContent("Details");
        DreamEntry created = dreamService.create(owner.getUserId(), form);

        assertThatThrownBy(() -> dreamService.delete(other.getUserId(), created.getId()))
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
