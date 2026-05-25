package junsang.cho.catchpro.reservation.infrastructure;

import junsang.cho.catchpro.reservation.domain.ReservationSlot;
import junsang.cho.catchpro.reservation.domain.ReservationSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationSlotRepositoryImpl implements ReservationSlotRepository {

    private final ReservationSlotJpaRepository jpaRepository;

    @Override
    public void saveAll(List<ReservationSlot> slotsToCreate) {
        jpaRepository.saveAll(slotsToCreate);
    }
}
