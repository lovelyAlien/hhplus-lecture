package com.hhplus.lecture.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations",
  uniqueConstraints = @UniqueConstraint(columnNames = {"lecture_id", "user_id"}))
@Setter
@Getter
@NoArgsConstructor
public class Registration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "lecture_id")
  private Lecture lecture;

  @Column(name = "user_id")
  private Long userId;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt; // 생성 시간

  public Registration(Lecture lecture, Long userId) {
    this.lecture = lecture;
    this.userId = userId;
  }
}
