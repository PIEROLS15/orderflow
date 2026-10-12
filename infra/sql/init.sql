create extension if not exists pgcrypto;

create table if not exists tb_customers (
  id uuid default gen_random_uuid() primary key,
  first_name varchar(120) not null,
  last_name varchar(120) not null,
  email varchar(180) not null unique,
  document_number varchar(30) not null unique,
  status varchar(20) not null default 'ACTIVO',
  created_at timestamp without time zone not null default current_timestamp,
  updated_at timestamp without time zone not null default current_timestamp
);

create table if not exists tb_products (
  id uuid default gen_random_uuid() primary key,
  name varchar(160) not null,
  description varchar(500),
  price numeric(12,2) not null,
  stock int not null,
  active boolean not null default true,
  created_at timestamp without time zone not null default current_timestamp,
  updated_at timestamp without time zone not null default current_timestamp
);

create table if not exists tb_orders (
  id uuid default gen_random_uuid() primary key,
  customer_id uuid not null references tb_customers(id),
  status varchar(20) not null,
  total_amount numeric(12,2) not null default 0,
  external_profile_id varchar(80),
  created_at timestamp without time zone not null default current_timestamp,
  confirmed_at timestamp without time zone,
  updated_at timestamp without time zone not null default current_timestamp
);

create table if not exists tb_order_items (
  id uuid default gen_random_uuid() primary key,
  order_id uuid not null references tb_orders(id) on delete cascade,
  product_id uuid not null references tb_products(id),
  quantity int not null,
  unit_price numeric(12,2) not null,
  subtotal numeric(12,2) not null
);

create table if not exists tb_order_event_logs (
  id uuid default gen_random_uuid() primary key,
  order_id uuid not null,
  event_type varchar(80) not null,
  status varchar(30) not null,
  message varchar(500),
  processed_at timestamp without time zone not null default current_timestamp,
  created_at timestamp without time zone not null default current_timestamp
);

create index if not exists idx_orders_customer on tb_orders(customer_id);
create index if not exists idx_orders_status on tb_orders(status);
create index if not exists idx_order_items_order on tb_order_items(order_id);
create index if not exists idx_order_logs_order on tb_order_event_logs(order_id);

insert into tb_products (name, description, price, stock, active)
select 'Laptop Lenovo', 'Laptop para trabajo remoto', 2500.00, 10, true
where not exists (select 1 from tb_products where name = 'Laptop Lenovo');

insert into tb_products (name, description, price, stock, active)
select 'Mouse Logitech', 'Mouse inalambrico', 80.00, 30, true
where not exists (select 1 from tb_products where name = 'Mouse Logitech');

insert into tb_products (name, description, price, stock, active)
select 'Monitor Samsung', 'Monitor 24 pulgadas', 650.00, 15, true
where not exists (select 1 from tb_products where name = 'Monitor Samsung');

insert into tb_products (name, description, price, stock, active)
select 'Teclado Mecanico', 'Teclado mecanico retroiluminado', 180.00, 20, true
where not exists (select 1 from tb_products where name = 'Teclado Mecanico');

insert into tb_products (name, description, price, stock, active)
select 'Audifonos Bluetooth', 'Audifonos con cancelacion de ruido', 220.00, 25, true
where not exists (select 1 from tb_products where name = 'Audifonos Bluetooth');
