package junsang.cho.catchpro.reservation.domain;

import jakarta.persistence.*;
import junsang.cho.catchpro.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id") // 내 예약 조회용 인덱스
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private ReservationSlot reservationSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Builder
    public Reservation(User user, ReservationSlot reservationSlot) {
        this.user = user;
        this.reservationSlot = reservationSlot;
        this.status = ReservationStatus.CONFIRMED;
    }
}