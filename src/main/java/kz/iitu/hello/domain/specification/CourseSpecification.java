package kz.iitu.hello.domain.specification;

import kz.iitu.hello.domain.entity.Course;
import org.springframework.data.jpa.domain.Specification;

public final class CourseSpecification {

    private CourseSpecification() {
    }

    public static Specification<Course> withFilters(String name, Integer minCredits, Integer maxCredits, Long teacherId) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("courseName")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (minCredits != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("credits"), minCredits));
            }

            if (maxCredits != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("credits"), maxCredits));
            }

            if (teacherId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("teacher").get("id"), teacherId));
            }

            return predicate;
        };
    }
}
