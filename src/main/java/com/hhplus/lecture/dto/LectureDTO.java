package com.hhplus.lecture.dto;

import com.hhplus.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class LectureDTO {
  private Long id;
  private String name;        // 강의명
  private String lecturer;    // 강사명

  private LocalDateTime lectureTime;  // 강의일
  private int registeredCount;     // 현재 등록 학생 수
  private boolean isAvailable;        // 신청 가능 여부

  public static LectureDTO from(Lecture lecture) {
    int currentRegisteredCount = lecture.getRegistrations().size();

    return LectureDTO.builder()
      .id(lecture.getId())
      .name(lecture.getName())
      .lecturer(lecture.getLecturer())
      .lectureTime(lecture.getLectureTime())
      .registeredCount(currentRegisteredCount)
      .isAvailable(isRegistrationOpen(currentRegisteredCount))
      .build();
  }

  public static boolean isRegistrationOpen(int registeredCount) {
    return registeredCount < 30;
  }
}
