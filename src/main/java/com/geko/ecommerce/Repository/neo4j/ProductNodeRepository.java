package com.geko.ecommerce.Repository.neo4j;

import com.geko.ecommerce.Entity.ProductNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductNodeRepository extends Neo4jRepository<ProductNode, String> {
    Optional<ProductNode> findByName(String name);

    @Query("MATCH (u:User)-[r:BOUGHT]->(p:Product) WHERE id(p) = $productId " +
            "SET u.productList = [x IN u.productList WHERE x <> $productId] " +
            "DELETE r, p")
    void deleteProductAndRelationships(String productId);
}
