package com.crocus.server.entity.search

import com.crocus.server.entity.parameter.content_Index
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = content_Index)
data class SearchContent(

    @Id
    var uuid: String,

    var key: String = "",

    @Field(type = FieldType.Text, index = false)
    var link: String? = null,

    @Field(type = FieldType.Keyword)
    var type: String = ""
)