package junsang.cho.catchpro.reservation.domain;


import jakarta.persistence.*;
import junsang.cho.catchpro.expert.domain.ExpertProfile;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_slots", uniqueConstraints = {
        // 특정 전문가의 예약 시간대가 겹치지 않도록 방어
        @UniqueConstraint(name = "uk_expert_start_time", columnNames = {"expert_id", "start_time"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false)
    private ExpertProfile expertProfile;

    @Column(nullable = false)
    private LocalDateTime startTime; // 시작 시간 (예: 14:00)

    @Column(nullable = false)
    private LocalDateTime endTime;   // 종료 시간 (예: 14:30)

    @Column(nullable = false)
    private Integer capacity;        // 최대 정원 (1:1이면 1)

    @Column(nullable = false)
    private Integer reservedCount;   // 현재 예약된 인원

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SlotStatus status;       // AVAILABLE, FULL, CLOSED

    public enum SlotStatus {
        AVAILABLE, FULL, CLOSED
    }

    @Builder
    public ReservationSlot(ExpertProfile expertProfile, LocalDateTime startTime, LocalDateTime endTime, Integer capacity) {
        this.expertProfile = expertProfile;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity != null ? capacity : 1;
        this.reservedCount = 0;
        this.status = SlotStatus.AVAILABLE;
    }

    // 비즈니스 로직: 예약 인원 증가 및 상태 변경
    public void addReservation() {
        if (this.status != SlotStatus.AVAILABLE || this.reservedCount >= this.capacity) {
            throw new IllegalStateException("해당 예약 시간은 이미 마감되었거나 예약할 수 없습니다.");
        }
        this.reservedCount++;

        // 정원이 꽉 차면 자동으로 상태를 FULL로 변경
        if (this.reservedCount.equals(this.capacity)) {
            this.status = SlotStatus.FULL;
        }
    }
}