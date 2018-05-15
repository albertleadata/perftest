
def deployViaSSH( war, id) {
	def sDHN = 'wwwlogin@wwwhost'
	def sDPS = '/path/to/j2ee/deployment/location'
	def sTgt = "${sDHN}:${sDPS}"
	sh "scp ${war} ${sTgt}/${id}.war"
}


def publishViaSSH( src, dst) {
	def sDHL = 'jenkins'
	def sDHN = 'bluejaydev'
	def sDPS = '/var/www/html'
	def iBld = "${env.BUILD_ID}"
	sh "rsync -ai --no-o --no-g --no-p --no-t ${src}/ ${sDHL}@${sDHN}:${sDPS}/${dst}/"
	sh 'echo "#===> RESULTS: please see result/reports at:"'
//	sh "ls ${src} | grep -v .csv | head -1 | sed 's/^/http:\\/\\/${sDHN}\\/${dst}\\//'"
//	sh "ls ${src} | grep -v .csv | head -1 | sed 's/^/http:\\/\\/${sDHN}\\/${dst}\\//' > rpturl.txt"
	sh "ls ${src} | grep -v .csv | head -1 > rptloc.txt"
	sh "echo \"${iBld}\" > jobid.txt"
	sh "echo \"http://${sDHN}/${dst}/\$(cat rptloc.txt)\" > rpturl.txt"
	sh 'echo "{\\"req\\":{\\"cmd\\":\\"tstupd\\",\\"id\\":\\"$(cat tstid.txt)\\",\\"job\\":\\"$(cat jobid.txt)\\",\\"url\\":\\"$(cat rpturl.txt)\\"}}" > req.json'
	sh "curl -vX POST http://${sDHN}/index.php?ctx=api -d @req.json"
}

def pullJARs( src, dst) {
	def sDHL = 'jenkins'
	def sDHN = 'bluejaydev'
	def sDPS = '/var/www/html/pub/eptrepo'
	sh "rsync -ai --no-g --no-p --no-t ${sDHL}@${sDHN}:${sDPS}/${src}/ ${dst}/"
}

def launchPerfTest() {
	def iApp='1'
	def sDHN='bluejaydev'
	pullJARs( "timbrado", '$(pwd)/target')
	sh 'if [ -r *.csv ]; then cp $(pwd)/*.csv $(pwd)/target/jmeter/bin ; fi'
	sh 'if [ -r *.jks ]; then cp $(pwd)/*.jks $(pwd)/target/jmeter/bin ; fi'
	sh 'rm -rf $(pwd)/target/jmeter/results ; mkdir -p $(pwd)/target/jmeter/results'
	sh "curl -o bjjob.json -vX POST http://${sDHN}/index.php?ctx=api -d '{\"req\":{\"cmd\":\"appjob\",\"id\":\"${iApp}\"}}'"
	sh "python -c \"import sys, json; print json.load(sys.stdin)['tst']\" < bjjob.json > tstid.txt"
	sh 'mvn jmeter:jmeter'
	sh 'java -jar target/jmeter/bin/ApacheJMeter-4.0.jar -g target/jmeter/results/$(date +%Y%m%d)-timbrado*.csv -o target/jmeter/results/dashboard'
	sh 'mv target/jmeter/results/dashboard target/jmeter/results/timbrado-$(date +%Y%m%d%H%M%S)'
	publishViaSSH( '$(pwd)/target/jmeter/results', "pub/bluejay/jmeter")
}

def launchLoadGen() {
	pullJARs( "timbrado", '$(pwd)/target')
	sh 'if [ -r *.csv ]; then cp $(pwd)/*.csv $(pwd)/target/jmeter/bin ; fi'
	sh 'if [ -r *.jks ]; then cp $(pwd)/*.jks $(pwd)/target/jmeter/bin ; fi'
	sh 'rm -rf $(pwd)/target/jmeter/results ; mkdir -p $(pwd)/target/jmeter/results'
//	sh 'java -jar target/jmeter/bin/ApacheJMeter-4.0.jar -Dserver_port=1099 -s -j jmeter-server.log'
	sh 'mvn jmeter:remote-server'
}

def launchParallelPerfTest_PoC() {
	def labels = ['loadgen001','loadgen002']	// labels for Jenkins node types we will build on
	def builders = [:]
	for (x in labels) {
		def label = x
		builders[label] = {
			node(label) {
				launchPerfTest()
			}
		}
	}
	parallel builders
}

pipeline {
	agent any
	stages {
		stage('test') {
			steps {
			//	Before enabling, review/change ALL occurances of "yourappname"
			//	to ensure your actual application name is reflected
			//	sh 'echo "Testing suspended - aborting"'
			//	launchPerfTest()
				parallel (
					"bluejay001" : { launchLoadGen() },
					"bluejay002" : { launchLoadGen() },
					"master" : { launchPerfTest() }
				)
			}
		}
	}
}
