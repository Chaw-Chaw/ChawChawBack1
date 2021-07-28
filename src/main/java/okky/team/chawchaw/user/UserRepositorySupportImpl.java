package okky.team.chawchaw.user;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.follow.QFollowEntity;
import okky.team.chawchaw.user.dto.FindUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserCardDto;
import okky.team.chawchaw.user.language.QLanguageEntity;
import okky.team.chawchaw.user.language.QUserHopeLanguageEntity;
import okky.team.chawchaw.user.language.QUserLanguageEntity;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositorySupportImpl implements UserRepositorySupport{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserCardDto> findAllByElement(FindUserVo findUserVo) {

        String languageVo = findUserVo.getLanguage();
        String hopeLanguageVo = findUserVo.getHopeLanguage();
        String name = findUserVo.getName();

        QUserEntity user = QUserEntity.userEntity;
        QUserLanguageEntity userLanguage = QUserLanguageEntity.userLanguageEntity;
        QUserHopeLanguageEntity userHopeLanguage = QUserHopeLanguageEntity.userHopeLanguageEntity;
        QLanguageEntity language = QLanguageEntity.languageEntity;
        QFollowEntity follow = QFollowEntity.followEntity;

        return jpaQueryFactory
                .select(Projections.constructor(
                        UserCardDto.class,
                        user.id,
                        user.imageUrl,
                        user.content,
                        user.repCountry,
                        user.repLanguage,
                        user.repHopeLanguage,
                        user.regDate,
                        user.views,
                        ExpressionUtils.as(
                                JPAExpressions.select(follow.count())
                                .from(follow)
                                .where(follow.userTo.eq(user)),
                                "follows"
                        )
                ))
                .from(user)
                .where(
                        !StringUtils.isEmpty(languageVo) ?
                                user.in(
                                        JPAExpressions
                                                .select(userLanguage.user)
                                                .from(userLanguage)
                                                .join(userLanguage.user, user)
                                                .join(userLanguage.language, language)
                                                .where(userLanguage.language.abbr.eq(languageVo))
                                ) : null,
                        !StringUtils.isEmpty(hopeLanguageVo) ?
                                user.in(
                                        JPAExpressions
                                                .select(userHopeLanguage.user)
                                                .from(userHopeLanguage)
                                                .join(userHopeLanguage.user, user)
                                                .join(userHopeLanguage.hopeLanguage, language)
                                                .where(userHopeLanguage.hopeLanguage.abbr.eq(hopeLanguageVo))
                                ) : null,
                        !StringUtils.isEmpty(name) ? user.name.eq(name) : null
                )
                .orderBy(NumberExpression.random().asc())
                .limit(3)
                .fetch();

    }

}
