FROM python:3.6-slim

WORKDIR /app

COPY . /app

RUN pip install --no-cache-dir --trusted-host pypi.python.org -r requirements.txt

ENV FLASK_APP app.py
ENV FLASK_RUN_PORT 8000

CMD ["flask", "run"]