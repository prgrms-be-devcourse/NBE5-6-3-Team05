CREATE table `worldcup`(
    `id` BIGINT not null comment '인덱스 번호. 자동증가',
    `title` varchar(255) not null,
    `content_type` varchar(20)  not null,
    `user_id` varchar(20) null,
    `hash_code` varchar(255) not null,
    `created_at` datetime not null default current_timestamp comment '월드컵 생성 일자'
);

ALTER TABLE `worldcup` ADD CONSTRAINT `PK_WORLDCUP` PRIMARY KEY (
                                                          `id`
    );
ALTER TABLE worldcup
    MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;
desc worldcup;


CREATE table `worldcup_item`(
    `id`            BIGINT          not null comment '인덱스 번호. 자동증가',
    `worldcup_id`   BIGINT          not null,
    `content_id` VARCHAR(255)       NOT NULL,
    `win_count`     BIGINT          not null default 0,
    `total_count`   Bigint          not null default 0
);

alter table `worldcup_item` add constraint `pk_worldcup_item` primary key (
                                                                 `id`
    );
alter table worldcup_item
    modify column id bigint not null auto_increment;
desc worldcup_item;

create table `worldcup_play`(
    `id`            BIGINT not null comment '인덱스 번호. 자동증가',
    `worldcup_id`   BIGINT not null ,
    `user_id`       varchar(20) null,
    `winner_id`     VARCHAR(255) not null,
    `created_at` datetime not null default current_timestamp comment '월드컵 플레이 날짜'
);

alter table `worldcup_play` add constraint `pk_worldcup_play` primary key (
                                                                           `id`
    );
alter table worldcup_play
    modify column id bigint not null auto_increment;
desc worldcup_play;
