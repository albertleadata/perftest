
def deployViaSSH( war, id) {
	def sDHN = 'wwwlogin@wwwhost'
	def sDPS = '/path/to/j2ee/deployment/location'
	def sTgt = "${sDHN}:${sDPS}"
	sh "scp ${war} ${sTgt}/${id}.war"
}


def publishViaSSH( src, dst) {
	def sDHL = 'jenkins'
	def sDHN = 'bluejaydev'
	def sDPS = '/var/www/html/pub/bluejay'
	sh "rsync -ai --no-o --no-g --no-p --no-t ${src}/ ${sDHL}@${sDHN}:${sDPS}/${dst}/"
	sh 'echo "#===> RESULTS: please see result/reports at:"'
	sh "ls ${src} | grep -v .csv | head -1 | sed 's/^/http:\\/\\/${sDHN}\\/${dst}\\//'"
}

def pullJARs( src, dst) {
	def sDHL = 'jenkins'
	def sDHN = 'bluejaydev'
	def sDPS = '/var/www/html/pub/eptrepo'
	sh "rsync -ai --no-g --no-p --no-t ${sDHL}@${sDHN}:${sDPS}/${src}/ ${dst}/"
}

def launchPerfTest() {
	pullJARs( "timbrado", '$(pwd)/target')
	sh 'if [ -r *.csv ]; then cp $(pwd)/*.csv $(pwd)/target/jmeter/bin ; fi'
	sh 'if [ -r *.jks ]; then cp $(pwd)/*.jks $(pwd)/target/jmeter/bin ; fi'
	sh 'rm -rf $(pwd)/target/jmeter/results ; mkdir -p $(pwd)/target/jmeter/results'
	sh 'mvn jmeter:jmeter'
	sh 'java -jar target/jmeter/bin/ApacheJMeter-3.3.jar -g target/jmeter/results/$(date +%Y%m%d)-timbrado*.csv -o target/jmeter/results/dashboard'
	sh 'mv target/jmeter/results/dashboard target/jmeter/results/timbrado-$(date +%Y%m%d%H%M%S)'
	publishViaSSH( '$(pwd)/target/jmeter/results', "jmeter")
}

pipeline {
	agent any
	stages {
		stage('test') {
			steps {
			//	Before enabling, review/change ALL occurances of "yourappname"
			//	to ensure your actual application name is reflected
			//	sh 'echo "Testing suspended - aborting"'
				launchPerfTest()
			}
		}
	}
}
