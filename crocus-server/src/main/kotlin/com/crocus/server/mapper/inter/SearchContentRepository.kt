package com.crocus.server.mapper.inter

import com.crocus.server.entity.search.SearchContent
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface SearchContentRepository : ElasticsearchRepository<SearchContent, String>