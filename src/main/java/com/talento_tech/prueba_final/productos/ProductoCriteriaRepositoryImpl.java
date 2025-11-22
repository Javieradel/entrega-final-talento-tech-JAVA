package com.talento_tech.prueba_final.productos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoCriteriaRepositoryImpl implements ProductoCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ProductoCriteriaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public List<Producto> findAll(ProductoSearchCriteria searchCriteria) {
        CriteriaQuery<Producto> criteriaQuery = criteriaBuilder.createQuery(Producto.class);
        Root<Producto> productoRoot = criteriaQuery.from(Producto.class);
        Predicate predicate = getPredicate(searchCriteria, productoRoot);
        criteriaQuery.where(predicate);
        TypedQuery<Producto> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private Predicate getPredicate(ProductoSearchCriteria searchCriteria, Root<Producto> productoRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.getNombre() != null && !searchCriteria.getNombre().isEmpty()) {
            predicates.add(criteriaBuilder.like(productoRoot.get("nombre"), "%" + searchCriteria.getNombre() + "%"));
        }

        if (searchCriteria.getDescripcion() != null && !searchCriteria.getDescripcion().isEmpty()) {
            predicates.add(criteriaBuilder.like(productoRoot.get("descripcion"), "%" + searchCriteria.getDescripcion() + "%"));
        }

        if (searchCriteria.getPrecioMin() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(productoRoot.get("precio"), searchCriteria.getPrecioMin()));
        }

        if (searchCriteria.getPrecioMax() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(productoRoot.get("precio"), searchCriteria.getPrecioMax()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
