package com.hhplus.lecture;

import com.hhplus.lecture.dto.LectureDTO;
import com.hhplus.lecture.entity.Registration;
import com.hhplus.lecture.service.LectureService;
import com.hhplus.lecture.entity.Lecture;
import com.hhplus.lecture.repository.LectureRepository;
import com.hhplus.lecture.repository.RegistrationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureServiceUnitTest {

  @Mock
  private LectureRepository lectureRepository;

  @Mock
  private RegistrationRepository registrationRepository;

  @InjectMocks
  private LectureService lectureService;

  @Test
  @DisplayName("특강 신청 성공")
  public void 특강_신청_성공() {
    // Given
    Long lectureId = 1L;
    Long userId = 1L;
    Lecture lecture = createTestLecture(lectureId);
    Registration expectedRegistration = new Registration();

    when(lectureRepository.findByIdWithLock(lectureId))
      .thenReturn(Optional.of(lecture));
    when(registrationRepository.existsByLectureAndUserId(lecture, userId))
      .thenReturn(false);
    when(registrationRepository.save(any(Registration.class)))
      .thenReturn(expectedRegistration);

    // When
    Registration result = lectureService.registerForLecture(lectureId, userId);

    // Then
    assertThat(result).isNotNull();
    verify(lectureRepository).findByIdWithLock(lectureId);
    verify(registrationRepository).existsByLectureAndUserId(lecture, userId);
    verify(registrationRepository).save(any(Registration.class));
  }

  @Test
  @DisplayName("존재하지 않는 특강 신청 시 예외 발생")
  public void 특강_존재_않음() {
    // Given
    Long lectureId = 1L;
    Long userId = 1L;

    when(lectureRepository.findByIdWithLock(lectureId)).
      thenReturn(Optional.empty());

    // When & Then
    // TODO: NotFoundException
    assertThrows(RuntimeException.class, () -> {
      lectureService.registerForLecture(lectureId, userId);
    });

    verify(lectureRepository).findByIdWithLock(lectureId);
    verify(registrationRepository, never()).save(any(Registration.class));
  }

  @Test
  @DisplayName("중복 신청 시 예외 발생")
  public void 특강_중복_신청() {

    // Given
    Long userId = 1L;
    Long lectureId = 1L;
    Lecture lecture = createTestLecture(lectureId);

    when(lectureRepository.findByIdWithLock(lectureId)).
      thenReturn(Optional.of(lecture));
    when(registrationRepository.existsByLectureAndUserId(lecture, userId))
      .thenReturn(true);

    // When & Then
    // TODO: DuplicationRegisterException
    assertThrows(RuntimeException.class, () -> {
      lectureService.registerForLecture(lectureId, userId);
    });
  }

  @Test
  @DisplayName("정원 초과 시 예외 발생")
  public void 특강_정원_초과() {
    // Given
    Long userId = 1L;
    Long lectureId = 1L;
    Lecture lecture = createTestLecture(lectureId);

    when(lectureRepository.findByIdWithLock(lectureId)).
      thenReturn(Optional.of(lecture));
    when(registrationRepository.existsByLectureAndUserId(lecture, userId))
      .thenReturn(false);
    when(registrationRepository.countByLecture(lecture))
      .thenReturn(31);

    // When & Then
    // TODO: FullCapacityException
    assertThrows(RuntimeException.class, () -> {
      lectureService.registerForLecture(lectureId, userId);
    });
  }

  @Test
  @DisplayName("유저 별 신청 완료된 특강 목록 조회")
  public void 유저_신청_완료_특강_목록() {
    // Given
    Long userId = 1L;
    List<Registration> registrations = createTestRegistrations(userId);

    when(registrationRepository.findByUserId(userId))
      .thenReturn(registrations);

    // When
    List<LectureDTO> result = lectureService.getUserRegistrations(userId);

    // Then
    assertThat(result).hasSize(registrations.size());
    verify(registrationRepository).findByUserId(userId);
  }

  private Lecture createTestLecture(Long id) {
    Lecture lecture = new Lecture();
    lecture.setId(id);
    lecture.setName("테스트 특강");
    lecture.setLecturer("강사명");
    return lecture;
  }

  private List<Registration> createTestRegistrations(Long userId) {
    List<Registration> registrations = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Registration reg = new Registration();
      reg.setLecture(createTestLecture((long) i));
      reg.setUserId(userId);
      registrations.add(reg);
    }
    return registrations;
  }

}
