from locust import Locust, TaskSet, task
#sys.path.append(os.getcwd())
#import common.auth

class TestPlan( TaskSet):
	@task
	def default_task( self):
		print( "executing default_task")

class vUser( Locust):
	task_set = TestPlan
	min_wait = 500
	max_wait = 5000
