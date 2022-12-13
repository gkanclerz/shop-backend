package pl.nullpointerexception.shop.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nullpointerexception.shop.common.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findBySlug(String slug);
}
