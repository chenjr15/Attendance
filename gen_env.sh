#!/usr/bin/env bash

function random_str()  {
  #  https://unix.stackexchange.com/a/230676
  tr -dc A-Za-z0-9 </dev/urandom | head -c $1
}

# 清除原内容
echo >.env

echo REDIS_PASSWORD=$(random_str 20) >>.env
echo MYSQL_PASSWORD=$(random_str 20) >>.env
echo MYSQL_ROOT_PASSWORD=$(random_str 20) >>.env
echo TOKEN_SECRET=$(random_str 200) >>.env

cat .env
