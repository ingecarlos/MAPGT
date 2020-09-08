from flask import Flask
from flask_restful import Resource, Api, reqparse
import mysql.connector

host="localhost"
user="marco_galindo"
passwd="marquino"
database="odk_prod"

app = Flask(__name__)
api = Api(app)

mydb = mysql.connector.connect(
	host=host,
	user=user,
	passwd=passwd,
	database=database
)




class versionApi(Resource):
	def get(self):
		return {'apiName':'odkapi','apiVersion':'0.0'}

class login(Resource):
	def post(self):

		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('codigo', required=True, help='codigo no puede ser nulo')
		parser.add_argument('password',required=True, help='password no puede ser nulo')
		args = parser.parse_args()
		#validar con base de datos
		db = mydb.cursor()
		db.callproc('odkapi_login',[args['codigo'],args['password']])
		result = db.stored_results().next().fetchall()
		if len(result)>0:
			data = result[0]
			response = {'codigo':data[0],'nombre':data[1],'tipo':data[2],'departamento':data[3],'sede':data[4],'especialidad':data[5]}
			return response
		return {}

class getCentro(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('codigo',required=True, help='codigo no puede ser nulo')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getCentro',[args['codigo']])
		result = db.stored_results().next().fetchall()
		if len(result)>0:
			data = result[0]
			response = {'codigo':data[0],'nombre':data[1],'municipio':data[2],'direccion':data[3],'latitud':data[4],'longitud':data[5]}
			return response
		return {}

class getDepartamentos(Resource):
	def get(self):
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getDepartamentos')
		deptos = db.stored_results().next().fetchall()
		response = {}
		deptosList = []
		#indice = 0
		for depto in deptos:
			deptoDict = {'id':depto[0],'departamento':depto[1]}
			deptosList.append(deptoDict) 
			#response.update({str(indice):deptoDict})
			#indice=indice+1
		response.update({'departamentos':deptosList})
		return response


class getMunicipios(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento', required=True, help='departamento no puede ser nulo')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getMunicipios',[args['departamento']])
		municipios = db.stored_results().next().fetchall()
		response = {}
		munisList = []
		for municipio in  municipios:
			muniDict = {'id_depto':municipio[0],'id_municipio':municipio[1],'municipio':municipio[2]}
			munisList.append(muniDict) 
		response.update({'municipios':munisList})
		return response

class getNiveles(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento', required=True, help='departamento no puede ser nulo')
		parser.add_argument('municipio', required=True, help='municipio no puede ser nulo')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getNiveles',[args['departamento'],args['municipio']])
		niveles = db.stored_results().next().fetchall()
		response = {}
		nivelList = []
		for nivel in  niveles:
			nivelDict = {'id_nivel':nivel[0],'nivel':nivel[1]}
			nivelList.append(nivelDict) 
		response.update({'niveles':nivelList})
		return response

class getSectores(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento', required=True, help='departamento no puede ser nulo')
		parser.add_argument('municipio', required=True, help='municipio no puede ser nulo')
		parser.add_argument('nivel', required=True, help='nivel no puede ser nulo')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getSectores',[args['departamento'],args['municipio'],args['nivel']])
		sectores = db.stored_results().next().fetchall()
		response = {}
		sectorList = []
		for sector in  sectores:
			sectorDict = {'id_sector':sector[0],'sector':sector[1]}
			sectorList.append(sectorDict) 
		response.update({'sectores':sectorList})
		return response

class getCentros(Resource):
	def post(self):
		#obtener datos del post
		parser = reqparse.RequestParser()
		parser.add_argument('departamento', required=True, help='departamento no puede ser nulo')
		parser.add_argument('municipio', required=True, help='municipio no puede ser nulo')
		parser.add_argument('nivel', required=True, help='nivel no puede ser nulo')
		parser.add_argument('sector', required=True, help='sector no puede ser nulo')
		args = parser.parse_args()
		#ejecutar store procedure
		db = mydb.cursor()
		db.callproc('odkapi_getCentros',[args['departamento'],args['municipio'],args['nivel'],args['sector']])
		centros = db.stored_results().next().fetchall()
		response = {}
		centrosList = []
		for centro in  centros:
			centroDict = {'codigo':centro[0],'nombre':centro[1],'municipio':centro[2],'direccion':centro[3],'latitud':centro[4],'longitud':centro[5]}
			centrosList.append(centroDict) 
		response.update({'centros':centrosList})
		return response

api.add_resource(versionApi,'/')
api.add_resource(login,'/login')
api.add_resource(getCentro,'/getCentro')
api.add_resource(getDepartamentos,'/getDepartamentos')
api.add_resource(getMunicipios,'/getMunicipios')
api.add_resource(getNiveles,'/getNiveles')
api.add_resource(getSectores,'/getSectores')
api.add_resource(getCentros,'/getCentros')


if (__name__=='__main__'):
	app.run(port = 5000, host = '0.0.0.0') 
