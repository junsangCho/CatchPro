package junsang.cho.catchpro.reservation.infrastructure;

import jakarta.persistence.LockModeType;
import junsang.cho.catchpro.reservation.domain.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationSlotJpaRepository extends JpaRepository<ReservationSlot, Long> {

    // 🔥 핵심: 비관적 락(Pessimistic Write)을 걸어 조회하는 메서드
    // SELECT ... FOR UPDATE 쿼리가 날아가서, 트랜잭션이 끝날 때까지 다른 쓰레드는 이 슬롯을 건드릴 수 없습니다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ReservationSlot s WHERE s.id = :id")
    Optional<ReservationSlot> findByIdWithPessimisticLock(@Param("id") Long id);

    // 전문가의 특정 날짜 예약 가능한 슬롯 리스트 조회 (락 불필요, 단순 조회용)
    List<ReservationSlot> findAllByExpertProfileIdAndStartTimeBetweenOrderByStartTimeAsc(
            Long expertId, LocalDateTime start, LocalDateTime end
    );
}