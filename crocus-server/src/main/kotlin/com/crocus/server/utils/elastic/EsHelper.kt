package com.crocus.server.utils.elastic

import org.elasticsearch.index.query.BoolQueryBuilder
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.elasticsearch.index.query.QueryBuilders.*
import org.springframework.data.domain.PageRequest

object EsHelper {

    /**
     * 获取索引定位器
     */
    fun getIndex(indexName: String): IndexCoordinates = IndexCoordinates.of(indexName)

    /**
     * 构建通用多字段模糊查询
     * @param value 值
     * @param fields value 的列 可多项
     * @param page 页
     */
    fun buildFuzzyQuery(value: String, vararg fields: String, page: PageRequest?): NativeSearchQuery {
        val query = multiMatchQuery(value, *fields)
            .fuzziness("AUTO")
        val builder = NativeSearchQueryBuilder()
            .withQuery(query)
        page?.let {
            builder.withPageable(it)
        }
        return builder.build()
    }

    /**
     * @param value 值
     * @param filterValue 过滤的值
     * @param filterField 过滤的值的列
     * @param fields value 的列 可多项
     * @param page 页
     */
    fun buildFuzzyQueryWithFilter(
        value: String,
        filterValue: String,
        filterField: String,
        vararg fields: String,
        page: PageRequest?
    ): NativeSearchQuery {
        val fuzzyQuery = multiMatchQuery(value, *fields)
            .fuzziness("AUTO")
        val boolQuery = BoolQueryBuilder().must(fuzzyQuery)
        boolQuery.filter(termQuery(filterField, filterValue))
        val builder = NativeSearchQueryBuilder()
            .withQuery(boolQuery)
        page?.let {
            builder.withPageable(it)
        }
        return builder.build()
    }

    /**
     * 构建精确 match 查询
     * @param value 值
     * @param field 字段
     */
    fun buildExactMatch(value: String, field: String): NativeSearchQuery {
        val query = matchQuery(field, value)
        return NativeSearchQueryBuilder()
            .withQuery(query)
            .build()
    }

    /**
     * 构建 term 精确匹配查询
     * @param value 值
     * @param field 字段
     */
    fun buildTermQuery(value: Any, field: String): NativeSearchQuery {
        val query = termQuery(field, value)
        return NativeSearchQueryBuilder()
            .withQuery(query)
            .build()
    }
}