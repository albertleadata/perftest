
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
	sh "rsync -rltDOi ${src}/ ${sDHL}@${sDHN}:${sDPS}/${dst}/"
	sh 'echo "#===> RESULTS: please see result/reports at:"'
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
	sh "rsync -rltDOi --exclude='*.swp' ${sDHL}@${sDHN}:${sDPS}/${src}/ ${dst}/"
}

def launchPerfTest() {
//	def iApp='1'
//	def sDHN='bluejaydev'
////	pullJARs( "timbrado", '$(pwd)/target')
//	sh './bin/bjtst sync'
//	sh 'if [ -r *.csv ]; then cp $(pwd)/*.csv $(pwd)/target/jmeter/bin ; fi'
//	sh 'if [ -r *.jks ]; then cp $(pwd)/*.jks $(pwd)/target/jmeter/bin ; fi'
//	sh 'rm -rf $(pwd)/target/jmeter/results ; mkdir -p $(pwd)/target/jmeter/results'
//	sh "curl -o bjjob.json -vX POST http://${sDHN}/index.php?ctx=api -d '{\"req\":{\"cmd\":\"appjob\",\"id\":\"${iApp}\"}}'"
//	sh "python -c \"import sys, json; print json.load(sys.stdin)['tst']\" < bjjob.json > tstid.txt"
////	sh 'mvn jmeter:jmeter'
	sh './bin/bjtst controller'
//	sh 'java -jar target/jmeter/bin/ApacheJMeter-4.0.jar -g target/jmeter/results/$(date +%Y)*-timbrado*.csv -o target/jmeter/results/dashboard'
//	sh 'mv target/jmeter/results/dashboard target/jmeter/results/timbrado-$(date +%Y%m%d%H%M%S)'
	publishViaSSH( '$(pwd)/target/jmeter/results', "pub/bluejay/jmeter")
}

def launchLoadGen() {
//	pullJARs( "timbrado", '$(pwd)/target')
	sh './bin/bjtst sync'
	sh 'if [ -r *.csv ]; then cp $(pwd)/*.csv $(pwd)/target/jmeter/bin ; fi'
	sh 'if [ -r *.jks ]; then cp $(pwd)/*.jks $(pwd)/target/jmeter/bin ; fi'
	sh 'rm -rf $(pwd)/target/jmeter/results ; mkdir -p $(pwd)/target/jmeter/results'
	sh './bin/bjtst loadgen'
}

def launchParallelPerfTest() {
	parallel (
		"loadgen002" : { launchLoadGen() },
		"loadgen001" : { launchLoadGen() },
		"controller" : { launchPerfTest() }
	)
}

pipeline {
//	agent any
	agent { label "controller" }
	stages {
		stage('test') {
			parallel {
				stage('Load Gen 2') {
					agent { label "loadgen002" }
					steps { launchLoadGen() }
				}
				stage('Load Gen 1') {
					agent { label "loadgen001" }
					steps { launchLoadGen() }
				}
				stage('Load Controller') {
					agent { label "controller" }
					steps { launchPerfTest() }
				}
			}
		}
	}
}
