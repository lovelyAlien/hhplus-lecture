package com.hhplus.lecture.controller;

import com.hhplus.lecture.dto.LectureDTO;
import com.hhplus.lecture.entity.Registration;
import com.hhplus.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureController {

  private final LectureService lectureService;

  @PostMapping("/{lectureId}/register")
  public ResponseEntity<Registration> registerForLecture(
    @PathVariable Long lectureId,
    @RequestParam Long userId) {
    return ResponseEntity.ok(
      lectureService.registerForLecture(lectureId, userId));
  }

  @GetMapping("/available")
  public ResponseEntity<List<LectureDTO>> getAvailableLectures(
    @RequestParam LocalDate date
    ) {
    return ResponseEntity.ok(lectureService.getAvailableLectures(date));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<LectureDTO>> getUserRegistrations(
    @PathVariable Long userId) {
    return ResponseEntity.ok(lectureService.getUserRegistrations(userId));
  }

}
