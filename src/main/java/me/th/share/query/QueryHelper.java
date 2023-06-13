package me.th.share.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class QueryHelper {

    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<>();
        if (query == null) {
            return cb.and(list.toArray(new Predicate[0]));
        }
        Field[] fields = ReflectUtil.getFields(query.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            Query qA = field.getAnnotation(Query.class);
            Object val = null;
            if (qA != null) {
                String propName = qA.propName();
                String joinName = qA.joinName();
                String[] blurry = qA.blurry();
                String attributeName = StringUtils.isBlank(propName) ? field.getName() : propName;
                Class<?> fieldType = field.getType();
                try {
                    val = field.get(query);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
                if (Objects.isNull(val)) {
                    continue;
                }
                if (fieldType.equals(String.class) && "".equals(val)) {
                    continue;
                }
                // 模糊字段查询
                if (CollectionUtil.isNotEmpty(Arrays.asList(blurry))) {
                    List<Predicate> orP = new ArrayList<>();
                    for (String bl : blurry) {
                        orP.add(cb.like(root.get(bl).as(String.class), "%" + val + "%"));
                    }
                    Predicate[] p = new Predicate[orP.size()];
                    list.add(cb.and(orP.toArray(p)));
                    continue;
                }
                // 连接查询
                Join join = null;
                if (StringUtils.isNotBlank(joinName)) {
                    switch (qA.join()) {
                        case LEFT:
                            join = root.join(joinName, JoinType.LEFT);
                            break;
                        case INNER:
                            join = root.join(joinName, JoinType.INNER);
                            break;
                        case RIGHT:
                            join = root.join(joinName, JoinType.RIGHT);
                            break;
                        default:
                            break;
                    }
                }
                switch (qA.type()) {
                    case EQUAL:
                        list.add(cb.equal(getExpression(attributeName, join, root), val));
                        break;
                    case NOT_EQUAL:
                        list.add(cb.notEqual(getExpression(attributeName, join, root), val));
                        break;
                    case GREATER_THAN:
                        list.add(cb.greaterThan(getExpression(attributeName, join, root)
                                .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                        break;
                    case GREATER_OR_EQUAL:
                        list.add(cb.greaterThanOrEqualTo(getExpression(attributeName, join, root)
                                .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                        break;
                    case LESS_THAN:
                        list.add(cb.lessThan(getExpression(attributeName, join, root)
                                .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                        break;
                    case LESS_OR_EQUAL:
                        list.add(cb.lessThanOrEqualTo(getExpression(attributeName, join, root)
                                .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                        break;
                    case INNER_LIKE:
                        list.add(cb.like(getExpression(attributeName, join, root)
                                .as(String.class), "%" + val + "%"));
                        break;
                    case LEFT_LIKE:
                        list.add(cb.like(getExpression(attributeName, join, root)
                                .as(String.class), "%" + val));
                        break;
                    case RIGHT_LIKE:
                        list.add(cb.like(getExpression(attributeName, join, root)
                                .as(String.class), val + "%"));
                        break;
                    case IN:
                        if (CollUtil.isNotEmpty((Collection<Object>) val)) {
                            list.add(getExpression(attributeName, join, root).in((Collection<Object>) val));
                        }
                        break;
                    case NOT_IN:
                        if (CollUtil.isNotEmpty((Collection<Object>) val)) {
                            list.add(getExpression(attributeName, join, root).in((Collection<Object>) val).not());
                        }
                        break;
                    case IS_NOT_NULL:
                        list.add(cb.isNotNull(getExpression(attributeName, join, root)));
                        break;
                    case IS_NULL:
                        list.add(cb.isNull(getExpression(attributeName, join, root)));
                        break;
                    case BETWEEN:
                        List<Object> between = new ArrayList<>((List<Object>) val);
                        if (between.size() == 2) {
                            list.add(cb.between(getExpression(attributeName, join, root)
                                            .as((Class<? extends Comparable>) between.get(0).getClass()),
                                    (Comparable) between.get(0), (Comparable) between.get(1)));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        int size = list.size();
        return cb.and(list.toArray(new Predicate[size]));
    }

    public static <Q> Pageable getPageable(Q query) {
        PageLink pageLink = (PageLink) ReflectUtil.getFieldValue(query, "pageLink");
        Integer pageSize, pageNum;
        if (pageLink != null) {
            pageSize = pageLink.getPageSize();
            pageNum = pageLink.getPageNum();
        } else {
            pageSize = 10;
            pageNum = 0;
        }
        QueryOrder order = (QueryOrder) ReflectUtil.getFieldValue(query, "queryOrder");
        if (order != null) {
            return PageRequest.of(pageNum, pageSize, toSort(order));
        } else {
            return PageRequest.of(pageNum, pageSize);
        }
    }

    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (ObjectUtil.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    private static Sort toSort(QueryOrder queryOrder) {
        Objects.requireNonNull(queryOrder);
        Integer direction = queryOrder.getDirection();
        // 从字典中获取，TODO
        return Sort.by(Sort.Direction.fromString("ASC"), queryOrder.getColumn());
    }
}
