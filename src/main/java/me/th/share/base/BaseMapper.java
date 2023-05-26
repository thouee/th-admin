package me.th.share.base;

import java.util.List;

public interface BaseMapper<E, D> {

    /**
     * DTO 转 Entity
     *
     * @param dto -
     * @return E
     */
    E toEntity(D dto);

    /**
     * DTO 集合转 Entity 集合
     *
     * @param dtoList -
     * @return List<E>
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity 转 DTO
     *
     * @param entity -
     * @return D
     */
    D toDto(E entity);

    /**
     * Entity 集合转 DTO 集合
     *
     * @param entityList -
     * @return List<D>
     */
    List<D> toDto(List<E> entityList);
}
