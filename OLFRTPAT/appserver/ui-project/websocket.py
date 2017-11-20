from flask import Flask
from kafka import KafkaConsumer
from flaskext.mysql import MySQL
import json
from flask import request, jsonify
from flask_cors import CORS, cross_origin
import time
from geventwebsocket.handler import WebSocketHandler
from gevent.pywsgi import WSGIServer

host, port = 'localhost', 5005
app = Flask(__name__)

mysql = MySQL()
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'root'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)
conn = mysql.connect()
cursor = conn.cursor()
cursor.execute("""USE onlinebot""")


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
        result = json.loads(message)
        print(result[u'output'])
        cursor.execute(
            "select predictTopicOutput from logicset where algorithm = %s",
            result[u'output'])
        conn.commit()
        data = cursor.fetchall()
        arr = []
        for row in data:
            arr.append(row[0])
        print(arr[0])
        # consumer = KafkaConsumer(bootstrap_servers='am372811-PC:9092')
        # consumer.subscribe(arr[0])
        while True:
            print("Demo message")
            ws.send(json.dumps({'output': 'nitin'}))
            time.sleep(0.2)
            # ws.send(json.dumps({'output': msg.value}))


if __name__ == '__main__':
    http_server = WSGIServer(
        (host, port), wsgi_app, handler_class=WebSocketHandler)
    print('Server started at %s:%s' % (host, port))
    http_server.serve_forever()
