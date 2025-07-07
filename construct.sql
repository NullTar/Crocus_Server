create table admins
(
    id                    bigint unsigned auto_increment comment 'ID'
        primary key,
    admin_uuid            varchar(128) not null comment 'uuid',
    admin_salt            varchar(32)  not null comment '盐',
    admin_account         varchar(20)  not null comment '登陆账号',
    admin_password        varchar(32)  not null comment '登陆密码',
    admin_authenticator   int          null comment '验证器',
    admin_email           varchar(50)  null comment '管理员邮箱',
    admin_create_time     timestamp    not null on update CURRENT_TIMESTAMP comment '创建时间',
    admin_modify_time     timestamp    null comment '修改时间',
    admin_delete_time     timestamp    null comment '删除时间',
    admin_last_login_time timestamp    null comment '最后登陆时间'
);

create table announcements
(
    id                        bigint unsigned auto_increment comment 'ID公告的唯一标识，自增主键'
        primary key,
    announcement_uuid         varchar(128)                                          not null comment 'uuid',
    announcement_admin_id     bigint unsigned                                       not null comment '公告创建者的管理员 ID，关联 admins 表',
    announcement_title        varchar(255)                                          not null comment '公告标题，必填',
    announcement_content      text                                                  not null comment '公告内容，必填',
    announcement_type         enum ('info', 'warning', 'update', 'event')           not null comment '公告类型：信息、警告、更新、活动事件',
    announcement_status       enum ('draft', 'published') default 'draft'           null comment '公告状态：草稿或已发布，默认草稿',
    announcement_publish_time timestamp                   default CURRENT_TIMESTAMP null comment '公告发布时间，默认为当前时间',
    announcement_modify_time  timestamp                   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '公告修改时间，默认为当前时间并自动更新',
    announcement_archive_time timestamp                                             null comment '公告归档时间，可为空',
    constraint fk_announcement_admin_id
        foreign key (announcement_admin_id) references admins (id)
            on delete cascade
)
    comment '公告表，存储网站公告信息';

create index idx_archive_time
    on announcements (announcement_archive_time);

create index idx_type_status
    on announcements (announcement_type, announcement_status);

create table articles
(
    id                   bigint unsigned auto_increment
        primary key,
    article_uuid         varchar(128)    not null comment 'uuid',
    article_admin_id     bigint unsigned null comment '管理员id',
    article_title        varchar(50)     not null comment '标题',
    article_content_cn   varchar(255)    not null comment '内容中',
    article_content_en   varchar(255)    not null comment '内容英',
    target_age           int             null comment '目标年龄',
    target_gender        varchar(12)     not null comment '目标人群',
    article_publish_time timestamp       not null on update CURRENT_TIMESTAMP comment '创建日期',
    article_modify_time  timestamp       null comment '修改日期',
    article_delete_time  timestamp       null comment '删除日期',
    constraint fk_articles_admin_id
        foreign key (article_admin_id) references admins (id)
            on update cascade on delete set null
);

create table books
(
    id               bigint unsigned auto_increment
        primary key,
    book_uuid        varchar(128)    not null comment 'uuid',
    book_admin_id    bigint unsigned null comment '管理员id',
    book_name        varchar(40)     not null comment '书名',
    book_author      varchar(40)     not null comment '作者',
    book_cover       varchar(100)    not null comment '封面',
    book_description varchar(100)    null,
    book_isbn        varchar(17)     not null,
    target_age       int             null comment '目标年龄',
    target_gender    varchar(12)     not null comment '目标人群',
    book_create_time timestamp       not null on update CURRENT_TIMESTAMP comment '创建时间',
    book_modify_time timestamp       null comment '修改时间',
    book_delete_time timestamp       null comment '删除时间',
    constraint fk_book_admin_id
        foreign key (book_admin_id) references admins (id)
            on update cascade on delete set null
);

create table common_questions
(
    id                    bigint unsigned auto_increment
        primary key,
    cquestion_uuid        varchar(128)    not null comment 'uuid',
    cquestion_title       varchar(50)     not null comment '标题',
    cquestion_content_cn  varchar(100)    not null comment '回答内容中',
    cquestion_content_en  varchar(100)    not null comment '回答内容英',
    target_age            int             null comment '目标年龄',
    target_gender         varchar(12)     not null comment '目标人群',
    cquestion_create_time timestamp       not null on update CURRENT_TIMESTAMP comment '创建日期',
    cquestion_modify_time timestamp       null comment '修改日期',
    cquestion_delete_time timestamp       null comment '删除日期',
    is_popular            int unsigned    not null comment '访问数量',
    cquestion_admin_id    bigint unsigned null,
    constraint fk_cquestion_admin_id
        foreign key (cquestion_admin_id) references admins (id)
);

