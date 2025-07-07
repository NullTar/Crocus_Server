package com.crocus.server.utils.response

import com.crocus.server.utils.json.Json
import com.crocus.server.utils.exception.E
import org.springframework.stereotype.Component
import java.io.Serializable


/**
 * - code为 "1.code", msg为 "1.message" && 2 为data
 * @param a Response
 * @param Serializable
 * @return 返回一个json字符串
 * - 手动转换对象为JSON请看@See
 * @see Json.toJson(Any)
 */
fun response(a: Response, b: Serializable) = R.responseToJson(a, b)

/**
 * - 转换 Response.*、String、List<*> 为JSON字符串
 * - 1、参数是 Response.*  code为 "参数.code", msg为 "参数.message"
 * - 2、参数是 List<*>, is Array<*>, is Set<*>, is Map<*, *>  code为 "200", message为 "操作成功", data为 "参数" 查询语句时大量用到
 * - 3、参数为其他类型  code为 "404", message为 "资源未找到 || 请求地址错误"
 * @return 返回一个json字符串
 * - 手动转换对象为JSON请看@See
 * @see Json.toJson(Any)
 */
fun <T> response(a: T) = R.responseToJson(a)


/**
 * - 转换 Response.* 为 ResponseDTO
 * @param a Response
 * @param tip DeResponse 中的 tip 参数
 * @param data 可能需要传递的数据
 * @return ResponseDTO
 */
fun toDTO(a: Response? = null, tip: String? = null, data: Serializable? = null): ResponseDTO<Serializable> {
    return toDTO<Serializable>(a, tip, data)
}

fun <T> toDTO(a: Response? = null, tip: String? = null, data: T? = null): ResponseDTO<T> {
    return ResponseDTO<T>().apply {
        info = DeResponse().apply {
            code = a?.code
            message = a?.message
            this.tip = tip
        }
        this.data = data
    }
}


@Component
class R {

    companion object {
        /**
         * fun response(a: Response, b: Serializable) 是此方法的外部方法，请使用 response
         * @param response Response
         * @param data Serializable
         * @return Response 消息: Response.SUCCESS.code, response.code, response.message, data
         * 返回信息: "Response.code、 Response.message && data
         */
        internal fun responseToJson(response: Response, data: Serializable): String {
            return Json.toJson(
                ResponseDTO<Serializable>().apply {
                    info = DeResponse().apply {
                        this.code = response.code
                        this.message = response.message
                    }
                    this.data = data
                })
        }

        /**
         * fun <T> response(a: T)  是此方法的外部方法，请使用 response
         * @param data Response
         * @return Response.SUCCESS.code, response.code, response.message
         * @param data List || Array || Set || Map
         * @return Response 消息: Response.SUCCESS.code, Response.SUCCESS.message, data
         */
        internal fun <T> responseToJson(data: T): String {
            return when (data) {
                is Response -> Json.toJson(
                    ResponseDTO<T>().apply {
                        info = DeResponse().apply {
                            this.code = data.code
                            this.message = data.message
                        }
                    })

                is Serializable -> Json.toJson(
                    ResponseDTO<T>().apply {
                        info = DeResponse().apply {
                            this.message = Response.SUCCESS.message
                        }
                        this.data = data
                    }
                )

                is List<*>, is Array<*>, is Set<*>, is Map<*, *> -> Json.toJson(
                    ResponseDTO<T>().apply {
                        this.data = data
                    })

                is String -> throw E(Response.TO_JSON_ERROR_CAUSE_STRING)

                else -> throw E(Response.FAIL)
            }
        }
    }
}