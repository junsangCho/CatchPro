package junsang.cho.catchpro.reservation.domain;

import java.util.List;

public interface ReservationSlotRepository {
    void saveAll(List<ReservationSlot> slotsToCreate);
}
