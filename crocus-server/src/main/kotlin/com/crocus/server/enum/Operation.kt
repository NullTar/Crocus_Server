package com.crocus.server.enum

enum class Operation {
    /**
     * 注册
     */
    Register,

    /**
     * 登陆
     */
    Login,

    /**
     * 绑定 2FA
     */
    BindAuth,

    /**
     * 修改 邮箱
     */
    ModifyEmail,

    /**
     * 删除账号
     */
    Delete,
}