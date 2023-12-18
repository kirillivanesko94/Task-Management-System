create type task_status AS ENUM ('NEW', 'IN_PROGRESS', 'SUSPEND', 'DONE', 'REJECT');
create type task_priority AS ENUM ('TRIVIAL', 'MINOR', 'MAJOR', 'CRITICAL', 'BLOCKER');

create table platform_user(
    id uuid primary key,
    name text not null,
    login text not null,
    pwd text not null
);

create unique index unique_login on platform_user(lower(login));

create table task(
    id uuid primary key,
    title text not null,
    description text not null,
    status task_status not null,
    priority task_priority not null,
    author_id uuid not null,
    assign_id uuid,

   constraint fk_task_author_id foreign key(author_id) references platform_user(id) on delete restrict on update restrict,
   constraint fk_task_assign_id foreign key(assign_id) references platform_user(id) on delete restrict on update restrict
);

create index idx_task_author_id on task(author_id);
create index idx_task_assign_id on task(assign_id);

create table task_comment(
    id uuid primary key,
    body text not null,
    author_id uuid not null,
    task_id uuid not null,

   constraint fk_task_comment_task_id foreign key(task_id) references task(id) on delete cascade on update restrict,
   constraint fk_task_comment_author_id foreign key(author_id) references platform_user(id) on delete restrict on update restrict
);
