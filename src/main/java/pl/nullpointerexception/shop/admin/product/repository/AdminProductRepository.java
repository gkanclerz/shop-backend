package pl.nullpointerexception.shop.admin.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nullpointerexception.shop.admin.product.model.AdminProduct;

@Repository
public interface AdminProductRepository extends JpaRepository<AdminProduct,Long> {
}
