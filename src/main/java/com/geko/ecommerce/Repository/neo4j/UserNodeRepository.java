package com.geko.ecommerce.Repository.neo4j;

import com.geko.ecommerce.Entity.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {
    Optional<UserNode> findByUsername(String username);

    @Query("MATCH (u:UserNode)-[r:BOUGHT]->(p:ProductNode) WHERE u.username = $username DELETE r, u")
    void deleteUserAndRelationships(String username);
}