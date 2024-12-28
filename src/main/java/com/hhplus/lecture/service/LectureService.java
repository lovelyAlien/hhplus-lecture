package com.hhplus.lecture.service;

import com.hhplus.lecture.dto.LectureDTO;
import com.hhplus.lecture.entity.Lecture;
import com.hhplus.lecture.entity.Registration;
import com.hhplus.lecture.repository.LectureRepository;
import com.hhplus.lecture.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

  private static final int MAX_PARTICIPANTS = 30;

  private final LectureRepository lectureRepository;
  private final RegistrationRepository registrationRepository;

  // 특강 신청
  public Registration registerForLecture(Long lectureId, Long userId) {

    // TODO: NotFoundException
    // 특강 존재 유무 체크
    Lecture lecture = lectureRepository.findByIdWithLock(lectureId)
      .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

    // TODO: PastLectureException
    // 지난 강의 체크
    if(lecture.getLectureTime().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("지난 강의는 신청할 수 없습니다.");
    }
    // TODO: DuplicateRegistrationException
    // 중복 신청 체크
    if(registrationRepository.existsByLectureAndUserId(lecture, userId)) {
      throw new RuntimeException("이미 신청한 강의입니다.");
    }

    // 정원 초과 체크
    if(registrationRepository.countByLecture(lecture) > MAX_PARTICIPANTS) {
      throw new RuntimeException("정원이 초과되었습니다.");
    }

    // 신청 처리
    Registration registration = new Registration(lecture, userId);
    return registrationRepository.save(registration);

  }

  // 특강 신청 가능 목록
  @Transactional(readOnly = true)
  public List<LectureDTO> getAvailableLectures(LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
    List<Lecture> lectures = lectureRepository.findLecturesByDate(startOfDay, endOfDay);

    return lectures.stream()
      .map(LectureDTO::from)
      .collect(Collectors.toList());

  };
  // 특강 신청 완료 목록
  @Transactional(readOnly = true)
  public List<LectureDTO> getUserRegistrations(Long userId) {
    List<Registration> registrations = registrationRepository.findByUserId(userId);
    return registrations.stream()
      .map(registration -> LectureDTO.from(registration.getLecture()))
      .collect(Collectors.toList());
  };
}
