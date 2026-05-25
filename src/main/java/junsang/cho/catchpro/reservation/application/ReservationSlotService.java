package junsang.cho.catchpro.reservation.application;

import junsang.cho.catchpro.expert.domain.ExpertProfile;
import junsang.cho.catchpro.reservation.domain.ReservationSlot;
import junsang.cho.catchpro.reservation.domain.ReservationSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationSlotService {

    private final ReservationSlotRepository reservationSlotRepository;

    /**
     * 전문가의 일정 지정을 받아 슬롯을 자동 분할하여 생성하는 메서드
     * @param expertProfile 전문가 프로필
     * @param openTime      운영 시작 일시 (예: 2026-06-01 14:00)
     * @param closeTime     운영 종료 일시 (예: 2026-06-01 16:00)
     * @param intervalMinutes 분할할 간격 (예: 30분)
     */
    @Transactional
    public void generateReservationSlots(ExpertProfile expertProfile,
                                         LocalDateTime openTime,
                                         LocalDateTime closeTime,
                                         int intervalMinutes) {

        List<ReservationSlot> slotsToCreate = new ArrayList<>();

        LocalDateTime currentStart = openTime;

        // 🔥 핵심 루프: 시작 시간이 종료 시간보다 전인 동안 간격만큼 더하며 슬롯을 생성
        while (currentStart.isBefore(closeTime)) {
            LocalDateTime currentEnd = currentStart.plusMinutes(intervalMinutes);

            // 만약 더한 종료 시간이 총 운영 종료 시간을 초과하면 슬롯 생성을 중단 (자투리 시간 버림)
            if (currentEnd.isAfter(closeTime)) {
                break;
            }

            // 빌더를 통해 고도화된 Slot 엔티티 생성
            ReservationSlot slot = ReservationSlot.builder()
                    .expertProfile(expertProfile)
                    .startTime(currentStart)
                    .endTime(currentEnd)
                    .capacity(1) // 1:1 예약 기본값
                    .build();

            slotsToCreate.add(slot);

            // 다음 슬롯의 시작 시간은 현재 슬롯의 종료 시간이 됨
            currentStart = currentEnd;
        }

        // 데이터베이스에 벌크 인서트(Bulk Insert) 형태로 한 번에 저장
        reservationSlotRepository.saveAll(slotsToCreate);
    }
}