package com.crocus.server.service.base

import com.crocus.server.mapper.RedisDao
import com.crocus.server.utils.permission.JWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BaseService {

    @Autowired
    lateinit var redis: RedisDao

    @Autowired
    lateinit var jwt: JWT


}