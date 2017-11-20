import sys
from flask import Blueprint
from flask import Flask
from flaskext.mysql import MySQL
from flask import request,jsonify,json
from flask_cors import CORS, cross_origin

app = Flask(__name__)

msg = Blueprint('msg', __name__)
mysql = MySQL()
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'root'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)
conn = mysql.connect()
cursor = conn.cursor()
cursor.execute("""USE onlinebot""")

try:
    @msg.route('/retrieveMsgSchemas',methods=['GET'])
    @cross_origin(origin='*')
    def retrieveMsgSchemas():
        query="select * from msg_table"
        cursor.execute(query)
        conn.commit();
        data=cursor.fetchall()
        arr = []
        for row in data:
            arr.append(row)
        print jsonify(arr)
        return jsonify(arr)
except:
    print "err-db0msg001:", sys.exc_info()[0]
    raise
# except Exception as e:  
#     print(e) 

try:
    @msg.route('/saveMsgSchema',methods=['GET','POST'])
    @cross_origin(origin='*')
    def saveMsgSchema():
        name=request.json['msgName'],
        structure=request.json['msgStructure']
        # query="create table if not exists msg_table( msg_id INT NOT NULL AUTO_INCREMENT  PRIMARY KEY,msg_name VARCHAR(40) NOT NULL UNIQUE,msg_structure VARCHAR(400) NOT NULL)"
        # cursor.execute(query)
        # conn.commit();
        query="insert into msg_table(msg_name,msg_structure) values (%s,%s)"
        args=(name,structure)
        cursor.execute(query,args)
        conn.commit();
        cursor.execute("select msg_name from msg_table")
        data=cursor.fetchall()
        arr = []
        for row in data:
            arr.append(row[0])
        return jsonify(arr)
        cursor.close()
except:
    print "err-db0msg002:", sys.exc_info()[0]
    raise
# except Exception as e:
#     raise e

try:
    @msg.route('/updateMsgSchema',methods=['GET','POST'])
    @cross_origin(origin='*')
    def updateMsgSchema():
        structure=request.json['msgStructure']
        msg_name=request.json['msgName'],
        print msg_name
        print structure
        query="update msg_table set msg_structure = %s  where msg_name = %s"
        args=(structure,msg_name)
        cursor.execute(query,args)
        conn.commit();
        cursor.execute("select * from msg_table where msg_name = %s",msg_name)
        data=cursor.fetchall()
        arr = []
        for row in data:
            arr.append(row)
        return jsonify(arr)
except:
    print "err-db0msg003:", sys.exc_info()[0]
    raise    
# except Exception as e:
#     raise e

try:
    @msg.route('/deleteMsgSchema',methods=['GET','POST'])
    @cross_origin(origin='*')
    def deleteMsgSchema():
        msg_name=request.json['msgName']
        print msg_name
        cursor.execute("select count(*) from logicset where msg_name = %s",msg_name)
        data=cursor.fetchone()[0]
        print data
        if data>=1:
            return 'You cannot delete this message'
        else :
            query="delete from msg_table where msg_name = %s"
            cursor.execute(query,(msg_name))
            conn.commit();
            return 'message deleted successfully'
            cursor.close()
except:
    print "err-db0msg004:", sys.exc_info()[0]
    raise             
# except Exception as e:
#     raise e