create table email_logs
(
    id                  bigint unsigned auto_increment comment '邮件的唯一标识，自增主键'
        primary key,
    email_uuid          varchar(128)                                                 not null comment 'uuid',
    email_recipient     varchar(255)                                                 not null comment '收件人邮箱地址',
    email_subject       varchar(255)                                                 null comment '邮件主题',
    email_content       text                                                         not null comment '邮件内容，正文部分',
    email_status        enum ('pending', 'sent', 'failed') default 'pending'         null comment '邮件发送状态，待发送、已发送、发送失败',
    email_error_message text                                                         null comment '错误信息，邮件发送失败时存储',
    email_create_time   timestamp                          default CURRENT_TIMESTAMP null comment '邮件创建时间，加入队列的时间',
    email_send_time     timestamp                                                    null comment '邮件实际发送时间',
    email_retry_count   int unsigned                       default '0'               null comment '邮件重试次数',
    email_admin_id      bigint unsigned                                              null,
    constraint fk_email_admin_id
        foreign key (email_admin_id) references admins (id)
)
    comment '存储邮件日志信息，包括邮件发送状态、重试等信息';

create table logs
(
    id              bigint unsigned auto_increment comment '日志的唯一标识，自增主键'
        primary key,
    log_uuid        varchar(128)                                                      not null comment 'uuid',
    log_level       enum ('INFO', 'WARN', 'ERROR', 'DEBUG') default 'INFO'            not null comment '日志级别',
    log_where       varchar(255)                                                      not null comment '日志来源，记录日志发生的位置或功能模块',
    log_message     text                                                              not null comment '日志内容，记录具体的日志信息或错误描述',
    log_old_object  varchar(40)                                                       null comment '旧对象，记录变更前的对象状态（可选）',
    log_new_object  varchar(40)                                                       null comment '新对象，记录变更后的对象状态（可选）',
    log_create_time timestamp                               default CURRENT_TIMESTAMP not null comment '日志创建时间，记录日志生成的时间'
)
    comment '记录系统运行时的日志，包括日志级别、日志来源、日志内容以及对象的变更信息';

