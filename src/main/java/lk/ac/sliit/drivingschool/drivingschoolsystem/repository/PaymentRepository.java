package lk.ac.sliit.drivingschool.drivingschoolsystem.repository;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Derived query method to fetch payment history for a specific student cleanly
    List<Payment> findByStudent_Id(Long studentId);
}