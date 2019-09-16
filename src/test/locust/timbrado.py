import time
from os.path import expanduser
from locust import HttpLocust, TaskSet, events, task
from influxdb import InfluxDBClient
#sys.path.append(os.getcwd())
#import common.auth

#sHome = expanduser("~")
sHome = "/opt/app-root"
sHost = "influx"
sApp = "bluejay"
sWWW = "http://timbrado:8080"
bAlive = True

def loadConfig( sCfgFile=".bluejayrc"):
	global sHost
	global sApp
	fCfg = open( sHome+"/"+sCfgFile, "r")
	for sLine in fCfg:
		pOpt = sLine.split( "=", 1)
		sKey = pOpt[0].rstrip()
		if sKey == "bluejay.locust.jns":
			sApp = pOpt[1].lstrip().rstrip()
		elif sKey == "bluejay.locust.dhn":
			sHost = pOpt[1].lstrip().rstrip()

def on_request_success( request_type, name, response_time, response_length):
	global pRTM
	global sWWW
	json_body = [ {
		"measurement": "timbrado",
		"tags": { "async": "true" },
		"fields": {
			"agt": "locust",
			"bytes": response_length,
			"err": 0,
			"rsp": int(response_time),
			"sent": 294,
			"tst": "PoC",
			"url": sWWW+name,
			"txt": request_type
		}
	} ]
	pRTM.write_points( json_body)

def on_quitting():
	global fRTM
	bAlive = False
	fRTM.close()

loadConfig()
pRTM = InfluxDBClient( sHost, 8086, 'jmeter', 'h0ckeypuck', 'bluejay')
fRTM = open( 'txn.csv', 'w')
# Trying to create an existing database will fail ...
#pRTM.create_database( 'bluejay')
#events.request_success += on_request_success
events.quitting += on_quitting

class TestPlan( TaskSet):
	@task
	def default_task( self):
		global pRTM
		global fRTM
		global bAlive
		global sApp
		while bAlive:
			pRsp = self.client.get( "/timbrado/portal?op=epsilon")
			pReq = pRsp.request
			sReq = pReq.method+' '+pReq.url+str('\n'.join('{}: {}'.format(k, v) for k, v in pReq.headers.items()))+str(pReq.body)
			iErr = int(pRsp.status_code)
			if iErr == 200:
				iErr = 0
			iLen = len(pRsp.content)
			iSz = len(sReq)
			iRsp = pRsp.elapsed.microseconds/1000
			sTst = 'locust';
			sURL = pRsp.url
			sMtd = pReq.method
			json_body = [ {
				"measurement": sApp,
				"tags": { "async": "true" },
				"fields": {
					"agt": "locust",
					"bytes": iLen,
					"err": iErr,
					"rsp": iRsp,
					"sent": iSz,
					"tst": sTst,
					"url": pRsp.url,
					"txt": pReq.method
				}
			} ]
			pRTM.write_points( json_body)
			sRow = str(time.time())+','+sTst+','+str(iRsp)+','+str(iErr)+','+str(iLen)+','+str(iSz)+','+sURL.replace(',',';')+','+sMtd.replace(',',';')+'\n'
			fRTM.write( sRow)
			time.sleep(0.768)

class vUser( HttpLocust):
	global sWWW
	host = sWWW
	task_set = TestPlan
	min_wait = 500
	max_wait = 5000
