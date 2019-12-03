FROM ubuntu:latest

WORKDIR /app

COPY . /app

RUN apt-get update && apt-get -y install curl && rm -rf /var/lib/apt/lists/*