create table minIO_files
(
    id               bigint unsigned auto_increment comment '文件的唯一标识，自增主键'
        primary key,
    file_uuid        varchar(128)                                                                                                                                                                                                                                      not null comment 'uuid',
    file_group_id    varchar(128)                                                                                                                                                                                                                                      not null comment '文件组标识，用于标识多个文件的关联',
    file_name        varchar(255)                                                                                                                                                                                                                                      not null comment '文件名',
    file_type        enum ('application/pdf', 'application/vnd.ms-powerpoint', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', 'application/vnd.openxmlformats-officedocument.presentationml.slideshow', 'application/vnd.apple.keynote') not null comment '文件类型',
    file_size        bigint                                                                                                                                                                                                                                            null comment '文件大小（字节）',
    file_bucket      varchar(255)                                                                                                                                                                                                                                      not null comment 'MinIO 存储桶名称',
    file_object      varchar(255)                                                                                                                                                                                                                                      not null comment 'MinIO 对象名称',
    file_upload_time timestamp default CURRENT_TIMESTAMP                                                                                                                                                                                                               null comment '文件上传时间',
    file_delete_time timestamp                                                                                                                                                                                                                                         null comment '文件删除时间（可选）',
    file_uploader_id bigint                                                                                                                                                                                                                                            null comment '上传者的用户 ID（可选，关联用户表）',
    file_url         varchar(1024)                                                                                                                                                                                                                                     null comment '文件访问 URL（HTTP 地址）',
    file_description text                                                                                                                                                                                                                                              null comment '文件描述（可选）',
    file_admin_id    bigint unsigned                                                                                                                                                                                                                                   null comment '管理员ID',
    constraint uq_file
        unique (file_bucket, file_object) comment '文件存储桶和对象的唯一约束，确保文件在存储桶中唯一',
    constraint fk_file_admin_id
        foreign key (file_admin_id) references admins (id)
)
    comment '存储文件信息，包括文件的基本信息、存储桶、对象标识和文件的元数据';

create table email_attachments
(
    id                   bigint unsigned auto_increment comment '附件的唯一标识，自增主键'
        primary key,
    attachments_uuid     varchar(128)    not null comment 'uuid',
    attachments_email_id bigint unsigned not null comment '关联的邮件 ID，外键引用 email_logs 表',
    attachments_group_id varchar(128)    not null comment '附件对应的文件组 ID，外键引用 minIO_files 表',
    constraint fk_email_id
        foreign key (attachments_email_id) references email_logs (id)
            on delete cascade,
    constraint fk_group_id
        foreign key (attachments_group_id) references minIO_files (file_group_id)
            on delete cascade
)
    comment '存储邮件附件信息，关联邮件和附件的文件组';

create index idx_email_id
    on email_attachments (attachments_email_id)
    comment '根据邮件 ID 索引附件';

create index idx_group_id
    on email_attachments (attachments_group_id)
    comment '根据文件组 ID 索引附件';

create index idx_file_group_id
    on minIO_files (file_group_id)
    comment '普通索引，用于加速查询文件组标识';

create table quizzes
(
    id                  bigint unsigned auto_increment comment '测验唯一标识，自增主键'
        primary key,
    quizzes_uuid        varchar(128)                                              not null comment 'UUID 副唯一标识',
    quizzes_title       varchar(255)                                              not null comment '测验标题',
    quizzes_difficulty  enum ('easy', 'medium', 'hard') default 'easy'            null comment '测验难度：easy、medium、hard',
    quizzes_description text                                                      null comment '测验描述',
    quizzes_time_limit  int                             default 0                 null comment '测验时间限制（单位：秒，0 表示无限制）',
    quizzes_create_time timestamp                       default CURRENT_TIMESTAMP null comment '创建时间',
    quizzes_modify_time timestamp                       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    quizzes_delete_time timestamp                                                 null comment '删除时间（可选，若不删除则为 NULL）',
    quizzed_admin_id    bigint unsigned                                           null,
    constraint id
        unique (id),
    constraint fk_quizzes_admin_id
        foreign key (quizzed_admin_id) references admins (id)
);

create table quizzes_questions
(
    id                      bigint unsigned auto_increment comment '问题唯一标识，自增主键'
        primary key,
    question_uuid           varchar(128)                                                  not null comment '问题的 UUID 标识',
    question_quiz_id        bigint unsigned                                               not null comment '关联测验的 ID',
    question_text           text                                                          not null comment '问题内容',
    question_type           enum ('single', 'multiple', 'text') default 'single'          null comment '问题类型：single（单选），multiple（多选），text（文本输入）',
    question_options        json                                                          null comment '问题选项（对于单选和多选问题），以 JSON 格式存储',
    question_correct_answer json                                                          null comment '问题的正确答案，以 JSON 格式存储',
    question_score          int                                 default 1                 null comment '问题的分数，默认 1 分',
    question_create_time    timestamp                           default CURRENT_TIMESTAMP null comment '问题创建时间',
    question_update_time    timestamp                           default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '问题更新时间，每次更新时自动修改',
    question_delete_time    timestamp                                                     null comment '问题删除时间（可选，若不删除则为 NULL）',
    constraint id
        unique (id),
    constraint fk_question_quiz_id
        foreign key (question_quiz_id) references quizzes (id)
            on delete cascade
);

create table rss_feeds
(
    id               bigint unsigned auto_increment comment '唯一标识，自增主键'
        primary key,
    feed_uuid        varchar(128)                        not null comment 'RSS 源的 UUID，用于标识唯一性',
    feed_title       varchar(255)                        not null comment 'RSS 源的标题',
    feed_url         varchar(255)                        not null comment 'RSS 源的链接地址，确保唯一性',
    feed_description text                                null comment 'RSS 源的描述信息',
    feed_create_time timestamp default CURRENT_TIMESTAMP null comment 'RSS 源创建时间，默认为当前时间',
    feed_update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'RSS 源更新时间，更新时自动修改',
    feed_delete_time timestamp                           null comment 'RSS 源删除时间，若未删除则为 NULL',
    feed_admin_id    bigint unsigned                     null,
    constraint feed_url
        unique (feed_url) comment '唯一约束，确保每个 RSS 链接在表中唯一',
    constraint id
        unique (id),
    constraint fk_feed_admin_id
        foreign key (feed_admin_id) references admins (id)
);

create table rss_link
(
    id              bigint unsigned auto_increment comment '唯一标识，自增主键'
        primary key,
    link_uuid       varchar(128)                        not null comment 'UUID，用于标识唯一性',
    link_feed_id    bigint unsigned                     not null comment '关联的 RSS 源 ID，外键指向 rss_feeds 表',
    link_article_id bigint unsigned                     not null comment '关联的文章 ID，外键指向 articles 表',
    link_bind_time  timestamp default CURRENT_TIMESTAMP null comment '绑定时间，记录链接创建时间',
    constraint fk_link_article_id
        foreign key (link_article_id) references articles (id)
            on delete cascade,
    constraint fk_link_feed_id
        foreign key (link_feed_id) references rss_feeds (id)
            on delete cascade
);

create index idx_link_article_id
    on rss_link (link_article_id)
    comment '索引，用于快速查找特定文章的 RSS 关联';

create index idx_link_feed_id
    on rss_link (link_feed_id)
    comment '索引，用于快速查找特定 RSS 源的文章关联';

create table users
(
    id                      bigint auto_increment comment 'id'
        primary key,
    user_uuid               varchar(255)                        null comment 'UUID',
    user_salt               varchar(32)                         not null comment '盐',
    user_account            varchar(40)                         not null comment '用户名',
    user_password           varchar(255)                        not null comment '密码',
    user_authenticator      int                                 null comment '验证器',
    user_email              varchar(128)                        null comment '邮箱',
    user_create_time        timestamp default CURRENT_TIMESTAMP null comment '用户创建时间',
    user_modify_time        timestamp                           null comment '修改时间',
    user_delete_time        timestamp                           null comment '删除时间',
    user_banded_by_admin_id bigint unsigned                     null,
    constraint fk_user_banded_by_admin_id
        foreign key (user_banded_by_admin_id) references admins (id)
);

create table submissions
(
    id                      bigint unsigned auto_increment comment '唯一标识，自增主键'
        primary key,
    submission_uuid         varchar(128)                                                       not null comment '提交的唯一标识，用于追踪和引用',
    submission_user_id      bigint                                                             null comment '提交者的用户 ID，游客为 NULL',
    submission_type         enum ('report', 'suggestion', 'article')                           not null comment '提交类型：错误报告、建议或投稿',
    submission_status       enum ('pending', 'approved', 'rejected') default 'pending'         null comment '提交状态：待审核、已批准、已拒绝',
    submission_related_id   bigint unsigned                                                    null comment '关联资源 ID（如文章或书籍 ID），错误报告时使用，建议为 NULL',
    submission_title        varchar(255)                                                       null comment '提交的标题，适用于投稿或详细描述',
    submission_description  text                                                               null comment '提交的内容描述或详细信息',
    submission_created_time timestamp                                default CURRENT_TIMESTAMP null comment '提交时间，记录表单创建的时间',
    submission_updated_time timestamp                                default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间，记录表单最后修改时间',
    submission_admin_id     bigint unsigned                                                    null comment '审核者的用户 ID，管理员操作后记录',
    submission_review_notes text                                                               null comment '审核备注，管理员填写的说明或意见',
    submissions_admin_id    bigint unsigned                                                    null,
    constraint fk_submission_admin_id
        foreign key (submission_admin_id) references admins (id),
    constraint fk_submissions_user_id
        foreign key (submission_user_id) references users (id)
)
    comment '存储用户提交的信息，如错误报告、建议或投稿';

create index idx_submission_related_id
    on submissions (submission_related_id)
    comment '索引，用于快速查找关联资源的提交记录';

create index idx_submission_type_status
    on submissions (submission_type, submission_status)
    comment '复合索引，用于加速基于提交类型和状态的查询';

create table user_questions
(
    id                             bigint unsigned auto_increment
        primary key,
    uquestion_uuid                 varchar(128)    not null comment 'uuid',
    uquestion_title                varchar(50)     not null comment '标题',
    uquestion_description          varchar(100)    not null comment '问题描述',
    uquestion_content              varchar(100)    not null comment '回答内容',
    uquestion_create_time          timestamp       not null on update CURRENT_TIMESTAMP comment '创建日期',
    uquestion_modify_time          timestamp       null comment '修改日期',
    uquestion_delete_time          timestamp       null comment '删除日期',
    uquestion_operation            varchar(2)      not null comment '是否移入公共池',
    uquestion_user_id              bigint          null comment '用户id',
    uquestion_answered_by_admin_id bigint unsigned null comment '管理员id',
    constraint fk_uquestion_answered_by_admin_id
        foreign key (uquestion_answered_by_admin_id) references admins (id),
    constraint fk_uquestion_user_id
        foreign key (uquestion_user_id) references users (id)
            on update cascade on delete set null
);


