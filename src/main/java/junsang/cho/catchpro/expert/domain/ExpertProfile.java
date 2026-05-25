package junsang.cho.catchpro.expert.domain;

import jakarta.persistence.*;
import junsang.cho.catchpro.user.domain.User;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expert_profiles", indexes = {
        @Index(name = "idx_specialty", columnList = "specialty") // 조회 성능 최적화 인덱스
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String specialty;

    @Column(columnDefinition = "TEXT")
    private String bio; //전문가의 상세경력, 상담 안내 등

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    @Builder
    public ExpertProfile(User user, String specialty, String bio) {
        this.user = user;
        this.specialty = specialty;
        this.bio = bio;
        this.rating = BigDecimal.ZERO;
    }
}