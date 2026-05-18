package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<LessonPackage, Long> {
}