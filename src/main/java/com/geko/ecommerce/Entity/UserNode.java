package com.geko.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNode {
    @Id
    private String username;

    @Relationship(type = "BOUGHT", direction = Relationship.Direction.OUTGOING)
    private List<ProductNode> boughtProducts;
}
