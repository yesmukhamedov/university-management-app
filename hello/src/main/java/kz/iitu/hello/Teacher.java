package kz.iitu.hello;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses;
}
