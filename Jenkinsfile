
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
	sh './bin/bjtst controller'
	publishViaSSH( '$(pwd)/target/jmeter/results', "pub/bluejay/jmeter")
}

def launchLoadGen() {
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
