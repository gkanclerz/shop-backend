package pl.nullpointerexception.shop.admin.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nullpointerexception.shop.admin.category.model.AdminCategory;

@Repository
public interface AdminCategoryRepository extends JpaRepository<AdminCategory,Long> {

}
