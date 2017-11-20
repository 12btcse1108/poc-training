from flask import Flask
from kafka import KafkaConsumer
import json
from geventwebsocket.handler import WebSocketHandler
from gevent.pywsgi import WSGIServer

host, port = 'localhost', 5006
app = Flask(__name__)


def wsgi_app(environ, start_response):
    path = environ["PATH_INFO"]
    if path == "/":
        return app(environ, start_response)
    elif path == "/websocket":
        handle_websocket(environ["wsgi.websocket"])
    else:
        return app(environ, start_response)


def handle_websocket(ws):
    while True:
        print("Running..")
        message = ws.receive()
        if message is None:
            break
        consumer = KafkaConsumer(bootstrap_servers='am372811-PC:9092')
        consumer.subscribe(['stockTickTrainTopic1_0'])
        for msg in consumer:
            print(msg)
            ws.send(json.dumps({'output': msg.value}))


if __name__ == '__main__':
    http_server = WSGIServer(
        (host, port), wsgi_app, handler_class=WebSocketHandler)
    print('Server started at %s:%s' % (host, port))
    http_server.serve_forever()
