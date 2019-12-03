FROM ubuntu:latest

WORKDIR /app

COPY . /app

RUN apt-get update && apt-get -y install gcc && apt-get -y install python3.6  python3-pip  && rm -rf /var/lib/apt/lists/*

RUN pip3 install --no-cache-dir --trusted-host pypi.python.org -r requirements.txt

