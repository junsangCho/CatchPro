package junsang.cho.catchpro.user.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junsang.cho.catchpro.user.domain.repository.UserRepositoryCustom;
import junsang.cho.catchpro.user.domain.repository.projection.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Supplier;

import static junsang.cho.catchpro.user.domain.QUser.user;


@RequiredArgsConstructor
@Repository
public class UserJpaRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<UserInfo> getUser(String email) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.fields(UserInfo.class,
                        user.id,
                        user.email,
                        user.password,
                        user.role,
                        user.status
                        ))
                .from(user)
                .where(nullSafeBooleanBuilder(()->user.email.eq(email)))
                .fetchOne());
    }

    private BooleanBuilder nullSafeBooleanBuilder(Supplier<BooleanExpression> supplier) {
        try {
            return new BooleanBuilder(supplier.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }
}
