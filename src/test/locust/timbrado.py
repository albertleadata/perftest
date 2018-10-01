import time
from os.path import expanduser
from locust import HttpLocust, TaskSet, events, task
from influxdb import InfluxDBClient
#sys.path.append(os.getcwd())
#import common.auth

sHome=expanduser("~")
sHost = "bluejay"
sApp = "bluejay"
sWWW = "http://canarydev:8080"
bAlive = True

def loadConfig( sCfgFile=".bluejayrc"):
	global sHome
	global sHost
	global sApp
	fCfg = open( sHome+"/"+sCfgFile, "r")
	for sLine in fCfg:
		pOpt = sLine.split( "=", 1)
		if pOpt[0].rstrip() == "bluejay.locust.jns":
			sApp = pOpt[1].lstrip().rstrip()
		elif pOpt[0].rstrip() == "bluejay.locust.dhn":
			sHost = pOpt[1].lstrip().rstrip()

def on_request_success( request_type, name, response_time, response_length):
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
	client.write_points( json_body)

def on_quitting():
	bAlive = False

loadConfig
client = InfluxDBClient( 'bluejaydev', 8086, 'jmeter', 'h0ckeypuck', 'bluejay')
# Trying to create an existing database will fail ...
#client.create_database( 'bluejay')
#events.request_success += on_request_success
events.quitting += on_quitting

class TestPlan( TaskSet):
	@task
	def default_task( self):
		global bAlive
		global sApp
		global sWWW
		global sHost
		while bAlive:
			pRsp = self.client.get( "/timbrado/portal?op=epsilon")
			pReq = pRsp.request
			sReq = pReq.method+' '+pReq.url+'\n'.join('{}: {}'.format(k, v) for k, v in pReq.headers.items())+pReq.body
			iErr = int(pRsp.status_code)
			if iErr == 200:
				iErr = 0
			json_body = [ {
				"measurement": sApp,
				"tags": { "async": "true" },
				"fields": {
					"agt": "locust",
					"bytes": len(pRsp.content),
					"err": iErr,
					"rsp": int(pRsp.elapsed),
					"sent": len(sReq),
					"tst": "PoC",
				#	"url": sHost+name,
					"url": pRsp.url,
					"txt": request_type
				}
			} ]
			client.write_points( json_body)
			time.sleep(0.768)

class vUser( HttpLocust):
	global sHost
	host = sHost
	task_set = TestPlan
	min_wait = 500
	max_wait = 5000
