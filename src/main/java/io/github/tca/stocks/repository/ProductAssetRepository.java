package io.github.tca.stocks.repository;

import io.github.tca.stocks.domain.ProductAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductAsset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, Long> {

}
