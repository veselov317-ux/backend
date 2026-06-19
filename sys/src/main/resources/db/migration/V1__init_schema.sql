create table users (
    id bigserial primary key,
    full_name varchar(120) not null,
    email varchar(160) not null unique,
    password varchar(255) not null,
    role varchar(30) not null,
    enabled boolean not null default true,
    created_at timestamp with time zone not null
);

create table categories (
    id bigserial primary key,
    name varchar(100) not null unique,
    description varchar(500),
    active boolean not null default true
);

create table tickets (
    id bigserial primary key,
    title varchar(160) not null,
    description text not null,
    status varchar(40) not null,
    category_id bigint not null references categories(id),
    requester_id bigint not null references users(id),
    assigned_agent_id bigint references users(id),
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table comments (
    id bigserial primary key,
    message text not null,
    ticket_id bigint not null references tickets(id) on delete cascade,
    author_id bigint not null references users(id),
    created_at timestamp with time zone not null
);

create index idx_tickets_requester on tickets(requester_id);
create index idx_tickets_status on tickets(status);
create index idx_tickets_updated_at on tickets(updated_at);
create index idx_comments_ticket on comments(ticket_id);
