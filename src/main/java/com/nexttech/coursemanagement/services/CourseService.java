package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.CourseResponseDTO;
import com.nexttech.coursemanagement.DTOs.CurriculumCreationDTO;
import com.nexttech.coursemanagement.DTOs.LessonDTO;
import com.nexttech.coursemanagement.mappers.CourseMapper;
import com.nexttech.coursemanagement.mappers.LessonMapper;
import com.nexttech.coursemanagement.models.Course;
import com.nexttech.coursemanagement.models.Curriculum;
import com.nexttech.coursemanagement.models.Lesson;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.CourseRepo;
import com.nexttech.coursemanagement.repositories.CurriculumRepo;
import com.nexttech.coursemanagement.repositories.LessonRepo;
import com.nexttech.coursemanagement.repositories.UserRepo;
import com.nexttech.coursemanagement.util.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Slf4j
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CurriculumRepo curriculumRepo;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired @Lazy
    private LessonService lessonService;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired @Lazy
    private AttendanceService attendanceService;

    public CourseResponseDTO addCourse(String name, String imgUrl, String username) throws BadRequestException {
        try {
            Assert.hasLength(name, "Please provide course name.");
            Course existingCourse = courseRepo.findByCourseName(name);
            User existingUser = userRepo.findByUserName(username).orElseThrow();
            if (existingCourse == null && existingUser != null) {
                Course newCourse = new Course(name, imgUrl, existingUser);
                courseRepo.save(newCourse);
                return courseMapper.toCreateCourseDto(newCourse);
            } else {
                throw new BadRequestException("Course already exists.");
            }
        }
        catch (NoSuchElementException exception) {
            log.error("User not found");
            throw exception;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(BadRequestException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public CourseResponseDTO addCourseCurriculum(CurriculumCreationDTO curriculumCreationDTO) {
        try {
            List<LessonDTO> lessonDTOList = new ArrayList<>();
            Course course = getCourse(curriculumCreationDTO.getCourseId());
            Assert.notNull(course, "Course not found.");
            List<Long> lessonIdList = curriculumCreationDTO.getLessonIdList();
            lessonIdList.forEach(lessonId -> {
                Lesson lesson = lessonService.getLesson(lessonId);
                Assert.notNull(lesson, "Lesson id not ok: " + lessonId);
                Curriculum existingCurriculum = curriculumRepo.findByCourse_IdAndLesson_Id(curriculumCreationDTO.getCourseId(), lessonId);
                if (existingCurriculum == null) {
                    Curriculum newCurriculum = new Curriculum(course, lesson);
                    curriculumRepo.save(newCurriculum);
                }
            });
            //Return the complete lesson list for the course - including existing ones
            curriculumRepo.findAllByCourse_Id(curriculumCreationDTO.getCourseId()).forEach(curriculum -> {
                lessonDTOList.add(lessonMapper.toDtoNoAttendance(lessonRepo.findById(curriculum.getLesson().getId()).get()));
            });
            return new CourseResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), lessonDTOList);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public Course getCourse(Long id) {
        try {
            Optional<Course> course = courseRepo.findById(id);
            Assert.isTrue(course.isPresent(), "Course not found.");
            return course.get();
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public CourseResponseDTO getCourseResponse(Long id) {
        try {
            Optional<Course> course = courseRepo.findById(id);
            Assert.isTrue(course.isPresent(), "Course not found.");
            return new CourseResponseDTO(course.get().getId(), course.get().getCourseName(), course.get().getImageUrl(), getLessonsByCourseId(course.get().getId()));
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public List<CourseResponseDTO> getCourses() {
        List<CourseResponseDTO> courseListResponse = new ArrayList<>();
        //TODO: Warning:(53, 54) Iteration can be replaced with bulk 'Collection.addAll()' call
//        Collections.addAll(courses, courseService.getCourses()); -> needs individual elements...?
        courseRepo.findAll().forEach(course -> courseListResponse.add(new CourseResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), getLessonsByCourseId(course.getId()))));
        return courseListResponse;
    }

    public List<CourseResponseDTO> getCoursesBySearchTerm(Optional<String> searchTerm) {
        if (searchTerm.isEmpty()) {
            return this.getCourses();
        }
        List<CourseResponseDTO> courseDTOList = new ArrayList<>();
        courseRepo.findByCourseNameContainingIgnoreCase(searchTerm.get()).forEach(course -> courseDTOList.add(new CourseResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), getLessonsByCourseId(course.getId()))));
        return courseDTOList;
    }

    public void deleteCourse(Long id) {
        try {
            Optional<Course> course = courseRepo.findById(id);
            Assert.isTrue(course.isPresent(), "Course not found.");
            deleteCourseCurriculaAndEnrollments(course.get().getId());
            courseRepo.delete(course.get());
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public CourseResponseDTO deleteLessonFromCourse(Long courseId, Long lessonId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            Assert.notNull(lessonId, "Lesson id cannot be null.");
            //TODO: also delete homework for this curriculum for all users enrolled to course
            Curriculum curriculum = curriculumRepo.findByCourse_IdAndLesson_Id(courseId, lessonId);
            deleteCurriculumAndAttendances(curriculum);
            List<LessonDTO> lessonDTOList = new ArrayList<>();
            curriculumRepo.findAllByCourse_Id(courseId).forEach(curriculumInCourse -> {
                lessonDTOList.add(lessonMapper.toDtoNoAttendance(lessonRepo.findById(curriculumInCourse.getLesson().getId()).get()));
            });
            Course course = courseRepo.findById(courseId).get();
            Assert.notNull(course, "Course doesn't exist.");
            return new CourseResponseDTO(course.getId(), course.getCourseName(), course.getImageUrl(), lessonDTOList);
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }
    public void deleteCourseCurriculaAndEnrollments(Long courseId) {
        try {
            Assert.notNull(courseId, "Course id cannot be null.");
            Optional<Course> course = courseRepo.findById(courseId);
            Assert.notNull(course.get());
            Set<User> enrolledUsers = course.get().getUsers();
            //https://www.baeldung.com/java-concurrentmodificationexception
            List<User> toRemove = new ArrayList<>();
            enrolledUsers.forEach(user -> toRemove.add(user));
            toRemove.forEach(user -> user.disenrollFromCourse(course.get()));
            curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> {
                deleteCurriculumAndAttendances(curriculum);
            });
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }

    public void deleteCurriculumAndAttendances(Curriculum curriculum) {
        attendanceService.removeAttendances(curriculum.getId());
        curriculumRepo.delete(curriculum);
    }

    private List<LessonDTO> getLessonsByCourseId(Long courseId) {
        try {
            Assert.notNull(courseId, "CourseId cannot be null.");
            List<LessonDTO> lessonDTOList = new ArrayList<>();
            curriculumRepo.findAllDistinctByCourse_Id(courseId).forEach(curriculum -> lessonDTOList.add(lessonMapper.toDtoNoAttendance(curriculum.getLesson())));
            return lessonDTOList;
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
    }
}
