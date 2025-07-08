-- Create schemas for each service
CREATE SCHEMA IF NOT EXISTS customer_schema;
CREATE SCHEMA IF NOT EXISTS loyalty_schema;
CREATE SCHEMA IF NOT EXISTS coupon_schema;

-- Create users for each service
CREATE USER customer_user WITH PASSWORD 'customer_password';
CREATE USER loyalty_user WITH PASSWORD 'loyalty_password';
CREATE USER coupon_user WITH PASSWORD 'coupon_password';
CREATE USER admin_user WITH PASSWORD 'admin_password';
ALTER USER admin_user WITH SUPERUSER;

-- Grant privileges
GRANT ALL PRIVILEGES ON SCHEMA customer_schema TO customer_user;
GRANT ALL PRIVILEGES ON SCHEMA loyalty_schema TO loyalty_user;
GRANT ALL PRIVILEGES ON SCHEMA coupon_schema TO coupon_user;

-- Allow users to use the schemas
ALTER ROLE customer_user SET search_path TO customer_schema;
ALTER ROLE loyalty_user SET search_path TO loyalty_schema;
ALTER ROLE coupon_user SET search_path TO coupon_schema;
