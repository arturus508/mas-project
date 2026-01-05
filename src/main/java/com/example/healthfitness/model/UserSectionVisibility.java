package com.example.healthfitness.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_section_visibility",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "section_key"}))
public class UserSectionVisibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "section_key", nullable = false, length = 100)
    private String sectionKey;

    @Column(nullable = false)
    private boolean hidden = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSectionKey() { return sectionKey; }
    public void setSectionKey(String sectionKey) { this.sectionKey = sectionKey; }

    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
}
