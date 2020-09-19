#!/usr/bin/env bash

kill=false
seed=false
seedUnit=false

while getopts "ksu" option; do
  case $option in
  k)
    kill=true
    ;;
  s)
    seed=true
    ;;
  u)
    seedUnit=true
    ;;
  *) ;;
  esac
done

if [ $kill = true ]; then
  docker stop hoodworking-postgres
else
  docker run \
    --rm \
    --name hoodworking-postgres \
    -e POSTGRES_DB=hoodworking \
    -e POSTGRES_USER=hoodworking \
    -e POSTGRES_PASSWORD=hoodworking \
    -p 5432:5432 \
    -d \
    postgres:12

  if [ $seed = true ] || [ $seedUnit = true ]; then
    until docker exec hoodworking-postgres psql -U hoodworking -d hoodworking -c "select 1" >/dev/null 2>&1; do
      sleep 1
    done

    POSTGRES_HOST=localhost \
      POSTGRES_PORT=5432 \
      POSTGRES_DB=hoodworking \
      POSTGRES_USER=hoodworking \
      POSTGRES_PASSWORD=hoodworking \
      ./gradlew liquibaseUpdate

    if [ $seedUnit = true ]; then
      docker exec -i hoodworking-postgres psql -U hoodworking -d hoodworking <src/dbTest/sql/seed.sql
    fi
    if [ $seed = true ]; then
      docker exec -i hoodworking-postgres psql -U hoodworking -d hoodworking <src/main/sql/seed.sql
    fi
  fi
fi
