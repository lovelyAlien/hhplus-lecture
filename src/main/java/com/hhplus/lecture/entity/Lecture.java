package com.hhplus.lecture.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lectures")
@Setter
@Getter
public class Lecture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String lecturer;

  @Column(name = "lecture_time", nullable = false)
  private LocalDateTime lectureTime; // 특강 날짜를 나타내는 필드

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt; // 생성 시간

  @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
  private Set<Registration> registrations = new HashSet<>();
}
