from flask import Blueprint
from flask import Flask
from flaskext.mysql import MySQL
from flask import request, jsonify, json
from flask_cors import CORS, cross_origin
import os
import sys
import subprocess
from subprocess import Popen, PIPE
import shlex

app = Flask(__name__)

logicset = Blueprint('logicset', __name__)
mysql = MySQL()
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'root'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)
conn = mysql.connect()
cursor = conn.cursor()
cursor.execute("""USE onlinebot""")

try:
    @logicset.route('/retrieveLogicSets', methods=['GET'])
    @cross_origin(origin='*')
    def retrieveLogicSets():
        query = "select * from logicset"
        cursor.execute(query)
        conn.commit()
        data = cursor.fetchall()
        arr = []
        for row in data:
            arr.append(row)
        return jsonify(arr)
except:
    print "err-db0log005:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/retrieveLogic', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def retrieveLogic():
        algorithm = str(request.json['algoName'])
        cursor.execute("select * from logicset where algorithm = %s", algorithm)
        conn.commit()
        data = cursor.fetchall()
        arr = []
        for row in data:
            arr.append(row)
        return jsonify(arr)
except:
    print "err-db0log006:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/saveLogicSet', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def saveLogicSet():
        msg_name = request.json['msgName'],
        algorithmName = request.json['algorithmName']
        algorithm = request.json['algoName']
        query = "insert into logicset(msg_name,algorithm_name,algorithm)"
        query += "values (%s,%s,%s)"
        args = (msg_name, algorithmName, algorithm)
        cursor.execute(query, args)
        conn.commit()
        return jsonify("Saved successfully!")
except:
    print "err-db0log007:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/updateLogicSet', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def updateLogicSet():
        print("inside updateLogicSet")
        msg_name = request.json['msgName'],
        topicName = request.json['topicName']
        algorithmName = request.json['algorithmName']
        trainTopic = request.json['trainTopic']
        predictTopicInput = request.json['predictTopicInput']
        predictTopicOutput = request.json['predictTopicOutput']
        regularisation = request.json['regularisation']
        learningRate = request.json['learningRate']
        modelSaveInterval = request.json['modelSaveInterval']
        algorithm = request.json['algoName']
        continousVariable = request.json['continousVariable']
        categoryVariable = request.json['categoryVariable']
        predictionVariable = request.json['predictionVariable']
        print learningRate
        query = "update logicset set msg_name = %s,topicName = %s,algorithmName = %s,trainTopic = %s,"
        query += "predictTopicInput = %s,predictTopicOutput = %s,regularisation = %s,"
        query += "learningRate = %s,modelSaveInterval = %s,algorithm = %s,continousVariable = %s,"
        query += "categoryVariable = %s,predictionVariable = %s where algorithm = %s"
        args = (msg_name, topicName, algorithmName, trainTopic, predictTopicInput,
                predictTopicOutput, regularisation, learningRate,
                modelSaveInterval, algorithm, continousVariable, categoryVariable,
                predictionVariable, algorithm)
        cursor.execute(query, args)
        conn.commit()
        return "logicset updated successfully"
except:
    print "err-db0log008:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/updateAnalyse', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def updateAnalyse():
        algoName = request.json['algoName']
        analyzeState = request.json['analyzeState']
        query = "update logicset set analyzeState = %s where algorithm = %s"
        args = (analyzeState, algoName)
        cursor.execute(query, args)
        conn.commit()
        return analyzeState
except:
    print "err-db0log009:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/deleteLogicSet', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def deleteLogicSet():
        algoName = request.json['algoName']
        print algoName
        query = "delete from logicset where algorithm = %s"
        cursor.execute(query, (algoName))
        conn.commit()
        return 'logicset deleted successfully'
except:
    print "err-db0log010:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/train', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def train():
        algorithmNameLogic = request.json['nameOfAlgorithm']
        if algorithmNameLogic == "Linear Regression":
            print("the file running is Linear regression")
            os.system(
                'nohup $JAVA_HOME/bin/java -cp /home/am372811/Downloads/mysql-connector-java-5.1.21.jar:/home/am372811/data/logic/jars/chatbot-0.2.0-SNAPSHOT-fat.jar com.wipro.chatbot.ml.SLiR_v1_Trainer '
                + request.json['algoName'] + ' &')
        else:
            print("the file running is Logistic regression")
            os.system(
                'nohup $JAVA_HOME/bin/java -cp /home/am372811/Downloads/mysql-connector-java-5.1.21.jar:/home/am372811/data/logic/jars/chatbot-0.2.0-SNAPSHOT-fat.jar com.wipro.chatbot.ml.SLoR_v1_Trainer '
                + request.json['algoName'] + ' &')
        stringData = 'Successfully submitted for ' + request.json['algoName']
        data = {
            'response': stringData,
            'data': {
                'Algorithm': request.json['algoName'],
                'Index': request.json['index']
            }
        }
        return jsonify(data)
except:
    print "err-db0log011:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/process', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def process():
        os.system(
            'nohup $JAVA_HOME/bin/java -cp /home/am372811/Downloads/mysql-connector-java-5.1.21.jar:/home/am372811/data/logic/jars/chatbot-0.2.0-SNAPSHOT-fat.jar com.wipro.chatbot.ml.SLiR_v1_Predictor '
            + request.json['algoName'] + ' &')
        stringData = 'Successfully submitted for ' + request.json['algoName']
        data = {
            'response': stringData,
            'data': {
                'Algorithm': request.json['algoName'],
                'Index': request.json['index']
            }
        }
        return jsonify(data)
except:
    print "err-db0log012:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/analyze', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def analyze():
        print("amit " + request.json['algoName'])
        os.system(
            'nohup $JAVA_HOME/bin/java -cp /home/am372811/Downloads/mysql-connector-java-5.1.21.jar:/home/am372811/data/logic/jars/chatbot-0.2.0-SNAPSHOT-fat.jar com.wipro.chatbot.kafka.DataGenStockTick '
            + request.json['algoName'] + ' &')
        stringData = 'Successfully submitted for ' + request.json['algoName']
        data = {
            'response': stringData,
            'data': {
                'Algorithm': request.json['algoName'],
                'Index': request.json['index']
            }
        }
        return jsonify(data)
except:
    print "err-db0log013:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @logicset.route('/stopAnalyze', methods=['GET', 'POST'])
    @cross_origin(origin='*')
    def stopAnalyze():
        print("amit " + request.json['algoName'])
        processes = Popen(['ps', '-ef'], stdout=PIPE, stderr=PIPE)
        stdout, error = processes.communicate()
        for line in stdout.splitlines():
            if (line.__contains__("DataGenStockTick")):
                pid = int(line.split(None, 1)[0])
                os.kill(pid, signal.SIGKILL)
        stringData = 'Successfully submitted for ' + request.json['algoName']
        data = {
            'response': stringData,
            'data': {
                'Algorithm': request.json['algoName'],
                'Index': request.json['index']
            }
        }
        return jsonify(data)
except:
    print "err-db0log014:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e
